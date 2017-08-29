package com.umasuo.datapoint.application.service;

import com.umasuo.datapoint.application.dto.PlatformDataDefinitionDraft;
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
 * PlatformDataApplication.
 */
@Service
public class PlatformDataApplication {

  /**
   * Logger.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(PlatformDataApplication.class);

  /**
   * Platform data definition service.
   */
  @Autowired
  private transient PlatformDataService platformDataService;

  /**
   * Cache application.
   */
  @Autowired
  private transient CacheApplication cacheApplication;

  /**
   * Get all platform.
   *
   * @return all all
   */
  public Map<String, List<PlatformDataDefinitionView>> getAll() {
    LOGGER.info("Enter.");

    Map<String, List<PlatformDataDefinition>> cacheDefinitions =
        cacheApplication.getAllPlatformDefinition();

    if (cacheDefinitions == null || cacheDefinitions.isEmpty()) {
      LOGGER.debug("Cache fail, get from database.");
      List<PlatformDataDefinition> dataDefinitions = platformDataService.getAll();

      cacheDefinitions = PlatformDataMapper.toModelMap(dataDefinitions);

      cacheApplication.cachePlatformDefinition(cacheDefinitions);
    }

    Map<String, List<PlatformDataDefinitionView>> result =
        PlatformDataMapper.toModelMap(cacheDefinitions);

    LOGGER.info("Exit. dataDefinition size: {}.", result.size());
    return result;
  }

  /**
   * Get all platform data definition by product type.
   *
   * @param productTypeId the product type id
   * @return by product type
   */
  public List<PlatformDataDefinitionView> getByProductType(String productTypeId) {
    LOGGER.debug("Enter. productTypeId: {}.", productTypeId);

    List<PlatformDataDefinition> cacheDefinitions =
        cacheApplication.getPlatformDefinitionByType(productTypeId);

    if (cacheDefinitions == null || cacheDefinitions.isEmpty()) {
      List<PlatformDataDefinition> dataDefinitions = platformDataService.getAll();

      Map<String, List<PlatformDataDefinition>> entityMap =
          PlatformDataMapper.toModelMap(dataDefinitions);

      cacheApplication.cachePlatformDefinition(entityMap);

      cacheDefinitions = entityMap.get(productTypeId);
    }

    List<PlatformDataDefinitionView> result = PlatformDataMapper.toView(cacheDefinitions);

    return result;
  }

  /**
   * Create platform data definition view.
   *
   * @param draft the draft
   * @return the platform data definition view
   */
  public PlatformDataDefinitionView create(PlatformDataDefinitionDraft draft) {
    LOGGER.debug("Enter. draft: {}.", draft);

    PlatformDataDefinition dataDefinition = PlatformDataMapper.toModel(draft);

    platformDataService.save(dataDefinition);

    PlatformDataDefinitionView result = PlatformDataMapper.toView(dataDefinition);
    cacheApplication.deletePlatformDefinition();

    LOGGER.debug("Exit. new platformDataDefinition id: {}.", result.getId());
    return result;
  }


  /**
   * Delete by product type.
   *
   * @param productTypeId the product type id
   */
  public void deleteByProductType(String productTypeId) {
    LOGGER.debug("Enter. productType id: {}.", productTypeId);

    platformDataService.deleteByProductType(productTypeId);

    cacheApplication.deletePlatformDefinition();

    LOGGER.debug("Exit.");
  }
}
