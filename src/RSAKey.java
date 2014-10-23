import java.math.BigInteger;

/**
 * A simple container for the modulus and value needed for an RSA key.
 * Getters and a toString nothing fancy.
 * @author Garrett Smith, 3018390
 *
 */
public class RSAKey {
  
  private final BigInteger value;
  private final BigInteger modulus;
  
  protected RSAKey(BigInteger key, BigInteger mod) {
    value = key;
    modulus = mod;
  }
  
  public BigInteger getValue() {
    return value;
  }
  
  public BigInteger getModulus() {
    return modulus;
  }
  
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append('{');
    builder.append(value);
    builder.append(", ");
    builder.append(modulus);
    builder.append('}');
    return builder.toString();    
  }
}