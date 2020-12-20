package com.dibakar.aggregation.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dibakar.aggregation.model.Shipments;


/**
 * Data access layer.
 *
 *
 */
@Repository
public interface ShipmentsRepository extends CrudRepository<Shipments, Integer>
{
}
