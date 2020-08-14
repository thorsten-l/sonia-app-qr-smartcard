package sonia.app.qrsmartcard;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import sonia.app.qrsmartcard.monitor.CardMonitorTask;
import java.util.List;
import java.util.Timer;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sonia.app.qrsmartcard.ui.BaseFrame;

// https://github.com/jnasmartcardio/jnasmartcardio
// https://github.com/jakobwenzel/MensaGuthaben/tree/master/MensaGuthaben/src/main/java/com/codebutler/farebot/card/desfire
// https://www.programcreek.com/java-api-examples/?code=EsupPortail%2Fesup-nfc-tag-server%2Fesup-nfc-tag-server-master%2Fsrc%2Fmain%2Fjava%2Forg%2Fesupportail%2Fnfctag%2Fservice%2Fdesfire%2FDESFireEV1Service.java
// https://github.com/grundid/nfctools
// https://nicedoc.io/jnasmartcardio/jnasmartcardio
// https://de.wikipedia.org/wiki/Application_Protocol_Data_Unit
/**
 *
 * @author Thorsten Ludewig <t.ludewig@ostfalia.de>th
 */
public class App
{
  private final static Logger LOGGER = LoggerFactory.getLogger(
    App.class.getName());

  // Seriennummer 36067801265368580 // Folgenummer 2

  /* Commands */
  static final byte GET_MANUFACTURING_DATA = (byte) 0x60;

  static final byte GET_APPLICATION_DIRECTORY = (byte) 0x6A;

  static final byte GET_ADDITIONAL_FRAME = (byte) 0xAF;

  static final byte SELECT_APPLICATION = (byte) 0x5A;

  static final byte READ_DATA = (byte) 0xBD;

  static final byte READ_RECORD = (byte) 0xBB;

  static final byte READ_VALUE = (byte) 0x6C;

  static final byte GET_FILES = (byte) 0x6F;

  static final byte GET_FILE_SETTINGS = (byte) 0xF5;

  /* Status codes */
  static final byte OPERATION_OK = (byte) 0x00;

  static final byte PERMISSION_DENIED = (byte) 0x9D;

  static final byte ADDITIONAL_FRAME = (byte) 0xAF;

  static final byte DESFIRE_CLA = (byte) 0x90;

  static final byte DESFIRE_RESPONSE_SW1 = (byte) 0x91;

  private static BaseFrame baseFrame;

  public static void printBytes(String name, byte[] bytes)
  {
    System.out.print(name + ":");
    for (byte a : bytes)
    {
      System.out.printf(" %02x", a);
    }
    System.out.println();
  }

  public static void printBytesAsLongLittleEndian(String name, byte[] bytes)
  {
    long uid = 0;

    for (int i = bytes.length - 1; i >= 0; i--)
    {
      byte a = bytes[i];
      uid <<= 8;
      uid += (0x00ff & a);
    }

    System.out.printf(name + ": %d\n", uid);
  }

  public static void main(String[] args) throws Exception
  {
    System.out.println("QR-SmartCard");

    if (System.getProperty("app.home") == null)
    {
      System.setProperty("app.home", ".");
    }

    Config config = Config.getInstance();

    BuildProperties build = BuildProperties.getInstance();
    LOGGER.info("Project Name    : " + build.getProjectName());
    LOGGER.info("Project Version : " + build.getProjectVersion());
    LOGGER.info("Build Timestamp : " + build.getTimestamp());

    TerminalFactory factory = TerminalFactory.getDefault();

    List<CardTerminal> terminals = null;

    try
    {
      terminals = factory.terminals().list();
    }
    catch (CardException ex)
    {
      terminals = null;
    }

    if (terminals == null || terminals.size() == 0)
    {
      System.out.println("ERROR: No terminals present.");
      System.exit(-1);
    }

    LOGGER.info("Terminals: " + terminals);

    CardTerminal terminal = terminals.get(0);
    LOGGER.info("Terminal Name : <" + terminal.getName()
      + ">  Card present : " + terminal.isCardPresent());

    CardMonitorTask task = new CardMonitorTask(terminal);
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(task, 0, 1000);

    java.awt.EventQueue.invokeLater(
      () ->
    {
      baseFrame = new BaseFrame();

      if (!config.isUiDevelopment())
      {
        GraphicsDevice device
          = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getScreenDevices()[0];

        baseFrame.setAlwaysOnTop(true);
        baseFrame.setAutoRequestFocus(true);

        Rectangle screenSize = device.getDefaultConfiguration().getBounds();
        Dimension appDimension = new Dimension(screenSize.width,
          screenSize.height);

        LOGGER.info("Screen size = " + screenSize);

        baseFrame.setPreferredSize(appDimension);
        baseFrame.setMinimumSize(appDimension);
        baseFrame.setVisible(true);
        device.setFullScreenWindow(baseFrame);
      }

      baseFrame.setVisible(true);
    });
  }
}
