package com.tqs.hw1;

import com.tqs.hw1.repository.WeatherRepo;
import com.tqs.hw1.entities.WeatherEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Nested
@RunWith(SpringRunner.class)
@DataJpaTest
class JPATest {

  @Autowired private WeatherRepo weatherRepo;

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  void persistWeather() {
    WeatherEntity w1 =
        new WeatherEntity(10, 15, 13, "2019-05-04 12:00:00", "testDesc", "testlocation1");
    WeatherEntity w2 =
        new WeatherEntity(10, 15, 13, "2019-05-04 12:00:00", "testDesc2", "testlocation2");

    weatherRepo.save(w1);
    weatherRepo.save(w2);

    assertEquals(1, weatherRepo.findAllByLocation("testlocation1").size());
    assertEquals(1, weatherRepo.findAllByLocation("testlocation2").size());
  }

  @Test
  @Transactional
  void findAndDelete() {
    weatherRepo.deleteWeatherEntityByLocation("testlocation1");
    assertEquals(0, weatherRepo.findAllByLocation("testlocation1").size());
    weatherRepo.deleteWeatherEntityByLocation("testlocation2");
    assertEquals(0, weatherRepo.findAllByLocation("testlocation2").size());
  }
}
