/**************************************************************************************************
 *       Copyright(c):                                                                            *
 *                         Program made by @Moldovan Alexandru-Vasile.                            *
 *                                                                                                *
 **************************************************************************************************/

package Model.CyclicBarrier;

public class Pair<K,V>
{
    private K first;
    private V second;

    public Pair(K f,V s)
    {
        first = f;
        second = s;
    }

    public K getFirst() {
        return first;
    }

    public void setFirst(K first) {
        this.first = first;
    }


    public V getSecond() {
        return second;
    }

    public void setSecond(V second) {
        this.second = second;
    }


    @Override
    public String toString()
    {
        return first.toString() + "  " + second.toString();
    }
}

