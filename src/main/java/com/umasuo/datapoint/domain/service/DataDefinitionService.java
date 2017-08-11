package com.umasuo.datapoint.domain.service;

import com.umasuo.datapoint.application.dto.DataDefinitionView;
import com.umasuo.datapoint.application.dto.mapper.DataDefinitionMapper;
import com.umasuo.datapoint.domain.model.DeviceDataDefinition;
import com.umasuo.datapoint.infrastructure.repository.DataDefinitionRepository;
import com.umasuo.exception.AlreadyExistException;
import com.umasuo.exception.NotExistException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by umasuo on 17/3/8.
 */
@Service
public class DataDefinitionService {

  /**
   * logger.
   */
  private final static Logger logger = LoggerFactory.getLogger(DataDefinitionService.class);

  @Autowired
  private DataDefinitionRepository repository;

  /**
   * 判断dataId是否已经在developer＋product下存在。
   *
   * @param developerId the developer id
   * @param productId the product id
   * @param dataId the data id
   */
  public void isExistDataId(String developerId, String productId, String dataId) {
    logger.debug("Enter. developerId: {}, productId: {}, dataId: {}.",
        developerId, productId, dataId);

    DeviceDataDefinition ex = new DeviceDataDefinition();
    ex.setDataId(dataId);
    ex.setDeveloperId(developerId);
    ex.setProductId(productId);
    Example<DeviceDataDefinition> example = Example.of(ex);

    boolean exists = this.repository.exists(example);
    if (exists) {
      logger.debug("DataId: {} has existed for product: {}, developer: {}.",
          dataId, productId, developerId);
      throw new AlreadyExistException("Data Definition already exist for dataId: " + dataId);
    }

    logger.debug("Exit. dataId is unique.");
  }

  /**
   * Is exist name in developer.
   *
   * @param developerId the developer id
   * @param productId the developer id
   * @param name the name
   * @return the boolean
   */
  public boolean isExistName(String developerId, String productId , String name) {
    logger.debug("Enter. developerId: {}, productId: {}, name: {}.", developerId, productId, name);

    DeviceDataDefinition sample = new DeviceDataDefinition();
    sample.setProductId(productId);
    sample.setName(name);

    Example<DeviceDataDefinition> example = Example.of(sample);

    boolean exists = repository.exists(example);

    if (exists) {
      logger.debug("Name: {} has existed in product: {}, developer: {}.",
          name, productId, developerId);
      throw new AlreadyExistException("Name has existed");
    }

    logger.debug("Exit. exist: {}.", exists);

    return exists;
  }

  /**
   * Save DeviceDataDefinition.
   *
   * @param dataDefinition the data definition
   * @return the data definition
   */
  public DeviceDataDefinition save(DeviceDataDefinition dataDefinition) {
    logger.debug("Enter. dataDefinition: {}.", dataDefinition);

    DeviceDataDefinition result = repository.save(dataDefinition);

    logger.debug("Exit. saved DeviceDataDefinition: {}.", result);

    return result;
  }

  /**
   * Save all list.
   *
   * @param dataDefinitions the data definitions
   * @return the list
   */
  public List<String> saveAll(List<DeviceDataDefinition> dataDefinitions) {
    logger.debug("Enter. dataDefinitions size: {}.", dataDefinitions.size());

    List<DeviceDataDefinition> savedDataDefinitions = repository.save(dataDefinitions);


    List<String> dataDefinitionIds = savedDataDefinitions.stream()
        .map(DeviceDataDefinition::getId).collect(Collectors.toList());

    logger.debug("Exit. dataDefinition ids: {}.", dataDefinitionIds);

    return dataDefinitionIds;
  }

  /**
   * Get one from db.
   *
   * @param id the id
   * @return by id
   */
  public DeviceDataDefinition getById(String id) {
    logger.debug("Enter. id: {}", id);

    DeviceDataDefinition valueInDb = this.repository.findOne(id);

    if (valueInDb == null) {
      logger.debug("Can not find dataDefinition: {}.", id);
      throw new NotExistException("DeviceDataDefinition not exist.");
    }

    logger.debug("Exit.");
    return valueInDb;
  }

