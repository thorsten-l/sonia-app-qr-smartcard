/**
 * OSTFALIA CONFIDENTIAL
 *
 * 2010 - 2013 Ostfalia University of Applied Sciences All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of
 * Ostfalia University of Applied Sciences and its suppliers, if any. The
 * intellectual and technical concepts contained herein are proprietary to
 * Ostfalia University of Applied Sciences and its suppliers and may be covered
 * by U.S. and Foreign Patents, patents in process, and are protected by trade
 * secret or copyright law. Dissemination of this information or reproduction of
 * this material is strictly forbidden unless prior written permission is
 * obtained from Ostfalia University of Applied Sciences.
 */



package sonia.commons.crypt.cipher;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@ostfalia.de>
 * @since 1.1.0
 */
public interface SimpleCipher
{

  /**
   * Method description
   *
   *
   * @param value
   *
   * @return
   */
  public String decrypt(String value);

  /**
   * Method description
   *
   *
   * @param value
   *
   * @return
   */
  public String encrypt(String value);
}
