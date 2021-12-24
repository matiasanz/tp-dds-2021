package Utils;

public class Pair<K,V>{
    private K key;
    private V value;

    public Pair(K key, V value){
        this.key=key;
        this.value=value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object pair){
        return super.equals(pair) ||
            pair instanceof Pair
                && this.equals((Pair<?, ?>) pair)
            ;
    }

    private boolean equals(Pair<?,?> pair){
        return getKey().equals(pair.getKey()) && getValue().equals(pair.getValue());
    }
}