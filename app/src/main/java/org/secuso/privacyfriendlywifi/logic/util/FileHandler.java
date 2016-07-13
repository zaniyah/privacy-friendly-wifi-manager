package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 *
 */
public class FileHandler {
    private static final String TAG = FileHandler.class.getSimpleName();


    /**
     * Loads an object from a specified file path
     *
     * @param context
     * @param fileName        File path
     * @param deleteAfterRead delete object after it has been read
     * @return The loaded object
     * @throws IOException
     */
    public static Object loadObject(Context context, String fileName, boolean deleteAfterRead) throws IOException {
        File file = new File(context.getFilesDir().getAbsolutePath().concat(File.separator).concat(fileName));
        Object ret = null;
        if (file.exists()) {
            FileInputStream fin = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fin);
            try {
                ret = in.readObject();
                if (deleteAfterRead) {
                    file.delete();
                }
            } catch (ClassNotFoundException e) {
                Logger.e(TAG, "Could not load file \"" + fileName + "\"");
                throw new IOException("Could not load file \"" + fileName + "\"");
            } finally {
                in.close();
                fin.close();
            }
        } else {
            Logger.d(TAG, "File \"" + fileName + "\" doesn't exist");
            throw new IOException("File \"" + fileName + "\" doesn't exist");
        }
        return ret;
    }


    /**
     * Store object to a specified file path
     *
     * @param context A context to use.
     * @param fileName Path to store the object to.
     * @param o        Object to store.
     * @return Storage successful
     * @throws IOException
     */
    public static boolean storeObject(Context context, String fileName, Object o) throws IOException {
        File file = new File(context.getFilesDir().getAbsolutePath().concat(File.separator).concat(fileName));

        if (!file.exists()) {
            if (!file.createNewFile()) {
                Logger.logADB("e", TAG, "File " + fileName + " could not be created.");
            }
        }

        FileOutputStream fos = new FileOutputStream(file, true);

        storeObject(fos, o);

        fos.close();

        Logger.logADB("e", TAG, "File " + fileName + (file.exists() ? " " : " not ") + "saved.");

        return file.exists();
    }

    public static boolean storeObject(OutputStream fos, Object o) throws IOException {
        if (o instanceof String) {
            OutputStreamWriter out = new OutputStreamWriter(fos);
            out.write((String) o);
            out.flush();
            out.close();
        } else {
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(o);
            out.flush();
            out.close();
        }

        return true;
    }
}
