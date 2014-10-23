import java.math.BigInteger;

/**
 * Main class used to encrypt and decrypt using RSA and a RSAKeyPair. * 
 * @author Garrett Smith, 3018390
 *
 */
public abstract class RSA {
  
  /**
   * Encrypts an array of bytes using the given RSAKey.
   * @param in
   * @param key
   * @return
   */
  public static byte[] encrypt(byte[] in, RSAKey key) {
    
    if (in.length % 8 != 0) {
      System.err.println("Array must be a multiple of 8");
      return null;
    }
    
    int modBytes = key.getModulus().bitLength() / 8;
    
    byte[] out = new byte[in.length / 8 * modBytes];
    
    for (int i = 0; i < in.length / 8; i++) {
      byte[] block = new byte[8];
      System.arraycopy(in, i * 8, block, 0, 8);
      encryptBlock(block, out, i * modBytes, key);    
    }
    
    return out;

    
//    BigInteger val = new BigInteger(in);
//    val = val.modPow(key.getValue(), key.getModulus());
//    return val.toByteArray();
  }
  
  /**
   * Encrypt a block.
   * @param in
   * @param out
   * @param outPos
   * @param key
   */
  private static void encryptBlock(byte[] in, byte[] out, int outPos, RSAKey key) {
    int modBytes = key.getModulus().bitLength() / 8;
    
    BigInteger val = new BigInteger(1, in);
    int compare = key.getModulus().compareTo(val.abs());
    
    if (compare != 1) {
      System.err.println("The end is nigh");
    }
    
    val = val.modPow(key.getValue(), key.getModulus());
    
    byte[] result = val.toByteArray();
    arraycopy(result, 0, out, outPos + (modBytes - result.length), result.length);
  }
  
  /**
   * Decrypts an array of bytes using a RSAKey.
   * @param in
   * @param key
   * @return
   */
  public static byte[] decrypt(byte[] in, RSAKey key) {

    int modBytes = key.getModulus().bitLength() / 8;
    
    if (in.length % modBytes != 0) {
      System.err.println("Array must be a multiple of the modulus byte length");
      return null;
    }
    
    byte[] out = new byte[in.length / modBytes * 8];
    
    for (int i = 0; i < in.length / modBytes; i++) {
      byte[] block = new byte[modBytes];
      System.arraycopy(in, i * modBytes, block, 0, modBytes);
      decryptBlock(block, out, i * 8, key);    
    }
    
    return out;
  }  
  
  /**
   * Decrypts a block.
   * @param in
   * @param out
   * @param outPos
   * @param key
   */
  private static void decryptBlock(byte[] in, byte[] out, int outPos, RSAKey key) {
    
    BigInteger val = new BigInteger(1, in);
    int compare = key.getModulus().compareTo(val.abs());
    
    if (compare != 1) {
      System.err.println("The end is nigh");
    }
    
    val = val.modPow(key.getValue(), key.getModulus());
    
    byte[] result = val.toByteArray();
    int startPos = Math.abs(Math.min(0,(8 - result.length)));
    int length = result.length - startPos;
    arraycopy(
        result, 
        startPos, 
        out, 
        outPos + (8 - length), 
        length);
  }
  
  /**
   * Returns a new RSAKeyPair.
   * @return
   */
  public static RSAKeyPair generateKeyPair() {    
    return new RSAKeyPair();
  }
  
  /**
   * Helper method to copy parts of one array to another since the built in one was
   * randomly throwing exceptions.
   * @param in
   * @param inStart
   * @param out
   * @param outStart
   * @param length
   */
  private static void arraycopy(
      byte[] in, 
      int inStart, 
      byte[] out, 
      int outStart, 
      int length) {
    outStart = Math.max(0, outStart);
    for (int i = 0; i < length; i++) {
      out[outStart + i] = in[inStart + i];
    }
  }

}
