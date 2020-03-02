package com.javaelementary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Task1 {

    /*
    1.) Есть документ со списком URL:
    https://drive.google.com/open?id=1wVBKKxpTKvWwuCzqY1cVXCQZYCsdCXTl
    Вывести топ 10 доменов которые встречаются чаще всего. В документе могут встречается пустые и недопустимые строки.
    */

    public static void main(String[] args) {
        textParsing("urls.txt");
    }
    private static void textParsing(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            Map<String, Integer> map = new HashMap<>();
            Scanner scanner = new Scanner(br);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                line.trim();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) != '/') {
                        stringBuilder.append(line.charAt(i));
                    } else {
                        break;
                    }
                }
                if (stringBuilder.length() != 0 && stringBuilder.substring(0, 4).equals("www.")) {
                    stringBuilder.delete(0, 4);
                }
                if (stringBuilder.length() != 0 && stringBuilder.substring(0, 2).equals("m.")) {
                    stringBuilder.delete(0, 2);
                }
                if (map.containsKey(stringBuilder.toString()) && stringBuilder.length() != 0 && stringBuilder.toString().contains(".")) {
                    int count = map.get(stringBuilder.toString());
                    count++;
                    map.put(stringBuilder.toString(), count);
                } else if (stringBuilder.length() != 0 && stringBuilder.toString().contains(".")) {
                    map.put(stringBuilder.toString(), 1);
                }
            }
            List<Map.Entry<String, Integer>> domains = new ArrayList<>(map.entrySet());
            domains.sort(new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return Integer.compare(o2.getValue(), o1.getValue());
                }
            });
            System.out.println("Top 10 domains: " + domains.subList(0, 10));
        } catch (IOException e) {
            System.out.println("File " + fileName + " not found");
        }
    }
}