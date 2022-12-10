package data_protection_lab4;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Program {

    public static void main(String[] args) {
        String path = "resources/pi.txt";
        String sourceKeyPath = "resources/key.txt";
        String sourceDataPath = "resources/data.txt";
        String encryptedDataPath = "resources/encrypted_data.txt";
        String decryptedDataPath = "resources/decrypted_data.txt";

        byte[][] keys = new byte[18][4]; // 18*4(32 бита)
        byte[][] s1 = new byte[256][4];
        byte[][] s2 = new byte[256][4];
        byte[][] s3 = new byte[256][4];
        byte[][] s4 = new byte[256][4];
        try (FileInputStream fin = new FileInputStream(path)) {
            byte[] bytes = fin.readAllBytes();
            fin.close();
            for (int i = 0; i < 18; i++) {
                for (int j = 0; j < 4; j++) {
                    keys[i][j] = bytes[i * 4 + j];
                }
            }
            for (int i = 0; i < 256; i++) {
                for (int j = 0; j < 4; j++) {
                    s1[i][j] = bytes[i * 4 + j + 72];
                    s2[i][j] = bytes[i * 4 + j + 72 + 1024];
                    s2[i][j] = bytes[i * 4 + j + 72 + 1024 * 2];
                    s2[i][j] = bytes[i * 4 + j + 72 + 1024 * 3];
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        byte[] key;
        try (FileInputStream fin = new FileInputStream(sourceKeyPath)) {
            key = fin.readAllBytes();
            fin.close();
        } catch (IOException ex) {
            key = null;
            System.out.println(ex.getMessage());
        }
        // Расширение ключа
        Controller.calculateKeysAndMatrices(keys, key, s1, s2, s3, s4);

        // Шифровка
        byte[] byteData;
        try (FileInputStream fin = new FileInputStream(sourceDataPath)) {
            byteData = fin.readAllBytes();
            fin.close();
        } catch (IOException ex) {
            byteData = null;
            System.out.println(ex.getMessage());
        }
        if (byteData != null) {
            byte[] result = Controller.Encrypt(Controller.prepareData(byteData, 8), keys,
                    s1, s2, s3, s4);
            try (FileOutputStream fout = new FileOutputStream(encryptedDataPath)) {
                fout.write(result);
                fout.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        // Дешифровка
        byte[] data;
        try (FileInputStream fin = new FileInputStream(encryptedDataPath)) {
            data = fin.readAllBytes();
            fin.close();
        } catch (IOException ex) {
            data = null;
            System.out.println(ex.getMessage());
        }
        if (data != null) {
            try (FileOutputStream fout = new FileOutputStream(decryptedDataPath)) {
                fout.write(Controller.Decrypt(data, keys, s1, s2, s3, s4));
                fout.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}

