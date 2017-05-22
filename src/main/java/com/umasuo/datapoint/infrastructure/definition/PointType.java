package com.umasuo.datapoint.infrastructure.definition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

/**
 * Created by umasuo on 17/3/8.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property =
    "type")
@JsonSubTypes( {
    @JsonSubTypes.Type(value = IntType.class, name = TypeUtil.INT_TYPE),
    @JsonSubTypes.Type(value = NumberType.class, name = TypeUtil.NUMBER_TYPE),
    @JsonSubTypes.Type(value = StringType.class, name = TypeUtil.STRING_TYPE),
    @JsonSubTypes.Type(value = ArrayType.class, name = TypeUtil.ARRAY_TYPE),
    @JsonSubTypes.Type(value = ObjectType.class, name = TypeUtil.OBJECT_TYPE),
})
public interface PointType extends Serializable {

  /**
   * filed name of the data.
   *
   * @return
   */
  String getKey();
}
