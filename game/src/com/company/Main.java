package com.company;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    private static final String HMAC_ALGO = "HmacSHA256";

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length*2);
        for(byte b: bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

        Scanner scanner = new Scanner(System.in);

        if(args.length < 3){
            System.out.println("Wrong count of args!\n" +
                    "Please, enter the correct count of args ( >= 3 )");
            System.exit(0);
        } else if(args.length%2 == 0){
            System.out.println("Wrong count of args!\n" +
                    "Please, enter the correct count of args ( odd count )");
            System.exit(0);
        }

        HashSet<String> hashSet = new HashSet<>();

        for (int i=0; i< args.length; i++) {
            if(hashSet.contains(args[i])){
                System.out.println("Wrong args!\n" +
                        "Please, enter the correct args ( non-repeating )");
                System.exit(0);
            }
            hashSet.add(args[i]);
        }

        int n = args.length/2;
        int compMove = (int)Math.floor(Math.random() * args.length);

        SecureRandom secureRandom = new SecureRandom();
        byte []bytes = new byte[16];
        secureRandom.nextBytes(bytes);

        Mac signer = Mac.getInstance(HMAC_ALGO);
        SecretKeySpec keySpec = new SecretKeySpec(bytes, HMAC_ALGO);
        signer.init(keySpec);

        String compStr = args[compMove];
        byte[] hmac = signer.doFinal(compStr.getBytes("utf-8"));

        System.out.println("HMAC: " + bytesToHex(hmac));

        int userNum = Integer.MAX_VALUE;
        while (userNum >= args.length) {
            System.out.println("Available moves: ");
            for (int i = 0; i < args.length; i++) {
                System.out.println(i + 1 + " - " + args[i]);
            }

            System.out.print("Enter your move: ");
            userNum = scanner.nextInt()-1;
            if(userNum >= args.length) {
                System.out.println("Wrong move!\n" +
                        "Please, enter the correct move: 1 - " + args.length);
            }
        }
        System.out.println("Your move: " + args[userNum]);

        System.out.println("Computer move: " + compStr);

        if(userNum == compMove){
            System.out.println("Draw!");
            System.out.println("HMAC key: " + bytesToHex(bytes));
            System.exit(0);
        }

        for(int i = 1; i < n+1; i++){

            if(userNum+i == compMove) {
                System.out.println("Computer win!");
                System.out.println("Key: " + bytesToHex(bytes));
                System.exit(0);
            }
        }
        System.out.println("You win!");
        System.out.println("HMAC key: " + bytesToHex(bytes));
        System.exit(0);
    }
}