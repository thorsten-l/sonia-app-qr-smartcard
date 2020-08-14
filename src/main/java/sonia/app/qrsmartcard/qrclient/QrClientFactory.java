/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonia.app.qrsmartcard.qrclient;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import sonia.app.qrsmartcard.Config;

/**
 *
 * @author th
 */
public class QrClientFactory
{

  private final static Config CONFIG = Config.getInstance();
  
  private QrClientFactory()
  {
  }

  public static QrClient getClient()
  {
    Client client = ClientBuilder.newClient();

    WebTarget target = client.target(CONFIG.getWebServiceUrl());

    return new QrClient(target, CONFIG.getApiAuthToken());
  }
}
