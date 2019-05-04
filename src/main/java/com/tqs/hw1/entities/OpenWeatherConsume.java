package com.tqs.hw1.entities;

import com.tqs.hw1.Repository.WeatherRepo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenWeatherConsume {

  private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
  private static final String FORECAST = "forecast";
  private static final String NOW = "weather";
  private static final String UNIT = "&units=metric";
  private static final String ACCESS_KEY = "2f899e3c15e20df8afd4612f6b537624";
  private static CloseableHttpClient httpClient = HttpClients.createDefault();

  @Autowired private WeatherRepo weatherRepo;

  public OpenWeatherConsume() {}

  public WeatherEntity sendRequestForWeatherNow(String city) {
    HttpGet get = new HttpGet(BASE_URL + NOW + "?q=" + city + "&appid=" + ACCESS_KEY + UNIT);

    WeatherEntity result = null;

    try {
      CloseableHttpResponse response = httpClient.execute(get);
      HttpEntity entity = response.getEntity();

      JSONObject weatherReport = new JSONObject(EntityUtils.toString(entity));
      System.out.println(weatherReport);
      if (weatherReport.has("dt")) {
        double temp = weatherReport.getJSONObject("main").getDouble("temp");
        double temp_min = weatherReport.getJSONObject("main").getDouble("temp_min");
        double temp_max = weatherReport.getJSONObject("main").getDouble("temp_max");
        String description =
            weatherReport.getJSONArray("weather").getJSONObject(0).getString("description");
        result = new WeatherEntity(temp_min, temp_max, temp, description, city);

        response.close();
      } else {
        return null;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  public List<WeatherEntity> sendRequestForWeatherInfo(String city) {

    List<WeatherEntity> reportWeather = new ArrayList<>();
    HttpGet get = new HttpGet(BASE_URL + FORECAST + "?q=" + city + "&appid=" + ACCESS_KEY + UNIT);

    try {
      CloseableHttpResponse response = httpClient.execute(get);
      HttpEntity entity = response.getEntity();

      JSONObject weatherReport = new JSONObject(EntityUtils.toString(entity));
      if (weatherReport.has("list")) {
        JSONArray report = weatherReport.getJSONArray("list");

        for (int i = 0; i < report.length(); i++) {
          JSONObject tmp = report.getJSONObject(i);

          double temp = tmp.getJSONObject("main").getDouble("temp");
          double temp_min = tmp.getJSONObject("main").getDouble("temp_min");
          double temp_max = tmp.getJSONObject("main").getDouble("temp_max");
          String description =
              tmp.getJSONArray("weather").getJSONObject(0).getString("description");
          String date = tmp.getString("dt_txt");

          WeatherEntity result =
              new WeatherEntity(temp_min, temp_max, temp, date, description, city);

          // NOTE: unique = true doesnt work this is workaround
          if (weatherRepo.findByUniqueKey(city.concat(date)) == null) weatherRepo.save(result);
          reportWeather.add(result);
        }
      } else {
        return null;
      }

      response.close();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

    return reportWeather;
  }
}
