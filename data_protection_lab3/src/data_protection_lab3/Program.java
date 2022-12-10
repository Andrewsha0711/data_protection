package data_protection_lab3;

import java.io.FileOutputStream;
import java.io.IOException;

public class Program {

        public static void main(String[] args) throws IOException {
                String resultPath = "resources/result.txt";
                String folderPath = "resources/sourceFile";
                String newFilePath = "resources/newFile";
                String textPath = "resources/text.txt";

                new Controller().writeMessages(newFilePath, textPath);
                byte[] bytes = new Controller()
                                .getBytes(new Controller().getFileFromFolder(folderPath));

                byte[] bytesNew = new Controller().getMessages(bytes);
                FileOutputStream fos = new FileOutputStream(resultPath);
                fos.write(bytesNew, 0, bytesNew.length);
                fos.close();
        }

}
