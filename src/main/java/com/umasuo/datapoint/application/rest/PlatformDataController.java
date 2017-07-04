package com.umasuo.datapoint.application.rest;

import com.umasuo.datapoint.application.dto.PlatformDataDefinitionView;
import com.umasuo.datapoint.application.dto.mapper.PlatformDataMapper;
import com.umasuo.datapoint.application.service.PlatformDataApplication;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

  @Autowired
  private transient PlatformDataApplication dataApplication;

  /**
   * Get data definitions by product type.
   * 暂时是内部接口。
   *
   * @param productTypeId the developer id
   * @return the all open data
   */
  @GetMapping(value = Router.PLATFORM_DATA_ROOT, params = {"productTypeId"})
  public List<PlatformDataDefinitionView> getProductTypeData(@RequestParam String productTypeId) {
    logger.info("Enter. productTypeId: {}.", productTypeId);

    List<PlatformDataDefinitionView> result = dataApplication.getByProductType(productTypeId);

    logger.info("Exit. dataDefinition size: {}.", result.size());
    return result;
  }

  /**
   * 根据product type ID列表查询对应的PlatformDataDefinitionView列表。
   * 暂时是内部接口
   *
   * @return PlatformDataDefinitionView列表
   */
  @GetMapping(value = Router.PLATFORM_DATA_ROOT)
  public Map<String, List<PlatformDataDefinitionView>> getAll() {
    logger.info("Enter.");

    Map<String, List<PlatformDataDefinitionView>> result = dataApplication.getAll();

    logger.info("Exit. dataDefinition size: {}.", result.size());
    return result;
  }
}
