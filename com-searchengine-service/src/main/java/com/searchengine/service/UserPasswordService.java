package com.searchengine.service;

/**
 * Created by [Ray Lew] on 2014/11/10.
 */
public interface UserPasswordService {

    String passwordEncode(String password, String salt);

    String createSalt();
}
