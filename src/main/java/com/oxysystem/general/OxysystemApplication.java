package com.oxysystem.general;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableRetry
@EnableScheduling
@EnableAsync

//extends SpringBootServletInitializer
public class OxysystemApplication  {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(OxysystemApplication.class);
		// Jika tidak ada profile yang di-set, pakai 'dev'
		if (System.getProperty("spring.profiles.active") == null &&
				System.getenv("SPRING_PROFILES_ACTIVE") == null) {
			app.setAdditionalProfiles("dev");
		}

		app.run(args);
	}



//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(OxysystemApplication.class);
//	}
}
