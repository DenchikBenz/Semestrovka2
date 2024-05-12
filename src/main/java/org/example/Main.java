package org.example;


import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Generator.testGenerate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TwoThreeTree tree = new TwoThreeTree();
        double mid_time = 0;
        double mid_count = 0;
        // 3. Поэлементно добавьте числа в структуру, замеряя время работы и
        // количество операций для каждого добавления.
        int[] array1 = Generator.getArray();
        for (int i = 0; i < array1.length; i++) {
            tree.insert(array1[i]);
            mid_count += tree.getCounter();
            mid_time += tree.getTime();
        }
        System.out.println(mid_count / array1.length);
        System.out.println(mid_time / array1.length);
        System.out.println();
        mid_count = 0;
        mid_time = 0;

        //4. Случайным образом выберите из массива 100 элементов и найдите их в структуре,
        // замеряя также время работы и количество операций для каждого поиска.
        int[] array2 = Generator.getArrayByCount(100);
        for (int i = 0; i < array2.length; i++) {
            tree.search(array2[i]);
            mid_count += tree.getCounter();
            mid_time += tree.getTime();
        }
        System.out.println(mid_count / array2.length);
        System.out.println(mid_time / array2.length);
        System.out.println();

        mid_count = 0;
        mid_time = 0;

        //5. Случайным образом выберите из массива 1000 элементов и удалите их из структуры,
        // замеряя время работы и количество операций для каждого удаления.
        int[] array3 = Generator.getArrayByCount(1000);
        for (int i = 0; i < array3.length; i++) {
            tree.remove(array3[i]);
            mid_count += tree.getCounter();
            mid_time += tree.getTime();
        }
        System.out.println(mid_count / array3.length);
        System.out.println(mid_time / array3.length);
    }

}