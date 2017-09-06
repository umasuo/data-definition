package com.umasuo.datapoint.application.service.update;

import com.umasuo.datapoint.application.dto.action.SetSchema;
import com.umasuo.datapoint.domain.model.DeviceDataDefinition;
import com.umasuo.datapoint.infrastructure.update.UpdateAction;
import com.umasuo.datapoint.infrastructure.update.UpdateActionUtils;
import com.umasuo.datapoint.infrastructure.validator.SchemaValidator;
import com.umasuo.model.Updater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Update service for schema.
 */
@Service(UpdateActionUtils.SET_SCHEMA)
public class SetSchemaService implements Updater<DeviceDataDefinition, UpdateAction> {

  /**
   * Logger.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(SetSchemaService.class);

  /**
   * Update data definition's schema.
   */
  @Override
  public void handle(DeviceDataDefinition deviceDataDefinition, UpdateAction updateAction) {
    LOGGER.debug("Enter.");

    String schema = ((SetSchema) updateAction).getSchema();

    SchemaValidator.validate(schema);

    deviceDataDefinition.setDataSchema(schema);

    LOGGER.debug("Exit.");
  }
}
