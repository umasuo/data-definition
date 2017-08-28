package com.umasuo.datapoint.application.service.update;

import com.umasuo.datapoint.application.dto.action.SetDescription;
import com.umasuo.datapoint.domain.model.DeviceDataDefinition;
import com.umasuo.datapoint.infrastructure.update.UpdateAction;
import com.umasuo.datapoint.infrastructure.update.UpdateActionUtils;
import com.umasuo.model.Updater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Update service for description.
 */
@Service(UpdateActionUtils.SET_DESCRIPTION)
public class SetDescriptionService implements Updater<DeviceDataDefinition, UpdateAction> {

  /**
   * Logger.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(SetDescriptionService.class);

  /**
   * Update data definition definition.
   *
   * @param deviceDataDefinition
   * @param updateAction
   */
  @Override
  public void handle(DeviceDataDefinition deviceDataDefinition, UpdateAction updateAction) {
    LOGGER.debug("Enter.");

    String description = ((SetDescription) updateAction).getDescription();
    deviceDataDefinition.setDescription(description);

    LOGGER.debug("Exit.");
  }
}
