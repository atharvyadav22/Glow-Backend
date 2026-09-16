package org.aystudios.Skincare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class
SkincareApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkincareApplication.class, args);
	}

}
