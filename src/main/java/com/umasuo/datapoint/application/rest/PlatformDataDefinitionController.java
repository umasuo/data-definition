package com.umasuo.datapoint.application.rest;

import com.umasuo.datapoint.application.dto.PlatformDataDefinitionDraft;
import com.umasuo.datapoint.application.dto.PlatformDataDefinitionView;
import com.umasuo.datapoint.application.service.PlatformDataApplication;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

/**
 * PlatformDataDefinitionController.
 */
@RestController
@CrossOrigin
public class PlatformDataDefinitionController {

  /**
   * LOG.
   */
  private final static Logger LOG = LoggerFactory.getLogger(PlatformDataDefinitionController.class);

  /**
   * Data definition app.
   */
  @Autowired
  private transient PlatformDataApplication dataApplication;

  /**
   * Create PlatformDataDefinition.
   *
   * @param draft the PlatformDataDefinitionDraft
   * @return PlatformDataDefinitionView platform data definition view
   */
  @PostMapping(value = Router.PLATFORM_DATA_ROOT)
  public PlatformDataDefinitionView create(@RequestBody PlatformDataDefinitionDraft draft) {
    LOG.info("Enter. draft: {}.", draft);

    PlatformDataDefinitionView result = dataApplication.create(draft);

    LOG.info("Exit. new platformDataDefinition id: {}.", result.getId());

    return result;
  }

  /**
   * Delete.
   *
   * @param productTypeId the product type id
   */
  @DeleteMapping(value = Router.PLATFORM_DATA_ROOT)
  public void delete(@RequestParam String productTypeId) {
    LOG.debug("Enter. productType id: {}.", productTypeId);

    dataApplication.deleteByProductType(productTypeId);

    LOG.debug("Exit.");
  }

  /**
   * Delete.
   *
   * @param id the id
   * @param productTypeId the product type id
   */
  @DeleteMapping(value = Router.PLATFORM_DATA_WITH_ID)
  public void delete(@PathVariable("id") String id, @RequestParam String productTypeId) {
    LOG.debug("Enter. id: {}, productType id: {}.", id, productTypeId);

    dataApplication.delete(id, productTypeId);

    LOG.debug("Exit.");
  }


  /**
   * Update.
   *
   * @param id the id
   * @param updateRequest the update request
   */
  @PutMapping(value = Router.PLATFORM_DATA_WITH_ID)
  public void update(@PathVariable("id") String id,
      @RequestBody @Valid UpdateRequest updateRequest) {
    LOG.info("Enter. dataDefinitionId: {}, updateRequest: {}.", id, updateRequest);

    dataApplication.update(id, updateRequest.getVersion(), updateRequest.getActions());

    LOG.info("Exit.");
  }

  /**
   * Get data definitions by product type.
   * 暂时是内部接口。
   *
   * @param productTypeId the developer id
   * @return the all open data
   */
  @GetMapping(value = Router.PLATFORM_DATA_ROOT, params = {"productTypeId"})
  public List<PlatformDataDefinitionView> getProductTypeData(@RequestParam String productTypeId) {
    LOG.info("Enter. productTypeId: {}.", productTypeId);

    List<PlatformDataDefinitionView> result = dataApplication.getByProductType(productTypeId);

    LOG.info("Exit. dataDefinition size: {}.", result.size());
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
    LOG.info("Enter.");

    Map<String, List<PlatformDataDefinitionView>> result = dataApplication.getAll();

    LOG.info("Exit. dataDefinition size: {}.", result.size());
    return result;
  }
}
