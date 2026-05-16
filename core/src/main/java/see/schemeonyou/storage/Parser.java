package see.schemeonyou.storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class Parser {
    private final String input;
    private int index;

    Parser(String input) { this.input = input; }

    Object parse() throws IOException {
        Object value = value();
        whitespace();
        if (index != input.length()) throw error("Unexpected trailing content");
        return value;
    }

    private Object value() throws IOException {
        whitespace();
        if (index >= input.length()) throw error("Unexpected end of JSON");
        char c = input.charAt(index);
        return switch (c) {
            case '{' -> object();
            case '[' -> array();
            case '"' -> string();
            case 't' -> literal("true", Boolean.TRUE);
            case 'f' -> literal("false", Boolean.FALSE);
            case 'n' -> literal("null", null);
            default -> number();
        };
    }

    private Map<String, Object> object() throws IOException {
        expect('{');
        Map<String, Object> result = new LinkedHashMap<>();
        whitespace();
        if (peek('}')) return result;
        do {
            whitespace();
            String key = string();
            if (result.containsKey(key)) throw error("Duplicate object key: " + key);
            whitespace();
            expect(':');
            result.put(key, value());
            whitespace();
        } while (peek(','));
        expect('}');
        return result;
    }

    private List<Object> array() throws IOException {
        expect('[');
        List<Object> result = new ArrayList<>();
        whitespace();
        if (peek(']')) return result;
        do {
            result.add(value());
            whitespace();
        } while (peek(','));
        expect(']');
        return result;
    }

    private String string() throws IOException {
        expect('"');
        StringBuilder out = new StringBuilder();
        while (index < input.length()) {
            char c = input.charAt(index++);
            if (c == '"') return out.toString();
            if (c < 0x20) throw error("Invalid control character in string");
            if (c != '\\') { out.append(c); continue; }
            if (index >= input.length()) throw error("Unterminated escape sequence");
            char esc = input.charAt(index++);
            switch (esc) {
                case '"' -> out.append('"');
                case '\\' -> out.append('\\');
                case '/' -> out.append('/');
                case 'b' -> out.append('\b');
                case 'f' -> out.append('\f');
                case 'n' -> out.append('\n');
                case 'r' -> out.append('\r');
                case 't' -> out.append('\t');
                case 'u' -> out.append(unicodeEscape());
                default -> throw error("Invalid escape sequence");
            }
        }
        throw error("Unterminated string");
    }

    private Number number() throws IOException {
        int start = index;
        if (peek('-')) {}
        integerDigits();
        if (peek('.')) digits();
        if (index < input.length() && (input.charAt(index) == 'e' || input.charAt(index) == 'E')) {
            index++;
            if (index < input.length() && (input.charAt(index) == '+' || input.charAt(index) == '-')) index++;
            digits();
        }
        try {
            double parsed = Double.parseDouble(input.substring(start, index));
            if (!Double.isFinite(parsed)) throw error("Invalid number");
            return parsed;
        } catch (NumberFormatException e) {
            throw error("Invalid number");
        }
    }

    private Object literal(String text, Object value) throws IOException {
        if (!input.startsWith(text, index)) throw error("Invalid literal");
        index += text.length();
        return value;
    }

    private void integerDigits() throws IOException {
        if (peek('0')) {
            if (index < input.length() && Character.isDigit(input.charAt(index))) throw error("Invalid number");
            return;
        }
        digits();
    }

    private void digits() throws IOException {
        int start = index;
        while (index < input.length() && Character.isDigit(input.charAt(index))) index++;
        if (index == start) throw error("Expected digit");
    }

    private boolean peek(char c) {
        if (index < input.length() && input.charAt(index) == c) { index++; return true; }
        return false;
    }

    private void expect(char c) throws IOException {
        if (!peek(c)) throw error("Expected '" + c + "'");
    }

    private String take(int length) throws IOException {
        if (index + length > input.length()) throw error("Unexpected end of escape sequence");
        String text = input.substring(index, index + length);
        index += length;
        return text;
    }

    private char unicodeEscape() throws IOException {
        String text = take(4);
        for (int i = 0; i < text.length(); i++) {
            if (Character.digit(text.charAt(i), 16) < 0) throw error("Invalid unicode escape");
        }
        return (char) Integer.parseInt(text, 16);
    }

    private void whitespace() {
        while (index < input.length()) {
            char c = input.charAt(index);
            if (c != ' ' && c != '\t' && c != '\n' && c != '\r') return;
            index++;
        }
    }

    private IOException error(String message) { return new IOException(message + " at offset " + index); }
}
