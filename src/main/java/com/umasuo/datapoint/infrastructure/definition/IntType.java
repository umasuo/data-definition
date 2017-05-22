package com.umasuo.datapoint.infrastructure.definition;

import lombok.Data;

/**
 * Created by umasuo on 17/3/8.
 */
@Data
public class IntType implements PointType {

  private static final long serialVersionUID = 51287892165385780L;
  /**
   * filed name.
   */
  private String key;

  private String type = TypeUtil.INT_TYPE;

}
