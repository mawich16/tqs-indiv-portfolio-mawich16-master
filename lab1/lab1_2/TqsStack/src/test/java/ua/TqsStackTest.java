package ua;

import static java.lang.invoke.MethodHandles.lookup;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;

class TqsStackTest {

    static final Logger log = org.slf4j.LoggerFactory.getLogger(lookup().lookupClass());

    @DisplayName("Testing if the stack is empty on construction")
    @org.junit.jupiter.api.Test
    void isEmptyOnConstruction() {
        TqsStack<Integer> t = new TqsStack<Integer>();

        assertEquals(true, t.isEmpty());
    }

    @DisplayName("Testing if the stack has size 0 on construction")
    @org.junit.jupiter.api.Test
    void size() {
        TqsStack<Integer> t = new TqsStack<Integer>();

        assertEquals(0, t.size());
    }

    @DisplayName("Testing if after n > 0 pushes to an empty stack, the stack is not empty and its size is n")
    @org.junit.jupiter.api.Test
    void push() {
        TqsStack<Integer> t = new TqsStack<Integer>();

        t.push(10);
        t.push(20);
        assertEquals(2, t.size());
        assertEquals(false, t.isEmpty());
    }

    @DisplayName("Testing if one pushes x then pops, the value popped is x.")
    @org.junit.jupiter.api.Test
    void pop() {
        TqsStack<String> t = new TqsStack<>();
       
        t.push("A");
        assertEquals("A", t.pop());
    }

    @DisplayName("Testing if one pushes x then peeks, the value returned is x, but the size stays the same")
    @org.junit.jupiter.api.Test
    void peek() {
        TqsStack<String> t = new TqsStack<>();
        
        t.push("A");
        assertEquals("A", t.peek());
        assertEquals(1, t.size());
    }

    @DisplayName("Testing if the size is n, then after n pops, the stack is empty and has a size 0")
    @org.junit.jupiter.api.Test
    void isEmptyAfterPop() {
        TqsStack<String> t = new TqsStack<>();
        
        t.push("A");
        t.push("B");
        assertEquals(2, t.size());
        t.pop();
        t.pop();
        assertEquals(0, t.size());
        assertEquals(true, t.isEmpty());
    }

    @DisplayName("Testing if popping from an empty stack throws a NoSuchElementException")
    @org.junit.jupiter.api.Test
    void popAnEmptyStack() {
        TqsStack<String> t = new TqsStack<>();
        
        assertThrows(NoSuchElementException.class, () -> t.pop());
    }

    @DisplayName("Testing if popping into an empty stack throws a NoSuchElementException")
    @org.junit.jupiter.api.Test
    void peekAnEmptyStack() {
        TqsStack<String> t = new TqsStack<>();
        
        assertThrows(NoSuchElementException.class, () -> t.peek());
    }

    @DisplayName("Testing to top the nth element of a stack")
    @org.junit.jupiter.api.Test
    void popTopN() {
        TqsStack<Integer> t = new TqsStack<>();
        log.debug("Testing popTopN method in {}", t.getClass().getName());
        
        t.push(1);
        t.push(2);
        t.push(3);
        t.push(4);
        t.push(5);
        assertEquals(3,t.popTopN(3));
        assertEquals(2, t.size());
    }
}