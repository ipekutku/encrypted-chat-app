import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Crypto {

    static String generateRandomKey(String mode) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(mode);
        SecureRandom random = new SecureRandom(); // cryptograph. secure random
        keyGen.init(random);
        SecretKey key = keyGen.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        return encodedKey;
    }

    static byte[] generateIV(int size) {
        SecureRandom randomSecureRandom = new SecureRandom();
        byte[] iv = new byte[size];
        randomSecureRandom.nextBytes(iv);
        //IvParameterSpec ivParams = new IvParameterSpec(iv);
        return iv;
    }

    static String encrypt(String str, String method, String mode, SecretKey key, IvParameterSpec iv)throws Exception {

        Cipher cipher;
        String algorithm = method + "/" + mode + "/PKCS5Padding";

        cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        
        byte[] enc = cipher.doFinal(str.getBytes("UTF8"));
        String encodedString = Base64.getEncoder().encodeToString(enc);

        return encodedString;
    }
    static String decrypt(String str, String method, String mode, SecretKey key, IvParameterSpec iv)throws Exception {

        Cipher cipher;
        String algorithm = method + "/" + mode + "/PKCS5Padding";

        cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
            
        String decodedString = new String(cipher.doFinal(Base64.getDecoder().decode(str)));

        return decodedString;
    }
}
