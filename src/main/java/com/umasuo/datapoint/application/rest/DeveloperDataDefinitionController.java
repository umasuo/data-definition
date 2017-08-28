package com.umasuo.datapoint.application.rest;

import com.umasuo.datapoint.application.dto.DeveloperDataDefinitionDraft;
import com.umasuo.datapoint.application.dto.DeveloperDataDefinitionView;
import com.umasuo.datapoint.application.service.DeveloperDataApplication;
import com.umasuo.datapoint.infrastructure.Router;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

/**
 * DeveloperDataDefinitionController.
 */
@CrossOrigin
@RestController
public class DeveloperDataDefinitionController {

  /**
   * Logger.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(DeveloperDataDefinitionController.class);

  /**
   * Developer data application.
   */
  @Autowired
  private transient DeveloperDataApplication developerDataApplication;

  /**
   * Create.
   *
   * @param developerId
   * @param draft
   * @return
   */
  @PostMapping(Router.DEVELOPER_DATA_ROOT)
  public DeveloperDataDefinitionView create(@RequestHeader String developerId,
                                            @RequestBody @Valid DeveloperDataDefinitionDraft
                                                draft) {
    LOGGER.info("Enter. developerId: {}, dataDefinition draft: {}.", developerId, draft);

    DeveloperDataDefinitionView newDataDefinition =
        developerDataApplication.create(developerId, draft);

    LOGGER.info("Exit. newDataDefinition id: {}.", newDataDefinition.getId());

    return newDataDefinition;
  }

  /**
   * Delete.
   *
   * @param developerId
   * @param id
   */
  @DeleteMapping(Router.DEVELOPER_DATA_WITH_ID)
  public void delete(@RequestHeader String developerId, @PathVariable String id) {
    LOGGER.info("Enter. developerId: {}, dataDefinition id: {}.", developerId, id);

    developerDataApplication.delete(developerId, id);

    LOGGER.info("Exit.");
  }

  /**
   * Get one data definition.
   *
   * @param developerId
   * @param id
   * @return
   */
  @GetMapping(Router.DEVELOPER_DATA_WITH_ID)
  public DeveloperDataDefinitionView getOne(@RequestHeader String developerId,
                                            @PathVariable String id) {
    LOGGER.info("Enter. developerId: {}, dataDefinition id: {}.", developerId, id);

    DeveloperDataDefinitionView result = developerDataApplication.getOne(developerId, id);

    LOGGER.trace("found developerDataDefinition: {}.", result);
    LOGGER.info("Exit.");

    return result;
  }

  /**
   * Get developer data.
   *
   * @param developerId
   * @return
   */
  @GetMapping(Router.DEVELOPER_DATA_ROOT)
  public List<DeveloperDataDefinitionView> getDeveloperData(
      @RequestHeader String developerId) {

    LOGGER.info("Enter. developerId: {}.", developerId);

    List<DeveloperDataDefinitionView> result =
        developerDataApplication.getDeveloperData(developerId);

    LOGGER.trace("found developerDataDefinition: {}.", result);
    LOGGER.info("Exit. developerDataDefinition size: {}.", result.size());

    return result;
  }

}
