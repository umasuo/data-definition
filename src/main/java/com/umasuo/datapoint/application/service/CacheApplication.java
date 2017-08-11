package com.umasuo.datapoint.application.service;

import com.google.common.collect.Maps;
import com.umasuo.datapoint.application.dto.mapper.DataDefinitionMapper;
import com.umasuo.datapoint.application.dto.mapper.DeveloperDataMapper;
import com.umasuo.datapoint.domain.model.DeveloperDataDefinition;
import com.umasuo.datapoint.domain.model.DeviceDataDefinition;
import com.umasuo.datapoint.domain.model.PlatformDataDefinition;
import com.umasuo.datapoint.infrastructure.util.RedisUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Davis on 17/7/4.
 */
@Service
public class CacheApplication {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(CacheApplication.class);

  @Autowired
  private transient RedisTemplate redisTemplate;

  /**
   * 获取平台预设的产品数据。
   *
   * @return key : productType Id, value: platformDataDefinition list
   */
  public Map<String, List<PlatformDataDefinition>> getAllPlatformDefinition() {
    LOG.debug("Enter.");

    Map<String, List<PlatformDataDefinition>> result =
        redisTemplate.opsForHash().entries(RedisUtils.PLATFORM_DEFINITION_KEY);

    if (CollectionUtils.isEmpty(result)) {
      result = Maps.newHashMap();
    }

    LOG.debug("Exit.");

    return result;
  }

  /**
   * 缓存平台预设的产品数据。
   *
   * @param views key: productType Id, value: platformDataDefinition list
   */
  public void cachePlatformDefinition(Map<String, List<PlatformDataDefinition>> views) {
    LOG.debug("Enter. platformDataDefinition size: {}.", views.size());

    redisTemplate.opsForHash().putAll(RedisUtils.PLATFORM_DEFINITION_KEY, views);

    LOG.debug("Exit.");
  }

  /**
   * 根据productTypeId获取对应的 PlatformDataDefinition list。
   *
   * @param productTypeId productTypeId
   * @return PlatformDataDefinition list
   */
  public List<PlatformDataDefinition> getPlatformDefinitionByType(String productTypeId) {
    LOG.debug("Enter. productTypeId: {}.", productTypeId);
    List<PlatformDataDefinition> result = (List<PlatformDataDefinition>) redisTemplate.opsForHash()
        .get(RedisUtils.PLATFORM_DEFINITION_KEY, productTypeId);

    LOG.debug("Exit. result: {}.", result);
    return result;
  }

  /**
   * 删除开发者的所有数据定义。
   *
   * @param developerId the developerId
   */
  public void deleteDeveloperDefinition(String developerId) {
    LOG.debug("Enter. developerId: {}.", developerId);

    String key = String.format(RedisUtils.DEVELOPER_DEFINITION_FORMAT, developerId);

    redisTemplate.delete(key);

    LOG.debug("Exit.");
  }

  /**
   * 获取开发者定义的所有数据定义。
   *
   * @param developerId the developerId
   * @return DeveloperDataDefinition list
   */
  public List<DeveloperDataDefinition> getAllDeveloperDefinition(String developerId) {
    LOG.debug("Enter. developerId: {}.", developerId);

    String key = String.format(RedisUtils.DEVELOPER_DEFINITION_FORMAT, developerId);

    List<DeveloperDataDefinition> result = redisTemplate.opsForHash().values(key);

    LOG.debug("Exit. developer dataDefinition size: {}.", result.size());

    return result;
  }

  /**
   * 缓存开发者定义的所有数据定义。
   *
   * @param developerId the developerId
   * @param definitions DeveloperDataDefinition list
   */
  public void cacheDeveloperDefinition(String developerId,
      List<DeveloperDataDefinition> definitions) {
    LOG.debug("Enter. developer: {}, dataDefinition size: {}.", developerId, definitions.size());

    Map<String, DeveloperDataDefinition> entityMap = DeveloperDataMapper.toEntityMap(definitions);

    String key = String.format(RedisUtils.DEVELOPER_DEFINITION_FORMAT, developerId);

    redisTemplate.opsForHash().putAll(key, entityMap);

    LOG.debug("Exit.");
  }

  /**
   * 根据id获取开发者定义的某一个数据定义。
   *
   * @param developerId the developerId
   * @param id dataDefinition id
   * @return DeveloperDataDefinition developer definition by id
   */
  public DeveloperDataDefinition getDeveloperDefinitionById(String developerId, String id) {
    LOG.debug("Enter. developerId: {}, id: {}.", developerId, id);

    String key = String.format(RedisUtils.DEVELOPER_DEFINITION_FORMAT, developerId);

    DeveloperDataDefinition result =
        (DeveloperDataDefinition) redisTemplate.opsForHash().get(key, id);

    LOG.debug("Exit. dataDefinition: {}.", result);

    return result;
  }

  /**
   * 根据developerId和productId获取对应的DeviceDataDefinition列表。
   *
   * @param developerId the developerId
   * @param productId the productId
   * @return DeviceDataDefinition list
   */
  public List<DeviceDataDefinition> getProductDataDefinition(String developerId, String productId) {

    LOG.debug("Enter. developerId: {}, productId: {}.", developerId, productId);

    String key = String.format(RedisUtils.DEVICE_DEFINITION_FORMAT, developerId, productId);

    List<DeviceDataDefinition> result = (List<DeviceDataDefinition>)
        redisTemplate.opsForHash().values(key);

    LOG.debug("Exit. dataDefinition size: {}.", result.size());
    return result;
  }

  /**
   * 根据developerId，productId，id获取对应的DeviceDataDefinition。
   *
   * @param developerId the
   * @param productId the product id
   * @param id the id
   * @return product data definition
   */
  public DeviceDataDefinition getProductDataDefinition(String developerId, String productId,
      String id) {
    LOG.debug("Enter. developerId: {}, productId: {}, id: {}.", developerId, productId, id);

    String key = String.format(RedisUtils.DEVICE_DEFINITION_FORMAT, developerId, productId);

    DeviceDataDefinition dataDefinition =
        (DeviceDataDefinition) redisTemplate.opsForHash().get(key, id);

    LOG.debug("Exit. dataDefinition: {}.", dataDefinition);

    return dataDefinition;
  }


  /**
   * 缓存某一个产品的数据定义.
   *
   * @param developerId the developer id
   * @param productId the product id
   * @param dataDefinitions the data definitions
   */
  public void cacheProductDataDefinition(String developerId, String productId,
      List<DeviceDataDefinition> dataDefinitions) {
    LOG.debug("Enter. developerId: {}, productId: {}, dataDefinition size: {}.",
        developerId, productId, dataDefinitions.size());

    String key = String.format(RedisUtils.DEVICE_DEFINITION_FORMAT, developerId, productId);

    Map<String, DeviceDataDefinition> entityMap = DataDefinitionMapper.toEntityMap(dataDefinitions);

    redisTemplate.opsForHash().putAll(key, entityMap);

    LOG.debug("Exit.");
  }

  /**
   * 删除某一个产品的数据定义.
   *
   * @param developerId the developer id
   * @param productId the product id
   */
  public void deleteProductDataDefinition(String developerId, String productId) {
    LOG.debug("Enter. developerId: {}, productId: {}, dataDefinition size: {}.",
        developerId, productId);

    String key = String.format(RedisUtils.DEVICE_DEFINITION_FORMAT, developerId, productId);

    redisTemplate.delete(key);

    LOG.debug("Exit.");
  }
}
