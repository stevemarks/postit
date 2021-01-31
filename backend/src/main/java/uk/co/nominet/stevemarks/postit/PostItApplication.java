package uk.co.nominet.stevemarks.postit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class PostItApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostItApplication.class, args);
	}
}