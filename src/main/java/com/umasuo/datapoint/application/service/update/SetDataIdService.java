package com.umasuo.datapoint.application.service.update;

import com.umasuo.datapoint.application.dto.action.SetDataId;
import com.umasuo.datapoint.domain.model.DeviceDataDefinition;
import com.umasuo.datapoint.infrastructure.update.UpdateAction;
import com.umasuo.datapoint.infrastructure.update.UpdateActionUtils;
import com.umasuo.model.Updater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Davis on 17/7/4.
 */
@Service(UpdateActionUtils.SET_DATA_ID)
public class SetDataIdService implements Updater<DeviceDataDefinition, UpdateAction> {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(SetDataIdService.class);

  @Override
  public void handle(DeviceDataDefinition deviceDataDefinition, UpdateAction updateAction) {
    LOG.debug("Enter.");

    String dataId = ((SetDataId) updateAction).getDataId();

    // TODO: 17/7/4 要查询一下数据看是否存在该dataId
    deviceDataDefinition.setDataId(dataId);

    LOG.debug("Exit.");
  }
}
