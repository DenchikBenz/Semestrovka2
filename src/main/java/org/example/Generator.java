package org.example;
import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
public class Generator {

    private static int size = 10000;
    private static int[] array = new int[size];
    public static void testGenerate() throws IOException {
        File myFile = new File("myFile.txt");
        myFile.createNewFile();

        try (FileWriter writer = new FileWriter("myFile.txt")){
            for (int j = 0; j < size; j++) {
                int t = (int) (Math.random() * 10000);
                writer.write(t + " ");
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader("myFile.txt"));
            String line = reader.readLine();
            while (line != null) {
                String[] str = line.split(" ");
                array = new int[str.length];
                array = Arrays.stream(str).mapToInt(Integer::parseInt).toArray();
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int[] getArray() {
        return array;
    }
    public static int[] getArrayByCount(int count) {
        int[] result = new int[count];
        for (int i = 0; i < count; i++) {
            result[i] = array[(int) (Math.random() * size)];
        }
        return result;
    }
}