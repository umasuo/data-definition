package com.umasuo.datapoint.application.dto;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by Davis on 17/6/30.
 */
@Data
public class DeveloperDataDefinitionDraft {

  @NotNull(message = "Name can not be null")
  private String name;

  @NotNull(message = "DataId can not be null")
  private String dataId;

  private String description;

  @NotNull(message = "DataSchema can not be null")
  private JsonNode dataSchema;

}
