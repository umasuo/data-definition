package com.umasuo.datapoint.application.dto;

import com.umasuo.datapoint.infrastructure.definition.PointType;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;

/**
 * 创建新的数据格点定义的时候需要上传的定义。
 * Created by umasuo on 17/3/8.
 */
@Data
public class DataDefinitionDraft implements Serializable {

  /**
   * 每个数据格点的定义都属于一个具体的开发者，需要确保开发者确实存在且已经登陆。
   */
  @NotNull
  private String developerId;

  /**
   * 数据格点ID，需要对开发者唯一，例如: s001。
   */
  @NotNull
  private String dataId;

  /**
   * 数据具体的结构.
   */
  @NotNull
  private PointType dataType;

  /**
   * 数据定义的名称，例如"手环步数数据"
   */
  @NotNull
  private String name;

  /**
   * 数据定义介绍，主要用于介绍此数据格点的用途，目的等。
   */
  @NotNull
  private String description;
}
