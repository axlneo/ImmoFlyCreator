package com.pandasoftware.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ImageUtils {

    public static Image convertToJavaFXImage(byte[] raw, final int width, final int height) {
        if(null != raw){

            WritableImage image = new WritableImage(width, height);
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(raw);
                BufferedImage read = ImageIO.read(bis);
                image = SwingFXUtils.toFXImage(read, null);
            } catch (IOException ex) {
                //Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
            return image;
        } else {
            return null;
        }
    }
}
