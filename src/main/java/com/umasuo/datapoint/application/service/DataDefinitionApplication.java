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
import com.umasuo.datapoint.infrastructure.validator.DefinitionValidator;
import com.umasuo.datapoint.infrastructure.validator.SchemaValidator;
import com.umasuo.exception.NotExistException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * DataDefinitionApplication.
 */
@Service
public class DataDefinitionApplication {

  /**
   * LOGGER.
   */
  private final static Logger LOGGER = LoggerFactory.getLogger(DataDefinitionApplication.class);

  /**
   * The DataDefinitionService.
   */
  @Autowired
  private transient DataDefinitionService definitionService;

  /**
   * Platform data definition service.
   */
  @Autowired
  private transient PlatformDataService platformDataService;

  /**
   * Developer data definition service.
   */
  @Autowired
  private transient DeveloperDataService developerDataService;

  /**
   * Cache application service.
   */
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
    LOGGER.debug("Enter. draft: {}, developerId: {}.", draft, developerId);

    SchemaValidator.validate(draft.getSchema());

    definitionService.isExistName(developerId, draft.getProductId(), draft.getName());

    definitionService.isExistDataId(developerId, draft.getProductId(), draft.getDataId());

    DeviceDataDefinition definition =
        definitionService.save(DataDefinitionMapper.toModel(draft, developerId));

    cacheApplication.deleteProductDataDefinition(developerId, draft.getProductId());

    DataDefinitionView view = DataDefinitionMapper.toView(definition);

    LOGGER.debug("Exit. view: {}.", view);

    return view;
  }

  /**
   * 处理拷贝数据定义的请求。
   * 请求分为开发者的数据定义和平台的数据定义。
   *
   * @param developerId the developer id
   * @param request the request
   * @return the list
   */
  public List<String> handleCopyRequest(String developerId, CopyRequest request) {
    LOGGER.info("Enter. developerId: {}, copyRequest: {}.", developerId, request);

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

    cacheApplication.deleteProductDataDefinition(developerId, request.getProductId());

    LOGGER.info("Exit. newDataDefinitionIds: {}.", newDataDefinitionIds);
    return newDataDefinitionIds;
  }

  /**
   * 拷贝平台的数据定义.
   *
   * @param developerId the developer id
   * @param productId the product id
   * @param requestIds the dataDefinition id list
   * @return new dataDefinition id list
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
   * 拷贝开发者的数据定义.
   *
   * @param developerId the developer id
   * @param productId the product id
   * @param requestIds the dataDefinition id list
   * @return new dataDefinition id list
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
    LOGGER.debug("Enter. id: {}, version: {}, developerId:{}, actions: {}.",
        id, version, developerId, actions);

    DeviceDataDefinition definition = definitionService.getById(id);

    DefinitionValidator.validateDeveloper(developerId, definition.getDeveloperId(), id);

//    VersionValidator.checkVersion(version, definition.getVersion());

    actions.stream().forEach(action -> updaterService.handle(definition, action));

    DeviceDataDefinition updatedDefinition = definitionService.save(definition);

    cacheApplication.deleteProductDataDefinition(developerId, definition.getProductId());

    DataDefinitionView result = DataDefinitionMapper.toView(updatedDefinition);

    LOGGER.trace("Updated DeviceDataDefinition: {}.", result);
    LOGGER.debug("Exit.");

    return result;
  }

  /**
   * Delete.
   *
   * @param developerId the developer id
   * @param productId the product id
   * @param id the id
   */
  public void delete(String developerId, String productId, String id) {
    LOGGER.debug("Enter. id: {}, developerId: {}, productId: {}.", id, developerId, productId);

    DeviceDataDefinition dataDefinition = definitionService.getById(id);

    DefinitionValidator.validateDeveloper(developerId, dataDefinition.getDeveloperId(), id);

    DefinitionValidator.validateProduct(productId, dataDefinition.getProductId(), id);

    definitionService.delete(id);

    cacheApplication.deleteProductDataDefinition(developerId, dataDefinition.getProductId());

    LOGGER.debug("Exit.");
  }

  /**
   * Delete.
   *
   * @param developerId the developer id
   * @param productId the product id
   */
  public void delete(String developerId, String productId) {
    LOGGER.debug("Enter. developerId: {}, productId: {}.", developerId, productId);

    definitionService.deleteByProduct(developerId, productId);

    cacheApplication.deleteProductDataDefinition(developerId, productId);

    LOGGER.debug("Exit.");
  }

  /**
   * 获取productId对应的所有dataDefinition.
   *
   * @param developerId the developer id
   * @param productId the product id
   * @return dataDefinition list
   */
  public List<DataDefinitionView> getByProductId(String developerId, String productId) {
    LOGGER.debug("Enter. developerId: {}, productId: {}.", developerId, productId);

    List<DeviceDataDefinition> dataDefinitions =
        cacheApplication.getProductDataDefinition(developerId, productId);

    if (CollectionUtils.isEmpty(dataDefinitions)) {
      dataDefinitions = definitionService.getByProductId(developerId, productId);

      cacheApplication.cacheProductDataDefinition(developerId, productId, dataDefinitions);
    }

    List<DataDefinitionView> result = DataDefinitionMapper.toView(dataDefinitions);

    LOGGER.debug("Exit. dataDefinition size: {}.", result.size());

    return result;
  }

  /**
   * Gets by product ids.
   *
   * @param developerId the developer id
   * @param productIds the product ids
   * @return the by product ids
   */
  public Map<String, List<DataDefinitionView>> getByProductIds(String developerId,
      List<String> productIds) {
    LOGGER.debug("Enter. developerId: {}, productIds: {}.", developerId, productIds);

    // TODO: 17/7/12 简单粗暴的实现方式，待优化成批量处理
    Map<String, List<DataDefinitionView>> result = Maps.newHashMap();

    Consumer<String> consumer = id -> result.put(id, getByProductId(developerId, id));

    productIds.stream().forEach(consumer);

    return result;
  }

  /**
   * Get data definition view.
   *
   * @param developerId the developer id
   * @param productId the product id
   * @param id the id
   * @return the data definition view
   */
  public DataDefinitionView get(String developerId, String productId, String id) {
    LOGGER.debug("Enter. developerId: {}, productId: {}, id: {}.", developerId, productId, id);

    DeviceDataDefinition dataDefinition =
        cacheApplication.getProductDataDefinition(developerId, productId, id);

    if (dataDefinition == null) {
      LOGGER.debug("Cache fail, query dataDefinition from database and cache.");

      List<DeviceDataDefinition> dataDefinitions =
          definitionService.getByProductId(developerId, productId);

      cacheApplication.cacheProductDataDefinition(developerId, productId, dataDefinitions);

      dataDefinition =
          dataDefinitions.stream().filter(data -> id.equals(data.getId())).findAny().orElse(null);

      if (dataDefinition == null) {
        LOGGER.debug("DataDefinition: {} not exist.", id);
        throw new NotExistException("DataDefinition not exist");
      }
    }

    DataDefinitionView result = DataDefinitionMapper.toView(dataDefinition);

    LOGGER.debug("Exit. dataDefinition: {}.", result);

    return result;
  }
}
