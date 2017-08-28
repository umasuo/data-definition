package com.umasuo.datapoint.infrastructure.validator;

import com.umasuo.exception.NotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DefinitionValidator.
 */
public final class DefinitionValidator {

  /**
   * Logger.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(DefinitionValidator.class);

  /**
   * Instantiates a new Definition validator.
   */
  private DefinitionValidator() {
  }

  /**
   * Validate developer.
   *
   * @param developerId
   * @param requestDeveloper
   * @param id
   */
  public static void validateDeveloper(String developerId, String requestDeveloper, String id) {
    if (!requestDeveloper.equals(developerId)) {
      LOGGER.debug("DataDefinition: {} not belong to developer: {}.", id, developerId);
      throw new NotExistException("DataDefinition: " + id +
          " not belong to developer: " + developerId);
    }
  }

  /**
   * Validate product.
   *
   * @param productId
   * @param requireProduct
   * @param id
   */
  public static void validateProduct(String productId, String requireProduct, String id) {
    if (!requireProduct.equals(productId)) {
      LOGGER.debug("DataDefinition: {} is not belong to product: {}.", id, productId);
      throw new NotExistException("Product do not have this dataDefinition.");
    }
  }
}
