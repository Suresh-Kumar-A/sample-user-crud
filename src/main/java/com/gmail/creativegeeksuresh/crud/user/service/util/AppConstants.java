package com.gmail.creativegeeksuresh.crud.user.service.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AppConstants {
        public static final String ADMIN_ROLE_STRING = "ADMIN";
        public static final String USER_ROLE_STRING = "USER";
        public static final String ROLE_STRING = "ROLE_";
        public static final Set<String> ROLE_SET = Collections
                        .unmodifiableSet(new HashSet<>(Arrays.asList(ADMIN_ROLE_STRING, USER_ROLE_STRING)));

// public static final String PRIVATE_KEY_PATH="security/keys/jwt_privatekey.der";
// public static final String PUBLIC_KEY_PATH="security/keys/jwt_publickey509.der";

public static final String PRIVATE_KEY_PATH="jwt_privatekey.der";
public static final String PUBLIC_KEY_PATH="jwt_publickey509.der";

}
