package com.umasuo.datapoint.application.service;

import com.google.common.collect.Maps;
import com.umasuo.datapoint.application.dto.mapper.DataDefinitionMapper;
import com.umasuo.datapoint.application.dto.mapper.DeveloperDataMapper;
import com.umasuo.datapoint.domain.model.DeveloperDataDefinition;
import com.umasuo.datapoint.domain.model.DeviceDataDefinition;
import com.umasuo.datapoint.domain.model.PlatformDataDefinition;
import com.umasuo.datapoint.domain.service.DataDefinitionService;
import com.umasuo.datapoint.infrastructure.util.RedisUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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

  @Autowired
  private transient DataDefinitionService definitionService;


  public Map<String, List<PlatformDataDefinition>> getAllPlatformDefinition() {
    Map<String, List<PlatformDataDefinition>> result =
        redisTemplate.opsForHash().entries(RedisUtils.PLATFORM_DEFINITION_KEY);

    if (result == null) {
      result = Maps.newHashMap();
    }

    return result;
  }

  public void cachePlatformDefinition(Map<String, List<PlatformDataDefinition>> views) {
    LOG.debug("Enter. platformDataDefinition size: {}.", views.size());

    redisTemplate.opsForHash().putAll(RedisUtils.PLATFORM_DEFINITION_KEY, views);

    LOG.debug("Exit.");
  }

  public List<PlatformDataDefinition> getPlatformDefinitionByType(String productTypeId) {
    LOG.debug("Enter. productTypeId: {}.", productTypeId);
    List<PlatformDataDefinition> result = (List<PlatformDataDefinition>) redisTemplate.opsForHash()
        .get(RedisUtils.PLATFORM_DEFINITION_KEY, productTypeId);

    LOG.debug("Exit. result: {}.", result);
    return result;
  }

  public void deleteDeveloperDefinition(String developerId) {
    LOG.debug("Enter. developerId: {}.", developerId);

    String key = String.format(RedisUtils.DEVELOPER_DEFINITION_FORMAT, developerId);

    redisTemplate.delete(key);

    LOG.debug("Exit.");
  }

  public List<DeveloperDataDefinition> getAllDeveloperDefinition(String developerId) {
    LOG.debug("Enter. developerId: {}.", developerId);

    String key = String.format(RedisUtils.DEVELOPER_DEFINITION_FORMAT, developerId);

    List<DeveloperDataDefinition> result = redisTemplate.opsForHash().values(key);

    return result;
  }

  public void cacheDeveloperDefinition(String developerId,
      List<DeveloperDataDefinition> definitions) {
    Map<String, DeveloperDataDefinition> entityMap = DeveloperDataMapper.toEntityMap(definitions);

    String key = String.format(RedisUtils.DEVELOPER_DEFINITION_FORMAT, developerId);

    redisTemplate.opsForHash().putAll(key, entityMap);
  }

  public DeveloperDataDefinition getDeveloperDefinitionById(String developerId, String id) {
    LOG.debug("Enter. developerId: {}, id: {}.", developerId, id);

    String key = String.format(RedisUtils.DEVELOPER_DEFINITION_FORMAT, developerId);

    DeveloperDataDefinition result =
        (DeveloperDataDefinition) redisTemplate.opsForHash().get(key, id);

    return result;
  }

  public List<DeviceDataDefinition> getDeviceDataDefinition(String developerId, String productId) {

    LOG.debug("Enter. developerId: {}, productId: {}.", developerId, productId);

    String key = String.format(RedisUtils.DEVICE_DEFINITION_FORMAT, developerId, productId);

    List<DeviceDataDefinition> result = (List<DeviceDataDefinition>)
        redisTemplate.opsForHash().values(key);

    LOG.debug("Exit. dataDefinition size: {}.", result.size());
    return result;
  }

  public DeviceDataDefinition getDeviceDataDefinition(String developerId, String productId,
      String id) {
    LOG.debug("Enter. developerId: {}, productId: {}, id: {}.", developerId, productId, id);

    String key = String.format(RedisUtils.DEVICE_DEFINITION_FORMAT, developerId, productId);

    DeviceDataDefinition dataDefinition =
        (DeviceDataDefinition) redisTemplate.opsForHash().get(key, id);

    if (dataDefinition == null) {
      List<DeviceDataDefinition> dataDefinitions =
          definitionService.getByProductId(developerId, productId);

      cacheDeviceDefinition(developerId, productId, dataDefinitions);

      dataDefinition =
          dataDefinitions.stream().filter(data -> id.equals(data.getId())).findAny().orElse(null);
    }

    LOG.debug("Exit. dataDefinition: {}.", dataDefinition);

    return dataDefinition;
  }


  public void cacheDeviceDefinition(String developerId, String productId,
      List<DeviceDataDefinition> dataDefinitions) {
    LOG.debug("Enter. developerId: {}, productId: {}, dataDefinition size: {}.",
        developerId, productId, dataDefinitions.size());

    String key = String.format(RedisUtils.DEVICE_DEFINITION_FORMAT, developerId, productId);

    Map<String, DeviceDataDefinition> entityMap = DataDefinitionMapper.toEntityMap(dataDefinitions);

    redisTemplate.opsForHash().putAll(key, entityMap);
  }

  public void deleteDeviceDefinition(String developerId, String productId) {
    LOG.debug("Enter. developerId: {}, productId: {}, dataDefinition size: {}.",
        developerId, productId);

    String key = String.format(RedisUtils.DEVICE_DEFINITION_FORMAT, developerId, productId);

    redisTemplate.delete(key);
  }
}
