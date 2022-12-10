import java.io.IOException;

public class Program {

    public static void main(String[] args) throws IOException {
        // Task1.run("resources/task1.doc");
        // Task2.run("resources/task2.doc");
        Task3.Encrypt("resources/task3_key.doc", "resources/task3_text.doc");
        // Task3.Decrypt("resources/task3_key.doc", "resources/task3_text.doc");
    }

}
