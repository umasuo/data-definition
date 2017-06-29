package com.umasuo.datapoint.application.service;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.common.collect.Lists;
import com.umasuo.datapoint.application.dto.CopyRequest;
import com.umasuo.datapoint.application.dto.DataDefinitionDraft;
import com.umasuo.datapoint.application.dto.DataDefinitionView;
import com.umasuo.datapoint.application.dto.mapper.DataDefinitionMapper;
import com.umasuo.datapoint.domain.model.DeviceDataDefinition;
import com.umasuo.datapoint.domain.model.PlatformDataDefinition;
import com.umasuo.datapoint.domain.service.DataDefinitionService;
import com.umasuo.datapoint.domain.service.PlatformDataService;
import com.umasuo.datapoint.infrastructure.update.UpdateAction;
import com.umasuo.datapoint.infrastructure.update.UpdaterService;
import com.umasuo.exception.AlreadyExistException;
import com.umasuo.exception.ConflictException;
import com.umasuo.exception.NotExistException;
import com.umasuo.exception.ParametersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

  @Autowired
  private transient PlatformDataService platformDataService;

  /**
   * The UpdateService.
   */
  @Autowired
  private transient UpdaterService updaterService;

  /**
   * Create DeviceDataDefinition.
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

      if (definitionService.isExistName(draft.getName(), developerId)) {
        logger.debug("Name: {} has existed in developer: {}.", draft.getName(), developerId);
        throw new AlreadyExistException("Name has existed");
      }

      DeviceDataDefinition definition = definitionService
          .create(DataDefinitionMapper.toEntity(draft,
              developerId));
      DataDefinitionView view = DataDefinitionMapper.toView(definition);

      logger.debug("Exit. view: {}.", view);
      return view;
    } catch (ProcessingException e) {
      logger.trace("DeviceDataDefinition is not a validator JsonSchema.", e);
      throw new ParametersException("DeviceDataDefinition is not a validator JsonSchema.");
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

    DeviceDataDefinition definition = definitionService.getById(id);
    if (!definition.getDeveloperId().equals(developerId)) {
      throw new ParametersException("");
    }
    DataDefinitionView view = DataDefinitionMapper.toView(definition);
    logger.debug("Exit. dataDefinitionView: {}.", view);
    return view;
  }

  /**
   * Update DeviceDataDefinition.
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

    DeviceDataDefinition definition = definitionService.getById(id);
    if (!definition.getDeveloperId().equals(developerId)) {
      logger.debug("Can not update dataDefinition: {} not belong to developer: {}.",
          id, developerId);
      throw new ParametersException("Can not update dataDefinition: " + id +
          " not belong to developer: " + developerId);
    }

    checkVersion(version, definition.getVersion());

    actions.stream().forEach(action -> updaterService.handle(definition, action));

    DeviceDataDefinition updatedDefinition = definitionService.save(definition);

    DataDefinitionView result = DataDefinitionMapper.toView(updatedDefinition);

    logger.trace("Updated DeviceDataDefinition: {}.", result);
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
      logger.debug("DeviceDataDefinition version is not correct.");
      throw new ConflictException("DeviceDataDefinition version is not correct.");
    }
  }

  public List<String> handleCopyRequest(String developerId, CopyRequest request) {
    logger.info("Enter. developerId: {}, copyRequest: {}.", developerId, request);
    List<String> newDataDefinitionIds = Lists.newArrayList();

    boolean isCopyFromPlatform = request.getPlatformDataDefinitionIds() != null &&
        !request.getPlatformDataDefinitionIds().isEmpty();
    if (isCopyFromPlatform) {
      List<String> copyPlatformDataIds = copyFromPlatformData(
          developerId, request.getDeviceDefinitionId(), request.getPlatformDataDefinitionIds());
      newDataDefinitionIds.addAll(copyPlatformDataIds);
    }

    boolean isCopyFromDeveloper = request.getDeveloperDataDefinitionIds() != null &&
        !request.getDeveloperDataDefinitionIds().isEmpty();
    if (!isCopyFromDeveloper) {
      List<String> copyDeveloperDataIds = copyFromDeveloperData(
          developerId, request.getDeviceDefinitionId(), request.getDeveloperDataDefinitionIds());
      newDataDefinitionIds.addAll(copyDeveloperDataIds);
    }

    if (!isCopyFromPlatform && !isCopyFromDeveloper) {
      logger.debug("Can not copy from null request data definition id");
      throw new ParametersException("Can not copy from null request data definition id");
    }

    logger.info("Exit. newDataDefinitionIds: {}.", newDataDefinitionIds);
    return newDataDefinitionIds;
  }

  private List<String> copyFromDeveloperData(String developerId, String deviceDefinitionId,
      List<String> developerDataDefinitionIds) {
    // TODO: 17/6/29  
    return Lists.newArrayList();
  }

  private List<String> copyFromPlatformData(String developerId, String deviceDefinitionId,
      List<String> requestIds) {

    List<PlatformDataDefinition> dataDefinitions =
        platformDataService.getByIds(requestIds);

    if (requestIds.size() != dataDefinitions.size()) {
      logger.debug("Can not find all dataDefinition: {}.", requestIds);
      throw new NotExistException("DeviceDataDefinition not exist");
    }

    List<DeviceDataDefinition> newDataDefinitions = DataDefinitionMapper
        .copyFromPlatformData(developerId, dataDefinitions);

    List<DeviceDataDefinition> savedDataDefinitions = definitionService.saveAll(newDataDefinitions);

    List<String> newDataDefinitionIds = savedDataDefinitions.stream()
        .map(DeviceDataDefinition::getId).collect(
            Collectors.toList());

    return newDataDefinitionIds;
  }

}
