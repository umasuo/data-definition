package com.umasuo.datapoint.infrastructure.definition;

import lombok.Data;

/**
 * Created by umasuo on 17/3/8.
 */
@Data
public class StringType implements PointType {

  private static final long serialVersionUID = 8012592802915478194L;

  /**
   * filed name.
   */
  private String key;

  private String type = "type.string";

}
