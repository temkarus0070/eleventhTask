package org.temkarus0070.application.services;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class PasswordEncoderImpl implements PasswordEncoder {
    @Override
    public String getHashFromPassword(String password) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest();
            for (int i = 0; i < bytes.length; i++) {
                stringBuilder.append(Integer.toString(
                        (bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return stringBuilder.toString();
        } catch ( NoSuchAlgorithmException algNotFoundException){
            return "";
        }
    }
}
