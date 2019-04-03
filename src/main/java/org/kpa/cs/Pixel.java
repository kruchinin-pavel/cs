package org.kpa.cs;

import com.google.common.base.Splitter;

import java.util.*;
import java.util.stream.Collectors;

public class Pixel {
    final int x, y;
    final char ch;

    Pixel(int x, int y, char ch) {
        this.x = x;
        this.y = y;
        this.ch = ch;
    }

    @Override
    public String toString() {
        return "Pixel{" +
                "x=" + x +
                ", y=" + y +
                ", ch='" + ch +
                "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pixel pixel = (Pixel) o;
        return x == pixel.x &&
                y == pixel.y &&
                ch == pixel.ch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, ch);
    }

    /**
     * Parse params to a pixel list
     *
     * @param params - "x,y,ch", "x1,y1,ch1" ....
     * @return pixel list
     */
    static List<Pixel> list(String... params) {
        return Arrays.stream(params).map(param -> {
            Iterator<String> iter = Splitter.on(",").split(param).iterator();
            return new Pixel(Integer.parseInt(iter.next()),
                    Integer.parseInt(iter.next()),
                    iter.next().charAt(0));
        }).collect(Collectors.toList());
    }

}
