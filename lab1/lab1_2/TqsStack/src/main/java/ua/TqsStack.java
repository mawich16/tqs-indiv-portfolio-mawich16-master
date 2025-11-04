package ua;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class TqsStack<T> {

    private LinkedList<T> collection; 

    public TqsStack() {
        collection = new LinkedList<T>();
    }

    public void push(T a) {
        collection.push(a);
    }

    public T pop() {
        if (collection.isEmpty() == true)
            throw new NoSuchElementException("the stack is empty, unable to pop elements");
        return collection.removeFirst();
    }

    public T peek() {
        if (collection.isEmpty() == true)
            throw new NoSuchElementException("the stack is empty, unable to peek elements");
        return collection.getFirst();
    }

    public int size() {
        return collection.size();
    }

    public boolean isEmpty() {
        return collection.size() == 0;
    }

    public T popTopN(int n) {
        T top = null;
        for ( int i = 0; i<n; i++) {
            top = collection.removeFirst();
        }
        return top;
    }
    
}
