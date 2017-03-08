package com.umasuo.datapoint.application.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umasuo.datapoint.domain.service.DataVerificationService;
import com.umasuo.datapoint.infrastructure.Router;
import com.umasuo.datapoint.infrastructure.definition.PointType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by umasuo on 17/3/8.
 */
@RestController
public class DataVerificationController {

  /**
   * logger.
   */
  private final static Logger logger = LoggerFactory.getLogger(DataVerificationController.class);

  @Autowired
  private transient DataVerificationService verificationService;

  private ObjectMapper mapper = new ObjectMapper();

  /**
   * verify String data.
   *
   * @param id
   * @param data
   * @return
   * @throws IOException
   */
  @PostMapping(value = Router.DATA_DEFINITION_DATA_VERIFY)
  public boolean verifyData(@PathVariable String id, @NotNull @Valid String data)
      throws IOException {
    logger.info("VerifyData: id: {}, data: {}", id, data);

    return verificationService.verify(id, data);
  }

}
