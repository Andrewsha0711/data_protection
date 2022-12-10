package data_protection_lab2;

import java.io.IOException;

public class Program {
    public static void main(String[] args) throws IOException {
        String keyPath = "resources/key.txt";
        String folderPath = "resources/sourceFile";
        String resultFolder = "resources";

        // byte[] bytes = new Controller().Encrypt(keyPath, folderPath);
        // new Controller().getFromBytes(new Controller().Decrypt(keyPath,
        // bytes),
        // resultFolder);

        int[] codes =
                new Controller().getCodes(new Controller().getFileFromFolder(folderPath));
        codes = new Controller().Encrypt(keyPath, folderPath);
        codes = new Controller().Decrypt(keyPath, codes);
        new Controller().getFromCodes(codes, resultFolder);
    }

}
