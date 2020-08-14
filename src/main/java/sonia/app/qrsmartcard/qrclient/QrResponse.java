package sonia.app.qrsmartcard.qrclient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@ostfalia.de>
 */
@ToString
public class QrResponse
{
  @Getter
  private int code;
  
  @Getter
  private String message;
  
  @Getter
  @Setter
  private String mail;

  @Getter
  @Setter
  private String uid;

  @Getter
  @Setter
  private String sn;

  @Getter
  @Setter
  private String givenName;

  @Getter
  @Setter
  private String soniaStudentNumber;
  
  @Getter
  @Setter
  private String ou;
  
  @Getter
  @Setter
  private String employeeType;
  
  @Getter
  @Setter
  private String phoneNumber;
  
  @Getter
  @Setter
  @ToString.Exclude
  private String jpegPhoto;  
}
