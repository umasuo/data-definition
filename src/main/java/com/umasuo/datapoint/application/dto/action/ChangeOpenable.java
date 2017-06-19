package com.umasuo.datapoint.application.dto.action;

import com.umasuo.datapoint.infrastructure.update.UpdateAction;
import com.umasuo.datapoint.infrastructure.update.UpdateActionUtils;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Created by Davis on 17/6/19.
 */
@Getter
@Setter
public class ChangeOpenable implements UpdateAction {

  /**
   * The openable.
   */
  @NotNull
  private Boolean openable;

  /**
   * Get action name.
   *
   * @return string with value 'changeOpenable'
   */
  @Override
  public String getActionName() {
    return UpdateActionUtils.CHANGE_OPENABLE;
  }
}
