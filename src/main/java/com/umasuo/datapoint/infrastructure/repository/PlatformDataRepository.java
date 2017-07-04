package com.umasuo.datapoint.infrastructure.repository;

import com.umasuo.datapoint.domain.model.PlatformDataDefinition;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by umasuo on 17/2/10.
 */
public interface PlatformDataRepository extends JpaRepository<PlatformDataDefinition, String> {
}
