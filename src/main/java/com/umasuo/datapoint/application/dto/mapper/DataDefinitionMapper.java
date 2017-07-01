package com.umasuo.datapoint.application.dto.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.umasuo.datapoint.application.dto.DataDefinitionDraft;
import com.umasuo.datapoint.application.dto.DataDefinitionView;
import com.umasuo.datapoint.domain.model.DeveloperDataDefinition;
import com.umasuo.datapoint.domain.model.DeviceDataDefinition;
import com.umasuo.datapoint.domain.model.PlatformDataDefinition;
import com.umasuo.datapoint.infrastructure.util.JsonUtils;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by umasuo on 17/3/8.
 */
public class DataDefinitionMapper {

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

      view.setDataSchema(JsonUtils.deserialize(model.getDataSchema(), JsonNode.class));
    }
    return view;
  }

  /**
   * 根据draft 创建数据定义model.
   *
   * @param draft
   * @return
   */
  public static DeviceDataDefinition toEntity(DataDefinitionDraft draft, String developerId) {

    DeviceDataDefinition model = null;
    model = new DeviceDataDefinition();
    model.setDeveloperId(developerId);
    model.setProductId(draft.getProductId());
    model.setDataId(draft.getDataId());
    model.setName(draft.getName());
    model.setDescription(draft.getDescription());
    model.setDataSchema(draft.getDataSchema().toString());
    model.setOpenable(draft.getOpenable());
    return model;
  }

  public static DeviceDataDefinition copyFromPlatformData(String developerId,
                                                          PlatformDataDefinition dataDefinition) {
    DeviceDataDefinition newDataDefinition = new DeviceDataDefinition();

    newDataDefinition.setName(dataDefinition.getName());
    newDataDefinition.setDeveloperId(developerId);
    newDataDefinition.setDescription(dataDefinition.getDescription());
    newDataDefinition.setDataId(dataDefinition.getDataId());
    newDataDefinition.setDataSchema(dataDefinition.getDataSchema());

    return newDataDefinition;
  }

  public static List<DeviceDataDefinition> copyFromPlatformData(String developerId,
                                                                List<PlatformDataDefinition>
                                                                    dataDefinitions) {
    List<DeviceDataDefinition> newDataDefinitions = Lists.newArrayList();

    dataDefinitions.stream().forEach(
        dataDefinition -> newDataDefinitions.add(copyFromPlatformData(developerId, dataDefinition))
    );

    return newDataDefinitions;
  }

  public static List<DeviceDataDefinition> copyFromDeveloperData(String developerId,
                                                                 List<DeveloperDataDefinition>
                                                                     dataDefinitions) {
    List<DeviceDataDefinition> newDataDefinitions = Lists.newArrayList();

    dataDefinitions.stream().forEach(
        dataDefinition -> newDataDefinitions.add(copyFromDeveloperData(developerId, dataDefinition))
    );

    return newDataDefinitions;
  }

  public static DeviceDataDefinition copyFromDeveloperData(String developerId,
                                                           DeveloperDataDefinition dataDefinition) {
    DeviceDataDefinition newDataDefinition = new DeviceDataDefinition();

    newDataDefinition.setName(dataDefinition.getName());
    newDataDefinition.setDeveloperId(developerId);
    newDataDefinition.setDescription(dataDefinition.getDescription());
    newDataDefinition.setDataId(dataDefinition.getDataId());
    newDataDefinition.setDataSchema(dataDefinition.getDataSchema());

    return newDataDefinition;
  }
}
