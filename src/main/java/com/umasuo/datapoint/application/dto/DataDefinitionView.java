package com.umasuo.datapoint.application.dto;

import com.umasuo.datapoint.infrastructure.definition.PointType;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;

/**
 * Created by umasuo on 17/3/8.
 */
@Data
public class DataDefinitionView implements Serializable {

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
  protected ZonedDateTime createdAt;

  /**
   * The Last modified at.
   */
  protected ZonedDateTime lastModifiedAt;

  /**
   * version used for update date check.
   */
  private Integer version;

  /**
   * which developer this data definition belong to.
   */
  @NotNull
  private String developerId;

  /**
   * data id defined by the developer.
   */
  @NotNull
  private String dataId;

  /**
   * the data structure.
   */
  @NotNull
  private PointType dataType;

  /**
   * name of this definition.
   */
  @NotNull
  private String name;

  /**
   * describe the usage of this definition.
   */
  @NotNull
  private String description;
}
