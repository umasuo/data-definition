package com.umasuo.datapoint.domain.service;

import com.umasuo.datapoint.application.dto.DataDefinitionView;
import com.umasuo.datapoint.application.dto.mapper.DataDefinitionMapper;
import com.umasuo.datapoint.application.service.RestClient;
import com.umasuo.datapoint.domain.model.DataDefinition;
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
  public DataDefinition create(DataDefinition sample) {
    logger.debug("Enter. sample: {}.", sample);

    // 同一个开发者下，起dataId需要唯一
    DataDefinition ex = new DataDefinition();
    ex.setDataId(sample.getDataId());
    ex.setDeveloperId(sample.getDeveloperId());
    Example<DataDefinition> example = Example.of(ex);
    DataDefinition valueInDb = this.repository.findOne(example);
    if (valueInDb != null) {
      throw new AlreadyExistException("Data Definition already exist for dataId: "
          + ex.getDataId());
    }

    return this.repository.save(sample);
  }

  /**
   * Save DataDefinition.
   *
   * @param dataDefinition the data definition
   * @return the data definition
   */
  public DataDefinition save(DataDefinition dataDefinition) {
    logger.debug("Enter. dataDefinition: {}.", dataDefinition);

    DataDefinition result = repository.save(dataDefinition);

    logger.debug("Exit. saved DataDefinition: {}.", result);

    return result;
  }

  /**
   * get one from db.
   *
   * @param id the id
   * @return by id
   */
  public DataDefinition getById(String id) {
    logger.debug("GetDataDefinitionById: id: {}", id);

    DataDefinition valueInDb = this.repository.findOne(id);
    if (valueInDb == null) {
      throw new NotExistException("DataDefinition not exist.");
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
  public DataDefinition getByDataId(String dataId, String developerId) {
    logger.debug("Enter. dataId: {}, developerId: {}.", dataId, developerId);

    DataDefinition sample = new DataDefinition();
    sample.setDeveloperId(developerId);
    sample.setDataId(dataId);
    Example<DataDefinition> example = Example.of(sample);
    DataDefinition valueInDb = this.repository.findOne(example);
    if (valueInDb == null) {
      throw new NotExistException("DataDefinition not exist for dataId: " + dataId);
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

    DataDefinition sample = new DataDefinition();
    sample.setOpenable(true);
    sample.setDeveloperId(developerId);

    Example<DataDefinition> example = Example.of(sample);
    List<DataDefinition> openDataDefinitions = repository.findAll(example);

    List<DataDefinitionView> result = DataDefinitionMapper.toView(openDataDefinitions);

    logger.info("Exit. dataDefinition size: {}.", result.size());

    return result;
  }

  /**
   * Get all data definitions by developer id.
   *
   * @param developerId the developer id
   * @return a map of result, key is the DataDefinition's id, and value is the exist result, if a
   * DataDefinition not exist or not belong to the developer, value is false.
   */
  public List<DataDefinition> getDeveloperDefinition(String developerId) {
    logger.debug("Enter. developerId: {}.", developerId);

    DataDefinition sample = new DataDefinition();
    sample.setDeveloperId(developerId);
    Example<DataDefinition> example = Example.of(sample);

    List<DataDefinition> result = repository.findAll(example);

    logger.debug("Exit. result size: {}.", result.size());

    return result;
  }


  /**
   * Is exist name in developer.
   *
   * @param name the name
   * @param developerId the developer id
   * @return the boolean
   */
  public boolean isExistName(String name, String developerId) {
    logger.debug("Enter. developerId: {}, name: {}.", developerId, name);
    DataDefinition sample = new DataDefinition();
    sample.setDeveloperId(developerId);
    sample.setName(name);

    Example<DataDefinition> example = Example.of(sample);

    boolean result = repository.exists(example);

    logger.debug("Exit. exist: {}.", result);

    return result;
  }
}
