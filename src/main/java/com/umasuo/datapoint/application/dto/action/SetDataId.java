package com.umasuo.datapoint.application.dto.action;

import com.umasuo.datapoint.infrastructure.update.UpdateAction;
import com.umasuo.datapoint.infrastructure.update.UpdateActionUtils;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by Davis on 17/7/4.
 */
@Data
public class SetDataId implements UpdateAction {

  @NotNull
  private String dataId;

  @Override
  public String getActionName() {
    return UpdateActionUtils.SET_DATA_ID;
  }
}
