package com.umasuo.datapoint.infrastructure.validator;

import com.umasuo.exception.NotExistException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Davis on 17/8/11.
 */
public final class DefinitionValidator {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(DefinitionValidator.class);

  /**
   * Instantiates a new Definition validator.
   */
  public DefinitionValidator() {
  }

  public static void validateDeveloper(String developerId, String requestDeveloper, String id) {
    if (!requestDeveloper.equals(developerId)) {
      LOG.debug("DataDefinition: {} not belong to developer: {}.", id, developerId);
      throw new NotExistException("DataDefinition: " + id +
          " not belong to developer: " + developerId);
    }
  }

  public static void validateProduct(String productId, String requireProduct, String id) {
    if (!requireProduct.equals(productId)) {
      LOG.debug("DataDefinition: {} is not belong to product: {}.", id, productId);
      throw new NotExistException("Product do not have this dataDefinition.");
    }
  }
}
