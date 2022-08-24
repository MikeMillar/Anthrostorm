package net.anthrostorm.client.config;

import java.io.File;
import java.nio.file.Paths;
import joptsimple.ValueConversionException;
import joptsimple.ValueConverter;
import net.anthrostorm.client.Anthrostorm;

/**
 * created by: author: MichaelMillar date: 8/20/2022
 */
public class ConfigFileConverter implements ValueConverter<File> {

  @Override
  public File convert(String fileName) {
    final File file;

    if (Paths.get(fileName).isAbsolute()
          || fileName.startsWith("./")
          || fileName.startsWith(".\\")) {
      file = new File(fileName);
    } else {
      file = new File(Anthrostorm.ANTHROSTORM_DIR, fileName);
    }

    if (file.exists() && (!file.isFile() || !file.canWrite())) {
      throw new ValueConversionException(
          String.format("File %s is not accessible", file.getAbsolutePath()));
    }

    return file;
  }

  @Override
  public Class<? extends File> valueType() {
    return File.class;
  }

  @Override
  public String valuePattern() {
    return null;
  }
}
