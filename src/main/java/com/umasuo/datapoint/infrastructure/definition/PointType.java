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
    @JsonSubTypes.Type(value = IntType.class, name = "type.int"),
    @JsonSubTypes.Type(value = StringType.class, name = "type.string"),
    @JsonSubTypes.Type(value = ArrayType.class, name = "type.array"),
    @JsonSubTypes.Type(value = ObjectType.class, name = "type.object"),
})
public interface PointType extends Serializable {
  /**
   * filed name of the data.
   *
   * @return
   */
  String getName();
}
