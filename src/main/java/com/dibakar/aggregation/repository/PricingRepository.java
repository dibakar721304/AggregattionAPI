package com.dibakar.aggregation.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dibakar.aggregation.model.Pricing;


/**
 * Data access layer.
 *
 *
 */
@Repository
public interface PricingRepository extends CrudRepository<Pricing, Integer>
{
}
