package com.umasuo.datapoint.application.rest;

import com.umasuo.datapoint.application.dto.CopyRequest;
import com.umasuo.datapoint.application.dto.DataDefinitionDraft;
import com.umasuo.datapoint.application.dto.DataDefinitionView;
import com.umasuo.datapoint.application.service.DataDefinitionApplication;
import com.umasuo.datapoint.domain.service.DataDefinitionService;
import com.umasuo.datapoint.infrastructure.Router;
import com.umasuo.datapoint.infrastructure.update.UpdateRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

/**
 * Created by umasuo on 17/3/8.
 */
@RestController
@CrossOrigin
public class DeviceDataController {

  /**
   * logger.
   */
  private final static Logger logger = LoggerFactory.getLogger(DeviceDataController.class);

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
   * 拷贝数据定义的接口。
   * 内部接口
   *
   * @param developerId developer id
   * @param request     拷贝请求
   * @return 拷贝后生成的数据定义id
   */
  @PostMapping(Router.DATA_COPY)
  public List<String> copy(@RequestHeader("developerId") String developerId,
                           @RequestBody @Valid CopyRequest request) {
    logger.info("Enter. developerId: {}, copyRequest: {}.", developerId, request);

    List<String> dateDefinitionIds = definitionApplication.handleCopyRequest(developerId, request);

    logger.info("Exit. newDataDefinitionIds: {}.", dateDefinitionIds);
    return dateDefinitionIds;
  }

  /**
   * Update DeviceDataDefinition.
   *
   * @param id            the DeviceDataDefinition id
   * @param developerId   the Developer id
   * @param updateRequest the UpdateRequest
   * @return updated DeviceDataDefinition
   */
  @PutMapping(value = Router.DATA_DEFINITION_WITH_ID)
  public DataDefinitionView update(@PathVariable String id,
                                   @RequestHeader String developerId,
                                   @RequestBody @Valid UpdateRequest updateRequest) {
    logger.info("Enter. dataDefinitionId: {}, updateRequest: {}, developerId: {}.",
        id, updateRequest, developerId);

    DataDefinitionView result =
        definitionApplication.update(id, developerId, updateRequest.getVersion(), updateRequest
            .getActions());

    logger.trace("Updated definition: {}.", result);
    logger.info("Exit.");

    return result;
  }

  @DeleteMapping(value = Router.DATA_DEFINITION_WITH_ID)
  public void delete(@PathVariable String id,
      @RequestHeader String developerId, @RequestParam String productId) {
    logger.info("Enter. id: {}, developerId: {}, productId: {}.", id, developerId, productId);

    definitionApplication.delete(id, developerId, productId);

    logger.info("Exit.");
  }


  @GetMapping(value = Router.DATA_DEFINITION_ROOT, params = {"productId"})
  public List<DataDefinitionView> getByProductId(@RequestHeader String developerId,
      @RequestParam String productId) {
    logger.info("Enter. developerId: {}, productId: {}.", developerId, productId);

    List<DataDefinitionView> result  = definitionApplication.getByProductId(developerId, productId);

    logger.info("Exit. dataDefinition size: {}.", result.size());

    return result;
  }

  /**
   * Gets all open data definition.
   *
   * @param developerId the developer id
   * @return the all open data
   */
  @GetMapping(value = Router.DATA_DEFINITION_ROOT, params = {"isOpen", "developerId"})
  public List<DataDefinitionView> getAllOpenData(@RequestParam String developerId,
                                                 @RequestParam Boolean isOpen) {
    logger.info("Enter. developerId: {}.", developerId);

    List<DataDefinitionView> result = definitionService.getAllOpenData(developerId);

    logger.info("Exit. dataDefinition size: {}.", result.size());

    return result;
  }
}
