package org.example.forum.services.interfaces;

public interface ISecurityService {

    String hashDataUsingSHA256(String data);

    boolean compareHashesUsingSHA256(String hash1, String hash2);

    boolean comparePassAndPassFromDb(String normal_password, String hashedPass);
}
