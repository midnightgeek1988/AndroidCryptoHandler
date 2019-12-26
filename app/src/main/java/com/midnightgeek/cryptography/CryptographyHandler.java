package com.midnightgeek.cryptography;

import android.content.Context;

import com.midnightgeek.cryptography.encryption.DefaultAES;
import com.midnightgeek.cryptography.enums.CryptographyAlgorithm;
import com.midnightgeek.cryptography.interfaces.INFCallBack;
import com.midnightgeek.cryptography.interfaces.INFEncryption;
import com.midnightgeek.cryptography.interfaces.INFStorage;
import com.midnightgeek.cryptography.storage.DefaultFileStorage;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * <p>Encryption handler</p>
 *
 * @author Alireza Karimi
 * @version 1.0.0
 */

public class CryptographyHandler {
    private static CryptographyHandler ourInstance;
    private ThreadPoolExecutor handler=(ThreadPoolExecutor) Executors.newFixedThreadPool(1);
    //private DispatchQueue handler = new DispatchQueue("Encryption");
    private INFEncryption encryption;
    private INFStorage storage;
    private Context context;

    /**
     *
     */
    private CryptographyHandler() {
    }

    /**
     * @return
     */
    public static CryptographyHandler getInstance() {
        if (ourInstance == null)
            ourInstance = new CryptographyHandler();
        return ourInstance;
    }

    /**
     * @param context
     */
    public void init(Context context) {
        this.context = context;
        this.encryption = new DefaultAES();
        this.storage = new DefaultFileStorage(this.context);

    }

    /**
     * @param context
     */
    public void init(Context context, INFStorage storage) {
        this.context = context;
        this.encryption = new DefaultAES();
        this.storage = storage;

    }

    /**
     * @param context
     */
    public void init(Context context, INFEncryption encryption) {
        this.context = context;
        this.encryption = encryption;
        this.storage = new DefaultFileStorage(this.context);

    }

    /**
     * @param context
     */
    public void init(Context context, INFEncryption encryption, INFStorage storage) {
        this.context = context;
        this.encryption = encryption;
        this.storage = storage;

    }

    /**
     * @return
     */
    public ThreadPoolExecutor getHandler() {
        return handler;
    }

    /**
     * @return
     */
    public INFEncryption getEncryption() {
        return encryption;
    }

    /**
     * @param encryption
     */
    public void setEncryption(INFEncryption encryption) {
        this.encryption = encryption;
    }

    /**
     * @param id
     * @param key
     */
    public void initEncryption(int id, String key) {
        this.storage.storeKey(id, key);
    }

    /**
     * @param id
     * @return
     */
    public boolean hasEncryption(int id) {
        return this.storage.isKeyExist(id);
    }

    /**
     * @param id
     * @return
     */
    public String getKey(int id) {
        return this.storage.loadKey(id);
    }

    /**
     * @param id
     */
    public void removeEncryption(int id) {
        this.storage.removeKey(id);
    }

    /**
     * @param id
     * @param key
     */
    public void reinitEncryption(int id, String key) {
        this.storage.removeKey(id);
        this.storage.storeKey(id, key);
    }

    /**
     *
     */
    public void cleanUp() {
        this.storage.cleanup();
    }

    /**
     * @param id
     * @param data
     * @param cryptographyAlgorithm
     * @return
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public String encrypt(int id, String data, CryptographyAlgorithm cryptographyAlgorithm) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        String key = storage.loadKey(id);
        if (key == null || key.isEmpty())
            throw new InvalidKeyException("Key Not Found");
        return this.encryption.encrypt(key, data, cryptographyAlgorithm);
    }

    /**
     * @param id
     * @param data
     * @param cryptographyAlgorithm
     * @return
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public String decrypt(int id, String data, CryptographyAlgorithm cryptographyAlgorithm) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        String key = storage.loadKey(id);
        if (key == null || key.isEmpty())
            throw new InvalidKeyException("Key Not Found");
        return this.encryption.decrypt(key, data, cryptographyAlgorithm);
    }

    /**
     * @param id
     * @param data
     * @param cryptographyAlgorithm
     * @param INFCallBack
     */
    public void encrypt(final int id, final String data, final CryptographyAlgorithm cryptographyAlgorithm, final INFCallBack INFCallBack) {
        getHandler().execute(new Runnable() {
            @Override
            public void run() {
                String key = storage.loadKey(id);
                if (key == null || key.isEmpty()) {
                    INFCallBack.failed(-1, "Key Not Found");
                    return;
                }
                String result = null;
                String error = null;
                try {
                    result = encryption.encrypt(key, data, cryptographyAlgorithm);
                } catch (NoSuchPaddingException e) {
                    error = e.toString();
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    error = e.toString();
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    error = e.toString();
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    error = e.toString();
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    error = e.toString();
                    e.printStackTrace();
                }
                if (INFCallBack != null) {
                    if (result != null) {
                        INFCallBack.success(1, result);
                    } else {
                        INFCallBack.failed(-2, error);
                    }
                }

            }
        });
    }

