package com.pablosaraiva.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Restores the Spring Boot 2.x default of matching request paths with a trailing
 * slash against patterns declared without one (e.g. {@code /about} matching
 * {@code /about/}). Spring Framework 6 / Spring Boot 3 disabled this by default,
 * which would break VuePress's nav links (which use trailing slashes such as
 * {@code /blog/}, {@code /music/}, {@code /about/}) since {@link VuePressController}
 * declares them without the trailing slash.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    configurer.setUseTrailingSlashMatch(true);
  }
}