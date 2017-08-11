package com.umasuo.datapoint.infrastructure.validator;

import com.umasuo.exception.ConflictException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Davis on 17/8/11.
 */
public final class VersionValidator {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(VersionValidator.class);

  /**
   * Instantiates a new Version validator.
   */
  private VersionValidator() {
  }

  /**
   * check the version.
   *
   * @param inputVersion Integer
   * @param existVersion Integer
   */
  public static void checkVersion(Integer inputVersion, Integer existVersion) {
    if (!inputVersion.equals(existVersion)) {
      LOG.debug("DeviceDataDefinition version is not correct.");
      throw new ConflictException("DeviceDataDefinition version is not correct.");
    }
  }
}
