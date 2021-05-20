import java.io.Serializable;

public class HashMap<K, V> implements Serializable {

    private final int size = 1000000;
    private DLList<K> keys;
    private V[] table;

    @SuppressWarnings("unchecked")
    public HashMap() {
        keys = new DLList<K>();
        table = (V[]) new Object[size];
    }

    private int getHashCode(K key) {
        int hC = key.hashCode();
        // return key.hashCode() % size;
        return (hC % size + size) % size;
    }

    public boolean hasKey(K key) {
        int hashCode = getHashCode(key);
        return table[hashCode] != null;
    }

    public void put(K key, V value) {
        if (!hasKey(key)) keys.add(key);
        int hashCode = getHashCode(key);

        table[hashCode] = value;
    }

    public V get(K key) {
        return table[getHashCode(key)];
    }

    public DLList<K> keySet() {
        return keys;
    }

    // public void remove(K key, V value) {
    //     int hashCode = getHashCode(key);
    //     table[hashCode] = null;
    // }

    public void remove(K key) {
        table[getHashCode(key)] = null;
        keys.remove(key);
    }
    //   @Override
    //   public String toString() {
    //     String f = "";
    //     for (K key : keys) {
    //       int hashCode = getHashCode(key);
    //       f += "Bucket " + hashCode + " - " + key.toString() + " - " + table[hashCode].toString() + "\n";
    //     }
    //     return f;
    //   }
}
