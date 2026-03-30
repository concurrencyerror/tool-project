package com.horace.toolbackend.util;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class LocalEnvLoader {

    private LocalEnvLoader() {
    }

    public static void load() {
        for (Path candidate : resolveCandidates()) {
            if (Files.isRegularFile(candidate)) {
                loadFile(candidate);
                return;
            }
        }
    }

    private static Set<Path> resolveCandidates() {
        String userDir = System.getProperty("user.dir", ".");
        Path workingDir = Path.of(userDir).toAbsolutePath().normalize();
        Set<Path> candidates = new LinkedHashSet<>(List.of(
                workingDir.resolve(".env"),
                workingDir.resolve("config").resolve(".env"),
                workingDir.resolve("tool-backend").resolve(".env"),
                workingDir.resolve("tool-backend").resolve("config").resolve(".env")
        ));

        Path codeSourceDir = resolveCodeSourceDir();
        if (codeSourceDir != null) {
            candidates.add(codeSourceDir.resolve(".env"));
            candidates.add(codeSourceDir.resolve("config").resolve(".env"));
            Path parent = codeSourceDir.getParent();
            if (parent != null) {
                candidates.add(parent.resolve(".env"));
                candidates.add(parent.resolve("config").resolve(".env"));
            }
        }

        return candidates;
    }

    private static Path resolveCodeSourceDir() {
        try {
            Path location = Path.of(LocalEnvLoader.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI());
            return Files.isDirectory(location) ? location : location.getParent();
        } catch (URISyntaxException | NullPointerException e) {
            return null;
        }
    }

    private static void loadFile(Path envFile) {
        try {
            for (String rawLine : Files.readAllLines(envFile)) {
                String line = rawLine.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                if (line.startsWith("export ")) {
                    line = line.substring("export ".length()).trim();
                }
                int index = line.indexOf('=');
                if (index <= 0) {
                    continue;
                }

                String key = line.substring(0, index).trim();
                if (key.isEmpty()) {
                    continue;
                }

                if (System.getenv(key) != null || System.getProperty(key) != null) {
                    continue;
                }

                String value = normalizeValue(line.substring(index + 1).trim());
                System.setProperty(key, value);
            }
        } catch (Exception ignored) {
            // Missing or malformed local env files should not block application startup.
        }
    }

    private static String normalizeValue(String value) {
        if (value.length() >= 2) {
            char first = value.charAt(0);
            char last = value.charAt(value.length() - 1);
            if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                return value.substring(1, value.length() - 1);
            }
        }
        return value;
    }
}
