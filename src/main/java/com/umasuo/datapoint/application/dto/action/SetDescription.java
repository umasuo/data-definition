package com.umasuo.datapoint.application.dto.action;

import com.umasuo.datapoint.infrastructure.update.UpdateAction;
import com.umasuo.datapoint.infrastructure.update.UpdateActionUtils;
import lombok.Data;

/**
 * Updater for  SetDescription.
 */
@Data
public class SetDescription implements UpdateAction {

  /**
   * Description for data definition.
   */
  private String description;

  /**
   * Get action name.
   *
   * @return
   */
  @Override
  public String getActionName() {
    return UpdateActionUtils.SET_DESCRIPTION;
  }
}
