package com.pablosaraiva.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class RedirectController {

  @GetMapping("/cats")
  public RedirectView cats()
  {
    return new RedirectView("https://giphy.com/explore/cat/");
  }
}
