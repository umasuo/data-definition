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
 * Created by umasuo on 17/3/8.
 */
@Service
public class PlatformDataService {

  /**
   * logger.
   */
  private final static Logger logger = LoggerFactory.getLogger(PlatformDataService.class);

  @Autowired
  private PlatformDataRepository repository;


  /**
   * get one from db.
   *
   * @param productTypeId the id
   * @return by id
   */
  public List<PlatformDataDefinition> getByProductTypeId(String productTypeId) {
    logger.debug("Enter. productTypeId: {}", productTypeId);

    PlatformDataDefinition sample = new PlatformDataDefinition();
    sample.setProductTypeId(productTypeId);

    Example<PlatformDataDefinition> example = Example.of(sample);

    List<PlatformDataDefinition> result = repository.findAll(example);

    logger.debug("Exit. dataDefinition size: {}.", result.size());

    return result;
  }

  public List<PlatformDataDefinition> getByIds(List<String> dataDefinitionIds) {
    logger.debug("Enter. dataDefinitionIds: {}.", dataDefinitionIds);

    List<PlatformDataDefinition> dataDefinitions = repository.findAll(dataDefinitionIds);

    logger.debug("Exit. found dataDefinitions size: {}.", dataDefinitions.size());

    return dataDefinitions;
  }
}
