package com.umasuo.datapoint.infrastructure.repository;

import com.umasuo.datapoint.domain.model.DeveloperDataDefinition;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Davis on 17/6/30.
 */
public interface DeveloperDataRepository extends JpaRepository<DeveloperDataDefinition, String> {

}
