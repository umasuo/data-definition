package com.umasuo.datapoint.application.service;

import com.umasuo.datapoint.application.dto.DataDefinitionView;
import com.umasuo.datapoint.application.dto.mapper.DataDefinitionMapper;
import com.umasuo.datapoint.domain.model.DataDefinition;
import com.umasuo.datapoint.domain.service.DataDefinitionService;
import com.umasuo.exception.ParametersException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by umasuo on 17/6/11.
 */
@Service
public class DataDefinitionApplication {

  /**
   * logger.
   */
  private final static Logger logger = LoggerFactory.getLogger(DataDefinitionService.class);

  @Autowired
  private transient DataDefinitionService definitionService;

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
}
