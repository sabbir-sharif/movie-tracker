package com.movie_tracker.movie_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieTrackerApplication {

	public static void main(String[] args) {
//		Dotenv dotenv = Dotenv.configure()
//							  .directory("./")
//							  .load();
//
//		System.setProperty("DB_URL", dotenv.get("DB_URL"));
//		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
//		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
//		System.setProperty("spring.profiles.active", dotenv.get("SPRING_PROFILES_ACTIVE"));

		SpringApplication.run(MovieTrackerApplication.class, args);
	}

}
