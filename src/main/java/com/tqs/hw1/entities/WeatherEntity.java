package com.tqs.hw1.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity

public class WeatherEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long ID;

  private double min;
  private double max;
  private double curr;
  private String date;
  private String description;
  private String location;

  @JsonIgnore
  @Column(unique = true)
  private String uniqueKey;

  public WeatherEntity() {}

  public WeatherEntity(
      double min, double max, double curr, String date, String description, String location) {
    this.min = min;
    this.max = max;
    this.curr = curr;
    this.date = date;
    this.description = description;
    this.location = location;
    this.uniqueKey = location.concat(date);
  }

  public WeatherEntity(double min, double max, double curr, String description, String location) {
    this.min = min;
    this.max = max;
    this.curr = curr;
    this.description = description;
    this.location = location;
  }

  public long getID() {
    return ID;
  }

  public void setID(long ID) {
    this.ID = ID;
  }

  public double getMin() {
    return min;
  }

  public void setMin(double min) {
    this.min = min;
  }

  public double getMax() {
    return max;
  }

  public void setMax(double max) {
    this.max = max;
  }

  public double getCurr() {
    return curr;
  }

  public void setCurr(double curr) {
    this.curr = curr;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getUniqueKey() {
    return uniqueKey;
  }

  public void setUniqueKey(String location, String date) {
    this.uniqueKey = location.concat(date);
  }
}
