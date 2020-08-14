/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonia.app.qrsmartcard.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sonia.app.qrsmartcard.Config;
import sonia.app.qrsmartcard.qrclient.QrClient;
import sonia.app.qrsmartcard.qrclient.QrClientFactory;
import sonia.app.qrsmartcard.qrclient.QrRequest;
import sonia.app.qrsmartcard.qrclient.QrResponse;
import sonia.app.qrsmartcard.ui.ViewPort;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@ostfalia.de>
 */
public class CardMonitorAction implements Runnable
{
  private final static Logger LOGGER = LoggerFactory.getLogger(
    CardMonitorAction.class.getName());

  private final static Config CONFIG = Config.getInstance();

  private final long serial;

  private final boolean insert;

  public CardMonitorAction(long serial, boolean insert)
  {
    this.serial = serial;
    this.insert = insert;
  }

  @Override
  public void run()
  {
    LOGGER.info("card serial = " + serial + ", present = " + insert);

    if ( insert )
    {
      ViewPort.setState(3);
    }
    
    QrClient client = QrClientFactory.getClient();
    QrRequest request = new QrRequest(CONFIG.getRoomPin(), serial, insert);
    QrResponse response = client.serachForSmartCard(request);

    if (insert)
    {
      if (response.getCode() == 0)
      {
        ViewPort.setQrResponse(response);
      }
      else
      {
        ViewPort.setState(1);
      }
    }
    else
    {
      ViewPort.setState(0);
    }
  }
}
