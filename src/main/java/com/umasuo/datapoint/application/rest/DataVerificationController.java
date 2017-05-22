package com.umasuo.datapoint.application.rest;

import com.umasuo.datapoint.application.dto.VerifyData;
import com.umasuo.datapoint.domain.service.DataVerificationService;
import com.umasuo.datapoint.infrastructure.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by umasuo on 17/3/8.
 */
@RestController
@CrossOrigin
public class DataVerificationController {

  /**
   * logger.
   */
  private final static Logger logger = LoggerFactory.getLogger(DataVerificationController.class);

  /**
   * Data verify service.
   */
  @Autowired
  private transient DataVerificationService verificationService;

  /**
   * 验证一个json string的数据是否符合某条数据格点的格式.
   *
   * @param id
   * @param data
   * @return
   * @throws IOException
   */
  @PostMapping(value = Router.DATA_VERIFY)
  public boolean verify(@PathVariable String id, @NotNull @Valid String data)
      throws IOException {
    logger.info("VerifyData: id: {}, data: {}", id, data);

    return verificationService.verify(id, data);
  }

  /**
   * 根据数据定义的ID和开发者ID来校验数据.
   *
   * @param developerId 开发者ID，存放在header中
   * @param verifyData  需要验证的数据，包含数据ID和具体数据，具体数据采用JsonNode接受.
   * @return boolean
   */
  @PostMapping(value = Router.DATA_VERIFY_WITH_DATA_ID)
  public boolean verify(@RequestHeader String developerId, @RequestBody VerifyData verifyData) {
    logger.info("VerifyData: {}", verifyData);

    return verificationService.verify(developerId, verifyData.getDataId(), verifyData.getData());
  }

}
