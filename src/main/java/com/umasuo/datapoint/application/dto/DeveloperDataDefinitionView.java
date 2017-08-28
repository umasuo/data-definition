package com.umasuo.datapoint.application.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * DeveloperDataDefinitionView.
 */
@Data
public class DeveloperDataDefinitionView {

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
   * which developer this data definition belong to.
   */
  private String developerId;

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
}
