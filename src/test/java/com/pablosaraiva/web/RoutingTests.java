package com.pablosaraiva.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Regression guard for the frontend routing that {@link VuePressController} owns.
 * Spring Boot 3 / Spring Framework 6 disabled trailing-slash matching by default,
 * so the canonical trailing-slash URLs (the ones VuePress's nav and sitemap emit)
 * must keep mapping to the controller — otherwise the live site silently 404s on
 * every nav link.
 *
 * <p>Assertions check the resolved view name rather than HTTP 200 so the test does
 * not depend on the built VuePress {@code dist} being present in
 * {@code src/main/resources/static} (it is only produced by {@code buildvue.sh}
 * or the Docker build). The point under test is the request mapping, not the
 * static resource content.
 */
@SpringBootTest
@AutoConfigureMockMvc
class RoutingTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void navRoutesWithTrailingSlashMapToFrontend() throws Exception {
    for (String path : new String[] {"/about/", "/blog/", "/music/", "/mdm/", "/todo/", "/posts/"}) {
      mockMvc.perform(get(path)).andExpect(view().name("index.html"));
    }
  }

  @Test
  void navRoutesWithoutTrailingSlashAlsoMapToFrontend() throws Exception {
    for (String path : new String[] {"/about", "/blog", "/music", "/posts"}) {
      mockMvc.perform(get(path)).andExpect(view().name("index.html"));
    }
  }

  @Test
  void paginationRouteWithTrailingSlashMapsToFrontend() throws Exception {
    mockMvc.perform(get("/posts/page/2/")).andExpect(view().name("index.html"));
  }

  @Test
  void datedPostPermalinkWithTrailingSlashMapsToFrontend() throws Exception {
    // The canonical sitemap URL for a post (trailing slash) must resolve.
    mockMvc.perform(get("/2020/02/20/continuous-deployment/")).andExpect(view().name("index.html"));
  }

  @Test
  void catsRedirectsExternally() throws Exception {
    mockMvc.perform(get("/cats"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("https://giphy.com/**"));
  }
}