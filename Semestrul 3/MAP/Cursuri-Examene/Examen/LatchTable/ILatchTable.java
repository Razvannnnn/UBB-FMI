package Model.LatchTable;

import java.util.Collection;
import java.util.Map;

public interface ILatchTable<K,V> {
    void put(K key, V value);
    V get(K key) ;
    Collection<V> values();
    Collection<K> keys();
    void remove(K fd);
    boolean contains(K key);
    ILatchTable<K, V> clone();
    Map<K, V> toMap();
}
