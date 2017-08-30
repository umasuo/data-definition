package com.umasuo.datapoint.application.dto;

import lombok.Data;

/**
 * PlatformDataDefinitionView.
 */
@Data
public final class PlatformDataDefinitionView {

  /**
   * auto generated uuid.
   */
  private String id;

  /**
   * The Created at.
   */
  private Long createdAt;

  /**
   * The Last modified at.
   */
  private Long lastModifiedAt;

  /**
   * version used for update date check.
   */
  private Integer version;

  /**
   * data id defined by the developer.
   */
  private String dataId;

  /**
   * the data structure.
   */
  private String schema;

  /**
   * name of this definition.
   */
  private String name;

  /**
   * describe the usage of this definition.
   */
  private String description;

  /**
   * Product type id.
   */
  private String productTypeId;
}
