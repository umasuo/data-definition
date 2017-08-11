package com.umasuo.datapoint.application.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.umasuo.datapoint.application.dto.CopyRequest;
import com.umasuo.datapoint.application.dto.DataDefinitionDraft;
import com.umasuo.datapoint.application.dto.DataDefinitionView;
import com.umasuo.datapoint.application.dto.mapper.DataDefinitionMapper;
import com.umasuo.datapoint.domain.model.DeveloperDataDefinition;
import com.umasuo.datapoint.domain.model.DeviceDataDefinition;
import com.umasuo.datapoint.domain.model.PlatformDataDefinition;
import com.umasuo.datapoint.domain.service.DataDefinitionService;
import com.umasuo.datapoint.domain.service.DeveloperDataService;
import com.umasuo.datapoint.domain.service.PlatformDataService;
import com.umasuo.datapoint.infrastructure.update.UpdateAction;
import com.umasuo.datapoint.infrastructure.update.UpdaterService;
import com.umasuo.datapoint.infrastructure.validator.CopyRequestValidator;
import com.umasuo.datapoint.infrastructure.validator.SchemaValidator;
import com.umasuo.exception.AuthFailedException;
import com.umasuo.exception.ConflictException;
import com.umasuo.exception.NotExistException;
import com.umasuo.exception.ParametersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by umasuo on 17/6/11.
 */
@Service
public class DataDefinitionApplication {

  /**
   * logger.
   */
  private final static Logger logger = LoggerFactory.getLogger(DataDefinitionApplication.class);

  /**
   * The DataDefinitionService.
   */
  @Autowired
  private transient DataDefinitionService definitionService;

  @Autowired
  private transient PlatformDataService platformDataService;

  @Autowired
  private transient DeveloperDataService developerDataService;


  @Autowired
  private transient CacheApplication cacheApplication;

  /**
   * The UpdateService.
   */
  @Autowired
  private transient UpdaterService updaterService;

  /**
   * Create DeviceDataDefinition.
   *
   * @param draft the draft
   * @param developerId the developer id
   * @return the data definition view
   */
  public DataDefinitionView create(DataDefinitionDraft draft, String developerId) {
    logger.debug("Enter. draft: {}, developerId: {}.", draft, developerId);

    SchemaValidator.validate(draft.getDataSchema());

    definitionService.isExistName(developerId, draft.getProductId(), draft.getName());

    definitionService.isExistDataId(developerId, draft.getProductId(), draft.getDataId());

    DeviceDataDefinition definition =
        definitionService.save(DataDefinitionMapper.toEntity(draft, developerId));

    cacheApplication.deleteDeviceDefinition(developerId, draft.getProductId());

    DataDefinitionView view = DataDefinitionMapper.toView(definition);

    logger.debug("Exit. view: {}.", view);

    return view;
  }

  /**
   * 处理拷贝数据定义的请求。
   * 请求分为开发者的数据定义和平台的数据定义。
   */
  public List<String> handleCopyRequest(String developerId, CopyRequest request) {
    logger.info("Enter. developerId: {}, copyRequest: {}.", developerId, request);

    List<String> newDataDefinitionIds = Lists.newArrayList();

    // 拷贝平台的数据定义
    boolean isCopyFromPlatform = !CollectionUtils.isEmpty(request.getPlatformDataDefinitionIds());

    // 拷贝开发者的数据定义
    boolean isCopyFromDeveloper = !CollectionUtils.isEmpty(request.getDeveloperDataDefinitionIds());

    // 平台和开发者的数据定义不能同时为空
    CopyRequestValidator.validateNullRequest(isCopyFromPlatform, isCopyFromDeveloper);

    if (isCopyFromPlatform) {
      List<String> copyPlatformDataIds = copyFromPlatformData(
          developerId, request.getProductId(), request.getPlatformDataDefinitionIds());
      newDataDefinitionIds.addAll(copyPlatformDataIds);
    }

    if (isCopyFromDeveloper) {
      List<String> copyDeveloperDataIds = copyFromDeveloperData(
          developerId, request.getProductId(), request.getDeveloperDataDefinitionIds());
      newDataDefinitionIds.addAll(copyDeveloperDataIds);
    }

    cacheApplication.deleteDeviceDefinition(developerId, request.getProductId());

    logger.info("Exit. newDataDefinitionIds: {}.", newDataDefinitionIds);
    return newDataDefinitionIds;
  }

  /**
   * 拷贝平台的数据定义.
   */
  private List<String> copyFromPlatformData(String developerId, String productId,
      List<String> requestIds) {

    List<PlatformDataDefinition> dataDefinitions = platformDataService.getByIds(requestIds);

    CopyRequestValidator.matchRequestIds(requestIds, dataDefinitions);

    List<DeviceDataDefinition> newDataDefinitions =
        DataDefinitionMapper.copyFromPlatformData(developerId, productId, dataDefinitions);

    List<String> newDataDefinitionIds = definitionService.saveAll(newDataDefinitions);

    return newDataDefinitionIds;
  }

