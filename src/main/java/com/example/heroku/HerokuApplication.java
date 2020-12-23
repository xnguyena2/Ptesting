/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.heroku;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@SpringBootApplication
public class HerokuApplication {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Value("${spring.datasource.username}")
  private String userName;

  @Value("${spring.datasource.password}")
  private String password;

  @Value("${spring.datasource.driverClassName}")
  private String driver;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(HerokuApplication.class, args);
  }

  @RequestMapping("/")
  String index() {
    return "index";
  }

  @RequestMapping("/login")
  String login() {
    return "login/index";
  }

  @RequestMapping("/angular")
  String angular() {
    return "angular/index";
  }

  /*
  @RequestMapping("/db")
  String db(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Read from DB: " + rs.getTimestamp("tick"));
      }

      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }
  */
/*
  @Bean
  public DataSource dataSource() {
    try{
      System.out.println(dbUrl);
      if (dbUrl == null || dbUrl.isEmpty()) {
        return new HikariDataSource();
      } else {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driver);
        config.setUsername(userName);
        config.setPassword(password);
        config.setJdbcUrl(dbUrl);
        return new HikariDataSource(config);
      }
    }catch (Exception ex){
      System.out.println(ex);
      ex.printStackTrace();
      throw ex;
    }
  }
*/
  /*
  @Bean
  public PasswordEncoder passwordEncoder() {
    System.out.println("PasswordEncoder");
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
*/
  // only for setup cors
/*
  @Bean
  public WebMvcConfigurer cors() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        System.out.println("CORS setup");
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")

                .allowedOrigins("http://localhost:4200/");
      }
    };
  }
*/
  /*
  @Configuration
  @EnableJpaAuditing
  class DataJpaConfig {

    @Bean
    public AuditorAware<User> auditor() {
      System.out.println("Audit entity!");
      return () -> Optional.ofNullable(SecurityContextHolder.getContext())
              .map(SecurityContext::getAuthentication)
              .filter(Authentication::isAuthenticated)
              .map(Authentication::getPrincipal)
              .map(User.class::cast);
    }
  }
  */
}