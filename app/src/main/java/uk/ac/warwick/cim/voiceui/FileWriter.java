package uk.ac.warwick.cim.voiceui;


import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Class to write data to file asynchronously
 */

public class FileWriter {
    //public class FileConnection {
    private File fileName;

    public FileWriter (File fName) {
        fileName = fName;
    }

    public void writeFile (String fileName, String params) {
        FileOutputStream outputStream;

        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
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

