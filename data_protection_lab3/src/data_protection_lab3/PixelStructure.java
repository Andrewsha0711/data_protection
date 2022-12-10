package data_protection_lab3;

import java.awt.Color;
import java.awt.image.ColorModel;

public class PixelStructure {
    private int rValue;
    private int gValue;
    private int bValue;
    private int alphaChannel;
    private int rgb;

    public PixelStructure(int rgb) {
        this.rgb = rgb;
        Color color = new Color(rgb);
        this.rValue = color.getRed();
        this.gValue = color.getGreen();
        this.bValue = color.getBlue();
        this.alphaChannel = color.getAlpha();
    }

    public PixelStructure(ColorModel model, int pixel) {
        Color color = new Color(rgb);
        this.rValue = model.getRed(pixel);
        this.gValue = model.getGreen(pixel);
        this.bValue = model.getBlue(pixel);
        this.alphaChannel = model.getAlpha(pixel);
    }

    public String getMessage() {
        String bStr = Integer.toBinaryString(this.bValue);
        String gStr = Integer.toBinaryString(this.gValue);
        String rStr = Integer.toBinaryString(this.rValue);
        String alphaStr = Integer.toBinaryString(this.alphaChannel);

        String message = "";
        if (bStr != "0" || gStr != "0" || rStr != "0" || alphaStr != "255") {
            while (bStr.length() < 2) {
                bStr = "0" + bStr;
            }
            while (gStr.length() < 2) {
                gStr = "0" + gStr;
            }
            while (rStr.length() < 2) {
                rStr = "0" + rStr;
            }
            while (alphaStr.length() < 2) {
                alphaStr = "0" + alphaStr;
            }
            message += bStr.substring(bStr.length() - 2, bStr.length());
            message += gStr.substring(gStr.length() - 2, gStr.length());
            message += rStr.substring(rStr.length() - 2, rStr.length());
            message += alphaStr.substring(alphaStr.length() - 2, alphaStr.length());

        }
        return message;
    }

}
