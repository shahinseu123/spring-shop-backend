package com.shop.shop.utils;
import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;
public class SlugUtils {
    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern EDGESEPARATOR = Pattern.compile("(^-|-$)");
    private static final Pattern MULTIPLE_SEPARATOR = Pattern.compile("-{2,}");

    /**
     * Converts a string to a URL-friendly slug
     * @param input The string to convert
     * @return The slugified string
     */
    public static String toSlug(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        // Convert to lowercase and trim
        String slug = input.toLowerCase(Locale.ENGLISH).trim();

        // Remove accents and normalize
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = slug.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        // Replace & with 'and'
        slug = slug.replace("&", "-and-");

        // Replace whitespace with hyphens
        slug = WHITESPACE.matcher(slug).replaceAll("-");

        // Remove all non-word characters (except hyphens)
        slug = NON_LATIN.matcher(slug).replaceAll("");

        // Replace multiple hyphens with single hyphen
        slug = MULTIPLE_SEPARATOR.matcher(slug).replaceAll("-");

        // Remove hyphens from beginning and end
        slug = EDGESEPARATOR.matcher(slug).replaceAll("");

        return slug;
    }

    @FunctionalInterface
    public interface SlugExistsChecker {
        boolean exists(String slug);
    }
}
