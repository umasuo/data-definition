package com.umasuo.datapoint.domain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.common.collect.Maps;
import com.umasuo.datapoint.domain.model.DeviceDataDefinition;
import com.umasuo.datapoint.infrastructure.util.JsonUtils;
import com.umasuo.exception.ParametersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
   */
  public boolean verify(String id, JsonNode data) {
    DeviceDataDefinition dataDefinition = dataDefinitionService.getById(id);
    JsonNode dataType = JsonUtils.deserialize(dataDefinition.getDataSchema(), JsonNode.class);

    return verify(dataType, data);
  }

  /**
   * verify JsonNode data with developer id and data id.
   */
  public boolean verify(String developerId, String dataId, JsonNode data) {
    DeviceDataDefinition dataDefinition = dataDefinitionService.getByDataId(developerId, dataId);
    JsonNode dataType = JsonUtils.deserialize(dataDefinition.getDataSchema(), JsonNode.class);

    return verify(dataType, data);
  }

  /**
   * verify PointType
   */
  public boolean verify(JsonNode dataSchema, JsonNode value) {
    try {
      JsonSchema schema = JsonSchemaFactory.byDefault().getJsonSchema(dataSchema);
      return schema.validate(value).isSuccess();
    } catch (ProcessingException ex) {
      return false;
    }
  }

  /**
   * Check if DeviceDataDefinition exist and belong to developer.
   *
   * @param developerId the developer id
   * @param definitionIds the DeviceDataDefinition id list
   * @return a map of result, key is the DeviceDataDefinition's id, and value is the exist result, if a
   * DeviceDataDefinition not exist or not belong to the developer, value is false.
   */
  public Map<String, Boolean> isExistDefinition(String developerId, List<String> definitionIds) {
    logger.debug("Enter. developerId: {}, dataIds: {}.", developerId, definitionIds);

    List<DeviceDataDefinition> valueInDb = dataDefinitionService.getDeveloperDefinition(developerId);

    Map result = checkExistDefinition(valueInDb, definitionIds);

    logger.debug("Exit. result: {}.", result);

    return result;
  }


  /**
   * Check if DeviceDataDefinition exist and belong to developer.
   *
   * @param definitions DeviceDataDefinition list
   * @param dataIds the DeviceDataDefinition id list
   * @return a map of result, key is the DeviceDataDefinition's id, and value is the exist result, if a
   * DeviceDataDefinition not exist or not belong to the developer, value is false.
   */
  private Map checkExistDefinition(List<DeviceDataDefinition> definitions, List<String> dataIds) {

    Map<String, Boolean> result = Maps.newHashMap();

    dataIds.stream().forEach(s -> result.put(s, false));

    Consumer<DeviceDataDefinition> consumer = dataDefinition -> {
      if (dataIds.contains(dataDefinition.getId())) {
        result.replace(dataDefinition.getId(), true);
      }
    };

    definitions.stream().forEach(consumer);

    return result;
  }
}
