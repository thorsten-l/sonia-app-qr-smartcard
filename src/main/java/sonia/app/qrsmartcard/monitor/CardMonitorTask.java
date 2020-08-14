
package sonia.app.qrsmartcard.monitor;

import java.util.Arrays;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import sonia.app.qrsmartcard.App;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@ostfalia.de>
 */
public class CardMonitorTask extends TimerTask
{

  private final byte[] ATR =
  {
    (byte) 0x3b, (byte) 0x81, (byte) 0x80, (byte) 0x01, (byte) 0x80, (byte) 0x80
  };

  private final CardTerminal terminal;
  private boolean lastCardPresentState;
  private long lastSerial;

  public CardMonitorTask(CardTerminal terminal)
  {
    this.terminal = terminal;
    this.lastSerial = 0;

    try
    {
      lastCardPresentState = terminal.isCardPresent();
    }
    catch (CardException ex)
    {
      Logger.getLogger(CardMonitorTask.class.getName()).log(Level.SEVERE, null,
        ex);
    }
  }

  private void runAction( long serial, boolean insert )
  {
    Thread action = new Thread(new CardMonitorAction(serial, insert), "CardMonitorAction");
    action.setDaemon(true);
    action.start();
  }
  
  @Override
  public void run()
  {
    boolean cardPresent = false;
    try
    {
      cardPresent = terminal.isCardPresent();
    }
    catch (CardException ex)
    {
      Logger.getLogger(CardMonitorTask.class.getName()).log(Level.SEVERE, null,
        ex);
      return;
    }

    if (cardPresent != lastCardPresentState)
    {
      if (cardPresent)
      {
        try
        {
          Card card = terminal.connect("T=1");
          CardChannel channel = card.getBasicChannel();

          byte[] cardAtr = card.getATR().getBytes();

          if (Arrays.compare(ATR, cardAtr) == 0)
          {
            ResponseAPDU answer = channel.transmit(new CommandAPDU((byte) 0xff,
              (byte) 0xca, 0, 0, 256));

            byte[] data = answer.getData();

            long serial = bytesToLongLittleEndian(data);
            
            runAction(serial, true);
            lastSerial = serial;
          }
          else
          {
            App.printBytes("ERROR: unknown card ATR", cardAtr );
            lastSerial = 0;
            runAction( 0, true);
          }
          card.disconnect(false);
        }
        catch (CardException ex)
        {
          Logger.getLogger(CardMonitorTask.class.getName()).
            log(Level.SEVERE, null, ex);
        }
      }
      else
      {
        runAction(lastSerial, false);
      }
    }

    lastCardPresentState = cardPresent;
  }

  public static long bytesToLongLittleEndian(byte[] bytes)
  {
    long uid = 0;

    for (int i = bytes.length - 1; i >= 0; i--)
    {
      byte a = bytes[i];
      uid <<= 8;
      uid += (0x00ff & a);
    }

    return uid;
  }

}
