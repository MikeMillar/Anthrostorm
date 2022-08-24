package net.anthrostorm.client.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;

/**
 * created by: author: MichaelMillar date: 8/20/2022
 */
@Slf4j
public class ImageUtil {

  /**
   * Reads an image resource from a given path relative to a given class. This method is primarily
   * shorthand for the synchronization and error handling required for loading image resources from
   * the classpath.
   *
   * @param c    The class to be referenced for the package path.
   * @param path The path, relative to the given class.
   * @return A {@link BufferedImage} of the loaded image resource from the given path.
   */
  public static BufferedImage loadImageResource(final Class<?> c, final String path) {
    try (InputStream in = c.getResourceAsStream(path)) {
      synchronized (ImageIO.class) {
        return ImageIO.read(in);
      }
    } catch (IllegalArgumentException e) {

      URL resource = c.getClassLoader().getResource(path);
      if (resource == null) {
        System.out.println("NULL");
      }

      final String filePath;

      if (path.startsWith("/")) {
        filePath = path;
      } else {
        filePath = c.getPackage().getName().replace('.', '/') + "/" + path;
      }

      log.warn("Failed to load image from class: {}, path: {}", c.getName(), filePath);

      throw new IllegalArgumentException(path, e);
    } catch (IOException e) {
      throw new RuntimeException(path, e);
    }
  }

}
