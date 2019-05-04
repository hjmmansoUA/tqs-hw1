package com.tqs.hw1.entities;

import com.tqs.hw1.Hw1Application;
import com.tqs.hw1.repository.WeatherRepo;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

  private static Logger logger = LoggerFactory.getLogger(Hw1Application.class);

  public WeatherEntity sendRequestForWeatherNow(String city) {

    HttpUriRequest get =
        RequestBuilder.get()
            .setUri(BASE_URL + NOW + "?q=" + city + "&appid=" + ACCESS_KEY + UNIT)
            .build();

    WeatherEntity result = null;

    try {
      CloseableHttpResponse response = httpClient.execute(get);
      HttpEntity entity = response.getEntity();

      JSONObject weatherReport = new JSONObject(EntityUtils.toString(entity));
      if (weatherReport.has("dt")) {
        double temp = weatherReport.getJSONObject("main").getDouble("temp");
        double tempMin = weatherReport.getJSONObject("main").getDouble("temp_min");
        double tempMax = weatherReport.getJSONObject("main").getDouble("temp_max");
        String description =
            weatherReport.getJSONArray("weather").getJSONObject(0).getString("description");
        result = new WeatherEntity(tempMin, tempMax, temp, description, city);

        response.close();
      } else {
        return null;
      }
    } catch (IOException e) {
      logger.error(e.toString());
    }
    return result;
  }

  public List<WeatherEntity> sendRequestForWeatherInfo(String city) {

    List<WeatherEntity> reportWeather = new ArrayList<>();
    HttpUriRequest get =
        RequestBuilder.get()
            .setUri(BASE_URL + FORECAST + "?q=" + city + "&appid=" + ACCESS_KEY + UNIT)
            .build();

    try {
      CloseableHttpResponse response = httpClient.execute(get);
      HttpEntity entity = response.getEntity();

      JSONObject weatherReport = new JSONObject(EntityUtils.toString(entity));
      if (weatherReport.has("list")) {
        JSONArray report = weatherReport.getJSONArray("list");

        for (int i = 0; i < report.length(); i++) {
          JSONObject tmp = report.getJSONObject(i);

          double temp = tmp.getJSONObject("main").getDouble("temp");
          double tempMin = tmp.getJSONObject("main").getDouble("temp_min");
          double tempMax = tmp.getJSONObject("main").getDouble("temp_max");
          String description =
              tmp.getJSONArray("weather").getJSONObject(0).getString("description");
          String date = tmp.getString("dt_txt");

          WeatherEntity result = new WeatherEntity(tempMin, tempMax, temp, date, description, city);

          // NOTE: unique = true doesnt work this is workaround
          if (weatherRepo.findByUniqueKey(city.concat(date)) == null) weatherRepo.save(result);
          reportWeather.add(result);
        }
      } else {
        return reportWeather;
      }

      response.close();
    } catch (IOException e) {
      logger.error(e.toString());
    }

    return reportWeather;
  }
}
