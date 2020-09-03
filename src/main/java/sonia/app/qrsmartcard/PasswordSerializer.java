package sonia.app.qrsmartcard;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sonia.commons.crypt.cipher.AesSimpleCipher;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@ostfalia.de>
 */
public class PasswordSerializer extends JsonSerializer<String>
{
  private final static Logger LOGGER = LoggerFactory.getLogger(
    PasswordSerializer.class.getName());

    /** secret key */
  private static final char[] KEY = Config.getInstance().getCipherKey().toCharArray();
   
  /** cipher */
  private static final AesSimpleCipher CIPHER = 
    AesSimpleCipher.builder().build(KEY);

    public static String encrypt(String value)
  {
    return CIPHER.encrypt(value);
  }

  /**
   * Decrypt an encrypted value.
   *
   * @param value value to encrypt
   *
   * @return decrypted value
   */
  public static String decrypt(String value)
  {
    return CIPHER.decrypt(value);
  }
  
  /**
   *
   * @param value
   * @param generator
   * @param provider
   * @throws IOException
   */
  @Override
  public void serialize(String value, JsonGenerator generator, SerializerProvider provider)
    throws IOException
  {
    LOGGER.debug( value );
    generator.writeString(encrypt(value));
  }
}
