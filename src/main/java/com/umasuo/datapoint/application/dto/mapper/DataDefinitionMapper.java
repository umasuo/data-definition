package com.umasuo.datapoint.application.dto.mapper;

import com.google.common.collect.Lists;
import com.umasuo.datapoint.application.dto.DataDefinitionDraft;
import com.umasuo.datapoint.application.dto.DataDefinitionView;
import com.umasuo.datapoint.domain.model.DeveloperDataDefinition;
import com.umasuo.datapoint.domain.model.DeviceDataDefinition;
import com.umasuo.datapoint.domain.model.PlatformDataDefinition;
import com.umasuo.datapoint.infrastructure.enums.Category;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * DataDefinitionMapper.
 */
public final class DataDefinitionMapper {

  /**
   * private Default constructor.
   */
  private DataDefinitionMapper() {
  }

  /**
   * To model list.
   *
   * @param entities the entities
   * @return the list
   */
  public static List<DataDefinitionView> toView(List<DeviceDataDefinition> entities) {
    List<DataDefinitionView> models = Lists.newArrayList();

    Consumer<DeviceDataDefinition> consumer = dataDefinition -> models.add(toView(dataDefinition));

    entities.stream().forEach(consumer);

    return models;
  }

  /**
   * To model data definition view.
   *
   * @param model the model
   * @return the data definition view
   */
  public static DataDefinitionView toView(DeviceDataDefinition model) {
    DataDefinitionView view = null;
    if (model != null) {
      view = new DataDefinitionView();
      view.setId(model.getId());
      view.setCreatedAt(model.getCreatedAt());
      view.setLastModifiedAt(model.getLastModifiedAt());
      view.setVersion(model.getVersion());
      view.setDeveloperId(model.getDeveloperId());
      view.setDataId(model.getDataId());
      view.setName(model.getName());
      view.setDescription(model.getDescription());
      view.setOpenable(model.getOpenable());
      view.setCategory(model.getCategory());
      view.setSchema(model.getDataSchema());
    }
    return view;
  }

  /**
   * 根据draft 创建数据定义model.
   */
  public static DeviceDataDefinition toModel(DataDefinitionDraft draft, String developerId) {

    DeviceDataDefinition model = null;
    model = new DeviceDataDefinition();
    model.setDeveloperId(developerId);
    model.setProductId(draft.getProductId());
    model.setDataId(draft.getDataId());
    model.setName(draft.getName());
    model.setDescription(draft.getDescription());
    model.setDataSchema(draft.getDataSchema().toString());
    if (draft.getOpenable() != null) {
      model.setOpenable(draft.getOpenable());
    }
    model.setCategory(Category.PRODUCT);

    return model;
  }

  /**
   * copy from platform data.
   * @param developerId
   * @param productId
   * @param dataDefinition
   * @return
   */
  public static DeviceDataDefinition copyFromPlatformData(String developerId,
                                                          String productId,
                                                          PlatformDataDefinition dataDefinition) {
    DeviceDataDefinition newDataDefinition = new DeviceDataDefinition();

    newDataDefinition.setName(dataDefinition.getName());
    newDataDefinition.setDeveloperId(developerId);
    newDataDefinition.setDescription(dataDefinition.getDescription());
    newDataDefinition.setDataId(dataDefinition.getDataId());
    newDataDefinition.setDataSchema(dataDefinition.getDataSchema());
    newDataDefinition.setProductId(productId);
    newDataDefinition.setCategory(Category.PLATFORM);

    return newDataDefinition;
  }

  /**
   * copy from platform data list.
   * @param developerId
   * @param productId
   * @param dataDefinitions
   * @return
   */
  public static List<DeviceDataDefinition> copyFromPlatformData(String developerId,
                                                                String productId,
                                                                List<PlatformDataDefinition>
                                                                    dataDefinitions) {
    List<DeviceDataDefinition> newDataDefinitions = Lists.newArrayList();

    dataDefinitions.stream().forEach(
        dataDefinition -> newDataDefinitions
            .add(copyFromPlatformData(developerId, productId, dataDefinition))
    );

    return newDataDefinitions;
  }

  /**
   * copy from developer data.
   * @param developerId
   * @param productId
   * @param dataDefinitions
   * @return
   */
  public static List<DeviceDataDefinition> copyFromDeveloperData(String developerId,
                                                                 String productId,
                                                                 List<DeveloperDataDefinition>
                                                                     dataDefinitions) {
    List<DeviceDataDefinition> newDataDefinitions = Lists.newArrayList();

    dataDefinitions.stream().forEach(
        dataDefinition -> newDataDefinitions.add(
            copyFromDeveloperData(developerId, productId, dataDefinition))
    );

    return newDataDefinitions;
  }

  /**
   * copy from developer data.
   * @param developerId
   * @param productId
   * @param dataDefinition
   * @return
   */
  public static DeviceDataDefinition copyFromDeveloperData(String developerId,
                                                           String productId,
                                                           DeveloperDataDefinition dataDefinition) {
    DeviceDataDefinition newDataDefinition = new DeviceDataDefinition();

    newDataDefinition.setName(dataDefinition.getName());
    newDataDefinition.setDeveloperId(developerId);
    newDataDefinition.setDescription(dataDefinition.getDescription());
    newDataDefinition.setDataId(dataDefinition.getDataId());
    newDataDefinition.setDataSchema(dataDefinition.getDataSchema());
    newDataDefinition.setProductId(productId);
    newDataDefinition.setCategory(Category.DEVELOPER);

    return newDataDefinition;
  }

  /**
   * To model map.
   * @param dataDefinitions
   * @return
   */
  public static Map<String, DeviceDataDefinition> toModelMap(
      List<DeviceDataDefinition> dataDefinitions) {
    Map<String, DeviceDataDefinition> entityMap =
        dataDefinitions.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));

    return entityMap;
  }
}
