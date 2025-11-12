package de.bogenliga.application.common.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public final class LigaSlugUtil {
    private LigaSlugUtil() {}

    private static final Pattern SLUG = Pattern.compile("^[a-z0-9]++(?:-[a-z0-9]++)*$");

    public static String toSlug(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "liga";
        }
        String umlaut = name
                .replace("Ä", "Ae").replace("ä", "ae")
                .replace("Ö", "Oe").replace("ö", "oe")
                .replace("Ü", "Ue").replace("ü", "ue")
                .replace("ß", "ss");
        String normalized = Normalizer.normalize(umlaut, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String lower = normalized.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("((?:^-+)|(?:-+$))", "");
        return lower.isEmpty() ? "liga" : lower;
    }

    public static boolean isValid(String slug) {
        return slug != null && SLUG.matcher(slug).matches();
    }
}