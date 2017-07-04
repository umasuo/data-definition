package com.umasuo.datapoint.application.dto.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.umasuo.datapoint.infrastructure.update.UpdateAction;
import com.umasuo.datapoint.infrastructure.update.UpdateActionUtils;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by Davis on 17/7/4.
 */
@Data
public class SetSchema implements UpdateAction{

  @NotNull
  private JsonNode schema;

  @Override
  public String getActionName() {
    return UpdateActionUtils.SET_SCHEMA;
  }
}
