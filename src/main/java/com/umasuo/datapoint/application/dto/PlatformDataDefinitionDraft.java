package com.umasuo.datapoint.application.dto;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * DeveloperDataDefinitionDraft.
 */
@Data
public class PlatformDataDefinitionDraft {

  /**
   * ProductType id.
   */
  private String productTypeId;

  /**
   * Data name.
   */
  @NotNull(message = "Name can not be null")
  private String name;

  /**
   * Data id.
   */
  @NotNull(message = "DataId can not be null")
  private String dataId;

  /**
   * Data description.
   */
  private String description;

  /**
   * Data Schema in json.
   */
  @NotNull(message = "DataSchema can not be null")
  private JsonNode dataSchema;

}
