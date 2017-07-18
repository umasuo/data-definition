package com.umasuo.datapoint.application.dto;

import com.umasuo.datapoint.infrastructure.enums.Category;

import lombok.Data;

import java.io.Serializable;

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
   * The Openable.
   * True means other developers can find this data, false means not.
   */
  private Boolean openable;

  private Category category;
}
