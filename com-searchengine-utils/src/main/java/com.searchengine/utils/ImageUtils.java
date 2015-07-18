package com.searchengine.utils;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by ZhuangXiaobin on 2014/10/11.
 */
public class ImageUtils {

    public static void main(String[] args) {
        try {
            int newWidth = 260;
            int newHeight = 260;
            File sourceFile = new File("C:\\Users\\ZhuangXiaobin\\Desktop\\缩略图20141020");
            File[] fs = sourceFile.listFiles();
            for(int i=0; i<fs.length; i++) {
                try {
                    File file = fs[i];
                    BufferedImage sourceImage = ImageIO.read(file);
                    BufferedImage resized = Scalr.resize(sourceImage, Scalr.Method.QUALITY, newWidth, newHeight);
                    File fileOut = new File("C:\\Users\\ZhuangXiaobin\\Desktop\\缩略图20141020\\260\\" + file.getName());
                    ImageIO.write(resized, "jpg", fileOut);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
