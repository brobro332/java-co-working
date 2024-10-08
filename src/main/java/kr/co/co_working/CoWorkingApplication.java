package kr.co.co_working;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CoWorkingApplication {
	public static void main(String[] args) {
		SpringApplication.run(CoWorkingApplication.class, args);
	}
}
