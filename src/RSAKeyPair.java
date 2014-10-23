import java.math.BigInteger;
import java.util.Random;

/**
 * A public and private key pair for RSA.
 * It includes getters for both keys, a toString to print both keys, and
 * a constructor to make valid key pairs composed of KEY_SIZE primes.
 * @author garrett
 *
 */
public class RSAKeyPair {
  private static final int KEY_SIZE = 64;
  private static final byte[] MIN_N_BYTES = 
    {-128, -128, -128, -128, -128, -128, -128, -128};
  private static final BigInteger MIN_N = new BigInteger(1, MIN_N_BYTES);
  
  private final RSAKey publicKey;
  private final RSAKey privateKey; 
  
  public RSAKey getPublicKey() {
    return publicKey;
  }
  
  public RSAKey getPrivateKey() {
    return privateKey;
  }
  
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Public key:  ");
    builder.append(publicKey.toString());
    builder.append("\nPrivate key: ");
    builder.append(privateKey.toString());
    return builder.toString();
  }
  
  /**
   * Creates a new pair of RSA keys.
   */
  protected RSAKeyPair() {
    BigInteger p;
    BigInteger q;
    BigInteger n;
    
    // until we get a large enough n that we can encrypt
    // 8 byte blocks
    do {    
      // select p and q
      p = generatePrime();

      // find a q, q != p
      do {
        q = generatePrime();
      }
      while (q.equals(p));

      // calculate n = pq
      n = p.multiply(q);
    }
    while(n.compareTo(MIN_N) < 0);
    
    // calculate sigma(n) = (p - 1)(q - 1)
    BigInteger sigmaN = sigma(p, q);
    
    // select e, gcd(sigma(n), e) = 1; 1 < e < sigma(n)
    BigInteger e = selectE(sigmaN);
    
    // calculate d, de mod sigma(n) = 1
    BigInteger d = calculateD(e, sigmaN);
    
    // store the keys
    publicKey = new RSAKey(e, n);
    privateKey = new RSAKey(d, n);
  }  
  
  // generate key
  
  /**
   * Returns a probablistic prime with KEY_SIZE number of bits.
   * @return
   */
  private static BigInteger generatePrime() {
    return BigInteger.probablePrime(KEY_SIZE, new Random());
  }
  
  /**
   * Calculates the sigma of n = pq; sigma(n) = (p - 1)(q - 1)
   * @param p
   * @param q
   * @return
   */
  private static BigInteger sigma(BigInteger p, BigInteger q) {
    return p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
  }

  /**
   * Select e, gcd(sigma(n), e) = 1; 1 < e < sigma(n)
   * @param sigmaN
   * @return
   */
  private static BigInteger selectE(BigInteger sigmaN) {
    Random rand = new Random();
    BigInteger e;
    
    // geuss an e in the range and check if e and sigma(n) are coprime.
    do {
      e = guessE(sigmaN, rand);
    }
    while(!e.gcd(sigmaN).equals(BigInteger.ONE));
    
    return e;
  }
  
  private static final BigInteger TWO = BigInteger.ONE.add(BigInteger.ONE);
  
  /**
   * Returns an e, 1 < e < sigma(n).
   * @param sigmaN
   * @param rand
   * @return
   */
  private static BigInteger guessE(BigInteger sigmaN, Random rand) {
    BigInteger e;
    
    // Guess a number with the right number of bits until we find one less than 
    // sigma(n)
    do {
      e = new BigInteger(sigmaN.bitLength(), rand);
      e = e.add(TWO);
    }
    while (e.compareTo(sigmaN) > 0);
    
    return e;
  }
  
  /**
   * Calculates a d such that de = 1 (mod sigma(n)); ie d = e'
   * @param e
   * @param sigmaN
   * @return
   */
  private static BigInteger calculateD(BigInteger e, BigInteger sigmaN) {
    return e.modInverse(sigmaN);
  }
}