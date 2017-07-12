package com.umasuo.datapoint.application.dto.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.umasuo.datapoint.infrastructure.update.UpdateAction;
import com.umasuo.datapoint.infrastructure.update.UpdateActionUtils;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by Davis on 17/7/12.
 */
@Data
public class UpdateDataDefinition implements UpdateAction {

  @NotNull
  private String dataId;

  @NotNull
  private String name;

  @NotNull
  private JsonNode dataSchema;

  @NotNull
  private String description;

  @NotNull
  private Boolean openable;

  @Override
  public String getActionName() {
    return UpdateActionUtils.UPDATE_DATA_DEFINITION;
  }
}
