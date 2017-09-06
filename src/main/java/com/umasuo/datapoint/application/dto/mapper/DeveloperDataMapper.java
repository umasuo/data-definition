package com.umasuo.datapoint.application.dto.mapper;

import com.google.common.collect.Lists;
import com.umasuo.datapoint.application.dto.DeveloperDataDefinitionDraft;
import com.umasuo.datapoint.application.dto.DeveloperDataDefinitionView;
import com.umasuo.datapoint.domain.model.DeveloperDataDefinition;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DeveloperDataMapper.
 */
public final class DeveloperDataMapper {

  /**
   * private default constructor.
   */
  private DeveloperDataMapper() {
  }

  /**
   * To model.
   */
  public static DeveloperDataDefinitionView toView(DeveloperDataDefinition entity) {
    DeveloperDataDefinitionView model = new DeveloperDataDefinitionView();

    model.setId(entity.getId());
    model.setCreatedAt(entity.getCreatedAt());
    model.setLastModifiedAt(entity.getLastModifiedAt());
    model.setVersion(entity.getVersion());
    model.setDeveloperId(entity.getDeveloperId());
    model.setDataId(entity.getDataId());
    model.setName(entity.getName());
    model.setDescription(entity.getDescription());

    model.setSchema(entity.getDataSchema());

    return model;
  }

  /**
   * To Model list.
   */
  public static List<DeveloperDataDefinitionView> toView(List<DeveloperDataDefinition> entities) {
    List<DeveloperDataDefinitionView> models = Lists.newArrayList();

    entities.stream().forEach(entity -> models.add(toView(entity)));

    return models;
  }

  /**
   * To model.
   */
  public static DeveloperDataDefinition toModel(String developerId,
      DeveloperDataDefinitionDraft draft) {
    DeveloperDataDefinition entity = new DeveloperDataDefinition();

    entity.setDeveloperId(developerId);
    entity.setDataId(draft.getDataId());
    entity.setName(draft.getName());
    entity.setDescription(draft.getDescription());
    entity.setDataSchema(draft.getSchema());

    return entity;
  }

  /**
   * To model map.
   */
  public static Map<String, DeveloperDataDefinition> toModelMap(
      List<DeveloperDataDefinition> definitions) {
    Map<String, DeveloperDataDefinition> entityMap =
        definitions.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));

    return entityMap;
  }
}
