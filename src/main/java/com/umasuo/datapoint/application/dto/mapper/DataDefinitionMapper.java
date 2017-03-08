package com.umasuo.datapoint.application.dto.mapper;

import com.umasuo.datapoint.application.dto.DataDefinitionView;
import com.umasuo.datapoint.domain.model.DataDefinition;
import com.umasuo.datapoint.infrastructure.definition.PointType;
import com.umasuo.datapoint.infrastructure.util.JsonUtils;

/**
 * Created by umasuo on 17/3/8.
 */
public class DataDefinitionMapper {

  public static DataDefinitionView modelToView(DataDefinition model) {
    DataDefinitionView view = null;
    if (model != null) {
      view = new DataDefinitionView();
      view.setId(model.getId());
      view.setCreatedAt(model.getCreatedAt());
      view.setLastModifiedAt(model.getLastModifiedAt());
      view.setVersion(model.getVersion());
      view.setDeveloperId(model.getDeveloperId());
      view.setDeviceDefinitionId(model.getDeviceDefinitionId());
      view.setDataId(model.getDataId());
      view.setName(model.getName());
      view.setDescription(model.getDescription());

      view.setDataType(JsonUtils.deserialize(model.getDataType(), PointType.class));
    }
    return view;
  }

  public static DataDefinition viewToModel(DataDefinitionView view) {
    DataDefinition model = null;
    if (view != null) {
      model = new DataDefinition();
      model.setId(view.getId());
      model.setCreatedAt(view.getCreatedAt());
      model.setLastModifiedAt(view.getLastModifiedAt());
      model.setVersion(view.getVersion());
      model.setDeveloperId(view.getDeveloperId());
      model.setDeviceDefinitionId(view.getDeviceDefinitionId());
      model.setDataId(view.getDataId());
      model.setName(view.getName());
      model.setDescription(view.getDescription());

      model.setDataType(JsonUtils.serialize(view.getDataType()));
    }
    return model;
  }
}
