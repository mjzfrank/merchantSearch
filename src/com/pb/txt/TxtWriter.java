package com.pb.txt;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by pb on 2017/5/3.
 */
public class TxtWriter {
    public void write(List<String> txts){
        try {
            Files.write(TxtConfig.PATH,txts, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
       // new TxtWriter().write("");
    }
}
