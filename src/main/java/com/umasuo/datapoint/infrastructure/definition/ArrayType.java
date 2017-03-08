package com.umasuo.datapoint.infrastructure.definition;

import lombok.Data;

/**
 * Created by umasuo on 17/3/8.
 */
@Data
public class ArrayType implements PointType {

  /**
   * filed name.
   */
  private String name;

  /**
   * type name.
   */
  private String type = "type.array";

  /**
   * Sub Types Type.
   */
  private PointType subType;

}
