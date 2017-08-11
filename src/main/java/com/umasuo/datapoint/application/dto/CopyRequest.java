package com.umasuo.datapoint.application.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * 拷贝数据的请求，platformDataDefinitionIds和developerDataDefinitionIds不能同时为空。
 */
@Data
public class CopyRequest implements Serializable {

  private static final long serialVersionUID = 761761676157756311L;

  /**
   * The productId.
   */
  @NotNull
  private String productId;

  /**
   * 平台定义的产品数据。
   */
  private List<String> platformDataDefinitionIds;

  /**
   * 开发者自定义的公用数据。
   */
  private List<String> developerDataDefinitionIds;
}
