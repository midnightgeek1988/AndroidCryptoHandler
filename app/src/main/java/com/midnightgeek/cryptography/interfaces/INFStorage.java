package com.midnightgeek.cryptography.interfaces;

/**
 * <p>Storage interface</p>
 * you can use your own storage by impliment this intereface
 * @author Alireza Karimi
 * @version 1.0.0
 */

public interface INFStorage {
    /**
     * <p>Store Encryption key</p>
     * @param id
     * @param key
     */
    void storeKey(int id, String key);

    /**
     * <p>Load Encryption key</p>
     * @param id
     * @return
     */
    String loadKey(int id);

    /**
     * <p>remove Encryption key</p>
     * @param id
     */
    void removeKey(int id);

    /**
     * <p>Cleanup all Encryption key</p>
     */
    void cleanup();

    /**
     * <p>check Encryption key exist</p>
     * @param id
     * @return
     */
    boolean isKeyExist(int id);
}
