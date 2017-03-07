package com.umasuo.device.definition;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by umasuo on 17/2/9.
 */
@SpringBootApplication(scanBasePackages = "com.umasuo")
@RestController
@Configuration
public class Application {

  @Value("${spring.application.name}")
  private String serviceName;

  @GetMapping("/")
  public String getServiceName() {
    return serviceName + " : " + System.currentTimeMillis();
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
