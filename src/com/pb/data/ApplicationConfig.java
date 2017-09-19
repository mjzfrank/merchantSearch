package com.pb.data;

/**
 * Created by pb on 2017/5/4.
 */
public class ApplicationConfig {
    public static final String HOST ="http://www.dianping.com";
    public static String constructFullPath(String href){
        if(href.startsWith("http://")||href.startsWith("https://")){
            return href;
        }
        return String.format("%s%s", HOST,href);
    }
}
