package com.umasuo.datapoint.domain.service;

import com.umasuo.datapoint.application.service.RestClient;
import com.umasuo.datapoint.domain.model.DataDefinition;
import com.umasuo.datapoint.infrastructure.repository.DataDefinitionRepository;
import com.umasuo.exception.AlreadyExistException;
import com.umasuo.exception.NotExistException;
import com.umasuo.exception.ParametersException;

import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

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
    Assert.notNull(sample);
    Assert.notNull(sample.getDeveloperId());
    Assert.notNull(sample.getDataId());

    boolean isDeveloperExist = restClient.isDeveloperExist(sample.getDeveloperId());

    if (!isDeveloperExist) {
      logger.debug("Developer: {} not exist.", sample.getDeveloperId());
      throw new ParametersException("Developer not exist");
    }

    Example<DataDefinition> example = Example.of(sample);
    DataDefinition valueInDb = this.repository.findOne(example);
    if (valueInDb != null) {
      throw new AlreadyExistException("Data Definition already exist.");
    }
    return this.repository.save(sample);
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
   * @param developerId the developer id
   * @param dataId the data id
   * @return the by data id
   */
  public DataDefinition getByDataId(String developerId, String dataId) {
    DataDefinition sample = new DataDefinition();
    sample.setDeveloperId(developerId);
    sample.setDataId(dataId);
    Example<DataDefinition> example = Example.of(sample);
    DataDefinition valueInDb = this.repository.findOne(example);
    if (valueInDb == null) {
      throw new NotExistException("DataDefinition not exist.");
    }
    return valueInDb;
  }
}
