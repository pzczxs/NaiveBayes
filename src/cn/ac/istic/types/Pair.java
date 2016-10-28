package cn.ac.istic.types;

/**
 * 
 * @author <a href = "mailto:pzczxs@gmail.com">XU, Shuo</a>
 * version 1.0
 *
 */
public class Pair<T1, T2> {
    private T1 first;
    private T2 second;
    
    public Pair(T1 first, T2 second) {
    	this.first = first;
    	this.second = second;
    }
   
    @Override
	public int hashCode() {
    	int hashFirst = (this.first != null? this.first.hashCode(): 0);
    	int hashSecond = (this.second != null? this.second.hashCode(): 0);

    	return (hashFirst + hashSecond) * hashSecond + hashFirst; 
	}

    @Override
    public boolean equals(Object obj) {
		if ((obj == null) || !(obj instanceof Pair)) {
			return false; 
		}
		
		if (this == obj) {
			return true; 
		}
		
		Pair<?, ?> other = (Pair<?, ?>) obj;

		return (this.first.equals(other.getFirst())) && (this.second.equals(other.getSecond()));
	}

    @Override
    public String toString() { 
           return "(" + this.first + ", " + this.second + ")"; 
    }

    public T1 getFirst() {
    	return this.first;
    }

    public void setFirst(T1 first) {
    	this.first = first;
    }

    public T2 getSecond() {
    	return this.second;
    }

    public void setSecond(T2 second) {
    	this.second = second;
    }
}
