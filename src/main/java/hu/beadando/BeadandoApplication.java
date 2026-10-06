package hu.beadando;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "hu.beadando")
public class BeadandoApplication {
	public static void main(String[] args) {
		SpringApplication.run(BeadandoApplication.class, args);
	}
}
