package com.pb.txt;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by pb on 2017/5/3.
 */
public class TxtConfig {

    public static final String TXT_PATH;
    public static final Path PATH;
    private static final String TXT_NAME="merchanrecord.txt";
    static {
        TXT_PATH=initialTxtPath();
        PATH= Paths.get(TXT_PATH);
    }

    private static String initialTxtPath(){
        String path=  TxtWriter.class.getResource("/").getPath();
        return String.format("%s%s",path,TXT_NAME);
    }
}
