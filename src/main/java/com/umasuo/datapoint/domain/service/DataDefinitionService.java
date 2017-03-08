package com.umasuo.datapoint.domain.service;

import com.umasuo.datapoint.domain.model.DataDefinition;
import com.umasuo.datapoint.infrastructure.repository.DataDefinitionRepository;
import com.umasuo.exception.AlreadyExistException;
import com.umasuo.exception.NotExistException;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
   * create data definition with sample
   *
   * @param sample
   */
  public void create(DataDefinition sample) {
    Assert.notNull(sample);
    Assert.notNull(sample.getDeveloperId());
    Assert.notNull(sample.getDeviceDefinitionId());

    DataDefinition valueInDb = this.repository.findOne(sample);
    if (valueInDb != null) {
      throw new AlreadyExistException("DataDefinition alrady exist.");
    }
    this.repository.save(sample);
  }

  /**
   * get one from db.
   *
   * @param id
   * @return
   */
  public DataDefinition getById(String id) {
    logger.debug("GetDataDefinitionById: id: {}", id);

    DataDefinition valueInDb = this.repository.findOne(id);
    if (valueInDb == null) {
      throw new NotExistException("DataDefinition not exist.")
    }
    return valueInDb;
  }

}
