package net.anthrostorm.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * created by: author: MichaelMillar date: 8/20/2022
 */
public class AnthrostormProperties {

  public static final String ANTHROSTORM_VERSION = "anthrostorm.version";
  public static final String ANTHROSTORM_COMMIT = "anthrostorm.commit";
  public static final String ANTHROSTORM_DIRTY = "anthrostorm.dirty";
  public static final String LAUNCHER_VERSION_PROPERTY = "anthrostorm.launcher.version";

  @Getter(AccessLevel.PACKAGE)
  private static final Properties properties = new Properties();

  static {
    try (InputStream in = AnthrostormProperties.class.getResourceAsStream(
        "anthrostorm.properties")) {
      properties.load(in);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  public static String getVersion() {
    return properties.getProperty(ANTHROSTORM_VERSION);
  }

  public static String getCommit() {
    return properties.getProperty(ANTHROSTORM_COMMIT);
  }

  public static boolean isDirty() {
    return Boolean.parseBoolean(properties.getProperty(ANTHROSTORM_DIRTY));
  }

  public static String getLauncherVersion() {
    return properties.getProperty(LAUNCHER_VERSION_PROPERTY);
  }

}
