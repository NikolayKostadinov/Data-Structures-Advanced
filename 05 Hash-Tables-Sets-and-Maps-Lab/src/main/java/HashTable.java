import java.util.*;

public class HashTable<K, V> implements Iterable<KeyValue<K, V>> {

    public static final int INITIAL_CAPACITY = 16;
    public static final double LOAD_FACTOR = 0.80d;

    private LinkedList<KeyValue<K, V>>[] slots;

    private int count;
    private int capacity;

    public HashTable() {
        this(INITIAL_CAPACITY);
    }

    public HashTable(int capacity) {
        this.slots = new LinkedList[capacity];
        this.count = 0;
        this.capacity = capacity;
    }

    public void add(K key, V value) {
        this.growIfNeeded();
        int slotNumber = findSlotNumber(key);

        if (this.slots[slotNumber] == null) {
            this.slots[slotNumber] = new LinkedList<>();
        }

        for (KeyValue<K, V> element : this.slots[slotNumber]) {
            if (element.getKey().equals(key)) {
                throw new IllegalArgumentException("Key already exists: " + key);
            }
        }

        KeyValue<K, V> toInsert = new KeyValue<>(key, value);
        this.slots[slotNumber].addLast(toInsert);
        this.count++;
    }

    private int findSlotNumber(K key) {
        return Math.abs(key.hashCode()) % this.slots.length;
    }

    private void growIfNeeded() {
        if ((double) (this.size() + 1) / this.capacity() > LOAD_FACTOR) {
            grow();
        }
    }

    private void grow() {
        HashTable<K, V> newHashTable = new HashTable<>(this.slots.length * 2);

        for (LinkedList<KeyValue<K, V>> slot : this.slots) {
            if (slot != null){
                for (KeyValue<K, V> keyValue : slot) {
                    newHashTable.add(keyValue.getKey(), keyValue.getValue());
                }
            }
        }

        this.capacity = newHashTable.capacity;
        this.slots = newHashTable.slots;
        this.count = newHashTable.count;
    }

    public int size() {
        return this.count;
    }

    public int capacity() {
        return this.capacity;
    }

    public boolean addOrReplace(K key, V value) {
        KeyValue<K, V> element = find(key);
        if (element != null){
            element.setValue(value);
            return true;
        }

        add(key, value);
        return true;
    }

    public V get(K key) {
        KeyValue<K, V> element = this.find(key);

        if (element == null) {
            throw new IllegalArgumentException();
        }

        return element.getValue();
    }

    public KeyValue<K, V> find(K key) {
        int slotNumber = findSlotNumber(key);
        if (this.slots[slotNumber] != null) {
            for (KeyValue<K, V> element : slots[slotNumber]) {
                if (element.getKey().equals(key)) {
                    return element;
                }
            }
        }
        return null;
    }

    public boolean containsKey(K key) {
        return find(key) != null;
    }

    public boolean remove(K key) {
        int slotNumber = findSlotNumber(key);
        if (this.slots[slotNumber] != null) {
            for (KeyValue<K, V> element : slots[slotNumber]) {
                if (element.getKey().equals(key)) {
                    slots[slotNumber].remove(element);
                    this.count--;
                    return true;
                }
            }
        }
        return false;
    }

    public void clear() {
        this.slots = new LinkedList[capacity];
        this.count = 0;
        this.capacity = capacity;
    }

    public Iterable<K> keys() {
        LinkedList<K> keys = new LinkedList<>();
        for (KeyValue<K, V> element : this) {
            keys.add(element.getKey());
        }

        return keys;
    }

    public Iterable<V> values() {
        LinkedList<V> values = new LinkedList<>();
        for (KeyValue<K, V> element : this) {
            values.add(element.getValue());
        }

        return values;
    }

    @Override
    public Iterator<KeyValue<K, V>> iterator() {
        return new Iterator<KeyValue<K, V>>() {
            int passTrueCount = 0;
            int currentSlot = 0;
            final Deque<KeyValue<K, V>> elements = new ArrayDeque<>();

            @Override
            public boolean hasNext() {
                return passTrueCount < count;
            }

            @Override
            public KeyValue<K, V> next() {
                if (elements.isEmpty())
                    while (slots[currentSlot] == null) {
                        currentSlot++;

                        elements.addAll(slots[currentSlot]);
                        passTrueCount += slots[currentSlot].size();

                        currentSlot++;
                    }

                return elements.poll();
            }
        };
    }
}
