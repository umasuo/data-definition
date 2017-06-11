package com.umasuo.datapoint.application.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * 创建新的数据格点定义的时候需要上传的定义。
 * Created by umasuo on 17/3/8.
 */
@Data
public class DataDefinitionDraft implements Serializable {

  private static final long serialVersionUID = 5759775280956048065L;

  /**
   * 数据格点ID，需要对开发者唯一，例如: s001。
   */
  @NotNull
  private String dataId;

  /**
   * 数据定义的名称，例如"手环步数"
   */
  @NotNull
  private String name;

  /**
   * 数据定义介绍，主要用于介绍此数据格点的用途，目的等。
   */
  private String description;

  /**
   * 数据具体的结构.
   */
  @NotNull
  private JsonNode dataSchema;

  /**
   * The Openable.
   * True means other developers can find this data, false means not.
   * Default is false;
   */
  private Boolean openable;
}
