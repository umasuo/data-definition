package com.umasuo.datapoint.domain.service.update;

import com.umasuo.datapoint.application.dto.action.ChangeOpenable;
import com.umasuo.datapoint.domain.model.DataDefinition;
import com.umasuo.datapoint.infrastructure.update.UpdateAction;
import com.umasuo.datapoint.infrastructure.update.UpdateActionUtils;
import com.umasuo.model.Updater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Davis on 17/6/19.
 */
@Service(UpdateActionUtils.CHANGE_OPENABLE)
public class ChangeOpenableService implements Updater<DataDefinition, UpdateAction>{

  /**
   * Change DataDefinition openable.
   *
   * @param dataDefinition the DataDefinition
   * @param updateAction the update action
   */
  @Override
  public void handle(DataDefinition dataDefinition, UpdateAction updateAction) {
    ChangeOpenable action = (ChangeOpenable) updateAction;
    Boolean openable = action.getOpenable();

    dataDefinition.setOpenable(openable);

  }
}
