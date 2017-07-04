package com.umasuo.datapoint.application.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.umasuo.datapoint.application.dto.PlatformDataDefinitionView;
import com.umasuo.datapoint.domain.model.PlatformDataDefinition;
import com.umasuo.datapoint.infrastructure.util.RedisUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Davis on 17/7/4.
 */
@Service
public class CacheApplication {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(CacheApplication.class);

  @Autowired
  private transient RedisTemplate redisTemplate;


  public Map<String, List<PlatformDataDefinition>> getAllPlatformDefinition() {
    Map<String, List<PlatformDataDefinition>> result =
        redisTemplate.opsForHash().entries(RedisUtils.PLATFORM_DEFINITION_KEY);

    if (result == null) {
      result = Maps.newHashMap();
    }

    return result;
  }

  public void batchCachePlatformDefinition(Map<String, List<PlatformDataDefinition>> views) {
    LOG.debug("Enter. platformDataDefinition size: {}.", views.size());

    redisTemplate.opsForHash().putAll(RedisUtils.PLATFORM_DEFINITION_KEY, views);

    LOG.debug("Exit.");
  }

  public List<PlatformDataDefinition> getPlatformDefinitionByType(String productTypeId) {
    LOG.debug("Enter. productTypeId: {}.", productTypeId);
    List<PlatformDataDefinition> result = (List<PlatformDataDefinition>) redisTemplate.opsForHash()
            .get(RedisUtils.PLATFORM_DEFINITION_KEY, productTypeId);

    LOG.debug("Exit. result: {}.", result);
    return result;
  }
}
