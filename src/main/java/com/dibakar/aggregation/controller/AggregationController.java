package com.dibakar.aggregation.controller;


import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dibakar.aggregation.model.TNTaggregation;
import com.dibakar.aggregation.services.AggregationServices;


@RestController
@RequestMapping("/aggregation")
public class AggregationController {

    private AggregationServices aggregationServices;

    public AggregationController(AggregationServices aggregationServices) {
        this.aggregationServices = aggregationServices;
    }
@CrossOrigin
    @GetMapping()
    public @NotNull TNTaggregation getProducts(@RequestParam Map<String ,String> queryParam ) {
        return aggregationServices.getAggregateResponse(queryParam);
    }
}
