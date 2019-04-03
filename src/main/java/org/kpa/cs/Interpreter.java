package org.kpa.cs;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.kpa.cs.Helper.secureRun;
import static org.kpa.cs.Helper.toInt;

public class Interpreter {
    private static final char LINE_CHAR = 'x';
    private final AtomicReference<Canvas> canvasRef = new AtomicReference<>();
    private final Map<String, Consumer<String[]>> commandMap = new ImmutableMap.Builder<String, Consumer<String[]>>()
            .put("C", this::createCanvas)
            .put("L", this::drawLine)
            .put("R", this::drawRectangle)
            .put("B", this::fillBorder)
            .build();

    private void createCanvas(String[] args) {
        secureRun(() -> {
            int[] iArgs = toInt(args);
            canvasRef.set(new Canvas(iArgs[0], iArgs[1]));
        }, 2, "x1 y1", args);
    }

    private void drawLine(String[] args) {
        secureRun(() -> {
            int[] p = toInt(args);
            drawLine(canvasRef, p[0], p[1], p[2], p[3]);
        }, 4, "x1 y1 x2 y2", args);
    }

    private void drawRectangle(String[] args) {
        secureRun(() -> {
            int[] p = toInt(args);
            drawLine(canvasRef, p[0], p[3], p[2], p[3]);
            drawLine(canvasRef, p[0], p[1], p[2], p[1]);
            drawLine(canvasRef, p[0], p[1], p[0], p[3]);
            drawLine(canvasRef, p[2], p[1], p[2], p[3]);
        }, 4, "x1 y1 x2 y2", args);
    }

    private void fillBorder(String[] args) {
        secureRun(() -> {
            Preconditions.checkArgument(args.length == 3);
            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            char ch = args[2].charAt(0);
            fillBorder(canvasRef, canvasRef.get().get(x, y).ch, ch, x, y);
        }, 3, "x1 y1 char", args);
    }

    private void fillBorder(AtomicReference<Canvas> canvasRef, char refCh, char ch, int x, int y) {
        checkCanvas(canvasRef);
        if (!canvasRef.get().inBound(x, y)) return;
        if (refCh == canvasRef.get().get(x, y).ch) {
            canvasRef.get().put(x, y, ch);
            fillBorder(canvasRef, refCh, ch, x + 1, y);
            fillBorder(canvasRef, refCh, ch, x - 1, y);
            fillBorder(canvasRef, refCh, ch, x, y + 1);
            fillBorder(canvasRef, refCh, ch, x, y - 1);
        }
    }

    private void drawLine(AtomicReference<Canvas> canvasRef, int x1, int y1, int x2, int y2) {
        checkCanvas(canvasRef);
        if (x2 < x1) {
            drawLine(canvasRef, x2, y2, x1, y1);
        } else {
            double tan = x2 - x1 == 0 ? Double.NaN : ((double) y2 - y1) / (x2 - x1);
            int lastYTo = -1;
            for (int x = x1; x <= x2; x++) {
                int yFrom = y1;
                int yTo = y2;
                if (!Double.isNaN(tan)) {
                    yFrom = yTo = y1 + (int) (tan * (x - x1));
                    if (lastYTo != -1) {
                        yFrom = lastYTo + (int) Math.signum(tan);
                        yFrom = tan < 0 ? Math.max(yFrom, yTo) : Math.min(yFrom, yTo);
                    }
                    lastYTo = yTo;
                }
                for (int y = Math.min(yFrom, yTo); y <= Math.max(yFrom, yTo); y++) canvasRef.get().put(x, y, LINE_CHAR);
            }
        }
    }

    private void checkCanvas(AtomicReference<Canvas> canvasRef) {
        Preconditions.checkArgument(canvasRef.get() != null, "Create canvas first");
    }

    /**
     * @param inputStr
     * @return true - quit command receipt, false - continue processing
     */
    public boolean intepret(String inputStr) {
        if (inputStr == null) return true;
        Iterator<String> iter = Splitter.on(" ").trimResults().omitEmptyStrings().split(inputStr).iterator();
        String command = iter.next();
        if ("Q".equalsIgnoreCase(command)) return true;
        Consumer<String[]> commandWorker = commandMap.get(command.toUpperCase());
        Preconditions.checkArgument(commandWorker != null, "Not found command %s", command);
        commandWorker.accept(Helper.toStr(iter));
        return false;
    }

    public Canvas getCanvas() {
        return canvasRef.get();
    }


}
