package com.tqs.hw1.Controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Nested
@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerTest {

  @Autowired private MockMvc mockMvc;

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  void currentOK() throws Exception {

    this.mockMvc
        .perform(get("/json/current/Aveiro"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"));
  }

  @Test
  void forecastOK() throws Exception {

    this.mockMvc
        .perform(get("/json/current/Aveiro"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"));
  }

  @Test
  void currentFail() throws Exception {

    this.mockMvc
        .perform(get("/json/current/FailTest"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/error"));
  }

  @Test
  void forecastFail() throws Exception {
    this.mockMvc
        .perform(get("/json/current/FailTest"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/error"));
  }

  @Test
  void urlNotFound() throws Exception {
    this.mockMvc.perform(get("/j")).andExpect(status().isNotFound());
  }
}
