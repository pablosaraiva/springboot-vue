package com.pablosaraiva.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VuePressController {

  private static final String INDEX_HTML = "index.html";

  @RequestMapping({"/mdm", "/about", "/todo", "/blog", "/cats"})
  public String routeFrontendUrls() {
    return INDEX_HTML;
  }
}
