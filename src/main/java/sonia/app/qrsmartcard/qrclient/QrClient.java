package sonia.app.qrsmartcard.qrclient;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sonia.app.qrsmartcard.monitor.CardMonitorAction;

/**
 *
 * @author th
 */
public class QrClient
{
  private final static Logger LOGGER = LoggerFactory.getLogger(
    QrClient.class.getName());

  private final WebTarget target;

  private final String authToken;

  public QrClient(WebTarget target, String authToken)
  {
    this.target = target;
    this.authToken = authToken;
  }

  public QrResponse serachForSmartCard(QrRequest request)
  {
    request.setAuthToken(authToken);

    LOGGER.debug(request.toString());

    QrResponse result = target.request(MediaType.APPLICATION_JSON).post(Entity.
      entity(request, MediaType.APPLICATION_JSON), QrResponse.class);

    LOGGER.debug(result.toString());

    return result;
  }

}
