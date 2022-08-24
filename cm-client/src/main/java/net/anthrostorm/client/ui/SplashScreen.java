package net.anthrostorm.client.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import javax.annotation.Nullable;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;
import lombok.extern.slf4j.Slf4j;
import net.anthrostorm.client.util.ImageUtil;

/**
 * created by: author: MichaelMillar date: 8/20/2022
 */
@Slf4j
public class SplashScreen extends JFrame implements ActionListener {

  private static final int WIDTH = 200;
  private static final int PAD = 10;

  private static SplashScreen INSTANCE;

  private final JLabel action = new JLabel("Loading");
  private final JProgressBar progress = new JProgressBar();
  private final JLabel subAction = new JLabel();
  private final Timer timer;

  private volatile double overallProgress = 0;
  private volatile String actionText = "Loading";
  private volatile String subActionText = "";
  private volatile String progressText = null;

  private SplashScreen() {

    BufferedImage logo = ImageUtil.loadImageResource(SplashScreen.class, "anthrostorm_transparent.png");

    setTitle("Anthrostorm Launcher");

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setUndecorated(true);
    setIconImage(logo);
    setLayout(null);

    Container pane = getContentPane();
    pane.setBackground(ColorScheme.DARKER_GRAY_COLOR);

    Font font = new Font(Font.DIALOG, Font.PLAIN, 12);

    JLabel logoLabel = new JLabel(new ImageIcon(logo));
    pane.add(logoLabel);
    logoLabel.setBounds(0, 0, WIDTH, WIDTH);

    int y = WIDTH;

    pane.add(action);
    action.setForeground(Color.WHITE);
    action.setBounds(0, y, WIDTH, 16);
    action.setHorizontalAlignment(SwingConstants.CENTER);
    action.setFont(font);
    y += action.getHeight() + PAD;

    pane.add(progress);
    progress.setForeground(ColorScheme.MAIN_COLOR);
    progress.setBackground(ColorScheme.MAIN_COLOR.darker().darker());
    progress.setBorder(new EmptyBorder(0,0,0,0));
    progress.setBounds(0, y, WIDTH, 14);
    progress.setFont(font);
    progress.setUI(new BasicProgressBarUI() {
      @Override
      protected Color getSelectionBackground() {
        return Color.BLACK;
      }

      @Override
      protected Color getSelectionForeground() {
        return Color.BLACK;
      }
    });
    y += 12 + PAD;

    pane.add(subAction);
    subAction.setForeground(Color.LIGHT_GRAY);
    subAction.setBounds(0, y, WIDTH, 16);
    subAction.setHorizontalAlignment(SwingConstants.CENTER);
    subAction.setFont(font);
    y+= subAction.getHeight() + PAD;

    setSize(WIDTH, y);
    setLocationRelativeTo(null);

    timer = new Timer(100, this);
    timer.setRepeats(true);
    timer.start();

    setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    action.setText(actionText);
    subAction.setText(subActionText);
    progress.setMaximum(1000);
    progress.setValue((int) (overallProgress * 1000));

    String progressText = this.progressText;
    if (progressText == null) {
      progress.setStringPainted(false);
    } else {
      progress.setStringPainted(true);
      progress.setString(progressText);
    }
  }

  public static boolean isOpen() {
    return INSTANCE != null;
  }

  public static void init() {
    try {
      if (INSTANCE != null) {
        return;
      }
      SwingUtilities.invokeAndWait(() -> INSTANCE = new SplashScreen());
    } catch (Exception e) {
      log.warn("Unable to start splash screen", e);
    }
  }

  public static void stop() {
    SwingUtilities.invokeLater(() -> {
      if (INSTANCE == null) {
        return;
      }

      INSTANCE.timer.stop();
      INSTANCE.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      INSTANCE.dispose();
      INSTANCE = null;
    });
  }

  public static void stage(double overallProgress, @Nullable String actionText, String subActionText) {
    stage(overallProgress, actionText, subActionText, null);
  }

  public static void stage(double startProgress, double endProgress,
      @Nullable String actionText, String subActionText,
      int done, int total, boolean mib) {
    String progress;
    if (mib) {
      final double MiB = 1024*1024;
      final double CEIL = 1.d / 10.d;
      progress = String.format("%.1f  %.1f MiB", done / MiB, (total / MiB) + CEIL);
    } else {
      progress = done + " / " + total;
    }
    stage(startProgress + ((endProgress - startProgress) * done / total), actionText, subActionText, progress);
  }

  public static void stage(double overallProgress, @Nullable String actionText, String subActionText,
      @Nullable String progressText) {
    if (INSTANCE != null) {
      INSTANCE.overallProgress = overallProgress;
      if (actionText != null) {
        INSTANCE.actionText = actionText;
      }
      INSTANCE.subActionText = subActionText;
      INSTANCE.progressText = progressText;
    }
  }


}
