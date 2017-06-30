package com.umasuo.datapoint.application.rest;

import com.umasuo.datapoint.application.dto.PlatformDataDefinitionView;
import com.umasuo.datapoint.application.dto.mapper.PlatformDataMapper;
import com.umasuo.datapoint.domain.model.PlatformDataDefinition;
import com.umasuo.datapoint.domain.service.PlatformDataService;
import com.umasuo.datapoint.infrastructure.Router;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by umasuo on 17/3/8.
 */
@RestController
@CrossOrigin
public class PlatformDataController {

  /**
   * logger.
   */
  private final static Logger logger = LoggerFactory.getLogger(PlatformDataController.class);

  /**
   * PlatformDataService.
   */
  @Autowired
  private transient PlatformDataService definitionService;

  /**
   * Gets all open data definition.
   *
   * @param productTypeId the developer id
   * @return the all open data
   */
  @GetMapping(value = Router.PLATFORM_DATA_ROOT)
  public List<PlatformDataDefinitionView> getProductTypeData(@RequestParam String productTypeId) {
    logger.info("Enter. productTypeId: {}.", productTypeId);

    List<PlatformDataDefinition> dataDefinitions =
        definitionService.getByProductTypeId(productTypeId);

    List<PlatformDataDefinitionView> result = PlatformDataMapper.toModel(dataDefinitions);

    logger.info("Exit. dataDefinition size: {}.", result.size());

    return result;
  }

  /**
   * 根据ID列表查询对应的PlatformDataDefinitionView列表。
   *
   * @param dataDefinitionIds id列表
   * @return PlatformDataDefinitionView列表
   */
  @GetMapping(value = Router.PLATFORM_DATA_ROOT, params = {"dataDefinitionIds"})
  public List<PlatformDataDefinitionView> getByIds(@RequestParam List<String> dataDefinitionIds) {
    logger.info("Enter. dataDefinitionIds: {}.", dataDefinitionIds);

    List<PlatformDataDefinition> dataDefinitions = definitionService.getByIds(dataDefinitionIds);

    List<PlatformDataDefinitionView> result = PlatformDataMapper.toModel(dataDefinitions);

    logger.info("Exit. dataDefinition size: {}.", result.size());

    return result;
  }
}
