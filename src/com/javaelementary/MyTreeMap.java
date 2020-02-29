package com.javaelementary;

import java.util.LinkedHashSet;
import java.util.Set;

public class MyTreeMap<K, V> implements MyMap<K, V> {
    private int size;
    private Entry<K, V> root;

    public static class Entry<K, V> implements MyMap.Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> left;
        private Entry<K, V> right;
        private Entry<K, V> parent;
        private int height;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        return findEntry(key) != null;
    }

    @Override
    public V get(K key) {
        Entry<K, V> entry = findEntry(key);
        if (entry == null) {
            return null;
        }
        return entry.value;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public V put(K key, V value) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (root == null) {
            root = new Entry<>(key, value);
            size++;
            return null;
        }
        return putToTree(root, key, value);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Set<MyTreeMap.Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = new LinkedHashSet<>();
        addToSet(root, set);
        return set;
    }

    @Override
    public String toString() {
        Set<Entry<K, V>> set = new LinkedHashSet<>();
        addToSet(root, set);
        return set.toString();
    }

    private void addToSet(Entry<K, V> entry, Set<Entry<K, V>> set) {
        if (entry == null) {
            return;
        }
        addToSet(entry.left, set);
        set.add(entry);
        addToSet(entry.right, set);
    }

    private Entry<K, V> leftRotate(Entry<K, V> x) {
        Entry<K, V> y = x.right;
        Entry<K, V> tmp = y.left;
        y.left = x;
        x.right = tmp;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        return y;
    }

    private Entry<K, V> rightRotate(Entry<K, V> y) {
        Entry<K, V> x = y.left;
        Entry<K, V> tmp = x.right;
        x.right = y;
        y.left = tmp;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        return x;
    }

    private Entry<K, V> balanceTree(Entry<K, V> entry, K key, V value, int balanceFactor) {
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;
        if (balanceFactor > 1 && k.compareTo(entry.left.key) < 0) {
            return rightRotate(entry);
        }
        if (balanceFactor < -1 && k.compareTo(entry.right.key) > 0) {
            return leftRotate(entry);
        }
        if (balanceFactor > 1 && k.compareTo(entry.left.key) > 0) {
            entry.left = leftRotate(entry.left);
            return rightRotate(entry);
        }
        if (balanceFactor < -1 && k.compareTo(entry.right.key) < 0) {
            entry.right = rightRotate(entry.right);
            return leftRotate(entry);
        }
        return entry;
    }

    private int height(Entry<K, V> entry) {
        if (entry == null) {
            return 0;
        }
        return entry.height;
    }

    private int getBalance(Entry<K, V> entry) {
        return height(entry.right) - height(entry.left);
    }

    private V putToTree(Entry<K, V> entry, K key, V value) {
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;
        int cmp = k.compareTo(entry.key);
        if (cmp < 0) {
            if (entry.left == null) {
                entry.left = new Entry<>(key, value);
                size++;

                entry.height = Math.max(height(entry.left), height(entry.right)) + 1;
                int balanceFactor = getBalance(entry);
                if (balanceFactor > 1 || balanceFactor < -1) {
                    balanceTree(entry, key, value, balanceFactor);
                }
                return entry.value;
            } else {
                putToTree(entry.left, key, value);
            }
        }
        if (cmp > 0) {
            if (entry.right == null) {
                entry.right = new Entry<>(key, value);
                size++;

                entry.height = Math.max(height(entry.left), height(entry.right)) + 1;
                int balanceFactor = getBalance(entry);
                if (balanceFactor > 1 || balanceFactor < -1) {
                    balanceTree(entry, key, value, balanceFactor);
                }
                return entry.value;
            } else {
                putToTree(entry.right, key, value);
            }
        }
        if (cmp == 0) {
            entry.setValue(value);
            return entry.value;
        }
        return entry.value;
    }

    private Entry<K, V> findEntry(Object key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;
        Entry<K, V> entry = root;
        while (entry != null) {
            int cmp = k.compareTo(entry.key);
            if (cmp < 0) {
                entry = entry.left;
            }
            if (cmp > 0) {
                entry = entry.right;
            } else {
                return entry;
            }
        }
        return null;
    }
}