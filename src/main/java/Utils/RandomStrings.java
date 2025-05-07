package Utils ;
import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;
/**
 *
 *
 * @author TheMaliik
 */
public class RandomStrings {
    public static String generatePassword(int length) {
        return RandomStringUtils.random(length, 0, 0, true, true, null, new SecureRandom());
    }

    public static void main(String[] args) {
        int passwordLength = 10; // Longueur du mot de passe à générer
        String generatedPassword = generatePassword(passwordLength);
        System.out.println("Mot de passe généré: " + generatedPassword);
    }
}
