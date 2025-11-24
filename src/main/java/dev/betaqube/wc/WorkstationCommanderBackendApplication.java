package dev.betaqube.wc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class WorkstationCommanderBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkstationCommanderBackendApplication.class, args);
	}

}
