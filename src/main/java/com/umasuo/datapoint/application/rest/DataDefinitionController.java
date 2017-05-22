package com.umasuo.datapoint.application.rest;

import com.umasuo.datapoint.application.dto.DataDefinitionDraft;
import com.umasuo.datapoint.application.dto.DataDefinitionView;
import com.umasuo.datapoint.application.dto.mapper.DataDefinitionMapper;
import com.umasuo.datapoint.domain.model.DataDefinition;
import com.umasuo.datapoint.domain.service.DataDefinitionService;
import com.umasuo.datapoint.infrastructure.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by umasuo on 17/3/8.
 */
@RestController
@CrossOrigin
public class DataDefinitionController {


  /**
   * logger.
   */
  private final static Logger logger = LoggerFactory.getLogger(DataDefinitionController.class);

  @Autowired
  DataDefinitionService definitionService;

  /**
   * 新建数据格点.
   *
   * @param definitionDraft
   */
  @PostMapping(value = Router.DATA_DEFINITION_ROOT)
  public DataDefinitionView create(@RequestBody @Valid DataDefinitionDraft definitionDraft) {
    logger.info("Enter. definitionDraft: {}", definitionDraft);

    DataDefinition dataDefinition = definitionService.create(DataDefinitionMapper.viewToModel
        (definitionDraft));

    logger.info("Exit. dataDefinition: {}", dataDefinition);
    return DataDefinitionMapper.modelToView(dataDefinition);
  }

  /**
   * get a definition by id.
   *
   * @param id
   * @return
   */
  @GetMapping(value = Router.DATA_DEFINITION_WITH_ID)
  public DataDefinitionView get(@PathVariable String id) {
    logger.info("GetDataDefinition: id: {}", id);

    return DataDefinitionMapper.modelToView(definitionService.getById(id));
  }
}
