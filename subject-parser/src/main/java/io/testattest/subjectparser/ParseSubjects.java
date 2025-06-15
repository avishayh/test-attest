package io.testattest.subjectparser;

import org.apache.commons.lang3.StringUtils;

public class ParseSubjects {
    public static String parse(String line) {
        if (!isParsable(line)) return "cannot parse";
        String cleanLine = line.trim();
        String repoKey = parseRepoKey(cleanLine);
        String packageName = parsePackageName(cleanLine);
        String version = parseVersion(cleanLine);
        if (StringUtils.isAnyEmpty(repoKey,packageName)) return "cannot parse";
        //repo key and package name should be a meaningful string with at least one a-z character
        if (!repoKey.matches(".*[a-zA-Z].*") || !packageName.matches(".*[a-zA-Z].*")) {
            return "cannot parse";
        }

        return String.format("repoKey=%s packageName=%s version=%s", repoKey, packageName, version);
    }

    private static boolean isParsable(String line) {
        if (line == null) return false;
        line = line.trim();
        if (line.isEmpty() || line.startsWith("#")) return false;
        if (!line.contains("/")) return false;
        String[] segments = line.split("[?]")[0].split("/");
        return segments.length >= 2 && !segments[segments.length - 2].isEmpty() && !segments[segments.length - 1].isEmpty();
    }

    private static String parseRepoKey(String line) {
        String mainPart = line.split("[?]")[0];
        String[] segments = mainPart.split("/");
        if (segments.length < 2) return null;
        // If there are only 2 segments, repoKey is the first segment
        if (segments.length == 2) {
            return segments[0];
        }
        // Otherwise, repoKey is always the second segment (index 1)
        return segments[1];
    }

    private static String parsePackageName(String line) {
        String mainPart = line;
        int qIdx = mainPart.indexOf('?');
        if (qIdx >= 0) {
            mainPart = mainPart.substring(0, qIdx);
        }
        String[] segments = mainPart.split("/");
        if (segments.length < 2) return null;
        int repoKeyIdx = (segments.length == 2) ? 0 : 1;
        int startIdx = repoKeyIdx + 1;
        if (startIdx >= segments.length) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = startIdx; i < segments.length; i++) {
            if (sb.length() > 0) sb.append("/");
            sb.append(segments[i]);
        }
        return sb.toString();
    }

    private static String parseVersion(String line) {
        int qIdx = line.indexOf('?');
        if (qIdx >= 0) {
            String v = line.substring(qIdx + 1).split("[ \t]")[0];
            return v.isEmpty() ? "null" : v;
        }
        return "null";
    }
}
