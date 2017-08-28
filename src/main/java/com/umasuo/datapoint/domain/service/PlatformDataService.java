package com.umasuo.datapoint.domain.service;

import com.umasuo.datapoint.domain.model.PlatformDataDefinition;
import com.umasuo.datapoint.infrastructure.repository.PlatformDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PlatformDataService.
 */
@Service
public class PlatformDataService {

  /**
   * LOGGER.
   */
  private final static Logger LOGGER = LoggerFactory.getLogger(PlatformDataService.class);

  /**
   * repository.
   */
  @Autowired
  private transient PlatformDataRepository repository;

  /**
   * get one from db.
   *
   * @param productTypeId the id
   * @return by id
   */
  public List<PlatformDataDefinition> getByProductTypeId(String productTypeId) {
    LOGGER.debug("Enter. productTypeId: {}", productTypeId);

    PlatformDataDefinition sample = new PlatformDataDefinition();
    sample.setProductTypeId(productTypeId);

    Example<PlatformDataDefinition> example = Example.of(sample);

    List<PlatformDataDefinition> result = repository.findAll(example);

    LOGGER.debug("Exit. dataDefinition size: {}.", result.size());

    return result;
  }

  /**
   * 根据id列表查询对应的PlatformDataDefinition。
   *
   * @param dataDefinitionIds id列表
   * @return PlatformDataDefinition列表
   */
  public List<PlatformDataDefinition> getByIds(List<String> dataDefinitionIds) {
    LOGGER.debug("Enter. dataDefinitionIds: {}.", dataDefinitionIds);

    List<PlatformDataDefinition> dataDefinitions = repository.findAll(dataDefinitionIds);

    LOGGER.debug("Exit. found dataDefinitions size: {}.", dataDefinitions.size());

    return dataDefinitions;
  }

  /**
   * Get all platform data definition.
   *
   * @return
   */
  public List<PlatformDataDefinition> getAll() {
    LOGGER.debug("Enter.");

    List<PlatformDataDefinition> dataDefinitions = repository.findAll();

    LOGGER.debug("Exit. platformDataDefinition size: {}.", dataDefinitions.size());

    return dataDefinitions;
  }
}
