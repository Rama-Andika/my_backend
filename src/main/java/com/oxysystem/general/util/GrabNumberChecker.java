package com.oxysystem.general.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrabNumberChecker {
    private static final Pattern PATTERN = Pattern.compile("^(\\d+)-([A-Z0-9]+)(?:-(\\d+))?$");

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ParsedId {
        private final String prefixNumeric;
        private final String code;
        private final Integer suffixNumber; // nullable
    }

    public static Optional<ParsedId> parse(String s) {
        if (s == null) return Optional.empty();

        String t = s.trim();
        Matcher m = PATTERN.matcher(t);
        if (!m.matches()) return Optional.empty();

        String numericPrefix = m.group(1);
        String code = m.group(2);
        String suffix = m.group(3); // may be null

        ParsedId p = new ParsedId(numericPrefix, code, suffix == null ? null : Integer.valueOf(suffix));
        return Optional.of(p);
    }

    public static boolean isValid(String s) {
        return parse(s).isPresent();
    }

    public static boolean isOrderSplit(String s){
        if(isValid(s)){
            ParsedId parsedId = parse(s).get();
            return parsedId.getSuffixNumber() != null;
        }

        return false;
    }
}
