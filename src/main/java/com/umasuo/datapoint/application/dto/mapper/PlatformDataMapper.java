package com.umasuo.datapoint.application.dto.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.umasuo.datapoint.application.dto.PlatformDataDefinitionView;
import com.umasuo.datapoint.domain.model.PlatformDataDefinition;
import com.umasuo.datapoint.infrastructure.util.JsonUtils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

/**
 * Created by Davis on 17/6/29.
 */
public final class PlatformDataMapper {

  /**
   * Instantiates a new Platform data mapper.
   */
  private PlatformDataMapper() {
  }


  public static List<PlatformDataDefinitionView> toModel(List<PlatformDataDefinition> entities) {
    List<PlatformDataDefinitionView> models = Lists.newArrayList();

    entities.stream().forEach(entity -> models.add(toModel(entity)));

    return models;
  }

  public static PlatformDataDefinitionView toModel(PlatformDataDefinition entity) {
    PlatformDataDefinitionView model = new PlatformDataDefinitionView();

    model.setName(entity.getName());
    model.setProductTypeId(entity.getProductTypeId());
    model.setDescription(entity.getDescription());
    model.setId(entity.getId());
    model.setDataId(entity.getDataId());
    model.setDataSchema(JsonUtils.deserialize(entity.getDataSchema(), JsonNode.class));

    return model;
  }

  public static Map<String, List<PlatformDataDefinition>> toEntityMap(
      List<PlatformDataDefinition> dataDefinitions) {

    Map<String, List<PlatformDataDefinition>> mapModel = Maps.newHashMap();

    Consumer<PlatformDataDefinition> consumer = dataDefinition -> {
      if (mapModel.containsKey(dataDefinition.getProductTypeId())) {
        mapModel.get(dataDefinition.getProductTypeId()).add(dataDefinition);
      } else {
        mapModel
            .put(dataDefinition.getProductTypeId(), Lists.newArrayList(dataDefinition));
      }
    };

    dataDefinitions.stream().forEach(consumer);

    return mapModel;
  }

  public static Map<String, List<PlatformDataDefinitionView>> toModelMap(
      Map<String, List<PlatformDataDefinition>> entityMap) {
    Map<String, List<PlatformDataDefinitionView>> modelMap = Maps.newHashMap();

    Consumer<? super Entry<String, List<PlatformDataDefinition>>> consumer =
        entry -> modelMap.put(entry.getKey(), toModel(entry.getValue()));

    entityMap.entrySet().stream().forEach(consumer);

    return modelMap;
  }
}
