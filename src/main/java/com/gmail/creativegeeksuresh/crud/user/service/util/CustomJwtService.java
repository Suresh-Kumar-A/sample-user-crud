package com.gmail.creativegeeksuresh.crud.user.service.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class CustomJwtService {

    private static final Map<String, Object> JWT_HEADER;
    private RSAPublicKey RSA_PUBLIC_KEY;
    private RSAPrivateKey RSA_PRIVATE_KEY;

    static {
        JWT_HEADER = new HashMap<>();
        JWT_HEADER.put("alg", "RS256");
        JWT_HEADER.put("typ", "JWT");
    }

    @SuppressWarnings("unchecked")
    public String generateSignedJwtToken(Map<String, Object> jwtPayloadData) throws Exception {
        RSA_PRIVATE_KEY = CustomJwtService.getPrivateKey();
        // Pass Null to Public Key if you don't have a public key (or) don't need to get
        // the public key from user
        // Note: Public Key are not needed for signing
        Algorithm algorithm = Algorithm.RSA384(null, RSA_PRIVATE_KEY);
        Builder builder = JWT.create().withHeader(JWT_HEADER).withIssuer(AppConstants.JWT_ISSUER);

        jwtPayloadData.forEach((key, value) -> {

            Class<?> valueClassType = value.getClass();

            if (String.class.equals(valueClassType)) {
                builder.withClaim(key, (String) value);
            } else if (Boolean.class.equals(valueClassType)) {
                builder.withClaim(key, (Boolean) value);
            } else if (Integer.class.equals(valueClassType)) {
                builder.withClaim(key, (Integer) value);
            } else if (Date.class.equals(valueClassType)) {
                builder.withClaim(key, (Date) value);
            } else if (Double.class.equals(valueClassType)) {
                builder.withClaim(key, (Double) value);
            } else if (List.class.equals(valueClassType)) {
                builder.withClaim(key, (List<?>) value);
            } else if (Map.class.equals(valueClassType)) {
                builder.withClaim(key, (Map<String, ?>) value);
            } else if (Long.class.equals(valueClassType)) {
                builder.withClaim(key, (Long) value);
            }
        });
        return builder.sign(algorithm);
    }

    public Map<String, Object> verifyJwtTokenAndGetValue(String jwtToken) throws Exception {
        RSA_PUBLIC_KEY = CustomJwtService.getPublicKey();
        // Pass Null to Private Key if you don't have a private key (or) don't need to
        // get the private key from user
        // Note: Private Key are not needed for verifying
        Algorithm algorithm = Algorithm.RSA384(RSA_PUBLIC_KEY, null);
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(AppConstants.JWT_ISSUER).build();
        DecodedJWT jwt = verifier.verify(jwtToken);
        Map<String, Object> jwtPayloadData = new HashMap<>();
        // Return JWT Payload / Claims
        jwt.getClaims().forEach((key, value) -> {
            jwtPayloadData.put(key, value.asString());

            // Class<?> valueClassType = value.getClass();
            // if (String.class.equals(valueClassType)) {
            // jwtPayloadData.put(key, value.asString());
            // } else if (Boolean.class.equals(valueClassType)) {
            // jwtPayloadData.put(key, value.asBoolean());
            // } else if (Integer.class.equals(valueClassType)) {
            // jwtPayloadData.put(key, value.asInt());
            // } else if (Date.class.equals(valueClassType)) {
            // jwtPayloadData.put(key, value.asDate());
            // } else if (Double.class.equals(valueClassType)) {
            // jwtPayloadData.put(key, value.asDouble());
            // } else if (List.class.equals(valueClassType)) {
            // jwtPayloadData.put(key, value.asList(Object.class));
            // } else if (Map.class.equals(valueClassType)) {
            // jwtPayloadData.put(key, value.asMap());
            // } else if (Long.class.equals(valueClassType)) {
            // jwtPayloadData.put(key, value.asLong());
            // }
        });

        return jwtPayloadData;
    }

    private static RSAPrivateKey getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        File file = new ClassPathResource(AppConstants.PRIVATE_KEY_PATH).getFile();
        FileInputStream fis = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) file.length()];
        dis.readFully(keyBytes);
        dis.close();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(spec);
        return privateKey;
    }

    private static RSAPublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        File file = new ClassPathResource(AppConstants.PUBLIC_KEY_PATH).getFile();
        FileInputStream fis = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) file.length()];
        dis.readFully(keyBytes);
        dis.close();
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(spec);
        return publicKey;
    }

}
