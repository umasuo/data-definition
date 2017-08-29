package com.umasuo.datapoint.application.dto.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.umasuo.datapoint.infrastructure.update.UpdateAction;
import com.umasuo.datapoint.infrastructure.update.UpdateActionUtils;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by Davis on 17/7/12.
 */
@Data
public class UpdateProductTypeData implements UpdateAction {

  /**
   * The id.
   */
  @NotNull
  private String dataDefinitionId;

  /**
   * The name.
   */
  @NotNull
  private String name;

  /**
   * The data schema.
   */
  @NotNull
  private JsonNode dataSchema;

  /**
   * The description.
   */
  private String description;

  /**
   * The version.
   */
  @NotNull
  private Integer version;

  /**
   * Get action name.
   *
   * @return updateProductTypeData
   */
  @Override
  public String getActionName() {
    return UpdateActionUtils.UPDATE_PRODUCT_TYPE_DATA;
  }
}
