package com.umasuo.datapoint.infrastructure.definition;

import lombok.Data;

/**
 * Created by umasuo on 17/3/8.
 */
@Data
public class IntType implements PointType {

  /**
   * filed name.
   */
  private String name;

  private String type = "type.int";

}