  /**
   * 拷贝开发者的数据定义
   */
  private List<String> copyFromDeveloperData(String developerId, String productId,
      List<String> requestIds) {
    List<DeveloperDataDefinition> dataDefinitions = developerDataService.getByIds(requestIds);

    CopyRequestValidator.matchRequestIds(requestIds, dataDefinitions);

    List<DeviceDataDefinition> newDataDefinitions = DataDefinitionMapper
        .copyFromDeveloperData(developerId, productId, dataDefinitions);

    List<String> newDataDefinitionIds = definitionService.saveAll(newDataDefinitions);

    return newDataDefinitionIds;
  }

  /**
   * Update DeviceDataDefinition.
   *
   * @param id the id
   * @param developerId the developer id
   * @param version the version
   * @param actions the actions
   * @return updated DataDefinitionView
   */
  public DataDefinitionView update(String id, String developerId, Integer version,
      List<UpdateAction> actions) {
    logger.debug("Enter: id: {}, version: {}, developerId actions: {}",
        id, version, developerId, actions);

    DeviceDataDefinition definition = definitionService.getById(id);
    if (!definition.getDeveloperId().equals(developerId)) {
      logger.debug("Can not update dataDefinition: {} not belong to developer: {}.",
          id, developerId);
      throw new ParametersException("Can not update dataDefinition: " + id +
          " not belong to developer: " + developerId);
    }

//    checkVersion(version, definition.getVersion());

    actions.stream().forEach(action -> updaterService.handle(definition, action));

    DeviceDataDefinition updatedDefinition = definitionService.save(definition);

    cacheApplication.deleteDeviceDefinition(developerId, definition.getProductId());

    DataDefinitionView result = DataDefinitionMapper.toView(updatedDefinition);

    logger.trace("Updated DeviceDataDefinition: {}.", result);
    logger.debug("Exit.");

    return result;
  }

  public List<DataDefinitionView> getByProductId(String developerId, String productId) {
    logger.debug("Enter. developerId: {}, productId: {}.", developerId, productId);

    List<DeviceDataDefinition> dataDefinitions =
        cacheApplication.getDeviceDataDefinition(developerId, productId);

    if (dataDefinitions.isEmpty()) {
      dataDefinitions = definitionService.getByProductId(developerId, productId);

      cacheApplication.cacheDeviceDefinition(developerId, productId, dataDefinitions);
    }

    List<DataDefinitionView> result = DataDefinitionMapper.toView(dataDefinitions);

    logger.debug("Exit. dataDefinition size: {}.", result.size());

    return result;
  }

  public Map<String, List<DataDefinitionView>> getByProductIds(String developerId,
      List<String> productIds) {
    logger.debug("Enter. developerId: {}, productIds: {}.", developerId, productIds);

    // TODO: 17/7/12 简单粗暴的实现方式，待优化成批量处理
    Map<String, List<DataDefinitionView>> result = Maps.newHashMap();

    Consumer<String> consumer = id -> result.put(id, getByProductId(developerId, id));

    productIds.stream().forEach(consumer);

    return result;
  }

  public void delete(String id, String developerId, String productId) {
    logger.debug("Enter. id: {}, developerId: {}, productId: {}.", id, developerId, productId);

    DeviceDataDefinition dataDefinition = definitionService.getById(id);

    if (!developerId.equals(dataDefinition.getDeveloperId())) {
      logger.debug("DataDefinition: {} is not belong to developer: {}.", id, developerId);
      throw new AuthFailedException("Developer has not auth to delete dataDefinition");
    }

    if (!productId.equals(dataDefinition.getProductId())) {
      logger.debug("DataDefinition: {} is not belong to product: {}.", id, productId);
      throw new NotExistException("Product do not have this dataDefinition.");
    }

    definitionService.delete(id);

    cacheApplication.deleteDeviceDefinition(developerId, dataDefinition.getProductId());
  }

  /**
   * check the version.
   *
   * @param inputVersion Integer
   * @param existVersion Integer
   */
  private void checkVersion(Integer inputVersion, Integer existVersion) {
    if (!inputVersion.equals(existVersion)) {
      logger.debug("DeviceDataDefinition version is not correct.");
      throw new ConflictException("DeviceDataDefinition version is not correct.");
    }
  }

  public void delete(String developerId, String productId) {
    logger.debug("Enter. developerId: {}, productId: {}.", developerId, productId);

    definitionService.deleteByProduct(developerId, productId);

    cacheApplication.deleteDeviceDefinition(developerId, productId);

    logger.debug("Exit.");
  }

  public DataDefinitionView get(String developerId, String productId, String id) {
    logger.debug("Enter. developerId: {}, productId: {}, id: {}.", developerId, productId, id);

    DeviceDataDefinition dataDefinition =
        cacheApplication.getDeviceDataDefinition(developerId, productId, id);

    if (dataDefinition == null) {
      logger.debug("DataDefinition: {} not exist.", id);
      throw new NotExistException("DataDefinition not exist");
    }

    if (!developerId.equals(dataDefinition.getDeveloperId())) {
      logger.debug("DataDefinition: {} is not belong to developer: {}.", id, developerId);
      throw new AuthFailedException("DataDefinition is not belong to developer");
    }

    if (!productId.equals(dataDefinition.getProductId())) {
      logger.debug("DataDefinition: {} is not belong to product: {}.", id, productId);
      throw new AuthFailedException("DataDefinition is not belong to product");
    }

    DataDefinitionView result = DataDefinitionMapper.toView(dataDefinition);

    logger.debug("Exit. dataDefinition: {}.", result);

    return result;
  }
}
