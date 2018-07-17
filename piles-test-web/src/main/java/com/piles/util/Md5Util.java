package com.piles.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;


/**
 * @author lgc48027
 * @version Id: Md5Util, v 0.1 2018/1/3 10:23 lgc48027 Exp $
 */
public class Md5Util {
    public static String getMd5ByFile(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    public static void main(String[] args) throws IOException {

        String path="D:\\workspace\\github\\pilestransfer\\piles-test-web\\src\\main\\webapp\\soft\\AcOneV2.13.bin";

        String v = getMd5ByFile(new File(path));
        System.out.println("MD5:"+v);



    }
}
