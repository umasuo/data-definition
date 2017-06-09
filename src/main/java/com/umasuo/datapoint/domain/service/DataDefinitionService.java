package com.umasuo.datapoint.domain.service;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.common.collect.Maps;
import com.umasuo.datapoint.application.dto.DataDefinitionView;
import com.umasuo.datapoint.application.dto.mapper.DataDefinitionMapper;
import com.umasuo.datapoint.application.service.RestClient;
import com.umasuo.datapoint.domain.model.DataDefinition;
import com.umasuo.datapoint.infrastructure.repository.DataDefinitionRepository;
import com.umasuo.exception.AlreadyExistException;
import com.umasuo.exception.NotExistException;
import com.umasuo.exception.ParametersException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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

    // 检查data  schema是否正确
    try {
      JsonSchemaFactory.byDefault().getJsonSchema(sample.getDataSchema());
    } catch (ProcessingException e) {
      throw new ParametersException("Data Definition not correct.");
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
   * @param dataId      the data id
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
   * Check if DataDefinition exist and belong to developer.
   *
   * @param developerId the developer id
   * @param dataIds     the DataDefinition id list
   * @return a map of result, key is the DataDefinition's id, and value is the exist result, if a
   * DataDefinition not exist or not belong to the developer, value is false.
   */
  public Map<String, Boolean> isExistDefinition(String developerId, List<String> dataIds) {
    logger.debug("Enter. developerId: {}, dataIds: {}.", developerId, dataIds);

    DataDefinition sample = new DataDefinition();
    sample.setDeveloperId(developerId);
    Example<DataDefinition> example = Example.of(sample);

    List<DataDefinition> valueInDb = repository.findAll(example);

    Map result = checkExistDefinition(valueInDb, dataIds);

    logger.debug("Exit. result: {}.", result);

    return result;
  }

  /**
   * Check if DataDefinition exist and belong to developer.
   *
   * @param definitions DataDefinition list
   * @param dataIds     the DataDefinition id list
   * @return a map of result, key is the DataDefinition's id, and value is the exist result, if a
   * DataDefinition not exist or not belong to the developer, value is false.
   */
  private Map checkExistDefinition(List<DataDefinition> definitions, List<String> dataIds) {

    Map<String, Boolean> result = Maps.newHashMap();

    dataIds.stream().forEach(s -> result.put(s, false));

    Consumer<DataDefinition> consumer = dataDefinition -> {
      if (dataIds.contains(dataDefinition.getId())) {
        result.replace(dataDefinition.getId(), true);
      }
    };

    definitions.stream().forEach(consumer);

    return result;
  }
}
