package com.bankstack.bank_stack.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Fingerprints {
    private Fingerprints(){}
    public static String customerCreate(String firstName,String lastName,String email,String phone,String address){
        String s = (firstName == null ? "" : firstName.trim().toLowerCase()) + "|"+
                (lastName == null ? "" : lastName.trim().toLowerCase()) +"|"+
                (email == null ? "" : email.trim().toLowerCase()) + "|"+
                (phone == null ? "" : phone.trim().toLowerCase())+"|"+
                (address == null ? "" : address.trim().toLowerCase());

        return sha256(s);
    }
    private static String sha256(String s){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] b = md.digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(byte st : b){
                sb.append(String.format("%02x",st));
            }
            return sb.toString();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
