package com.umasuo.datapoint.application.dto;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by umasuo on 17/3/8.
 */
@Data
public class PlatformDataDefinitionView implements Serializable {

  /**
   * auto generated serial id.
   */
  private static final long serialVersionUID = 7500245666736988395L;

  /**
   * auto generated uuid.
   */
  private String id;

  /**
   * The Created at.
   */
  protected Long createdAt;

  /**
   * The Last modified at.
   */
  protected Long lastModifiedAt;

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
  private JsonNode dataSchema;

  /**
   * name of this definition.
   */
  private String name;

  /**
   * describe the usage of this definition.
   */
  private String description;

  private String productTypeId;
}
