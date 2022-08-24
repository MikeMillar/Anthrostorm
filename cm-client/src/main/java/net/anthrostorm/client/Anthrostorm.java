package net.anthrostorm.client;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.inject.Injector;
import com.google.inject.Provider;
import java.io.File;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.anthrostorm.client.account.SessionManager;
import net.anthrostorm.client.config.ConfigFileConverter;
import net.anthrostorm.client.config.ConfigManager;
import net.anthrostorm.client.plugins.PluginManager;
import net.anthrostorm.client.ui.SplashScreen;
import net.anthrostorm.client.ui.overlay.OverlayManager;
import net.anthrostorm.client.ui.overlay.tooltip.TooltipOverlay;
import org.slf4j.LoggerFactory;

/**
 * created by: author: MichaelMillar date: 8/11/2022
 */
@Singleton
@Slf4j
public class Anthrostorm {

  public static final File ANTHROSTORM_DIR = new File(System.getProperty("user.home"), ".anthrostorm");
  public static final File PLUGINS_DIR = new File(ANTHROSTORM_DIR, "plugins");
  public static final File PROFILES_DIR = new File(ANTHROSTORM_DIR, "profiles");
  public static final File LOGS_DIR = new File(ANTHROSTORM_DIR, "logs");
  public static final File DEFAULT_SESSION_FILE = new File(ANTHROSTORM_DIR, "session");
  public static final File DEFAULT_CONFIG_FILE = new File(ANTHROSTORM_DIR, "settings.properties");

  public static String USER_AGENT = "Anthrostorm/" + AnthrostormProperties.getVersion()
      + "-" + AnthrostormProperties.getCommit()
      + (AnthrostormProperties.isDirty() ? "+" : "");

  @Getter
  private static Injector injector;

  @Inject
  private PluginManager pluginManager;

  @Inject
  private ConfigManager configManager;

  @Inject
  private SessionManager sessionManager;

  @Inject
  private ClientSessionManager clientSessionManager;

  @Inject
  private OverlayManager overlayManager;

  @Inject
  private Provider<TooltipOverlay> tooltipOverlay;


  public static void main(String[] args) throws Exception {

    Locale.setDefault(Locale.ENGLISH);

    final OptionParser parser = new OptionParser(false);
    parser.accepts("developer-mode", "Enable developer tools");
    parser.accepts("debug", "Show extra debugging output");

    final ArgumentAcceptingOptionSpec<File> sessionfile = parser.accepts(
        "sessionfile", "Use a specified session file")
        .withRequiredArg()
        .withValuesConvertedBy(new ConfigFileConverter())
        .defaultsTo(DEFAULT_SESSION_FILE);

    final ArgumentAcceptingOptionSpec<File> configfile = parser.accepts(
        "config", "Use a specified config file")
        .withRequiredArg()
        .withValuesConvertedBy(new ConfigFileConverter())
        .defaultsTo(DEFAULT_CONFIG_FILE);

    parser.accepts("help", "Show this text").forHelp();
    OptionSet options = parser.parse(args);

    if (options.has("help")) {
      parser.printHelpOn(System.out);
      System.exit(0);
    }

    if (options.has("debug")) {
      final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
      logger.setLevel(Level.DEBUG);
    }

    Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
      log.error("Uncaught execption:", throwable);
      if (throwable instanceof AbstractMethodError) {
        log.error("Classes are out of date; Build with maven again.");
      }
    });

    SplashScreen.init();
    SplashScreen.stage(0, "Retrieving client", "");




  }

}
