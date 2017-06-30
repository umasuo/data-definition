package com.umasuo.datapoint.application.service;

import com.umasuo.datapoint.application.dto.DeveloperDataDefinitionDraft;
import com.umasuo.datapoint.application.dto.DeveloperDataDefinitionView;
import com.umasuo.datapoint.application.dto.mapper.DeveloperDataMapper;
import com.umasuo.datapoint.domain.model.DeveloperDataDefinition;
import com.umasuo.datapoint.domain.service.DeveloperDataService;
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

  public DeveloperDataDefinitionView create(String developerId,
      DeveloperDataDefinitionDraft draft) {

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

    DeveloperDataDefinitionView result = DeveloperDataMapper.toModel(dataDefinition);

    LOG.debug("Exit. newDataDefinition id: {}.", result.getId());

    return result;
  }

  public void delete(String developerId, String id) {
    LOG.info("Enter. developerId: {}, dataDefinition id: {}.", developerId, id);

    getById(developerId, id);

    developerDataService.delete(id);
  }

  public DeveloperDataDefinitionView getOne(String developerId, String id) {
    LOG.info("Enter. developerId: {}, dataDefinition id: {}.", developerId, id);

    DeveloperDataDefinition dataDefinition = getById(developerId, id);

    DeveloperDataDefinitionView result = DeveloperDataMapper.toModel(dataDefinition);

    LOG.debug("Exit.");

    return result;
  }
  
  public List<DeveloperDataDefinitionView> getDeveloperData(String developerId) {
    LOG.info("Enter. developerId: {}.", developerId);

    List<DeveloperDataDefinition> dataDefinitions =
        developerDataService.getDeveloperDefinition(developerId);

    List<DeveloperDataDefinitionView> result = DeveloperDataMapper.toModel(dataDefinitions);

    LOG.info("Exit. developerDataDefinition size: {}.", result.size());

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
