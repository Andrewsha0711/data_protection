import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Task3 {
    public static int m = 5;

    public static String getKey(String filePathValue) throws IOException {
        String key = "";
        String filePath = filePathValue;
        ArrayList<Character> array = new ArrayList<Character>();
        // Посимвольное считывание файла
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            int symbol = bufferedReader.read();
            while (symbol != -1) {
                array.add((char) symbol);
                symbol = bufferedReader.read();
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
            e.printStackTrace();
        } finally {
            if (array.size() != 0) {
                for (int i = 0; i < array.size(); i++) {
                    key += array.get(i).toString();
                }
            }
        }
        if (key.length() == m)
            return key;
        return null;
    }

    public static ArrayList<Byte> getByteArray(String filePathValue) throws IOException {
        // Путь к файлу
        String filePath = filePathValue;
        // Массив байтов
        ArrayList<Byte> bytes = new ArrayList<Byte>();
        ArrayList<Character> array = new ArrayList<Character>();
        // Посимвольное считывание файла
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            int symbol = bufferedReader.read();
            while (symbol != -1) {
                array.add((char) symbol);
                symbol = bufferedReader.read();
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
            e.printStackTrace();
        } finally {
            if (array.size() != 0) {
                for (int i = 0; i < array.size(); i++) {
                    bytes.add((byte) ((char) array.get(i)));
                }
                while (bytes.size() % m != 0) {
                    bytes.add((byte) 'z');
                }
            }
        }
        if (bytes.size() != 0) {
            return bytes;
        }
        return null;
    }

    public static void Encrypt(String keyPathValue, String textPathValue)
            throws IOException {
        String key = getKey(keyPathValue);
        if (key != null) {
            ArrayList<Byte> bytes = getByteArray(textPathValue);
            ArrayList<Byte> bytesNew = new ArrayList<Byte>();
            for (int i = 0; i < key.length(); i++) {
                // Получение соответствующего ключу номера столбца
                int pointer = key.indexOf(String.valueOf(i + 1));
                for (int j = 0; j < bytes.size() / m; j++) {
                    bytesNew.add(bytes.get(pointer + m * j));
                }
            }
            FileWriter writer = new FileWriter(textPathValue, false);
            writer.write(bytesNew.toString());
            writer.close();
        }
    }

    public static void Decrypt(String keyPathValue, String textPathValue)
            throws IOException {
        ArrayList<Byte> bytes = new ArrayList<Byte>();
        String[] strArray = null;
        try {
            BufferedReader bufferedReader =
                    new BufferedReader(new FileReader(textPathValue));
            String str = bufferedReader.readLine();
            str = str.substring(1, str.length() - 1);
            strArray = str.split(", ");
            for (int i = 0; i < strArray.length; i++) {
                bytes.add((byte) Integer.parseInt(strArray[i]));
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
            e.printStackTrace();
        } finally {
            if (strArray != null) {
                String key = getKey(keyPathValue);
                if (key != null) {
                    ArrayList<Byte> bytesNew = new ArrayList<Byte>();
                    for (int j = 0; j < bytes.size() / m; j++) {
                        for (int i = 0; i < key.length(); i++) {
                            int step = Integer.parseInt(key.substring(i, i + 1)) - 1;
                            bytesNew.add(bytes.get((bytes.size() / m) * step + j));
                        }
                    }
                    String text = "";
                    for (int i = 0; i < bytesNew.size(); i++) {
                        text += (char) (bytesNew.get(i).byteValue());
                    }
                    FileWriter writer = new FileWriter(textPathValue, false);
                    writer.write(text);
                    writer.close();
                }
            }
        }
    }
}
