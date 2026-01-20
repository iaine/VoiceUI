package uk.ac.warwick.cim.voiceui;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Class to write data to file asynchronously
 */

public class FileWriter {
    //public class FileConnection {
    public File fileName;

    public FileWriter (File fName) {
        fileName = fName;
    }

    public void writeFile (String fileName, String params) {
        FileOutputStream outputStream;

        try {
            File file = new File(fileName);
            if (!file.exists()) {
                boolean newfile = file.createNewFile();
                if (!newfile) {
                    throw new Exception("Could not create " + file);
                }
            }
            outputStream = new FileOutputStream(file, true);
            outputStream.write(params.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            Log.i("FILE", e.toString());
        }
    }
}

