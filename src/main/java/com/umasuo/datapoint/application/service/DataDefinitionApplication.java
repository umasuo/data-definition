package com.umasuo.datapoint.application.service;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.umasuo.datapoint.application.dto.DataDefinitionDraft;
import com.umasuo.datapoint.application.dto.DataDefinitionView;
import com.umasuo.datapoint.application.dto.mapper.DataDefinitionMapper;
import com.umasuo.datapoint.domain.model.DataDefinition;
import com.umasuo.datapoint.domain.service.DataDefinitionService;
import com.umasuo.datapoint.infrastructure.update.UpdateAction;
import com.umasuo.datapoint.infrastructure.update.UpdaterService;
import com.umasuo.exception.ConflictException;
import com.umasuo.exception.ParametersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by umasuo on 17/6/11.
 */
@Service
public class DataDefinitionApplication {

  /**
   * logger.
   */
  private final static Logger logger = LoggerFactory.getLogger(DataDefinitionService.class);

  /**
   * The DataDefinitionService.
   */
  @Autowired
  private transient DataDefinitionService definitionService;

  /**
   * The UpdateService.
   */
  @Autowired
  private transient UpdaterService updaterService;

  /**
   * Create DataDefinition.
   *
   * @param draft the draft
   * @param developerId the developer id
   * @return the data definition view
   */
  public DataDefinitionView create(DataDefinitionDraft draft, String developerId) {
    logger.debug("Enter. draft: {}, developerId: {}.", draft, developerId);
    try {
      //检查schema是否正确
      JsonSchemaFactory.byDefault().getJsonSchema(draft.getDataSchema());

      DataDefinition definition = definitionService.create(DataDefinitionMapper.toEntity(draft,
          developerId));
      DataDefinitionView view = DataDefinitionMapper.toView(definition);

      logger.debug("Exit. view: {}.", view);
      return view;
    } catch (ProcessingException e) {
      logger.trace("DataDefinition is not a validator JsonSchema.", e);
      throw new ParametersException("DataDefinition is not a validator JsonSchema.");
    }
  }

  /**
   * Gets by id.
   *
   * @param id the id
   * @param developerId the developer id
   * @return the by id
   */
  public DataDefinitionView getById(String id, String developerId) {
    logger.debug("Enter. id: {}, developerId: {}.", id, developerId);

    DataDefinition definition = definitionService.getById(id);
    if (!definition.getDeveloperId().equals(developerId)) {
      throw new ParametersException("");
    }
    DataDefinitionView view = DataDefinitionMapper.toView(definition);
    logger.debug("Exit. dataDefinitionView: {}.", view);
    return view;
  }

  /**
   * Update DataDefinition.
   *
   * @param id the id
   * @param developerId the developer id
   * @param version the version
   * @param actions the actions
   * @return updated DataDefinitionView
   */
  public DataDefinitionView update(String id, String developerId, Integer version,
      List<UpdateAction> actions) {
    logger.debug("Enter: id: {}, version: {}, developerId actions: {}",
        id, version, developerId, actions);

    DataDefinition definition = definitionService.getById(id);
    if (!definition.getDeveloperId().equals(developerId)) {
      logger.debug("Can not update dataDefinition: {} not belong to developer: {}.",
          id, developerId);
      throw new ParametersException("Can not update dataDefinition: " + id +
          " not belong to developer: " + developerId);
    }

    checkVersion(version, definition.getVersion());

    actions.stream().forEach(action -> updaterService.handle(definition, action));

    DataDefinition updatedDefinition = definitionService.save(definition);

    DataDefinitionView result = DataDefinitionMapper.toView(updatedDefinition);

    logger.trace("Updated DataDefinition: {}.", result);
    logger.debug("Exit.");

    return result;
  }

  /**
   * check the version.
   *
   * @param inputVersion Integer
   * @param existVersion Integer
   */
  private void checkVersion(Integer inputVersion, Integer existVersion) {
    if (!inputVersion.equals(existVersion)) {
      logger.debug("DataDefinition version is not correct.");
      throw new ConflictException("DataDefinition version is not correct.");
    }
  }
}
