package org.kpa.cs;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class InterpreterTest {
    private final List<String> commands;
    private final List<Pixel> expectedPixels;

    public InterpreterTest(List<String> commands, List<Pixel> expectedPixels) {
        this.commands = commands;
        this.expectedPixels = expectedPixels;
    }

    @Test
    public void performSampleTest() {
        Interpreter interpreter = new Interpreter();
        commands.forEach(cmd -> {
            interpreter.intepret(cmd);
            System.out.println("On cmd: " + cmd);
            interpreter.getCanvas().toStream(System.out);
        });
        expectedPixels.forEach(v -> Assert.assertEquals(v, interpreter.getCanvas().get(v.x, v.y)));
    }

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(
                new Object[]{
                        Arrays.asList("C 20 4", "L 1 2 6 2", "L 6 3 6 4", "R 14 1 18 3", "B 10 3 o"),
                        Pixel.list("15,4,o", "6,2,x", "7,2,o")},
                new Object[]{
                        Arrays.asList("C 40 20", "L 6 3 6 17", "L 1 2 6 2", "L 6 17 40 17", "R 14 1 18 3", "B 1 4 b", "B 17 2 z"),
                        Pixel.list("15,15, ", "7,2, ", "16,2,z")},
                new Object[]{
                        Arrays.asList("C 40 20", "L 1 1 40 20", "R 5 5 20 8", "B 17 2 z"),
                        Pixel.list("15,15, ", "7,2,z", "5,8,x")},
                new Object[]{
                        Arrays.asList("C 40 20", "l 1 1 5 20", "l 40 15 3 15 ", "l 40 7 3 10 ", "B 17 13 z"),
                        Pixel.list("29,9,z", "5,14,z", "27,8, ")},
                new Object[]{
                        Arrays.asList("C 40 30", "l 1 30 40 1", "B 29 29 o"),
                        Pixel.list("2,29, ", "3,29,x", "4,29,o")}
        );

    }

}