package com.midnightgeek.cryptography.interfaces;

/**
 * <p>call back</p>
 * @author Alireza Karimi
 * @version 1.0.0
 */

public interface INFCallBack {
    /**
     *
     * @param id
     * @param data
     */
    public void success(int id, String data);

    /**
     *
     * @param id
     * @param error
     */
    public void failed(int id, String error);
}
