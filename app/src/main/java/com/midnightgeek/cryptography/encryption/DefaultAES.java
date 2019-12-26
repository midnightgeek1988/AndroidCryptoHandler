package com.midnightgeek.cryptography.encryption;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.midnightgeek.cryptography.enums.CryptographyAlgorithm;
import com.midnightgeek.cryptography.interfaces.INFEncryption;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * <p>AES Encryption</p>
 * a default AES encryption
 *
 * @author Alireza Karimi
 * @version 1.0.0
 */

public class DefaultAES implements INFEncryption {
    private final String TAG = "AES";

    @Override
    public String encrypt(String key, String data, CryptographyAlgorithm cryptographyAlgorithm) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher c = Cipher.getInstance(cryptographyAlgorithm.toString());
        c.init(Cipher.ENCRYPT_MODE, generateKey(key, cryptographyAlgorithm));
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.encodeToString(encVal, Base64.NO_WRAP);
    }

    @Override
    public String decrypt(String key, String data, CryptographyAlgorithm cryptographyAlgorithm) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher c = Cipher.getInstance(cryptographyAlgorithm.toString());
        c.init(Cipher.DECRYPT_MODE, generateKey(key, cryptographyAlgorithm));
        byte[] decordedValue = Base64.decode(data, Base64.NO_WRAP);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }

    @NonNull
    private Key generateKey(String key, CryptographyAlgorithm cryptographyAlgorithm) {
        return new SecretKeySpec(key.getBytes(), cryptographyAlgorithm.toString());
    }
}