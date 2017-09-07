package com.umasuo.datapoint.domain.service;

import com.umasuo.datapoint.domain.model.PlatformDataDefinition;
import com.umasuo.datapoint.infrastructure.repository.PlatformDataRepository;
import com.umasuo.exception.AlreadyExistException;
import com.umasuo.exception.NotExistException;

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
   * Save platform data definition.
   *
   * @param dataDefinition the data definition
   * @return the platform data definition
   */
  public PlatformDataDefinition save(PlatformDataDefinition dataDefinition) {
    LOGGER.debug("Enter.");

    repository.save(dataDefinition);

    LOGGER.debug("Enter. new platformDataDefinition id: {}.", dataDefinition.getId());

    return dataDefinition;
  }


  /**
   * Delete by product type.
   *
   * @param productTypeId the product type id
   */
  public void deleteByProductType(String productTypeId) {
    LOGGER.debug("Enter. productType id: {}.", productTypeId);

    List<PlatformDataDefinition> dataDefinitions = getByProductTypeId(productTypeId);

    LOGGER.debug("delete dataDefinition size: {}.", dataDefinitions.size());

    repository.delete(dataDefinitions);

    LOGGER.debug("Exit.");
  }

  /**
   * Delete.
   *
   * @param id the id
   */
  public void delete(String id) {
    LOGGER.debug("Enter. id: {}.", id);

    repository.delete(id);

    LOGGER.debug("Exit.");

  }

  /**
   * Get all platform data definition.
   *
   * @return all
   */
  public List<PlatformDataDefinition> getAll() {
    LOGGER.debug("Enter.");

    List<PlatformDataDefinition> dataDefinitions = repository.findAll();

    LOGGER.debug("Exit. platformDataDefinition size: {}.", dataDefinitions.size());

    return dataDefinitions;
  }

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
   * @return PlatformDataDefinition列表 by ids
   */
  public List<PlatformDataDefinition> getByIds(List<String> dataDefinitionIds) {
    LOGGER.debug("Enter. dataDefinitionIds: {}.", dataDefinitionIds);

    List<PlatformDataDefinition> dataDefinitions = repository.findAll(dataDefinitionIds);

    LOGGER.debug("Exit. found dataDefinitions size: {}.", dataDefinitions.size());

    return dataDefinitions;
  }

  /**
   * Gets by id.
   *
   * @param id the id
   * @return the by id
   */
  public PlatformDataDefinition getById(String id) {
    LOGGER.debug("Enter. id: {}.", id);

    PlatformDataDefinition dataDefinition = repository.getOne(id);

    if (dataDefinition == null) {
      LOGGER.debug("Can not find platformDataDefinition: {}.", id);
      throw new NotExistException("PlatformDataDefinition not exist");
    }

    LOGGER.trace("platformDataDefinition: {}.", dataDefinition);
    LOGGER.debug("Exit.");

    return dataDefinition;
  }

  /**
   * Get PlatformDataDefinition by productTypeId and dataId.
   *
   * @param productTypeId the productTypeId
   * @param dataId the dataId
   * @return PlatformDataDefinition
   */
  public void existDataId(String productTypeId, String dataId) {
    LOGGER.debug("Enter. productTypeId: {}, dataId: {}.", productTypeId, dataId);

    PlatformDataDefinition sample = new PlatformDataDefinition();
    sample.setProductTypeId(productTypeId);
    sample.setDataId(dataId);

    Example example = Example.of(sample);

    boolean exists = repository.exists(example);

    if (exists) {
      LOGGER.debug("dataId: {} has exist in productType: {}.", dataId, productTypeId);
      throw new AlreadyExistException("DataId already exist");
    }

    LOGGER.debug("Exit. dataId: {} exist? {}.", dataId, exists);
  }
}
