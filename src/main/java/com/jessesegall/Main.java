package com.jessesegall;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.Scanner;

public class Main {
    static {
        Security.addProvider(new BouncyCastleProvider()); // Register BC as a sec provider
    }
    public static void main(String[] args) {

    }
}