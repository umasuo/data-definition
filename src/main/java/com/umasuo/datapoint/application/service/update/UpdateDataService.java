package com.umasuo.datapoint.application.service.update;

import com.fasterxml.jackson.databind.JsonNode;
import com.umasuo.datapoint.application.dto.action.SetSchema;
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
 * Created by Davis on 17/7/12.
 */
@Service(UpdateActionUtils.UPDATE_DATA_DEFINITION)
public class UpdateDataService implements Updater<DeviceDataDefinition, UpdateAction> {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(SetSchemaService.class);

  @Override
  public void handle(DeviceDataDefinition deviceDataDefinition, UpdateAction updateAction) {
    LOG.debug("Enter.");

    UpdateDataDefinition action = (UpdateDataDefinition) updateAction;

    JsonNode schema = action.getDataSchema();

    SchemaValidator.validate(schema);

    deviceDataDefinition.setDataSchema(schema.toString());

    deviceDataDefinition.setDescription(action.getDescription());
    deviceDataDefinition.setName(action.getName());
    deviceDataDefinition.setDataId(action.getDataId());
    deviceDataDefinition.setOpenable(action.getOpenable());

    LOG.debug("Exit.");
  }
}