  /**
   * Gets by data id.
   *
   * @param dataId the data id
   * @param developerId the developer id
   * @return the by data id
   */
  public DeviceDataDefinition getByDataId(String dataId, String developerId) {
    logger.debug("Enter. dataId: {}, developerId: {}.", dataId, developerId);

    DeviceDataDefinition sample = new DeviceDataDefinition();
    sample.setDeveloperId(developerId);
    sample.setDataId(dataId);
    Example<DeviceDataDefinition> example = Example.of(sample);
    DeviceDataDefinition valueInDb = this.repository.findOne(example);
    if (valueInDb == null) {
      throw new NotExistException("DeviceDataDefinition not exist for dataId: " + dataId);
    }

    logger.debug("Exit. dataDefinition: {}.", valueInDb);
    return valueInDb;
  }

  /**
   * Gets all open data.
   *
   * @param developerId the developer id
   * @return the all open data
   */
  public List<DataDefinitionView> getAllOpenData(String developerId) {
    logger.info("Enter. developerId: {}.", developerId);

    DeviceDataDefinition sample = new DeviceDataDefinition();
    sample.setOpenable(true);
    sample.setDeveloperId(developerId);

    Example<DeviceDataDefinition> example = Example.of(sample);
    List<DeviceDataDefinition> openDataDefinitions = repository.findAll(example);

    List<DataDefinitionView> result = DataDefinitionMapper.toView(openDataDefinitions);

    logger.info("Exit. dataDefinition size: {}.", result.size());

    return result;
  }

  /**
   * Get all data definitions by developer id.
   *
   * @param developerId the developer id
   * @return a map of result, key is the DeviceDataDefinition's id, and value is the exist result,
   * if a DeviceDataDefinition not exist or not belong to the developer, value is false.
   */
  public List<DeviceDataDefinition> getDeveloperDefinition(String developerId) {
    logger.debug("Enter. developerId: {}.", developerId);

    DeviceDataDefinition sample = new DeviceDataDefinition();
    sample.setDeveloperId(developerId);
    Example<DeviceDataDefinition> example = Example.of(sample);

    List<DeviceDataDefinition> result = repository.findAll(example);

    logger.debug("Exit. result size: {}.", result.size());

    return result;
  }

  /**
   * Gets by ids.
   *
   * @param dataDefinitionIds the data definition ids
   * @return the by ids
   */
  public List<DeviceDataDefinition> getByIds(List<String> dataDefinitionIds) {
    logger.debug("Enter. dataDefinitionIds: {}.", dataDefinitionIds);

    List<DeviceDataDefinition> result = repository.findAll(dataDefinitionIds);

    logger.debug("Exit. dataDefinition size: {}.", result.size());

    return result;
  }

  /**
   * Gets by product id.
   *
   * @param developerId the developer id
   * @param productId the product id
   * @return the by product id
   */
  public List<DeviceDataDefinition> getByProductId(String developerId, String productId) {
    logger.debug("Enter. developerId: {}, productId: {}.", developerId, productId);

    DeviceDataDefinition sample = new DeviceDataDefinition();
    sample.setDeveloperId(developerId);
    sample.setProductId(productId);

    Example<DeviceDataDefinition> example = Example.of(sample);

    List<DeviceDataDefinition> result = repository.findAll(example);

    logger.debug("Exit. dataDefinition size: {}.", result.size());

    return result;
  }

  /**
   * Delete.
   *
   * @param id the id
   */
  public void delete(String id) {
    logger.debug("Enter. id: {}.", id);

    repository.delete(id);

    logger.debug("Exit.");
  }

  /**
   * Delete by product.
   *
   * @param developerId the developer id
   * @param productId the product id
   */
  public void deleteByProduct(String developerId, String productId) {
    logger.debug("Enter. developerId: {}, productId: {}.", developerId, productId);

    List<DeviceDataDefinition> dataDefinitions = getByProductId(developerId, productId);

    repository.delete(dataDefinitions);

    logger.debug("Exit.");
  }
}
