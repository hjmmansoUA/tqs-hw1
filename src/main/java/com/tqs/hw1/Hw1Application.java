package com.tqs.hw1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tqs.hw1.entities.OpenWeatherConsume;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Hw1Application {

  public static void main(String[] args) {
    SpringApplication.run(Hw1Application.class, args);
  }

  @Bean(name = "consume")
  public OpenWeatherConsume apiConsume() {
    return new OpenWeatherConsume();
  }
}
