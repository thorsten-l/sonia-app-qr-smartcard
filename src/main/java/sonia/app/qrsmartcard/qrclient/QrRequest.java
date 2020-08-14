package sonia.app.qrsmartcard.qrclient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@ostfalia.de>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@RequiredArgsConstructor
public class QrRequest
{
  @Getter
  private final String pin;

  @Getter
  private final long cardSerialNumber;

  @Getter
  private final boolean present;

  @Getter
  @Setter
  private String authToken;

  @Getter
  @Setter
  private String location;
}
