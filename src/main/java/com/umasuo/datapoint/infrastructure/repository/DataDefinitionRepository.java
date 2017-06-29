package com.umasuo.datapoint.infrastructure.repository;

import com.umasuo.datapoint.domain.model.DataDefinition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by umasuo on 17/2/10.
 */
@Repository
public interface DataDefinitionRepository extends JpaRepository<DataDefinition, String> {

}
