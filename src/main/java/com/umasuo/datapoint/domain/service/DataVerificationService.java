package com.umasuo.datapoint.domain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.umasuo.datapoint.domain.model.DataDefinition;
import com.umasuo.datapoint.infrastructure.util.JsonUtils;
import com.umasuo.exception.ParametersException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by umasuo on 17/3/8.
 */
@Service
public class DataVerificationService {

  /**
   * logger.
   */
  private final static Logger logger = LoggerFactory.getLogger(DataDefinitionService.class);

  /**
   * mapper for json node.
   */
  private ObjectMapper mapper = new ObjectMapper();

  /**
   * Data definition service, used to fetch data definition.
   */
  @Autowired
  private transient DataDefinitionService dataDefinitionService;

  /**
   * verify json string data.
   *
   * @param id
   * @param data
   * @return
   */
  public boolean verify(String id, String data) {
    try {
      JsonNode value = mapper.readTree(data);
      return verify(id, value);
    } catch (Exception e) {
      throw new ParametersException("Data is not a validate json.");
    }
  }

  /**
   * verify JsonNode data.
   *
   * @param id
   * @param data
   * @return
   */
  public boolean verify(String id, JsonNode data) {
    DataDefinition dataDefinition = dataDefinitionService.getById(id);
    JsonNode dataType = JsonUtils.deserialize(dataDefinition.getDataSchema(), JsonNode.class);

    return verify(dataType, data);
  }

  /**
   * verify JsonNode data with developer id and data id.
   *
   * @param dataId
   * @param data
   * @return
   */
  public boolean verify(String developerId, String dataId, JsonNode data) {
    DataDefinition dataDefinition = dataDefinitionService.getByDataId(developerId, dataId);
    JsonNode dataType = JsonUtils.deserialize(dataDefinition.getDataSchema(), JsonNode.class);

    return verify(dataType, data);
  }

  /**
   * verify PointType
   *
   * @param dataSchema
   * @param value
   * @return
   */
  public boolean verify(JsonNode dataSchema, JsonNode value) {
    try {
      JsonSchema schema = JsonSchemaFactory.byDefault().getJsonSchema(dataSchema);
      return schema.validate(value).isSuccess();
    } catch (ProcessingException ex) {
      return false;
    }
  }

}
