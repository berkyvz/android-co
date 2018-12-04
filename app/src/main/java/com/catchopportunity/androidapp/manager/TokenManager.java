package com.catchopportunity.androidapp.manager;

import java.util.Base64;

public class TokenManager {

    public TokenManager(){

    }

    public String[] decodeUserToken(String token) {
        String emailpassword = new String(Base64.getDecoder().decode(token.getBytes()));
        String[] up = emailpassword.split(":");
        return up;
    }

    public String encodeUserEmailPassword(String email, String password) { //Custom token generator for Company;
        String str = email + ":" + password;
        String token = new String(Base64.getEncoder().encode(str.getBytes()));
        return token;
    }

}
