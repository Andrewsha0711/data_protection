package data_protection_lab4;

import java.nio.ByteBuffer;

public class Controller {
    // Конкатенация и подготовка исходного ключа
    public static byte[][] prepareKey(byte[] key) {
        byte[] newKey = key;
        while (newKey.length < 72) {
            String keyTemp = new String(newKey);
            newKey = (keyTemp + keyTemp).getBytes();
        }
        byte[][] preparedKey = new byte[18][4];
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 4; j++) {
                preparedKey[i][j] = newKey[(i * 4) + j];
            }
        }
        return preparedKey;
    }

    // Подготовка данных
    public static byte[] prepareData(byte[] data, int param) {
        int newSize = data.length;
        while (newSize % param != 0) {
            newSize += 1;
        }
        byte[] temp = new byte[newSize];
        for (int i = 0; i < data.length; i++) {
            temp[i] = data[i];
        }
        for (int i = data.length; i < newSize; i++) {
            temp[i] = 0;
        }
        return temp;
    }

    // Расширение ключей и матриц
    public static void calculateKeysAndMatrices(byte[][] keys, byte[] key, byte[][] s1,
            byte[][] s2, byte[][] s3, byte[][] s4) {
        byte[][] preparedKey = prepareKey(key);
        // xor раундовых ключей с исходным
        for (int i = 0; i < 18; i++) {
            int xor = ByteBuffer.wrap(keys[i]).getInt()
                    ^ ByteBuffer.wrap(preparedKey[i]).getInt();
            keys[i] = ByteBuffer.allocate(4).putInt(xor).array();
        }
        // 64 битная последовательность нуля
        byte[] temp = new byte[8];
        for (int i = 0; i < 8; i++) {
            temp[i] = 0;
        }
        // Шифрование ключей
        for (int i = 0; i < 18; i += 2) {
            temp = Encrypt(temp, keys, s1, s2, s3, s4);
            // Перезапись ключей
            for (int j = 0; j < 4; j++) {
                keys[i][j] = temp[j];
                keys[i + 1][j] = temp[j + 4];
            }
        }
        for (int i = 0; i < 8; i++) {
            temp[i] = 0;
        }
        // Шифрование матриц
        for (int i = 0; i < 256; i += 2) {
            temp = Encrypt(temp, keys, s1, s2, s3, s4);
            // Перезапись матриц
            for (int j = 0; j < 4; j++) {
                s1[i][j] = temp[j];
                s1[i + 1][j] = temp[j + 4];
            }
        }
        for (int i = 0; i < 256; i += 2) {
            temp = Encrypt(temp, keys, s1, s2, s3, s4);
            // Перезапись матриц
            for (int j = 0; j < 4; j++) {
                s2[i][j] = temp[j];
                s2[i + 1][j] = temp[j + 4];
            }
        }
        for (int i = 0; i < 256; i += 2) {
            temp = Encrypt(temp, keys, s1, s2, s3, s4);
            // Перезапись матриц
            for (int j = 0; j < 4; j++) {
                s3[i][j] = temp[j];
                s3[i + 1][j] = temp[j + 4];
            }
        }
        for (int i = 0; i < 256; i += 2) {
            temp = Encrypt(temp, keys, s1, s2, s3, s4);
            // Перезапись матриц
            for (int j = 0; j < 4; j++) {
                s4[i][j] = temp[j];
                s4[i + 1][j] = temp[j + 4];
            }
        }
    }

    // Входит 32 битный блок то есть 4 байта
    public static byte[] iterationFunction(byte[] data, byte[][] s1, byte[][] s2,
            byte[][] s3, byte[][] s4) {
        byte x1 = data[0];
        byte x2 = data[1];
        byte x3 = data[2];
        byte x4 = data[3];

        long f = (ByteBuffer.wrap(s1[x1 + 128]).getInt()
                + ByteBuffer.wrap(s2[x2 + 128]).getInt()) & 0xffffffffL;
        f = f ^ ByteBuffer.wrap(s3[x3 + 128]).getInt();
        f = (f + ByteBuffer.wrap(s4[x4 + 128]).getInt()) & 0xffffffffL;

        // return ByteBuffer.allocate(4).putInt(f).array();
        return ByteBuffer.allocate(8).putLong(f).array();
    }

    public static byte[] Encrypt(byte[] data, byte[][] p, byte[][] s1, byte[][] s2,
            byte[][] s3, byte[][] s4) {
        byte[] newData = new byte[data.length];
        // Цикл по исходным данным
        for (int i = 0; i < data.length; i += 8) {
            // Выдираем блоки по 32 бита (4 байта)
            byte[] l = new byte[4];
            byte[] r = new byte[4];
            for (int j = 0; j < 4; j++) {
                l[j] = data[i + j];
                r[j] = data[i + 4 + j];
            }

            // Цикл по раундовым ключам (по первым 16)
            for (int k = 0; k < 15; k++) {
                int l_int = ByteBuffer.wrap(l).getInt();
                int r_int = ByteBuffer.wrap(r).getInt();
                int p_int = ByteBuffer.wrap(p[k]).getInt();

                // Вычисление l и r
                // r = l xor p
                r = ByteBuffer.allocate(4).putInt(l_int ^ p_int).array();

                // l = ((l xor p)-> function) xor r
                int f = ByteBuffer.wrap(iterationFunction(r, s1, s2, s3, s4)).getInt();
                l = ByteBuffer.allocate(4).putInt(f ^ r_int).array();
            }

            int l_int = ByteBuffer.wrap(l).getInt();
            int r_int = ByteBuffer.wrap(r).getInt();
            int p_int = ByteBuffer.wrap(p[15]).getInt();

            // Вычисление l и r
            // r = l xor p
            byte[] l_xor_p15 = ByteBuffer.allocate(4).putInt(l_int ^ p_int).array();

            // l = ((l xor p)-> function) xor r
            int f = ByteBuffer.wrap(iterationFunction(l_xor_p15, s1, s2, s3, s4))
                    .getInt();
            r = ByteBuffer.allocate(4).putInt(f ^ r_int).array();

            l = ByteBuffer.allocate(4).putInt(
                    ByteBuffer.wrap(l_xor_p15).getInt() ^ ByteBuffer.wrap(p[17]).getInt())
                    .array();
            r = ByteBuffer.allocate(4)
                    .putInt(ByteBuffer.wrap(r).getInt() ^ ByteBuffer.wrap(p[16]).getInt())
                    .array();

            // Загоняем новые l и r вместо старых
            for (int j = 0; j < 4; j++) {
                newData[i + j] = l[j];
                newData[i + 4 + j] = r[j];
            }
        }
        return newData;
    }

    public static byte[] Decrypt(byte[] data, byte[][] p, byte[][] s1, byte[][] s2,
            byte[][] s3, byte[][] s4) {
        byte[] newData = new byte[data.length];
        // Цикл по исходным данным
        for (int i = 0; i < data.length; i += 8) {
            // Выдираем блоки по 32 бита (4 байта)
            byte[] l = new byte[4];
            byte[] r = new byte[4];
            for (int j = 0; j < 4; j++) {
                l[j] = data[i + j];
                r[j] = data[i + 4 + j];
            }

            l = ByteBuffer.allocate(4)
                    .putInt(ByteBuffer.wrap(l).getInt() ^ ByteBuffer.wrap(p[17]).getInt())
                    .array();
            r = ByteBuffer.allocate(4)
                    .putInt(ByteBuffer.wrap(r).getInt() ^ ByteBuffer.wrap(p[16]).getInt())
                    .array();
            // Цикл по раундовым ключам (по первым 16)
            for (int k = 15; k > 0; k--) {
                int l_int = ByteBuffer.wrap(l).getInt();
                int r_int = ByteBuffer.wrap(r).getInt();
                int p_int = ByteBuffer.wrap(p[k]).getInt();

                // Вычисление l и r
                // r = l xor p
                r = ByteBuffer.allocate(4).putInt(l_int ^ p_int).array();

                // l = ((l xor p)-> function) xor r
                int f = ByteBuffer.wrap(iterationFunction(r, s1, s2, s3, s4)).getInt();
                l = ByteBuffer.allocate(4).putInt(f ^ r_int).array();
            }

            int l_int = ByteBuffer.wrap(l).getInt();
            int r_int = ByteBuffer.wrap(r).getInt();
            int p_int = ByteBuffer.wrap(p[0]).getInt();

            // Вычисление l и r
            // r = l xor p
            byte[] new_l = ByteBuffer.allocate(4).putInt(l_int ^ p_int).array();

            // l = ((l xor p)-> function) xor r
            int f = ByteBuffer.wrap(iterationFunction(new_l, s1, s2, s3, s4)).getInt();
            r = ByteBuffer.allocate(4).putInt(f ^ r_int).array();
            l = new_l;
            // Загоняем новые l и r вместо старых
            for (int j = 0; j < 4; j++) {
                newData[i + j] = l[j];
                newData[i + 4 + j] = r[j];
            }
        }
        return newData;
    }
}
