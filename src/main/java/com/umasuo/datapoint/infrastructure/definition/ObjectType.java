package com.umasuo.datapoint.infrastructure.definition;

import lombok.Data;

import java.util.List;

/**
 * Created by umasuo on 17/3/8.
 */
@Data
public class ObjectType implements PointType {

  private static final long serialVersionUID = -5215141627699079726L;
  /**
   * filed name.
   */
  private String key;

  /**
   * type name.
   */
  private String type = "type.object";

  /**
   * Sub Types Type，列表中的对象name需要唯一
   */
  private List<PointType> subTypes;

}
