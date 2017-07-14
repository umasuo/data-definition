package com.umasuo.datapoint.domain.service;

import com.umasuo.datapoint.application.dto.DataDefinitionView;
import com.umasuo.datapoint.application.dto.mapper.DataDefinitionMapper;
import com.umasuo.datapoint.application.service.RestClient;
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
   * The Rest client.
   */
  @Autowired
  private transient RestClient restClient;

  /**
   * create data definition with sample
   *
   * @param sample the sample
   * @return the data definition
   */
  public DeviceDataDefinition create(DeviceDataDefinition sample) {
    logger.debug("Enter. sample: {}.", sample);

    // 同一个开发者的同一个产品下，其dataId需要唯一
    DeviceDataDefinition ex = new DeviceDataDefinition();
    ex.setDataId(sample.getDataId());
    ex.setDeveloperId(sample.getDeveloperId());
    ex.setProductId(sample.getProductId());
    Example<DeviceDataDefinition> example = Example.of(ex);
    DeviceDataDefinition valueInDb = this.repository.findOne(example);
    if (valueInDb != null) {
      throw new AlreadyExistException("Data Definition already exist for dataId: "
          + ex.getDataId());
    }

    return this.repository.save(sample);
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
   * get one from db.
   *
   * @param id the id
   * @return by id
   */
  public DeviceDataDefinition getById(String id) {
    logger.debug("GetDataDefinitionById: id: {}", id);

    DeviceDataDefinition valueInDb = this.repository.findOne(id);
    if (valueInDb == null) {
      throw new NotExistException("DeviceDataDefinition not exist.");
    }
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
   * Is exist name in developer.
   *
   * @param name the name
   * @param productId the developer id
   * @return the boolean
   */
  public boolean isExistName(String name, String productId) {
    logger.debug("Enter. productId: {}, name: {}.", productId, name);
    DeviceDataDefinition sample = new DeviceDataDefinition();
    sample.setProductId(productId);
    sample.setName(name);

    Example<DeviceDataDefinition> example = Example.of(sample);

    boolean result = repository.exists(example);

    logger.debug("Exit. exist: {}.", result);

    return result;
  }

  public List<DeviceDataDefinition> getByIds(List<String> dataDefinitionIds) {
    logger.debug("Enter. dataDefinitionIds: {}.", dataDefinitionIds);

    List<DeviceDataDefinition> result = repository.findAll(dataDefinitionIds);

    logger.debug("Exit. dataDefinition size: {}.", result.size());

    return result;
  }

  public List<DeviceDataDefinition> saveAll(List<DeviceDataDefinition> dataDefinitions) {
    logger.debug("Enter. dataDefinitions size: {}.", dataDefinitions.size());
    List<DeviceDataDefinition> savedDataDefinitions = repository.save(dataDefinitions);

    logger.debug("Exit.");

    return savedDataDefinitions;
  }

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

  public void delete(String id) {
    logger.debug("Enter. id: {}.", id);

    repository.delete(id);

    logger.debug("Exit.");
  }

  public void deleteByProduct(String developerId, String productId) {
    logger.debug("Enter. developerId: {}, productId: {}.", developerId, productId);

    List<DeviceDataDefinition> dataDefinitions = getByProductId(developerId, productId);

    repository.delete(dataDefinitions);

    logger.debug("Exit.");
  }
}
