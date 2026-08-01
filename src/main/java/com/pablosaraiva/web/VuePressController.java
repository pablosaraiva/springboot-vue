package com.pablosaraiva.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Maps VuePress's frontend routes to the static {@code index.html} so that deep
 * links resolve server-side (the VuePress client router then owns rendering).
 *
 * <p>Each route is declared in both slash and no-slash forms. Spring Framework 6
 * (Spring Boot 3) no longer matches a trailing slash against a slash-less pattern
 * by default, so both forms are listed explicitly rather than relying on
 * {@code setUseTrailingSlashMatch} (deprecated in Spring 6, removed in Spring 7).
 * VuePress's nav and sitemap use trailing-slash URLs (e.g. {@code /about/},
 * {@code /posts/page/2/}, {@code /2020/02/20/continuous-deployment/}), so the
 * trailing-slash variants must be present or those canonical URLs 404.
 *
 * <p>Only GET/HEAD are served — the frontend is read-only.
 */
@Controller
public class VuePressController {

  private static final String INDEX_HTML = "index.html";

  @GetMapping({
      "/mdm", "/mdm/",
      "/about", "/about/",
      "/todo", "/todo/",
      "/blog", "/blog/",
      "/music", "/music/",
      "/posts", "/posts/",
      "/posts/page/*", "/posts/page/*/",
      "/2*/*/*/*", "/2*/*/*/*/"
  })
  public String routeFrontendUrls() {
    return INDEX_HTML;
  }
}