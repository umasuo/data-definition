package com.umasuo.datapoint.application.dto.action;

import com.umasuo.datapoint.infrastructure.update.UpdateAction;
import com.umasuo.datapoint.infrastructure.update.UpdateActionUtils;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Updater for data id.
 */
@Data
public class SetDataId implements UpdateAction {

  /**
   * Data id in String.
   */
  @NotNull
  private String dataId;

  /**
   * Action name.
   *
   * @return
   */
  @Override
  public String getActionName() {
    return UpdateActionUtils.SET_DATA_ID;
  }
}
