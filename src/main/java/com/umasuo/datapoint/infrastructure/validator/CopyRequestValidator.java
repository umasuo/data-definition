package com.umasuo.datapoint.infrastructure.validator;

import com.umasuo.exception.NotExistException;
import com.umasuo.exception.ParametersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Davis on 17/8/11.
 */
public final class CopyRequestValidator {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(CopyRequestValidator.class);

  /**
   * Instantiates a new Copy request validator.
   */
  private CopyRequestValidator() {
  }

  /**
   * 判断要拷贝的数据定义是否全部存在。
   * 因为dataDefinitions是根据requestIds查询出来的，所有只需要比对数量即可。
   *
   * @param requestIds the request ids
   * @param dataDefinitions the data definitions
   */
  public static void matchRequestIds(List<String> requestIds, List dataDefinitions) {
    if (requestIds.size() != dataDefinitions.size()) {
      LOG.debug("Can not find all dataDefinition: {}.", requestIds);
      throw new NotExistException("DeviceDataDefinition not exist");
    }
  }

  /**
   * 判断是不是空的请求：平台数据定义和开发者数据定义同时为空。
   *
   * @param isCopyFromPlatform the is copy from platform
   * @param isCopyFromDeveloper the is copy from developer
   */
  public static void validateNullRequest(boolean isCopyFromPlatform, boolean isCopyFromDeveloper) {
    if (!isCopyFromPlatform && !isCopyFromDeveloper) {
      LOG.debug("Can not copy from null request data definition id");
      throw new ParametersException("Can not copy from null request data definition id");
    }
  }
}
