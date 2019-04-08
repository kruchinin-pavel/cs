package org.kpa.cs;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
            int[] p = toInt(args);
            canvasRef.set(new Canvas(p[0], p[1]));
        }, 2, "create canvas", "x1 y1", args);
    }

    private void drawLine(String[] args) {
        secureRun(() -> {
            int[] p = toInt(args);
            drawLine(p[0], p[1], p[2], p[3]);
        }, 4, "draw line", "x1 y1 x2 y2", args);
    }

    private void drawRectangle(String[] args) {
        secureRun(() -> {
            int[] p = toInt(args);
            drawLine(p[0], p[3], p[2], p[3]);
            drawLine(p[0], p[1], p[2], p[1]);
            drawLine(p[0], p[1], p[0], p[3]);
            drawLine(p[2], p[1], p[2], p[3]);
        }, 4, "draw rectangle", "x1 y1 x2 y2", args);
    }

    private void fillBorder(String[] args) {
        secureRun(() -> {
            Preconditions.checkArgument(args.length == 3);
            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            char ch = args[2].charAt(0);
            List<Runnable> backlog = new ArrayList<>();
            fillBorder(canvasRef.get().get(x, y).ch, ch, x, y, backlog);
            while (backlog.size() > 0) {
                List<Runnable> currentTasks = new ArrayList<>(backlog);
                backlog.clear();
                currentTasks.forEach(Runnable::run);
            }
        }, 3, "fill border", "x1 y1 char", args);
    }

    private void fillBorder(char refCh, char ch, int x, int y, List<Runnable> tasks) {
        validateCanvas();
        if (!canvasRef.get().isInBound(x, y)) return;
        if (refCh == canvasRef.get().get(x, y).ch) {
            canvasRef.get().put(x, y, ch);
            tasks.add(() -> fillBorder(refCh, ch, x + 1, y, tasks));
            tasks.add(() -> fillBorder(refCh, ch, x - 1, y, tasks));
            tasks.add(() -> fillBorder(refCh, ch, x, y + 1, tasks));
            tasks.add(() -> fillBorder(refCh, ch, x, y - 1, tasks));
        }
    }

    private void drawLine(int x1, int y1, int x2, int y2) {
        validateCanvas();
        if (x2 < x1) {
            drawLine(x2, y2, x1, y1);
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

    private void validateCanvas() {
        Preconditions.checkArgument(canvasRef.get() != null, "Create canvas first");
    }

    /**
     * @param inputStr
     * @return true - quit command receipt, false - continue processing
     */
    public boolean intepret(String inputStr) {
        if (Strings.isNullOrEmpty(inputStr)) return true;
        Iterator<String> iter = Splitter.on(" ").trimResults().omitEmptyStrings().split(inputStr).iterator();
        String command = iter.next();
        if ("Q".equalsIgnoreCase(command)) return true;
        Consumer<String[]> commandWorker = commandMap.get(command.toUpperCase());
        Preconditions.checkArgument(commandWorker != null,
                "Not found command: %s. Commands are: %s. Q - exit.", command, commandMap.keySet());
        commandWorker.accept(Helper.toStr(iter));
        return false;
    }

    public Canvas getCanvas() {
        return canvasRef.get();
    }


}
