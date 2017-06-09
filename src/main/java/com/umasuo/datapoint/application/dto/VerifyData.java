package com.umasuo.datapoint.application.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by umasuo on 17/5/22.
 */
@Data
public class VerifyData implements Serializable {

  private static final long serialVersionUID = -6696513490989307761L;

  /**
   * 用户自定义的数据定义ID.
   */
  private String dataId;

  /**
   * 用户上传的数据.
   */
  private JsonNode data;
}
