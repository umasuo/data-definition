package com.umasuo.datapoint.application.dto.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.umasuo.datapoint.infrastructure.update.UpdateAction;
import com.umasuo.datapoint.infrastructure.update.UpdateActionUtils;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Updater for data schema.
 */
@Data
public class SetSchema implements UpdateAction {

  /**
   * Schema in json.
   */
  @NotNull
  private JsonNode schema;

  /**
   * Get action name.
   *
   * @return
   */
  @Override
  public String getActionName() {
    return UpdateActionUtils.SET_SCHEMA;
  }
}
