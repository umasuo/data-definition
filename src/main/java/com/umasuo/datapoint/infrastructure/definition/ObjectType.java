package com.umasuo.datapoint.infrastructure.definition;

import lombok.Data;

import java.util.List;

/**
 * Created by umasuo on 17/3/8.
 */
@Data
public class ObjectType implements PointType {

  /**
   * filed name.
   */
  private String name;

  /**
   * type name.
   */
  private String type = "type.object";

  /**
   * Sub Types Type.
   */
  private List<PointType> subTypes;

}
