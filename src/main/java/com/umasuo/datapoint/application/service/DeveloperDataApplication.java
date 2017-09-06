package com.umasuo.datapoint.application.service;

import com.umasuo.datapoint.application.dto.DeveloperDataDefinitionDraft;
import com.umasuo.datapoint.application.dto.DeveloperDataDefinitionView;
import com.umasuo.datapoint.application.dto.mapper.DeveloperDataMapper;
import com.umasuo.datapoint.domain.model.DeveloperDataDefinition;
import com.umasuo.datapoint.domain.service.DeveloperDataService;
import com.umasuo.datapoint.infrastructure.validator.SchemaValidator;
import com.umasuo.exception.AlreadyExistException;
import com.umasuo.exception.AuthFailedException;
import com.umasuo.exception.NotExistException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * DeveloperDataApplication.
 */
@Service
public class DeveloperDataApplication {

  /**
   * Logger.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(DeveloperDataApplication.class);

  /**
   * Developer data definition service.
   */
  @Autowired
  private transient DeveloperDataService developerDataService;

  /**
   * Cache application.
   */
  @Autowired
  private transient CacheApplication cacheApplication;

  /**
   * Create developer data definition.
   * @param developerId
   * @param draft
   * @return
   */
  public DeveloperDataDefinitionView create(String developerId,
      DeveloperDataDefinitionDraft draft) {

    //检查schema是否正确
    SchemaValidator.validate(draft.getSchema());

    boolean isNameExist = developerDataService.isNameExist(developerId, draft.getName());

    if (isNameExist) {
      LOGGER.debug("Can not add dateDefinition with name exist.");
      throw new AlreadyExistException("DataDefinition name exist");
    }

    boolean isDataIdExist = developerDataService.isDataIdExist(developerId, draft.getDataId());

    if (isDataIdExist) {
      LOGGER.debug("Can not add dateDefinition with dataId exist.");
      throw new AlreadyExistException("DataDefinition dataId exist");
    }

    DeveloperDataDefinition dataDefinition = DeveloperDataMapper.toModel(developerId, draft);

    developerDataService.save(dataDefinition);

    cacheApplication.deleteDeveloperDefinition(developerId);

    DeveloperDataDefinitionView result = DeveloperDataMapper.toView(dataDefinition);

    LOGGER.debug("Exit. newDataDefinition id: {}.", result.getId());

    return result;
  }

  /**
   * Delete Developer data definition.
   * @param developerId
   * @param id
   */
  public void delete(String developerId, String id) {
    LOGGER.debug("Enter. developerId: {}, dataDefinition id: {}.", developerId, id);

    //todo version
    getById(developerId, id);

    developerDataService.delete(id);

    cacheApplication.deleteDeveloperDefinition(developerId);
  }

  /**
   * Get one data definition by id.
   * @param developerId
   * @param id
   * @return
   */
  public DeveloperDataDefinitionView getOne(String developerId, String id) {
    LOGGER.debug("Enter. developerId: {}, dataDefinition id: {}.", developerId, id);

    DeveloperDataDefinition dataDefinition = getById(developerId, id);

    DeveloperDataDefinitionView result = DeveloperDataMapper.toView(dataDefinition);

    LOGGER.debug("Exit.");

    return result;
  }

  /**
   * Get developer's data definition.
   * @param developerId
   * @return
   */
  public List<DeveloperDataDefinitionView> getDeveloperData(String developerId) {
    LOGGER.debug("Enter. developerId: {}.", developerId);

    List<DeveloperDataDefinition> dataDefinitions =
        cacheApplication.getAllDeveloperDefinition(developerId);

    if (dataDefinitions == null || dataDefinitions.isEmpty()) {
      dataDefinitions = developerDataService.getDeveloperDefinition(developerId);

      cacheApplication.cacheDeveloperDefinition(developerId, dataDefinitions);
    }

    List<DeveloperDataDefinitionView> result = DeveloperDataMapper.toView(dataDefinitions);

    LOGGER.debug("Exit. developerDataDefinition size: {}.", result.size());

    return result;
  }

  /**
   * Get
   * @param developerId
   * @param id
   * @return
   */
  private DeveloperDataDefinition getById(String developerId, String id) {

    DeveloperDataDefinition dataDefinition = developerDataService.getById(id);
    if (dataDefinition == null) {
      LOGGER.debug("Can not find dataDefinition by id: {}.", id);
      throw new NotExistException("DataDefinition not found");
    }

    if (!dataDefinition.getDeveloperId().equals(developerId)) {
      LOGGER.debug("Do not have auth to delete dataDefinition.");
      throw new AuthFailedException("Do not have auth to delete dataDefinition");
    }
    return dataDefinition;
  }
}
