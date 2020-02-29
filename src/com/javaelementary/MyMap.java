package com.javaelementary;

import java.util.Set;

public interface MyMap<K, V> {
    interface Entry<K, V> {
        K getKey();

        V getValue();

        V setValue(V value);
    }

    void clear();

    boolean containsKey(K key);

    V get(K key);

    boolean isEmpty();

    V put(K key, V value);

    int size();

    Set<MyTreeMap.Entry<K, V>> entrySet();
}