package com.umasuo.datapoint.application.dto.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.umasuo.datapoint.application.dto.DeveloperDataDefinitionDraft;
import com.umasuo.datapoint.application.dto.DeveloperDataDefinitionView;
import com.umasuo.datapoint.domain.model.DeveloperDataDefinition;
import com.umasuo.datapoint.infrastructure.util.JsonUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by Davis on 17/6/30.
 */
public final class DeveloperDataMapper {

  /**
   * Instantiates a new Developer data mapper.
   */
  public DeveloperDataMapper() {
  }

  public static DeveloperDataDefinitionView toModel(DeveloperDataDefinition entity) {
    DeveloperDataDefinitionView model = new DeveloperDataDefinitionView();

    model.setId(entity.getId());
    model.setCreatedAt(entity.getCreatedAt());
    model.setLastModifiedAt(entity.getLastModifiedAt());
    model.setVersion(entity.getVersion());
    model.setDeveloperId(entity.getDeveloperId());
    model.setDataId(entity.getDataId());
    model.setName(entity.getName());
    model.setDescription(entity.getDescription());

    model.setDataSchema(JsonUtils.deserialize(entity.getDataSchema(), JsonNode.class));

    return model;
  }

  public static List<DeveloperDataDefinitionView> toModel(List<DeveloperDataDefinition> entities) {
    List<DeveloperDataDefinitionView> models = Lists.newArrayList();

    entities.stream().forEach(entity -> models.add(toModel(entity)));

    return models;
  }

  public static DeveloperDataDefinition toEntity(String developerId,
      DeveloperDataDefinitionDraft draft) {
    DeveloperDataDefinition entity = new DeveloperDataDefinition();

    entity.setDeveloperId(developerId);
    entity.setDataId(draft.getDataId());
    entity.setName(draft.getName());
    entity.setDescription(draft.getDescription());
    entity.setDataSchema(draft.getDataSchema().toString());

    return entity;
  }

  public static Map<String, DeveloperDataDefinition> toEntityMap(
      List<DeveloperDataDefinition> definitions) {
    Map<String, DeveloperDataDefinition> entityMap =
        definitions.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));

    return entityMap;
  }
}
