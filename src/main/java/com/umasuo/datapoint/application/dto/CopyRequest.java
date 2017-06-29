package com.umasuo.datapoint.application.dto;

import lombok.Data;

import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * Created by Davis on 17/6/29.
 */
@Data
public class CopyRequest {

  @NotNull
  private String deviceDefinitionId;

  private List<String> platformDataDefinitionIds;

  private List<String> developerDataDefinitionIds;
}
