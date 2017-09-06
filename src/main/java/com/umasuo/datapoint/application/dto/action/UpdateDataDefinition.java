package com.umasuo.datapoint.application.dto.action;

import com.umasuo.datapoint.infrastructure.update.UpdateAction;
import com.umasuo.datapoint.infrastructure.update.UpdateActionUtils;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * UpdateDataDefinition.
 */
@Data
public class UpdateDataDefinition implements UpdateAction {

  /**
   * Data definition id.
   */
  @NotNull
  private String dataId;

  /**
   * Data definition name.
   */
  @NotNull
  private String name;

  /**
   * Data definition json schema.
   */
  @NotNull
  private String schema;

  /**
   * Data definition description.
   */
  @NotNull
  private String description;

  /**
   * If this data definition is openable.
   */
  @NotNull
  private Boolean openable;

  /**
   * Get action name.
   *
   * @return
   */
  @Override
  public String getActionName() {
    return UpdateActionUtils.UPDATE_DATA_DEFINITION;
  }
}
