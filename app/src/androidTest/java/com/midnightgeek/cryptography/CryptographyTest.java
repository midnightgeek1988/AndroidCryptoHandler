package com.midnightgeek.cryptography;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.midnightgeek.cryptography.enums.CryptographyAlgorithm;
import com.midnightgeek.cryptography.interfaces.INFCallBack;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CryptographyTest {
    String key = "zCx4RPVn7pCFdWVWG7969aGbvxVZ9gTS";
    String orginalData = "The divine gift does not come from a higher power, but from out mind";
    String TAG = "MIDNIGHT_AES";

    @Test
    public void Cryptography() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        CryptographyHandler.getInstance().init(appContext);
        CryptographyHandler.getInstance().cleanUp();
        //******************************************************************************************
        String encData = null;
        try {
            encData = CryptographyHandler.getInstance().encrypt(key, orginalData, CryptographyAlgorithm.AES);
        } catch (IllegalBlockSizeException e) {
            encData = e.toString();
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            encData = e.toString();
            e.printStackTrace();
        } catch (BadPaddingException e) {
            encData = e.toString();
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            encData = e.toString();
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            encData = e.toString();
            e.printStackTrace();
        }
        Log.i(TAG, " Sync : " + encData);
        String decData = null;
        try {
            decData = CryptographyHandler.getInstance().decrypt(key, encData, CryptographyAlgorithm.AES);
        } catch (IllegalBlockSizeException e) {
            decData = e.toString();
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            decData = e.toString();
            e.printStackTrace();
        } catch (BadPaddingException e) {
            decData = e.toString();
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            decData = e.toString();
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            decData = e.toString();
            e.printStackTrace();
        }
        Log.i(TAG, "Sync : " + decData);
        assertEquals(orginalData, decData);

        //******************************************************************************************
        CryptographyHandler.getInstance().initEncryption(1, key);

        try {
            encData = CryptographyHandler.getInstance().encrypt(1, orginalData, CryptographyAlgorithm.AES);
        } catch (IllegalBlockSizeException e) {
            encData = e.toString();
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            encData = e.toString();
            e.printStackTrace();
        } catch (BadPaddingException e) {
            encData = e.toString();
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            encData = e.toString();
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            encData = e.toString();
            e.printStackTrace();
        }
        Log.i(TAG, "Sync By id 1: " + encData);

        try {
            decData = CryptographyHandler.getInstance().decrypt(1, encData, CryptographyAlgorithm.AES);
        } catch (IllegalBlockSizeException e) {
            decData = e.toString();
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            decData = e.toString();
            e.printStackTrace();
        } catch (BadPaddingException e) {
            decData = e.toString();
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            decData = e.toString();
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            decData = e.toString();
            e.printStackTrace();
        }
        Log.i(TAG, "Sync By id 1: " + decData);
        assertEquals(orginalData, decData);

        //******************************************************************************************
        final String ENC_DATA = encData;
        final CountDownLatch latch1 = new CountDownLatch(2);
        CryptographyHandler.getInstance().encrypt(key, orginalData, CryptographyAlgorithm.AES, new INFCallBack() {
            @Override
            public void success(int id, String data) {
                latch1.countDown();
                Log.i(TAG, "ASync : " + data);
                assertEquals(data, ENC_DATA);
            }

            @Override
            public void failed(int id, String error) {
                latch1.countDown();
                Log.i(TAG, "ASync : " + error);
                assertTrue(error, id > 0);

            }
        });

        CryptographyHandler.getInstance().decrypt(key, encData, CryptographyAlgorithm.AES, new INFCallBack() {
            @Override
            public void success(int id, String data) {
                latch1.countDown();
                Log.i(TAG, "ASync : " + data);
                assertEquals(orginalData, data);

            }

            @Override
            public void failed(int id, String error) {
                latch1.countDown();
                Log.i(TAG, "ASync : " + error);
                assertTrue(error, id > 0);
            }
        });
        latch1.await(2000, TimeUnit.MILLISECONDS);
        //******************************************************************************************
        final CountDownLatch latch2 = new CountDownLatch(2);
        CryptographyHandler.getInstance().encrypt(1, orginalData, CryptographyAlgorithm.AES, new INFCallBack() {
            @Override
            public void success(int id, String data) {
                latch2.countDown();
                Log.i(TAG, "ASync : " + data);
                assertEquals(data, ENC_DATA);

            }

            @Override
            public void failed(int id, String error) {
                latch2.countDown();
                Log.i(TAG, "ASync : " + error);
                assertTrue(error, id > 0);
            }
        });
        CryptographyHandler.getInstance().decrypt(1, encData, CryptographyAlgorithm.AES, new INFCallBack() {
            @Override
            public void success(int id, String data) {
                latch2.countDown();
                Log.i(TAG, "ASync : " + data);
                assertEquals(orginalData, data);
            }

            @Override
            public void failed(int id, String error) {
                latch2.countDown();
                Log.i(TAG, "ASync : " + error);
                assertTrue(error, id > 0);
            }
        });
        latch2.await(2000, TimeUnit.MILLISECONDS);
        CryptographyHandler.getInstance().cleanUp();
    }
}
