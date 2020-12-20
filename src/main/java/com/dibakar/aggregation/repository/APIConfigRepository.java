package com.dibakar.aggregation.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dibakar.aggregation.model.APIConfig;


/**
 * Data access layer.
 *
 *
 */
@Repository
public interface APIConfigRepository extends CrudRepository<APIConfig, Integer>
{
}
