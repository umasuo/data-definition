package com.umasuo.datapoint.application.service.update;

import com.umasuo.datapoint.application.dto.action.UpdateDataDefinition;
import com.umasuo.datapoint.domain.model.DeviceDataDefinition;
import com.umasuo.datapoint.infrastructure.update.UpdateAction;
import com.umasuo.datapoint.infrastructure.update.UpdateActionUtils;
import com.umasuo.datapoint.infrastructure.validator.SchemaValidator;
import com.umasuo.model.Updater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Update data definition service.
 */
@Service(UpdateActionUtils.UPDATE_DATA_DEFINITION)
public class UpdateDataService implements Updater<DeviceDataDefinition, UpdateAction> {

  /**
   * Logger.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(SetSchemaService.class);

  /**
   * Update data definition.
   */
  @Override
  public void handle(DeviceDataDefinition deviceDataDefinition, UpdateAction updateAction) {
    LOGGER.debug("Enter.");

    UpdateDataDefinition action = (UpdateDataDefinition) updateAction;

    String schema = action.getSchema();

    SchemaValidator.validate(schema);

    deviceDataDefinition.setDataSchema(schema);

    deviceDataDefinition.setDescription(action.getDescription());
    deviceDataDefinition.setName(action.getName());
    deviceDataDefinition.setDataId(action.getDataId());
    deviceDataDefinition.setOpenable(action.getOpenable());

    LOGGER.debug("Exit.");
  }
}