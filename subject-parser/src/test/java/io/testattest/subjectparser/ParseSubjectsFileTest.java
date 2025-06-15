package io.testattest.subjectparser;

import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParseSubjectsFileTest {
    @Test
    public void testSubjectsFileParsing() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("subjects");
        Objects.requireNonNull(is, "subjects file not found in resources");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;
                String input = trimmed;
                String expected = null;
                int hashIdx = trimmed.indexOf('#');
                if (hashIdx >= 0) {
                    input = trimmed.substring(0, hashIdx).trim();
                    expected = trimmed.substring(hashIdx + 1).trim();
                }
                if (expected != null && !expected.isEmpty()) {
                    String actual = ParseSubjects.parse(input);
                    assertEquals(expected, actual, "Mismatch at line " + lineNum + ": " + input);
                }
                lineNum++;
            }
        }
    }
}

