package com.dibakar.aggregation;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.dibakar.aggregation.constant.Constant;
import com.dibakar.aggregation.model.APIConfig;
import com.dibakar.aggregation.repository.APIConfigRepository;

/**
 * Entry point for the aggregation API to start
 * 
 * @author Dibakar
 *
 */
@SpringBootApplication
@EnableJms
@EnableScheduling
public class AggregationApplication {
	Logger logger = LoggerFactory.getLogger(AggregationApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AggregationApplication.class, args);
	}
/**
 * Initial data load for API configuration at the start of application
 * @param apiConfigRepository
 * @return
 */
	@Bean
	public CommandLineRunner init(APIConfigRepository apiConfigRepository ) {
		logger.debug("Initial data load through commandLineRunner");

		return args -> {
			Stream.of(Constant.SHIPMENTS, Constant.TRACKING, Constant.PRICING).forEach(name -> { 
				APIConfig apiConfig = new APIConfig();
				apiConfig.setApi_name(name);
				apiConfig.setRequest_count(0);
				apiConfig.setQuery_param("");
				apiConfig.setFirst_request_timestamp(null);
				apiConfigRepository.save(apiConfig);
			});
		};
	}

}
