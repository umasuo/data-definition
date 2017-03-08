package com.umasuo.datapoint.infrastructure.repository;

import com.umasuo.developer.domain.model.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by umasuo on 17/2/10.
 */
@Repository
public interface DeveloperRepository extends JpaRepository<Developer, String>,
    CrudRepository<Developer, String> {

  Developer findOneByEmail(String email);
}
