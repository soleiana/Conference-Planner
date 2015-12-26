package com.conferenceplanner.rest;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TestHelper {

    public String getTooLongNameString(int maxSymbolsInName) {
        return Stream.iterate(0, i -> i++)
                .limit(maxSymbolsInName + 1)
                .map(i -> "a")
                .collect(Collectors.joining());
    }

    public String getValidNameString(int symbolsInName) {
        String name = Stream.iterate(0, i -> i++)
                .limit(symbolsInName)
                .map(i -> "a")
                .collect(Collectors.joining());
        return " ".concat(name).concat(" ");
    }
}
