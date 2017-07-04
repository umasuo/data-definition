package com.umasuo.datapoint.application.service;

import com.umasuo.datapoint.application.dto.PlatformDataDefinitionView;
import com.umasuo.datapoint.application.dto.mapper.PlatformDataMapper;
import com.umasuo.datapoint.domain.model.PlatformDataDefinition;
import com.umasuo.datapoint.domain.service.PlatformDataService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Davis on 17/7/4.
 */
@Service
public class PlatformDataApplication {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(PlatformDataApplication.class);

  @Autowired
  private transient PlatformDataService platformDataService;

  @Autowired
  private transient CacheApplication cacheApplication;

  public Map<String, List<PlatformDataDefinitionView>> getAll() {
    LOG.info("Enter.");

    Map<String, List<PlatformDataDefinition>> cachePlatformDefinitions =
        cacheApplication.getAllPlatformDefinition();

    if (cachePlatformDefinitions == null || cachePlatformDefinitions.isEmpty()) {
      LOG.debug("Cache fail, get from database.");
      List<PlatformDataDefinition> dataDefinitions = platformDataService.getAll();

     cachePlatformDefinitions = PlatformDataMapper.toEntityMap(dataDefinitions);

      cacheApplication.batchCachePlatformDefinition(cachePlatformDefinitions);
    }

    Map<String, List<PlatformDataDefinitionView>> result =
        PlatformDataMapper.toModelMap(cachePlatformDefinitions);

    LOG.info("Exit. dataDefinition size: {}.", result.size());
    return result;
  }
}
