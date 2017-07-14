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
import com.umasuo.datapoint.infrastructure.validator.SchemaValidator;
import com.umasuo.exception.AlreadyExistException;
import com.umasuo.exception.AuthFailedException;
import com.umasuo.exception.ConflictException;
import com.umasuo.exception.NotExistException;
import com.umasuo.exception.ParametersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    if (definitionService.isExistName(draft.getName(), draft.getProductId())) {
      logger.debug("Name: {} has existed in developer: {}.", draft.getName(), draft.getProductId());
      throw new AlreadyExistException("Name has existed");
    }

    DeviceDataDefinition definition = definitionService
        .create(DataDefinitionMapper.toEntity(draft, developerId));

    cacheApplication.deleteDeviceDefinition(developerId, draft.getProductId());

    DataDefinitionView view = DataDefinitionMapper.toView(definition);

    logger.debug("Exit. view: {}.", view);
    return view;
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

  /**
   * 处理拷贝数据定义的请求。
   * 请求分为开发者的数据定义和平台的数据定义。
   */
  public List<String> handleCopyRequest(String developerId, CopyRequest request) {
    logger.info("Enter. developerId: {}, copyRequest: {}.", developerId, request);
    List<String> newDataDefinitionIds = Lists.newArrayList();

    // 拷贝平台的数据定义
    boolean isCopyFromPlatform = request.getPlatformDataDefinitionIds() != null &&
        !request.getPlatformDataDefinitionIds().isEmpty();

    // 拷贝开发者的数据定义
    boolean isCopyFromDeveloper = request.getDeveloperDataDefinitionIds() != null &&
        !request.getDeveloperDataDefinitionIds().isEmpty();

    // 平台和开发者的数据定义不能同时为空
    if (!isCopyFromPlatform && !isCopyFromDeveloper) {
      logger.debug("Can not copy from null request data definition id");
      throw new ParametersException("Can not copy from null request data definition id");
    }

    if (isCopyFromPlatform) {
      List<String> copyPlatformDataIds = copyFromPlatformData(
          developerId, request.getDeviceDefinitionId(), request.getPlatformDataDefinitionIds());
      newDataDefinitionIds.addAll(copyPlatformDataIds);
    }

    if (isCopyFromDeveloper) {
      List<String> copyDeveloperDataIds = copyFromDeveloperData(
          developerId, request.getDeviceDefinitionId(), request.getDeveloperDataDefinitionIds());
      newDataDefinitionIds.addAll(copyDeveloperDataIds);
    }

    cacheApplication.deleteDeviceDefinition(developerId, request.getDeviceDefinitionId());

    logger.info("Exit. newDataDefinitionIds: {}.", newDataDefinitionIds);
    return newDataDefinitionIds;
  }

  /**
   * 拷贝开发者的数据定义
   */
  private List<String> copyFromDeveloperData(String developerId, String deviceDefinitionId,
      List<String> requestIds) {
    List<DeveloperDataDefinition> dataDefinitions = developerDataService.getByIds(requestIds);

    if (requestIds.size() != dataDefinitions.size()) {
      logger.debug("Can not find all dataDefinition: {}.", requestIds);
      throw new NotExistException("DeviceDataDefinition not exist");
    }

    List<DeviceDataDefinition> newDataDefinitions = DataDefinitionMapper
        .copyFromDeveloperData(developerId, deviceDefinitionId, dataDefinitions);

    List<DeviceDataDefinition> savedDataDefinitions = definitionService.saveAll(newDataDefinitions);

    List<String> newDataDefinitionIds = savedDataDefinitions.stream()
        .map(DeviceDataDefinition::getId).collect(Collectors.toList());

    return newDataDefinitionIds;
  }

  /**
   * 拷贝平台的数据定义.
   */
  private List<String> copyFromPlatformData(String developerId, String deviceDefinitionId,
      List<String> requestIds) {

    List<PlatformDataDefinition> dataDefinitions =
        platformDataService.getByIds(requestIds);

    if (requestIds.size() != dataDefinitions.size()) {
      logger.debug("Can not find all dataDefinition: {}.", requestIds);
      throw new NotExistException("DeviceDataDefinition not exist");
    }

    List<DeviceDataDefinition> newDataDefinitions = DataDefinitionMapper
        .copyFromPlatformData(developerId, deviceDefinitionId, dataDefinitions);

    List<DeviceDataDefinition> savedDataDefinitions = definitionService.saveAll(newDataDefinitions);

    List<String> newDataDefinitionIds = savedDataDefinitions.stream()
        .map(DeviceDataDefinition::getId).collect(Collectors.toList());

    return newDataDefinitionIds;
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
}
