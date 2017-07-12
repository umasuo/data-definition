package com.umasuo.datapoint.infrastructure.update;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.umasuo.datapoint.application.dto.action.ChangeOpenable;
import com.umasuo.datapoint.application.dto.action.SetDataId;
import com.umasuo.datapoint.application.dto.action.SetDescription;
import com.umasuo.datapoint.application.dto.action.SetName;
import com.umasuo.datapoint.application.dto.action.SetSchema;
import com.umasuo.datapoint.application.dto.action.UpdateDataDefinition;

import java.io.Serializable;

/**
 * configurations for common update actions that will be used in more thant one service
 * and this action also extends other action configure in each service.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property =
    "action")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ChangeOpenable.class, name = UpdateActionUtils.CHANGE_OPENABLE),
    @JsonSubTypes.Type(value = SetDataId.class, name = UpdateActionUtils.SET_DATA_ID),
    @JsonSubTypes.Type(value = SetSchema.class, name = UpdateActionUtils.SET_SCHEMA),
    @JsonSubTypes.Type(value = SetName.class, name = UpdateActionUtils.SET_NAME),
    @JsonSubTypes.Type(value = SetDescription.class, name = UpdateActionUtils.SET_DESCRIPTION),
    @JsonSubTypes.Type(value = UpdateDataDefinition.class,
        name = UpdateActionUtils.UPDATE_DATA_DEFINITION),
})
public interface UpdateAction extends Serializable {

  /**
   * get action name.
   *
   * @return name in string.
   */
  String getActionName();
}
