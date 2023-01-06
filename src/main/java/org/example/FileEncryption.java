package org.example;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import javax.crypto.*;

public class FileEncryption {
    public static void main(String[] args) {
        File inputFile = new File("src/main/resources/inputFile.txt");
        File encryptedFile = new File("src/main/resources/outputFile.txt");
        File decryptedFile = new File("src/main/resources/decrypted.txt");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter 1 for encrypt or 2 for decrypt");
        int option = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter encryption password:");
        String password = scanner.nextLine();

        if(option == 1) {
            // Encrypt the file
            encrypt(password, inputFile, encryptedFile);
        }else {
            // Decrypt the file
            decrypt(password, encryptedFile, decryptedFile);
        }
    }
    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void encrypt(String password, File inputFile, File outputFile) {
        try {
            setKey(password);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            inputStream(inputFile, outputFile, cipher);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException |
                 IllegalBlockSizeException | IOException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public static void decrypt(String password, File inputFile, File outputFile) {
        try {
            setKey(password);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            inputStream(inputFile, outputFile, cipher);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void inputStream(File inputFile, File outputFile, Cipher cipher) throws IOException, IllegalBlockSizeException, BadPaddingException {
        FileInputStream inputStream = new FileInputStream(inputFile);
        byte[] inputBytes = new byte[(int) inputFile.length()];
        inputStream.read(inputBytes);

        byte[] outputBytes = cipher.doFinal(inputBytes);

        FileOutputStream outputStream = new FileOutputStream(outputFile);
        outputStream.write(outputBytes);

        inputStream.close();
        outputStream.close();
    }
}




