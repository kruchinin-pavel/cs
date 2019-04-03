package org.kpa.cs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Starter {
    private static final Logger log = LoggerFactory.getLogger(Starter.class);

    public static void main(String[] args) {
        try {
            Interpreter interpreter = new Interpreter();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                try {
                    System.out.print("\nenter command: ");
                    if (interpreter.intepret(reader.readLine())) break;
                    if (interpreter.getCanvas() != null) interpreter.getCanvas().toStream(System.out);
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Unprocessed error happened(exit(1)): {}", e.getMessage(), e);
            System.exit(1);
        }
    }
}
