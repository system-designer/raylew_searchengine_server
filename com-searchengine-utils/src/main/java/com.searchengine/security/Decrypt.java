package com.searchengine.security;

public interface Decrypt {

    byte[] decryptBytes(byte[] buffer);
    
    byte[] decryptDefaultBytes(byte[] buffer);

}