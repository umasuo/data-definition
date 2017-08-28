package com.umasuo.datapoint.domain.service;

import com.umasuo.datapoint.domain.model.DeveloperDataDefinition;
import com.umasuo.datapoint.infrastructure.repository.DeveloperDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * DeveloperDataService.
 */
@Service
public class DeveloperDataService {

  /**
   * LOGGER.
   */
  private final static Logger LOGGER = LoggerFactory.getLogger(DeveloperDataService.class);

  /**
   * DeveloperDataRepository.
   */
  @Autowired
  private transient DeveloperDataRepository repository;

  /**
   * Save DeviceDataDefinition.
   *
   * @param dataDefinition the data definition
   * @return the data definition
   */
  public DeveloperDataDefinition save(DeveloperDataDefinition dataDefinition) {
    LOGGER.debug("Enter. dataDefinition: {}.", dataDefinition);

    DeveloperDataDefinition result = repository.save(dataDefinition);

    LOGGER.debug("Exit. saved DeveloperDataDefinition: {}.", result);

    return result;
  }

  /**
   * get one from db.
   *
   * @param id the id
   * @return by id
   */
  public DeveloperDataDefinition getById(String id) {
    LOGGER.debug("Enter. id: {}.", id);

    DeveloperDataDefinition result = this.repository.findOne(id);

    return result;
  }

  /**
   * Gets by data id.
   *
   * @param dataId      the data id
   * @param developerId the developer id
   * @return the by data id
   */
  public DeveloperDataDefinition getByDataId(String dataId, String developerId) {
    LOGGER.debug("Enter. dataId: {}, developerId: {}.", dataId, developerId);

    DeveloperDataDefinition sample = new DeveloperDataDefinition();
    sample.setDeveloperId(developerId);
    sample.setDataId(dataId);

    Example<DeveloperDataDefinition> example = Example.of(sample);

    DeveloperDataDefinition result = this.repository.findOne(example);

    LOGGER.debug("Exit. dataDefinition: {}.", result);

    return result;
  }

  /**
   * Get all data definitions by developer id.
   *
   * @param developerId the developer id
   * @return a map of result, key is the DeviceDataDefinition's id, and value is the exist result,
   * if a DeviceDataDefinition not exist or not belong to the developer, value is false.
   */
  public List<DeveloperDataDefinition> getDeveloperDefinition(String developerId) {
    LOGGER.debug("Enter. developerId: {}.", developerId);

    DeveloperDataDefinition sample = new DeveloperDataDefinition();
    sample.setDeveloperId(developerId);
    Example<DeveloperDataDefinition> example = Example.of(sample);

    List<DeveloperDataDefinition> result = repository.findAll(example);

    LOGGER.debug("Exit. result size: {}.", result.size());

    return result;
  }

  /**
   * Delete developer data definition.
   *
   * @param id
   */
  public void delete(String id) {
    LOGGER.debug("Enter. id: {}.", id);

    repository.delete(id);

    LOGGER.debug("Exit.");
  }

  /**
   * Check if name is exist.
   *
   * @param developerId
   * @param name
   * @return
   */
  public boolean isNameExist(String developerId, String name) {
    LOGGER.debug("Enter. developerId: {}, name: {}.", developerId, name);

    DeveloperDataDefinition sample = new DeveloperDataDefinition();

    sample.setDeveloperId(developerId);
    sample.setName(name);

    Example<DeveloperDataDefinition> example = Example.of(sample);

    boolean result = repository.exists(example);

    LOGGER.debug("Exit. is name exist? {}", result);

    return result;
  }

  /**
   * Check if data definition id exist.
   *
   * @param developerId
   * @param dataId
   * @return
   */
  public boolean isDataIdExist(String developerId, String dataId) {
    LOGGER.debug("Enter. developerId: {}, dataId: {}.", developerId, dataId);

    DeveloperDataDefinition sample = new DeveloperDataDefinition();

    sample.setDeveloperId(developerId);
    sample.setDataId(dataId);

    Example<DeveloperDataDefinition> example = Example.of(sample);

    boolean result = repository.exists(example);

    LOGGER.debug("Exit. is dataId exist? {}", result);

    return result;
  }

  /**
   * Get all developer data definitions by data definition id.
   *
   * @param developerDataDefinitionIds
   * @return
   */
  public List<DeveloperDataDefinition> getByIds(List<String> developerDataDefinitionIds) {
    LOGGER.debug("Enter. developerDataDefinitionIds: {}.", developerDataDefinitionIds);

    List<DeveloperDataDefinition> result = repository.findAll(developerDataDefinitionIds);

    LOGGER.debug("Exit.");
    return result;
  }
}
