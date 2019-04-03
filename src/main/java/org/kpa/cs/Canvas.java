package org.kpa.cs;

import com.google.common.base.Preconditions;

import java.io.PrintStream;

import static org.kpa.cs.Helper.buf;

public class Canvas {
    private final Pixel[][] rows;
    public final int width;
    public final int height;
    private final String horizontalBorder;

    public Canvas(int width, int height) {
        horizontalBorder = buf(width + 2, '-').toString();
        rows = new Pixel[width][height];
        this.width = width;
        this.height = height;
        for (int x = 1; x <= width; x++)
            for (int y = 1; y <= height; y++)
                put(x, y, ' ');
    }

    public Pixel get(int x, int y) {
        validate(x, y);
        return rows[x - 1][y - 1];
    }

    private void validate(int x, int y) {
        Preconditions.checkArgument(inBound(x, y), "Not in bounds: x[1:%s]=%s, y[1:%s]=%s",
                width, x, height, y);
    }

    public boolean inBound(int x, int y) {
        return 0 < x && x <= width && 0 < y && y <= height;
    }

    public void put(int x, int y, char ch) {
        validate(x, y);
        rows[x - 1][y - 1] = new Pixel(x, y, ch);
    }

    public void toStream(PrintStream ps) {
        ps.println(horizontalBorder);
        for (int y = 1; y <= height; y++) {
            ps.print("|");
            for (int x = 1; x <= width; x++) {
                ps.print(get(x, y).ch);
            }
            ps.println("|");
        }
        ps.println(horizontalBorder);
    }

    @Override
    public String toString() {
        return "Canvas{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}
