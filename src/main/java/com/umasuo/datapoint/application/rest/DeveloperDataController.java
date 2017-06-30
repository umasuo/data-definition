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
 * Created by Davis on 17/6/30.
 */
@CrossOrigin
@RestController
public class DeveloperDataController {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(DeveloperDataController.class);

  @Autowired
  private transient DeveloperDataApplication developerDataApplication;

  @PostMapping(Router.DEVELOPER_DATA_ROOT)
  public DeveloperDataDefinitionView create(@RequestHeader("developerId") String developerId,
      @RequestBody @Valid DeveloperDataDefinitionDraft draft) {
    LOG.info("Enter. developerId: {}, dataDefinition draft: {}.", developerId, draft);

    DeveloperDataDefinitionView newDataDefinition =
        developerDataApplication.create(developerId, draft);

    LOG.info("Exit. newDataDefinition id: {}.", newDataDefinition.getId());

    return newDataDefinition;
  }

  @DeleteMapping(Router.DEVELOPER_DATA_WITH_ID)
  public void delete(@RequestHeader("developerId") String developerId, @PathVariable String id) {
    LOG.info("Enter. developerId: {}, dataDefinition id: {}.", developerId, id);

    developerDataApplication.delete(developerId, id);

    LOG.info("Exit.");
  }

  public DeveloperDataDefinitionView update() {
    // TODO: 17/6/30
    return null;
  }

  @GetMapping(Router.DEVELOPER_DATA_WITH_ID)
  public DeveloperDataDefinitionView getOne(@RequestHeader("developerId") String developerId,
      @PathVariable String id) {
    LOG.info("Enter. developerId: {}, dataDefinition id: {}.", developerId, id);

    DeveloperDataDefinitionView result = developerDataApplication.getOne(developerId, id);

    LOG.trace("found developerDataDefinition: {}.", result);
    LOG.info("Exit.");

    return result;
  }

  @GetMapping(Router.DEVELOPER_DATA_ROOT)
  public List<DeveloperDataDefinitionView> getDeveloperData(
      @RequestHeader("developerId") String developerId) {

    LOG.info("Enter. developerId: {}.", developerId);

    List<DeveloperDataDefinitionView> result =
        developerDataApplication.getDeveloperData(developerId);

    LOG.trace("found developerDataDefinition: {}.", result);
    LOG.info("Exit. developerDataDefinition size: {}.", result.size());

    return result;
  }

}
