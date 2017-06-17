package com.umasuo.datapoint.application.rest;

import com.umasuo.datapoint.application.dto.DataDefinitionDraft;
import com.umasuo.datapoint.application.dto.DataDefinitionView;
import com.umasuo.datapoint.application.dto.mapper.DataDefinitionMapper;
import com.umasuo.datapoint.application.service.DataDefinitionApplication;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
  private transient DataDefinitionService definitionService;

  @Autowired
  private transient DataDefinitionApplication definitionApplication;

  /**
   * 新建数据定义
   * <p>
   *
   * @param definitionDraft 数据定义draft
   * @param developerId     开发者ID
   * @param definitionDraft the definition draft
   * @return the data definition view
   */
  @PostMapping(value = Router.DATA_DEFINITION_ROOT)
  public DataDefinitionView create(@RequestBody @Valid DataDefinitionDraft definitionDraft,
                                   @RequestHeader String developerId) {
    logger.info("Enter. definitionDraft: {}, developerId: {}.", definitionDraft, developerId);

    DataDefinitionView view = definitionApplication.create(definitionDraft, developerId);

    logger.info("Exit. dataDefinition: {}", view);
    return view;
  }

  /**
   * get a definition by id.
   *
   * @param id the id
   * @return data definition view
   */
  @GetMapping(value = Router.DATA_DEFINITION_WITH_ID)
  public DataDefinitionView getById(@PathVariable String id, @RequestHeader String developerId) {
    logger.info("Enter. id: {}", id);

    DataDefinitionView view = definitionApplication.getById(id, developerId);

    logger.info("Exit. dataDefinitionView: {}.", view);
    return view;
  }

  /**
   * 通过数据定义Id和开发者ID获取唯一的数据定义.
   *
   * @param dataId      自定义的数据定义ID
   * @param developerId 开发者ID
   * @return
   */
  @GetMapping(value = Router.DATA_DEFINITION_ROOT, params = {"dataId"})
  public DataDefinitionView get(@RequestParam("dataId") String dataId, @RequestHeader String developerId) {
    logger.info("Enter. dataId: {}, developerId: {}.", dataId, developerId);

    DataDefinition definition = definitionService.getByDataId(dataId, developerId);
    DataDefinitionView view = DataDefinitionMapper.toView(definition);

    logger.info("Exit. view: {}.", view);
    return view;
  }

  /**
   * Check DataDefinition exist and belong to the developer.
   * 此接口只开放给内部使用，而不通过API－Gateway暴露到外部.
   * 调用此接口的主要是在定义设备的数据格式时调用.
   *
   * @param definitionIds     the DataDefinition id
   * @param developerId the developer id
   * @return a map of result.
   */
  @GetMapping(value = Router.DATA_DEFINITION_ROOT, params = {"definitionIds", "developerId"})
  public Map isExistDefinition(@RequestParam("definitionIds") List<String> definitionIds,
                               @RequestParam("developerId") String developerId) {
    logger.info("Enter. dataDefinitionIds: {}, developerId: {}.", definitionIds, developerId);
    Map<String, Boolean> result = definitionService.isExistDefinition(developerId, definitionIds);

    logger.info("Exit. result: {}.", result);
    return result;
  }

  /**
   * Gets all open data definition.
   *
   * @param developerId the developer id
   * @return the all open data
   */
  @GetMapping(value = Router.OPEN_DATA_DEFINITION)
  public List<DataDefinitionView> getAllOpenData(@RequestParam String developerId) {
    logger.info("Enter. developerId: {}.", developerId);

    List<DataDefinitionView> result = null;

    logger.info("Exit. dataDefinition size: {}.", result.size());

    return result;
  }
}
