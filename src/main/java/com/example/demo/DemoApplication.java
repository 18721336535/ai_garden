package com.example.demo;

import com.example.demo.service.VideoStreamWebSocketHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext applicationContext = SpringApplication.run(DemoApplication.class, args);
		VideoStreamWebSocketHandler.setApplicationContext(applicationContext);
	}
	@Bean
	MeterRegistryCustomizer<io.micrometer.core.instrument.MeterRegistry> configureMetrics(
			@Value("${spring.application.name}") String applicationName
	) {
		return registry -> registry.config().commonTags("application", applicationName);
	}

}
