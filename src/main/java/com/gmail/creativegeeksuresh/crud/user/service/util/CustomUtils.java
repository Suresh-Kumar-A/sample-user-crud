package com.gmail.creativegeeksuresh.crud.user.service.util;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUtils {
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public final String encodeUsingBcryptPasswordEncoder(String input) {
    return bCryptPasswordEncoder.encode(input);
  }

  public boolean verifyUserPassword(String plainText, String hashString) {
    return bCryptPasswordEncoder.matches(plainText, hashString);
  }

  public final String generateToken() throws Exception {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();
    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    messageDigest.update((dateFormat.format(date).toString() + String.valueOf(Math.random())).getBytes());
    byte[] digiest = messageDigest.digest();
    return DatatypeConverter.printHexBinary(digiest);
  }
}
