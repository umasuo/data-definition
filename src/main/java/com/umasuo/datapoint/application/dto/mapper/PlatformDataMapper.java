package com.umasuo.datapoint.application.dto.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.umasuo.datapoint.application.dto.PlatformDataDefinitionView;
import com.umasuo.datapoint.domain.model.PlatformDataDefinition;
import com.umasuo.datapoint.infrastructure.util.JsonUtils;

import java.util.List;

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
}
