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

    Map<String, List<PlatformDataDefinition>> cacheDefinitions =
        cacheApplication.getAllPlatformDefinition();

    if (cacheDefinitions == null || cacheDefinitions.isEmpty()) {
      LOG.debug("Cache fail, get from database.");
      List<PlatformDataDefinition> dataDefinitions = platformDataService.getAll();

      cacheDefinitions = PlatformDataMapper.toEntityMap(dataDefinitions);

      cacheApplication.cachePlatformDefinition(cacheDefinitions);
    }

    Map<String, List<PlatformDataDefinitionView>> result =
        PlatformDataMapper.toModelMap(cacheDefinitions);

    LOG.info("Exit. dataDefinition size: {}.", result.size());
    return result;
  }

  public List<PlatformDataDefinitionView> getByProductType(String productTypeId) {
    LOG.debug("Enter. productTypeId: {}.", productTypeId);

    List<PlatformDataDefinition> cacheDefinitions =
        cacheApplication.getPlatformDefinitionByType(productTypeId);

    if (cacheDefinitions == null || cacheDefinitions.isEmpty()) {
      List<PlatformDataDefinition> dataDefinitions = platformDataService.getAll();

      Map<String, List<PlatformDataDefinition>> entityMap =
          PlatformDataMapper.toEntityMap(dataDefinitions);

      cacheApplication.cachePlatformDefinition(entityMap);

      cacheDefinitions = entityMap.get(productTypeId);
    }

    List<PlatformDataDefinitionView> result = PlatformDataMapper.toModel(cacheDefinitions);

    return result;
  }
}
