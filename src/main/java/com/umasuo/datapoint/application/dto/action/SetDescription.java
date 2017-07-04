package com.umasuo.datapoint.application.dto.action;

import com.umasuo.datapoint.infrastructure.update.UpdateAction;
import com.umasuo.datapoint.infrastructure.update.UpdateActionUtils;

import lombok.Data;

/**
 * Created by Davis on 17/7/4.
 */
@Data
public class SetDescription implements UpdateAction{

  private String description;

  @Override
  public String getActionName() {
    return UpdateActionUtils.SET_DESCRIPTION;
  }
}
