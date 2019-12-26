package com.midnightgeek.cryptography.interfaces;


import com.midnightgeek.cryptography.enums.CryptographyAlgorithm;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * <p>Encryption Impliment Interface</p>
 * You can use your own encryption impliment by use of this interface
 *
 * @author Alireza Karimi
 * @version 1.0.0
 */

public interface INFEncryption {
    /**
     * <p>Encrypt</p>
     * @param key
     * @param data
     * @param cryptographyAlgorithm
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    String encrypt(String key, String data, CryptographyAlgorithm cryptographyAlgorithm) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException;

    /**
     * <p>Decryption</p>
     * @param key
     * @param data
     * @param cryptographyAlgorithm
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    String decrypt(String key, String data, CryptographyAlgorithm cryptographyAlgorithm) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException;
}
