package Model.LatchTable;

import Model.LatchTable.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

public class LatchTable<K,V> implements ILatchTable<K,V> {
    private HashMap<K, V> _map;
    public LatchTable() {
        _map=new HashMap<>();
    }

    @Override
    public void put(K key, V value) {
        this._map.put(key, value);
    }

    @Override
    public V get(K key) {
        return this._map.get(key);
    }

    @Override
    public Collection<V> values() {
        return this._map.values();
    }

    @Override
    public Collection<K> keys() {
        return this._map.keySet();
    }

    @Override
    public void remove(K fd) {
        _map.remove(fd);
    }

    @Override
    public ILatchTable clone() {
        ILatchTable dict = new LatchTable();
        for(K key : _map.keySet())
            dict.put(key, _map.get(key));
        return dict;
    }

    @Override
    public boolean contains(K key)
    {
        return _map.containsKey(key);
    }

    @Override
    public Map<K, V> toMap() {
        return _map;
    }

    @Override
    public String toString() {
        String ret = "";
        boolean ok = false;
        for(HashMap.Entry<K, V> entry : this._map.entrySet()) {
            if(ok)
                ret = ret + "\n";
            ret += entry.getKey().toString() + " -> " + entry.getValue().toString();
            ok = true;
        }
        return ret;
    }
}

