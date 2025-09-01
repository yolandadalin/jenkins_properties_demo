package com.example.hello.config;

import java.io.InputStream;
import java.util.Properties;

public final class EnvSupport {
  private EnvSupport() {}
  public static String resolveEnv() {
    String env = null;
    try (InputStream in = EnvSupport.class.getClassLoader().getResourceAsStream("app-env.properties")) {
      if (in != null) {
        Properties p = new Properties();
        p.load(in);
        env = p.getProperty("envName");
      }
    } catch (Exception ignore) {}
    if (env == null || env.isBlank()) env = System.getProperty("APP_ENV");
    if (env == null || env.isBlank()) env = System.getenv("APP_ENV");
    return (env == null || env.isBlank()) ? "sit" : env;
  }
}
