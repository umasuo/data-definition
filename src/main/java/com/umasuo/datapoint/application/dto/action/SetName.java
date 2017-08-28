package com.umasuo.datapoint.application.dto.action;

import com.umasuo.datapoint.infrastructure.update.UpdateAction;
import com.umasuo.datapoint.infrastructure.update.UpdateActionUtils;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Updater for name.
 */
@Data
public class SetName implements UpdateAction {

  /**
   * Data name.
   */
  @NotNull
  private String name;

  /**
   * Get action name.
   * @return
   */
  @Override
  public String getActionName() {
    return UpdateActionUtils.SET_NAME;
  }
}
