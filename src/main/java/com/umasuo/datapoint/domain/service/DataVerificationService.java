package com.umasuo.datapoint.domain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umasuo.datapoint.domain.model.DataDefinition;
import com.umasuo.datapoint.infrastructure.definition.ArrayType;
import com.umasuo.datapoint.infrastructure.definition.IntType;
import com.umasuo.datapoint.infrastructure.definition.ObjectType;
import com.umasuo.datapoint.infrastructure.definition.PointType;
import com.umasuo.datapoint.infrastructure.definition.StringType;
import com.umasuo.datapoint.infrastructure.util.JsonUtils;
import com.umasuo.exception.ParametersException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
    PointType dataType = JsonUtils.deserialize(dataDefinition.getDataType(), PointType.class);

    return verify(dataType, data);
  }

  /**
   * verify PointType
   *
   * @param dataType
   * @param value
   * @return
   */
  public boolean verify(PointType dataType, JsonNode value) {
    if (dataType instanceof IntType) {
      return value.isInt();
    }

    if (dataType instanceof StringType) {
      return value.isTextual();
    }

    if (dataType instanceof ArrayType) {
      return verifyArray((ArrayType) dataType, value);
    }

    if (dataType instanceof ObjectType) {
      return verifyObject((ObjectType) dataType, value);
    }
    return false;
  }

  /**
   * verify array.
   *
   * @param dataType
   * @param value
   * @return
   */
  private boolean verifyArray(ArrayType dataType, JsonNode value) {
    PointType subType = dataType.getSubType();
    if (value.isArray() && value.size() > 0) {
      return verify(subType, value.get(0));
    }
    return false;
  }

  /**
   * verify object type.
   *
   * @param dataType
   * @param value
   * @return
   */
  private boolean verifyObject(ObjectType dataType, JsonNode value) {
    for (PointType subType : dataType.getSubTypes()) {
      String name = subType.getName();
      boolean result = verify(subType, value.get(name));
      //快速失败，不要再往下检查
      if (!result) {
        return false;
      }
    }
    return true;
  }
}
