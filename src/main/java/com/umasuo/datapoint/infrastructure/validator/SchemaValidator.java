package com.umasuo.datapoint.infrastructure.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.umasuo.exception.ParametersException;
import com.umasuo.util.JsonUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SchemaValidator.
 */
public final class SchemaValidator {

  /**
   * Logger.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(SchemaValidator.class);

  /**
   * Instantiates a new Schema validator.
   */
  private SchemaValidator() {
  }

  /**
   * Validate json schema.
   *
   * @param schema
   */
  public static void validate(String schema) {
    try {
      JsonNode jsonNode = JsonUtils.deserialize(schema, JsonNode.class);
      JsonSchemaFactory.byDefault().getJsonSchema(jsonNode);
    } catch (ProcessingException e) {
      LOGGER.trace("DataDefinition is not a validator JsonSchema.", e);
      throw new ParametersException("DataDefinition is not a validator JsonSchema.");
    }
  }
}
