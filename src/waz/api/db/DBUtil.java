package waz.api.db;

import com.google.gson.Gson;
import waz.api.entity.EventData;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DBUtil {

    /**
     * Serialize the object as xml data in file.
     * @param filename
     * @param data
     */
    public static void serialize(String filename, Object data) throws IOException {
        File file = new File(filename);
        if(!file.exists()) {
            file.createNewFile();
        }
        XMLEncoder e = new XMLEncoder(
                new BufferedOutputStream(
                        new FileOutputStream(filename)));
        if(data != null) {
            e.writeObject(data);
        }
        e.close();
    }

    /**
     * Serialize the object as json data in file.
     * @param filename
     * @param data
     * @throws IOException
     */
    public static void serialize(String filename, byte[] data) throws IOException {
        File file = new File(filename);
        if(!file.exists()) {
            file.createNewFile();
        }
        Path path = Paths.get(file.getPath());
        Files.write(path, data);
    }

    /**
     * Retrieves the data from file as list of objects
     * and store in Thread Queue.
     * @param filename
     * @return
     */
    public static Set<Object> deserialize(String filename) throws FileNotFoundException, InterruptedException {
        XMLDecoder d = new XMLDecoder(
                new BufferedInputStream(
                        new FileInputStream(filename)));
        Object result = d.readObject();
        d.close();
        return (Set<Object>) result;
    }

    /**
     *
     * @param o
     * @return
     */
    public static String objectToJson(Object o) {
        Gson gson = new Gson();
        return gson.toJson(o);
    }

    public static void newJsonFile(String filename, Object o) throws IOException {
        serialize(filename, objectToJson(o).getBytes());
    }
}
