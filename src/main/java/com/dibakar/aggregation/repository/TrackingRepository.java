package com.dibakar.aggregation.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dibakar.aggregation.model.Shipments;
import com.dibakar.aggregation.model.Tracking;


/**
 * Data access layer.
 *
 *
 */
@Repository
public interface TrackingRepository extends CrudRepository<Tracking, Integer>
{
}