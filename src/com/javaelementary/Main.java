package com.javaelementary;

public class Main {

    /*
    2*)Реализовать интерфейс MyMap и его имплементацию MyTreeMap - аналог Map<String, String>
    3*) Вместо 2* сделать MyMap<K,V>
    4**) Сделать дерево с балансировкой, черно-белое или AVL
    */

    public static void main(String[] args) {
        MyMap<Integer, String> map1 = new MyTreeMap<>();
        map1.put(3, "zzzzz");
        map1.put(2, "sdfds");
        map1.put(3, "eeee");
        map1.put(5, "aaaaa");
        map1.put(30, "aaaaa");
        map1.put(44, "aaaaa");
        map1.put(75, "aaaaa");
        map1.put(22, "aaaaa");
        map1.put(18, "aaaaa");
        map1.put(4, "aaaaa");
        map1.put(11, "aaaaa");
        map1.put(53, "aaaaa");
        map1.put(34, "aaaaa");
        map1.put(1, "aaaaa");
        System.out.println(map1.size());
        System.out.println(map1.containsKey(34));
        System.out.println(map1.get(53));
        System.out.println(map1.entrySet());
        System.out.println(map1);
    }
}