package by.minsk.kes.soap.secured.soapsecured;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class SoapSecuredApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoapSecuredApplication.class, args);
	}
}
