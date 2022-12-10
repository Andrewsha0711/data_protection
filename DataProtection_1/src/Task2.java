import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class Task2 {
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
                byte[] bytes = str.getBytes("UTF-8");
                HashMap<Byte, Integer> map = new HashMap<Byte, Integer>();
                for (int i = 0; i < bytes.length; i++) {
                    if (map.containsKey(bytes[i])) {
                        map.put(bytes[i], map.get(bytes[i]) + 1); // Увеличивает
                                                                  // значение по
                                                                  // ключу
                                                                  // на 1
                    } else {
                        map.put(bytes[i], 1); // Первое появление байта
                    }
                }
                System.out.println("Размер в байтах UTF-8 " + bytes.length);
                for (int i = 0; i < map.size(); i++) {
                    System.out.println(map.keySet().toArray()[i] + " = "
                            + (char) ((byte) (map.keySet().toArray()[i])) + " : "
                            + map.values().toArray()[i]);
                }
                // Использованные специальные символы
                System.out.println("----------------");
                System.out.println("\n".getBytes()[0] + " is backslash n");
                System.out.println("\r".getBytes()[0] + " is backslash r");
                System.out.println("\t".getBytes()[0] + " is backslash t");
                System.out.println(" ".getBytes()[0] + " is space");
            }
        }
    }
}
