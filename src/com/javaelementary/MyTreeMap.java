package com.javaelementary;

import java.util.LinkedHashSet;
import java.util.Set;

public class MyTreeMap<K extends Comparable<? super K>, V> implements MyMap<K, V> {
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

    /**
     * Removes all of the mappings from this map.
     */
    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    /**
     * Returns true if this map contains a mapping for the specified key.
     * @param key - key whose presence in this map is to be tested
     * @return true if this map contains a mapping for the specified key
     * @throws NullPointerException - if the specified key is null
     */
    @Override
    public boolean containsKey(K key) {
        return findEntry(key) != null;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
     * @param key - the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null if this map contains no mapping for the key
     * @throws NullPointerException - if the specified key is null
     */
    @Override
    public V get(K key) {
        Entry<K, V> entry = findEntry(key);
        if (entry == null) {
            return null;
        }
        return entry.value;
    }

    /**
     * Returns true if this map contains no key-value mappings.
     * @return true if this map contains no key-value mappings
     */
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * @param key - key with which the specified value is to be associated
     * @param value - value to be associated with the specified key
     * @return the previous value associated with key, or null if there was no mapping for key.
     * @throws NullPointerException - if the specified key is null
     */
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

    /**
     * Returns the number of key-value mappings in this map.
     * @return the number of key-value mappings in this map
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns a Set view of the mappings contained in this map.
     * @return a set view of the mappings contained in this map
     */
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

    private Entry<K, V> rotateLeft(Entry<K, V> entry) {
        if (entry.parent == null) {
            root = entry.right;
            root.parent = null;
            root.left.left = entry;
            entry.parent = root.left;
            entry.right = null;
        } else {
            entry.right.parent = entry.parent;
            entry.parent.right = entry.right;
            entry.parent = entry.right;
            entry.right.left = entry;
            entry.right = null;
        }
        entry.height = Math.max(height(entry.left), height(entry.right)) + 1;
        entry.parent.height = Math.max(height(entry.parent.left), height(entry.parent.right)) + 1;
        return entry.parent;
    }

    private Entry<K, V> rotateRight(Entry<K, V> entry) {
        if (entry.parent == null) {
            root = entry.left;
            root.parent = null;
            root.right.right = entry;
            entry.parent = root.right;
            entry.left = null;
        } else {
            entry.left.parent = entry.parent;
            entry.parent.right = entry.left;
            entry.parent = entry.left;
            entry.left.right = entry;
            entry.left = null;
        }
        entry.height = Math.max(height(entry.left), height(entry.right)) + 1;
        entry.parent.height = Math.max(height(entry.parent.left), height(entry.parent.right)) + 1;
        return entry.parent;
    }

    /**
     * AVL tree balancing
     */
    private Entry<K, V> balanceTree(Entry<K, V> entry, K key, V value) {
        entry.height = Math.max(height(entry.left), height(entry.right)) + 1;
        int balanceFactor = getBalance(entry);

        if (balanceFactor > 1 && key.compareTo(entry.left.key) < 0) {
            return rotateRight(entry);
        }
        if (balanceFactor < -1 && key.compareTo(entry.right.key) > 0) {
            return rotateLeft(entry);
        }
        if (balanceFactor > 1 && key.compareTo(entry.left.key) > 0) {
            entry.left = rotateLeft(entry.left);
            return rotateRight(entry);
        }
        if (balanceFactor < -1 && key.compareTo(entry.right.key) < 0) {
            entry.right = rotateRight(entry.right);
            return rotateLeft(entry);
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
        return height(entry.left) - height(entry.right);
    }

    private V putToTree(Entry<K, V> entry, K key, V value) {
        int cmp = key.compareTo(entry.key);
        if (cmp < 0) {
            if (entry.left == null) {
                entry.left = new Entry<>(key, value);
                entry.left.parent = entry;
                size++;
            } else {
                putToTree(entry.left, key, value);
            }
        }
        if (cmp > 0) {
            if (entry.right == null) {
                entry.right = new Entry<>(key, value);
                entry.right.parent = entry;
                size++;
            } else {
                putToTree(entry.right, key, value);
            }
        }
        if (cmp == 0) {
            entry.setValue(value);
            return value;
        }
        balanceTree(entry, key, value);
        return null;
    }

    private Entry<K, V> findEntry(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Entry<K, V> entry = root;
        while (entry != null) {
            int cmp = key.compareTo(entry.key);
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