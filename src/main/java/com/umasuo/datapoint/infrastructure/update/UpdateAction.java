package com.umasuo.datapoint.infrastructure.update;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.umasuo.datapoint.application.dto.action.ChangeOpenable;

import java.io.Serializable;

/**
 * configurations for common update actions that will be used in more thant one service
 * and this action also extends other action configure in each service.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property =
    "action")
@JsonSubTypes( {
    @JsonSubTypes.Type(value = ChangeOpenable.class, name = UpdateActionUtils
        .CHANGE_OPENABLE),
})
public interface UpdateAction extends Serializable {
  /**
   * get action name.
   *
   * @return name in string.
   */
  String getActionName();
}
