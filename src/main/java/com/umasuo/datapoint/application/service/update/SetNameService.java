package com.umasuo.datapoint.application.service.update;

import com.umasuo.datapoint.application.dto.action.SetName;
import com.umasuo.datapoint.domain.model.DeveloperDataDefinition;
import com.umasuo.datapoint.infrastructure.update.UpdateAction;
import com.umasuo.datapoint.infrastructure.update.UpdateActionUtils;
import com.umasuo.model.Updater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Davis on 17/7/4.
 */
@Service(UpdateActionUtils.SET_NAME)
public class SetNameService implements Updater<DeveloperDataDefinition, UpdateAction> {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(SetNameService.class);

  @Override
  public void handle(DeveloperDataDefinition dataDefinition, UpdateAction updateAction) {
    LOG.debug("Enter.");

    String name = ((SetName) updateAction).getName();

    // TODO: 17/7/4 需要查询数据库判断是否重复
    dataDefinition.setName(name);

    LOG.debug("Exit.");
  }
}
