package com.umasuo.datapoint.infrastructure.definition;

import lombok.Data;

/**
 * Created by umasuo on 17/3/8.
 */
@Data
public class ArrayType implements PointType {

  private static final long serialVersionUID = -5818301375489100645L;
  /**
   * filed key, json数据里面的key.
   */
  private String key;

  /**
   * type name.
   */
  private String type = "type.array";

  /**
   * Sub Types Type.
   */
  private PointType subType;

}
