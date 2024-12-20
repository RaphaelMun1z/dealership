package com.merco.dealership.integrationtests.testcontainers;

import java.util.Map;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

	static public class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public static PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>("postgres:16");

		private static void startContainers() {
			Startables.deepStart(Stream.of(postgresql)).join();
		}

		private static Map<String, String> createConnectionConfiguration() {
			return Map.of("spring.datasource.url", postgresql.getJdbcUrl(), "spring.datasource.username",
					postgresql.getUsername(), "spring.datasource.password", postgresql.getPassword());
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			startContainers();
			ConfigurableEnvironment environment = applicationContext.getEnvironment();
			MapPropertySource testcontainers = new MapPropertySource("testcontainers",
					(Map) createConnectionConfiguration());
			environment.getPropertySources().addFirst(testcontainers);
		}

	}

}
