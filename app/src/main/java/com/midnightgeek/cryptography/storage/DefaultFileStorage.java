package com.midnightgeek.cryptography.storage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.midnightgeek.cryptography.interfaces.INFStorage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * <p>Default Storage</p>
 *
 * @author Alireza Karimi
 * @version 1.0.0
 */

public class DefaultFileStorage implements INFStorage {
    public static final String ENC_DIRECTORY = "enckeys";
    public static final String ext = ".enc";
    private final Context context;

    public DefaultFileStorage(Context context) {
        this.context = context;
    }

    @Override
    public void storeKey(int id, String key) {
        if (!isKeyExist(id)) {
            saveKeyFile(key, getFileAddress(ENC_DIRECTORY, id));
        }
    }

    @Override
    public String loadKey(int id) {
        if (isKeyExist(id)) {
            return readKeyFile(getFileAddress(ENC_DIRECTORY, id));
        } else {
            return null;
        }
    }

    @Override
    public void removeKey(int id) {
        if (isKeyExist(id)) {
            new File(getFileAddress(ENC_DIRECTORY, id)).delete();
        }
    }

    @Override
    public void cleanup() {
        File dir = getRecordsDirectory(ENC_DIRECTORY);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
    }

    @Override
    public boolean isKeyExist(int id) {
        File file = new File(getRecordsDirectory(ENC_DIRECTORY) + "/" + String.valueOf(id) + ext);
        return file.exists();
    }

    private String readKeyFile(String file) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            br.close();
            result = stringBuilder.toString();
        } catch (IOException e) {
        }
        return result;
    }

    private void saveKeyFile(String data, String file) {
        try {
            FileWriter outputStreamWriter = new FileWriter(new File(file));
            outputStreamWriter.write(data);
            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (Exception e) {
            Log.e("AES", "File write failed: " + e.toString());
        }
    }

    @NonNull
    private String getFileAddress(String directoryName, int id) {
        return context.getFilesDir() + "/" + directoryName + "/" + String.valueOf(id) + ext;
    }

    private File getRecordsDirectory(String directoryName) {
        File directory = new File(context.getFilesDir(), directoryName);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.w("AES", "encKey directory creation failed!");
            }
        }
        return directory;
    }

}
