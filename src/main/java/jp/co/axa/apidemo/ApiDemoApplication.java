package jp.co.axa.apidemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Main Spring boot application
 */
@EnableSwagger2
@SpringBootApplication
public class ApiDemoApplication {

	/**
	 * Spring application runner main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(ApiDemoApplication.class, args);
	}

}
