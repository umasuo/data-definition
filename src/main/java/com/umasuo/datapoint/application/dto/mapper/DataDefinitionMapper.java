package com.umasuo.datapoint.application.dto.mapper;

import com.google.common.collect.Lists;
import com.umasuo.datapoint.application.dto.DataDefinitionDraft;
import com.umasuo.datapoint.application.dto.DataDefinitionView;
import com.umasuo.datapoint.domain.model.DataDefinition;
import com.umasuo.datapoint.infrastructure.definition.PointType;
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
  public static List<DataDefinitionView> toModel(List<DataDefinition> entities) {
    List<DataDefinitionView> models = Lists.newArrayList();

    Consumer<DataDefinition> consumer = dataDefinition -> models.add(toModel(dataDefinition));

    entities.stream().forEach(consumer);

    return models;
  }

  /**
   * To model data definition view.
   *
   * @param model the model
   * @return the data definition view
   */
  public static DataDefinitionView toModel(DataDefinition model) {
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

      view.setDataType(JsonUtils.deserialize(model.getDataType(), PointType.class));
    }
    return view;
  }

  /**
   * 根据draft 创建数据定义model.
   *
   * @param draft
   * @return
   */
  public static DataDefinition viewToModel(DataDefinitionDraft draft, String developerId) {

    DataDefinition model = null;
    if (draft != null) {
      model = new DataDefinition();
      model.setDeveloperId(developerId);
      model.setDataId(draft.getDataId());
      model.setName(draft.getName());
      model.setDescription(draft.getDescription());
      model.setDataType(JsonUtils.serialize(draft.getDataType()));
      model.setOpenable(draft.getOpenable());
    }
    return model;
  }
}
