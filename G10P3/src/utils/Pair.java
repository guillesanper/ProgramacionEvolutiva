
package utils;

public class Pair<F, S> {


    private F first;
    private S second;

    public Pair(F first, S second) {
        this.first=first;
        this.second=second;
    }

    public F get_first() { return first; }
    public S get_second() { return second; }

    public void set_first(F val)  {  this.first=val; }
    public void set_second(S val) { this.second=val; }

    @Override
    public String toString() { return "("+first+", "+second+")"; }
}
