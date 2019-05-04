package com.tqs.hw1.controller;

import com.tqs.hw1.entities.OpenWeatherConsume;
import com.tqs.hw1.entities.WeatherEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Component
@RestController
@RequestMapping(path = "/json")
public class WeatherAPI {

  @Autowired private OpenWeatherConsume response;

  @GetMapping(path = "/current/{location}", produces = "application/json")
  public WeatherEntity current(@PathVariable("location") String location) {
    WeatherEntity report = response.sendRequestForWeatherNow(location);
    if (report == null) {
      throw new CustomExcection();
    }
    return report;
  }

  @GetMapping(path = "/forecast/{location}", produces = "application/json")
  public List forecast(@PathVariable("location") String location) {
    List<WeatherEntity> report = response.sendRequestForWeatherInfo(location);
    if (report.isEmpty()) {
      throw new CustomExcection();
    }
    return report;
  }

  @ExceptionHandler(CustomExcection.class)
  public ModelAndView error() {
    return new ModelAndView("redirect:/error");
  }
}
