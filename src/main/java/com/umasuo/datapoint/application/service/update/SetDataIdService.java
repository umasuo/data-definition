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
 * Updater for SetDataIdService.
 */
@Service(UpdateActionUtils.SET_DATA_ID)
public class SetDataIdService implements Updater<DeviceDataDefinition, UpdateAction> {

  /**
   * Logger.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(SetDataIdService.class);

  /**
   * Update data definition id.
   *
   * @param deviceDataDefinition
   * @param updateAction
   */
  @Override
  public void handle(DeviceDataDefinition deviceDataDefinition, UpdateAction updateAction) {
    LOGGER.debug("Enter.");

    String dataId = ((SetDataId) updateAction).getDataId();

    // TODO: 17/7/4 要查询一下数据看是否存在该dataId
    deviceDataDefinition.setDataId(dataId);

    LOGGER.debug("Exit.");
  }
}
