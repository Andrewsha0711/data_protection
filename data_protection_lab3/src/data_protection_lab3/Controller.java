package data_protection_lab3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {
    public String getFileFromFolder(String folderPath) {
        File dir = new File(folderPath);
        String filePath = (dir.list())[0];
        return folderPath + "/" + filePath;
    }

    public byte[] getBytes(String path) {
        try (FileInputStream fin = new FileInputStream(path)) {
            byte[] bytes = fin.readAllBytes();
            fin.close();
            return bytes;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public byte[] getMessages(byte[] bytes) {
        ArrayList<Byte> bytesList = new ArrayList<Byte>();
        for (int i = 54; i < bytes.length - 4; i += 4) {
            String bStr = Integer.toBinaryString(bytes[i]);
            String gStr = Integer.toBinaryString(bytes[i + 1]);
            String rStr = Integer.toBinaryString(bytes[i + 2]);
            String alphaStr = Integer.toBinaryString(bytes[i + 3]);
            String message = "";
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

            if (message.equals("11111111")) {
                break;
            }
            int byteCode = Integer.parseInt(message, 2);
            bytesList.add((byte) byteCode);
        }
        byte[] result = new byte[bytesList.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = bytesList.get(i);
        }
        return result;
    }

    public void writeMessages(String folderPath, String textFilePath) {
        String file = this.getFileFromFolder(folderPath);
        byte[] bytes = new byte[0];
        byte[] messageBytes = new byte[0];
        try (FileInputStream fin = new FileInputStream(file)) {
            bytes = fin.readAllBytes();
            fin.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        try (FileInputStream fin = new FileInputStream(textFilePath)) {
            messageBytes = fin.readAllBytes();
            fin.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        try {
            ArrayList<String> messageParts = new ArrayList<String>();
            for (int i = 0; i < messageBytes.length; i++) {
                String str = Integer.toBinaryString(messageBytes[i]);
                while (str.length() < 8) {
                    str = "0" + str;
                }
                messageParts.add(str.substring(0, 2));
                messageParts.add(str.substring(2, 4));
                messageParts.add(str.substring(4, 6));
                messageParts.add(str.substring(6, 8));
            }
            messageParts.add("11");
            messageParts.add("11");
            messageParts.add("11");
            messageParts.add("11");

            for (int i = 0; i < messageParts.size() - 4; i += 4) {
                String bStr = Integer.toBinaryString(bytes[i + 54]);
                while (bStr.length() < 8) {
                    bStr = "0" + bStr;
                }
                String gStr = Integer.toBinaryString(bytes[i + 54 + 1]);
                while (gStr.length() < 8) {
                    gStr = "0" + gStr;
                }
                String rStr = Integer.toBinaryString(bytes[i + 54 + 2]);
                while (rStr.length() < 8) {
                    rStr = "0" + rStr;
                }
                String alphaStr = Integer.toBinaryString(bytes[i + 54 + 3]);
                while (alphaStr.length() < 8) {
                    alphaStr = "0" + alphaStr;
                }
                bStr = bStr.substring(bStr.length() - 2, bStr.length())
                        + messageParts.get(i);
                gStr = gStr.substring(gStr.length() - 2, gStr.length())
                        + messageParts.get(i + 1);
                rStr = rStr.substring(rStr.length() - 2, rStr.length())
                        + messageParts.get(i + 2);
                alphaStr = alphaStr.substring(alphaStr.length() - 2, alphaStr.length())
                        + messageParts.get(i + 3);
                bytes[i + 54] = (byte) Integer.parseInt(bStr, 2);
                bytes[i + 54 + 1] = (byte) Integer.parseInt(gStr, 2);
                bytes[i + 54 + 2] = (byte) Integer.parseInt(rStr, 2);
                bytes[i + 54 + 3] = (byte) Integer.parseInt(alphaStr, 2);
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes, 0, bytes.length);
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }
}
