package by.minsk.kes.soap.secured.soapsecured;

import by.minsk.kes.soap.secured.soapsecured.client.WebServiceGateway;
import by.minsk.kes.soap.secured.soapsecured.model.PingRequest;
import by.minsk.kes.soap.secured.soapsecured.model.PingResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jmx.export.annotation.ManagedOperation;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
//@EnableMBeanExport
public class SoapSecuredApplication {

	@Autowired
	private WebServiceGateway gateway;

	public static void main(String[] args) {
		SpringApplication.run(SoapSecuredApplication.class, args);
	}

//	@ManagedOperation(description = "call soap")
	@Bean
	public Object callToService() throws IOException {
		final ScheduledExecutorService service =  Executors.newScheduledThreadPool(1);
		service.scheduleWithFixedDelay(() -> {
			final PingRequest rq = new PingRequest();
			rq.setText(String.valueOf(new Random().nextInt(1000)));
			final PingResponse rs;
			try {
				rs = gateway.callService(rq);
				System.out.println(rs.getResult().getResult());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}, 5, 5, TimeUnit.SECONDS);

	  return null;
	}
}
