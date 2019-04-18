package org.kpa.cs;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;

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

    static void secureRun(Runnable task, String cmdName, String argsMask, String... args) {
        try {
            int argsCount = Splitter.on(CharMatcher.whitespace()).trimResults().splitToList(argsMask).size();
            Preconditions.checkArgument(args.length == argsCount, "Illegal args count");
            task.run();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format("Error: %s. Given parameters to %s must be: %s. Currently: %s",
                    e.getMessage(), cmdName, argsMask, Arrays.toString(args)), e);
        }
    }
}
