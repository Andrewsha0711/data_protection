package data_protection_lab2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class Controller {
    public String getFileFromFolder(String folderPath) {
        File dir = new File(folderPath);
        String filePath = (dir.list())[0];
        return folderPath + "/" + filePath;
    }

    public int[] getCodes(String path) {
        String[] str = path.split("\\.");
        if (str.length == 2) {
            String extensie = str[1];
            try (FileInputStream fin = new FileInputStream(path)) {
                byte[] bytes = fin.readAllBytes();
                fin.close();
                // Добавление информации о расширении файла
                byte[] extensieBytes = extensie.getBytes();
                byte[] newbytes = new byte[1 + extensieBytes.length + bytes.length];
                newbytes[0] = (byte) (extensie.length());
                for (int i = 0; i < extensieBytes.length; i++) {
                    newbytes[i + 1] = extensieBytes[i];
                }
                for (int i = 0; i < bytes.length; i++) {
                    newbytes[i + extensieBytes.length + 1] = bytes[i];
                }
                int[] codes = new int[newbytes.length];
                for (int i = 0; i < newbytes.length; i++) {
                    codes[i] = ((int) newbytes[i] + 255);
                }
                return codes;
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return null;
    }

    public void getFromCodes(int[] codes, String dir) {

        // Получение расширения файла
        int extensieLength = codes[0] - 255;
        byte[] extensieBytes = new byte[extensieLength];
        for (int i = 0; i < extensieLength; i++) {
            extensieBytes[i] = (byte) (codes[i + 1] - 255);
        }
        String extensie = new String(extensieBytes);

        // Получение байт содержимого
        byte[] fileBytes = new byte[codes.length - extensieLength - 1];
        for (int i = 0; i < fileBytes.length; i++) {
            fileBytes[i] = (byte) (codes[i + 1 + extensieLength] - 255);
        }
        String fileSeparator = System.getProperty("file.separator");
        File file = new File("result." + extensie);
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try (FileOutputStream fos = new FileOutputStream(file.getPath())) {
            fos.write(fileBytes, 0, fileBytes.length);
            fos.close();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

    public int[] getOpenKey(String path) {
        try (FileInputStream fin = new FileInputStream(path)) {
            byte[] bytes = fin.readAllBytes();
            fin.close();
            String str = new String(bytes);
            String[] values = str.split(" ");
            if (values.length == 2) {
                int p = Integer.parseInt(values[0]);
                int q = Integer.parseInt(values[1]);
                int n = p * q;
                int f = (p - 1) * (q - 1);
                // Доделать
                int e = 23;
                //
                int[] openKey = new int[2];
                openKey[0] = e;
                openKey[1] = n;
                return openKey;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public int[] getLockedKey(String path) {
        try (FileInputStream fin = new FileInputStream(path)) {
            byte[] bytes = fin.readAllBytes();
            fin.close();
            String str = new String(bytes);
            String[] values = str.split(" ");
            if (values.length == 2) {
                int p = Integer.parseInt(values[0]);
                int q = Integer.parseInt(values[1]);
                int n = p * q;
                int f = (p - 1) * (q - 1);
                // Доделать
                int e = 23;
                //
                // Вычисление d
                int k = 1;
                double d = 0.5;
                while (d % 1 != 0) {
                    d = (k * (double) f + 1) / (double) e;
                    k++;
                }
                int[] lockedKey = new int[2];
                lockedKey[0] = (int) d;
                lockedKey[1] = n;
                return lockedKey;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public int[] Encrypt(String keyPath, String folderPath) {
        int[] key = this.getOpenKey(keyPath);
        int[] codes = this.getCodes(this.getFileFromFolder(folderPath));
        for (int i = 0; i < codes.length; i++) {
            BigInteger x = BigInteger.valueOf(codes[i]);
            x = x.pow(key[0]);
            x = x.mod(BigInteger.valueOf(key[1]));
            codes[i] = x.intValue();
        }
        return codes;
    }

    public int[] Decrypt(String keyPath, int[] codes) {
        int[] key = this.getLockedKey(keyPath);
        for (int i = 0; i < codes.length; i++) {
            BigInteger x = BigInteger.valueOf(codes[i]);
            x = x.pow(key[0]);
            x = x.mod(BigInteger.valueOf(key[1]));
            codes[i] = x.intValue();
        }
        return codes;
    }
}