    /**
     * @param id
     * @param data
     * @param cryptographyAlgorithm
     * @param INFCallBack
     */
    public void decrypt(final int id, final String data, final CryptographyAlgorithm cryptographyAlgorithm, final INFCallBack INFCallBack) {
        getHandler().execute(new Runnable() {
            @Override
            public void run() {
                String key = storage.loadKey(id);
                if (key == null || key.isEmpty()) {
                    INFCallBack.failed(-1, "Key Not Found");
                    return;
                }
                String result = null;
                String error = null;
                try {
                    result = encryption.decrypt(key, data, cryptographyAlgorithm);
                } catch (NoSuchPaddingException e) {
                    error = e.toString();
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    error = e.toString();
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    error = e.toString();
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    error = e.toString();
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    error = e.toString();
                    e.printStackTrace();
                }
                if (INFCallBack != null) {
                    if (result != null) {
                        INFCallBack.success(1, result);
                    } else {
                        INFCallBack.failed(-2, error);
                    }
                }
            }
        });
    }

    /**
     * @param key
     * @param data
     * @param cryptographyAlgorithm
     * @return
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public String encrypt(String key, String data, CryptographyAlgorithm cryptographyAlgorithm) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return this.encryption.encrypt(key, data, cryptographyAlgorithm);
    }

    /**
     * @param key
     * @param data
     * @param cryptographyAlgorithm
     * @return
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public String decrypt(String key, String data, CryptographyAlgorithm cryptographyAlgorithm) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return this.encryption.decrypt(key, data, cryptographyAlgorithm);
    }

    /**
     * @param key
     * @param data
     * @param cryptographyAlgorithm
     * @param INFCallBack
     */
    public void encrypt(final String key, final String data, final CryptographyAlgorithm cryptographyAlgorithm, final INFCallBack INFCallBack) {
        getHandler().execute(new Runnable() {
            @Override
            public void run() {
                String result = null;
                String error = null;
                try {
                    result = encryption.encrypt(key, data, cryptographyAlgorithm);
                } catch (NoSuchPaddingException e) {
                    error = e.toString();
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    error = e.toString();
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    error = e.toString();
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    error = e.toString();
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    error = e.toString();
                    e.printStackTrace();
                }
                if (INFCallBack != null) {
                    if (result != null) {
                        INFCallBack.success(-1, result);
                    } else {
                        INFCallBack.failed(0, error);
                    }
                }
            }
        });
    }

    /**
     * @param key
     * @param data
     * @param cryptographyAlgorithm
     * @param INFCallBack
     */
    public void decrypt(final String key, final String data, final CryptographyAlgorithm cryptographyAlgorithm, final INFCallBack INFCallBack) {
        getHandler().execute(new Runnable() {
            @Override
            public void run() {
                String result = null;
                String error = null;
                try {
                    result = encryption.decrypt(key, data, cryptographyAlgorithm);
                } catch (NoSuchPaddingException e) {
                    error = e.toString();
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    error = e.toString();
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    error = e.toString();
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    error = e.toString();
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    error = e.toString();
                    e.printStackTrace();
                }
                if (INFCallBack != null) {
                    if (result != null) {
                        INFCallBack.success(-1, result);
                    } else {
                        INFCallBack.failed(0, error);
                    }
                }
            }
        });
    }
}
