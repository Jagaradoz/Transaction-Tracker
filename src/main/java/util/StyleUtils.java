package util;

import java.awt.*;
import java.io.*;

public class StyleUtils {
    public static Font getFont() {
        Font mainFont;

        try {
            InputStream fontStream = StyleUtils.class.getResourceAsStream("/fonts/SourGummy-VariableFont_wdth,wght.ttf");
            if (fontStream == null) {
                throw new IOException("Font resource not found.");
            }

            mainFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.PLAIN, 16f);
        } catch (IOException | FontFormatException e) {
            mainFont = new Font("Arial", Font.PLAIN, 16);
        }

        return mainFont;
    }

}