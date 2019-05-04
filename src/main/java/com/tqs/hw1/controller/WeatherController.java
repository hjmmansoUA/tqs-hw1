package com.tqs.hw1.controller;

import com.tqs.hw1.entities.OpenWeatherConsume;
import com.tqs.hw1.entities.WeatherEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component
@Controller
@RequestMapping(path = "/")
public class WeatherController {

  @Autowired private OpenWeatherConsume response;

  @PostMapping(path = "/current")
  public String current(@RequestParam("location") String location, Model model) {
    WeatherEntity report = response.sendRequestForWeatherNow(location);
    if (report == null) {
      throw new CustomExcection();
    }
    model.addAttribute("current", report);
    return "current";
  }

  @PostMapping(path = "/forecast")
  public String forecast(@RequestParam("location") String location, Model model) {
    List<WeatherEntity> report = response.sendRequestForWeatherInfo(location);
    if (report.isEmpty()) {
      throw new CustomExcection();
    }
    model.addAttribute("forecast", report);
    return "forecast";
  }

  @ExceptionHandler(CustomExcection.class)
  public String error() {
    return "redirect:/error";
  }
}
