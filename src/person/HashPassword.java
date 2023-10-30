package person;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class HashPassword {
    public static byte [] generateSalt(){
        byte [] salt =  new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        return salt;
    }

    public static String getHashPassword(String pwd, byte [] salt){
        try {
            MessageDigest mD = MessageDigest.getInstance("SHA-256");
            mD.update(salt);
            byte [] hashedPwd = mD.digest(pwd.getBytes());
            String hashedPwdBase64 = Base64.getEncoder().encodeToString(hashedPwd);

            return hashedPwdBase64;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("The Algorithm SHA-256 does not exist");
            return null;
        }
    }
}
