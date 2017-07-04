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
 * Created by Davis on 17/6/30.
 */
@Service
public class DeveloperDataApplication {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(DeveloperDataApplication.class);

  @Autowired
  private transient DeveloperDataService developerDataService;

  @Autowired
  private transient CacheApplication cacheApplication;

  public DeveloperDataDefinitionView create(String developerId,
      DeveloperDataDefinitionDraft draft) {

    //检查schema是否正确
    SchemaValidator.validate(draft.getDataSchema());

    boolean isNameExist = developerDataService.isNameExist(developerId, draft.getName());

    if (isNameExist) {
      LOG.debug("Can not add dateDefinition with name exist.");
      throw new AlreadyExistException("DataDefinition name exist");
    }

    boolean isDataIdExist = developerDataService.isDataIdExist(developerId, draft.getDataId());

    if (isDataIdExist) {
      LOG.debug("Can not add dateDefinition with dataId exist.");
      throw new AlreadyExistException("DataDefinition dataId exist");
    }

    DeveloperDataDefinition dataDefinition = DeveloperDataMapper.toEntity(developerId, draft);

    developerDataService.save(dataDefinition);

    cacheApplication.deleteDeveloperDefinition(developerId);

    DeveloperDataDefinitionView result = DeveloperDataMapper.toModel(dataDefinition);

    LOG.debug("Exit. newDataDefinition id: {}.", result.getId());

    return result;
  }

  public void delete(String developerId, String id) {
    LOG.debug("Enter. developerId: {}, dataDefinition id: {}.", developerId, id);

    //todo version
    getById(developerId, id);

    developerDataService.delete(id);

    cacheApplication.deleteDeveloperDefinition(developerId);
  }

  public DeveloperDataDefinitionView getOne(String developerId, String id) {
    LOG.debug("Enter. developerId: {}, dataDefinition id: {}.", developerId, id);

    DeveloperDataDefinition dataDefinition = getById(developerId, id);

    DeveloperDataDefinitionView result = DeveloperDataMapper.toModel(dataDefinition);

    LOG.debug("Exit.");

    return result;
  }

  public List<DeveloperDataDefinitionView> getDeveloperData(String developerId) {
    LOG.debug("Enter. developerId: {}.", developerId);

    List<DeveloperDataDefinition> dataDefinitions =
        cacheApplication.getAllDeveloperDefinition(developerId);

    if (dataDefinitions == null || dataDefinitions.isEmpty()) {
      dataDefinitions = developerDataService.getDeveloperDefinition(developerId);

      cacheApplication.cacheDeveloperDefinition(developerId, dataDefinitions);
    }

    List<DeveloperDataDefinitionView> result = DeveloperDataMapper.toModel(dataDefinitions);

    LOG.debug("Exit. developerDataDefinition size: {}.", result.size());

    return result;
  }

  private DeveloperDataDefinition getById(String developerId, String id) {

    DeveloperDataDefinition dataDefinition = developerDataService.getById(id);
    if (dataDefinition == null) {
      LOG.debug("Can not find dataDefinition by id: {}.", id);
      throw new NotExistException("DataDefinition not found");
    }

    if (!dataDefinition.getDeveloperId().equals(developerId)) {
      LOG.debug("Do not have auth to delete dataDefinition.");
      throw new AuthFailedException("Do not have auth to delete dataDefinition");
    }
    return dataDefinition;
  }
}
