package com.piles.util;

import java.io.File;

/**
 * @author lgc48027
 * @version Id: FileUtil, v 0.1 2018/1/3 10:29 lgc48027 Exp $
 */
public class FileUtil {
    private static final String path="/piletransfer/soft";
    public static File getFile(){
        File fileParent=new File( path );
        if (fileParent.exists()){
            File[] files=fileParent.listFiles();
            for (File file:files){
                if (file.getName().endsWith( "bin" )){
                    return file;
                }
            }
        }
        return null;
    }
}
