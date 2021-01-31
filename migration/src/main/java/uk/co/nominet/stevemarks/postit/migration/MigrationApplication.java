package uk.co.nominet.stevemarks.postit.migration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MigrationApplication {

	public static void main(String[] args) {
		System.exit(SpringApplication.exit(SpringApplication.run(MigrationApplication.class, args)));
	}
}
