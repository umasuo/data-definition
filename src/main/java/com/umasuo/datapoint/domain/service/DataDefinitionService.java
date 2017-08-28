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
 * DataDefinitionService.
 */
@Service
public class DataDefinitionService {

  /**
   * LOGGER.
   */
  private final static Logger LOGGER = LoggerFactory.getLogger(DataDefinitionService.class);

  /**
   * Repository.
   */
  @Autowired
  private transient DataDefinitionRepository repository;

  /**
   * 判断dataId是否已经在developer＋product下存在。
   *
   * @param developerId the developer id
   * @param productId   the product id
   * @param dataId      the data id
   */
  public void isExistDataId(String developerId, String productId, String dataId) {
    LOGGER.debug("Enter. developerId: {}, productId: {}, dataId: {}.",
        developerId, productId, dataId);

    DeviceDataDefinition ex = new DeviceDataDefinition();
    ex.setDataId(dataId);
    ex.setDeveloperId(developerId);
    ex.setProductId(productId);
    Example<DeviceDataDefinition> example = Example.of(ex);

    boolean exists = this.repository.exists(example);
    if (exists) {
      LOGGER.debug("DataId: {} has existed for product: {}, developer: {}.",
          dataId, productId, developerId);
      throw new AlreadyExistException("Data Definition already exist for dataId: " + dataId);
    }

    LOGGER.debug("Exit. dataId is unique.");
  }

  /**
   * Is exist name in developer.
   *
   * @param developerId the developer id
   * @param productId   the developer id
   * @param name        the name
   * @return the boolean
   */
  public void isExistName(String developerId, String productId, String name) {
    LOGGER.debug("Enter. developerId: {}, productId: {}, name: {}.", developerId, productId, name);

    DeviceDataDefinition sample = new DeviceDataDefinition();
    sample.setProductId(productId);
    sample.setName(name);

    Example<DeviceDataDefinition> example = Example.of(sample);

    boolean exists = repository.exists(example);

    if (exists) {
      LOGGER.debug("Name: {} has existed in product: {}, developer: {}.",
          name, productId, developerId);
      throw new AlreadyExistException("Name has existed");
    }

    LOGGER.debug("Exit. name is unique.");
  }

  /**
   * Save DeviceDataDefinition.
   *
   * @param dataDefinition the data definition
   * @return the data definition
   */
  public DeviceDataDefinition save(DeviceDataDefinition dataDefinition) {
    LOGGER.debug("Enter. dataDefinition: {}.", dataDefinition);

    DeviceDataDefinition result = repository.save(dataDefinition);

    LOGGER.debug("Exit. saved DeviceDataDefinition: {}.", result);

    return result;
  }

  /**
   * Save all list.
   *
   * @param dataDefinitions the data definitions
   * @return the list
   */
  public List<String> saveAll(List<DeviceDataDefinition> dataDefinitions) {
    LOGGER.debug("Enter. dataDefinitions size: {}.", dataDefinitions.size());

    List<DeviceDataDefinition> savedDataDefinitions = repository.save(dataDefinitions);


    List<String> dataDefinitionIds = savedDataDefinitions.stream()
        .map(DeviceDataDefinition::getId).collect(Collectors.toList());

    LOGGER.debug("Exit. dataDefinition ids: {}.", dataDefinitionIds);

    return dataDefinitionIds;
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
   * Delete by product.
   *
   * @param developerId the developer id
   * @param productId   the product id
   */
  public void deleteByProduct(String developerId, String productId) {
    LOGGER.debug("Enter. developerId: {}, productId: {}.", developerId, productId);

    List<DeviceDataDefinition> dataDefinitions = getByProductId(developerId, productId);

    repository.delete(dataDefinitions);

    LOGGER.debug("Exit.");
  }

  /**
   * Get one from db.
   *
   * @param id the id
   * @return by id
   */
  public DeviceDataDefinition getById(String id) {
    LOGGER.debug("Enter. id: {}", id);

    DeviceDataDefinition valueInDb = this.repository.findOne(id);

    if (valueInDb == null) {
      LOGGER.debug("Can not find dataDefinition: {}.", id);
      throw new NotExistException("DataDefinition not exist.");
    }

    LOGGER.debug("Exit.");

    return valueInDb;
  }

  /**
   * Gets by product id.
   *
   * @param developerId the developer id
   * @param productId   the product id
   * @return the by product id
   */
  public List<DeviceDataDefinition> getByProductId(String developerId, String productId) {
    LOGGER.debug("Enter. developerId: {}, productId: {}.", developerId, productId);

    DeviceDataDefinition sample = new DeviceDataDefinition();
    sample.setDeveloperId(developerId);
    sample.setProductId(productId);

    Example<DeviceDataDefinition> example = Example.of(sample);

    List<DeviceDataDefinition> result = repository.findAll(example);

    LOGGER.debug("Exit. dataDefinition size: {}.", result.size());

    return result;
  }

  /**
   * Gets all open data.
   *
   * @param developerId the developer id
   * @return the all open data
   */
  public List<DataDefinitionView> getAllOpenData(String developerId) {
    LOGGER.info("Enter. developerId: {}.", developerId);

    DeviceDataDefinition sample = new DeviceDataDefinition();
    sample.setOpenable(true);
    sample.setDeveloperId(developerId);

    Example<DeviceDataDefinition> example = Example.of(sample);
    List<DeviceDataDefinition> openDataDefinitions = repository.findAll(example);

    List<DataDefinitionView> result = DataDefinitionMapper.toView(openDataDefinitions);

    LOGGER.info("Exit. dataDefinition size: {}.", result.size());

    return result;
  }
}
