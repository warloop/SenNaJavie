package org.example.forum.services;

import org.example.forum.services.interfaces.ISecurityService;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Klasa odpowiedzialna za implementację mechanizmów bezpieczeństwa w apliakcji.
 * @author Artur Leszczak
 * @version 1.0.0
 */

@Service
public class SecurityService implements ISecurityService {

    /**
     * Metoda tworzy funkcję skrótu przy użyciu algorytmu SHA-256 z przekazanego łańcucha znaków w parametrze metody.
     * @param data - Łańcuch znaków przekazany do zahashowania.
     * @return String Zwraca łańcuch zanków reprezentujący funkcję skrótu.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public String hashDataUsingSHA256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(data.getBytes());

            StringBuilder stringBuilder = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                stringBuilder.append(Integer.toString((hashedByte & 0xff) + 0x100, 16).substring(1));
            }

            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Metoda porównuje obie wartości łańcuchów znakowych.
     * @param hash1 - Pierwszy łańcuch znaków.
     * @param hash2 - Drugi łańcuch znaków.
     * @return boolean zwraca True / False w zależności , czy wartości skrótów są takie same.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public boolean compareHashesUsingSHA256(String hash1, String hash2) {
        return hash1.equals(hash2);
    }

    /**
     * Metoda porównuje obie wartości łańcuchów znakowych.
     * @param normal_password - Hasło w postaci nieprzekształconej.
     * @param hashedPass - Hasło w postaci funkcji skrótu.
     * @return boolean zwraca True / False w zależności , czy wartości skrótów są takie same.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public boolean comparePassAndPassFromDb(String normal_password, String hashedPass) {

        return  this.compareHashesUsingSHA256(this.hashDataUsingSHA256(normal_password), hashedPass);
    }
}
