import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Task1 {
    public static void run(String filePathValue) throws IOException {
        String filePath = filePathValue;
        ArrayList<Character> array = new ArrayList<Character>();
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
                String str = "";
                for (int i = 0; i < array.size(); i++) {
                    str += array.get(i).toString();
                }
                System.out.println(str);
                byte[] bytesASCII = str.getBytes("ASCII");
                byte[] bytesUTF8 = str.getBytes("UTF-8");
                byte[] bytesUTF16 = str.getBytes("UTF-16");
                System.out.println("Размер в байтах ASCII " + bytesASCII.length);
                System.out.println("Размер в байтах UTF-8 " + bytesUTF8.length);
                System.out.println("Размер в байтах UTF-16 " + bytesUTF16.length);
            }
        }
    }
}
