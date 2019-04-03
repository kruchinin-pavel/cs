package org.kpa.cs;

import org.junit.Test;

public class InterpreterNegativeTest {

    @Test(expected = IllegalArgumentException.class)
    public void testOutOfBounds() {
        Interpreter interpreter = new Interpreter();
        interpreter.intepret("C 40 20");
        interpreter.intepret("L 1 1 60 20");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongCommand() {
        Interpreter interpreter = new Interpreter();
        interpreter.intepret("asd");
    }

}