package com.tqs.hw1.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorHandling implements ErrorController {

  @RequestMapping(
      value = "/error",
      method = {RequestMethod.POST, RequestMethod.GET})
  public String renderErrorPage(HttpServletRequest httpRequest, Model errorPage) {

    String error = "";
    int httpErrorCode = getErrorCode(httpRequest);
    switch (httpErrorCode) {
      case 400:
        error = "Http Error Code: 400 Bad Request";
        break;

      case 401:
        error = "Http Error Code: 401 Unauthorized";
        break;

      case 404:
        error = "Http Error Code: 404 Resource not found";
        break;

      case 500:
        error = "Http Error Code: 500 Internal Server Error";
        break;

      default:
        error = "Http Error Code: " + httpErrorCode + " Please try again";
    }
    errorPage.addAttribute("error", error);
    return "errorPage";
  }

  @Override
  public String getErrorPath() {
    return "/error";
  }

  private int getErrorCode(HttpServletRequest httpRequest) {
    if (httpRequest.getAttribute("javax.servlet.error.status_code") == null) return 400;
    return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
  }
}
