package org.kpa.cs;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class Helper {
    static String[] toStr(Iterator<String> args) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(args, Spliterator.SIZED), false)
                .toArray(String[]::new);
    }

    static int[] toInt(String[] strs) {
        return Arrays.stream(strs).mapToInt(Integer::parseInt).toArray();
    }

    static StringBuffer buf(int width, char ch) {
        char[] charArray = new char[width];
        Arrays.fill(charArray, ch);
        StringBuffer e = new StringBuffer();
        e.append(charArray);
        return e;
    }

    static void secureRun(Runnable task, int argsCount, String argsMask, String... args) {
        try {
            Preconditions.checkArgument(args.length == argsCount, "Illegal args count");
            task.run();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format("Error: %s. Given parameters must be: %s. Currently: %s",
                    e.getMessage(), argsMask, Arrays.toString(args)), e);
        }
    }
}
