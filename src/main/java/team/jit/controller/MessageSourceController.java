package team.jit.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.bind.annotation.*;
import team.jit.service.TranslationService;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * MessageSourceController – demonstrates Spring's i18n / MessageSource mechanism.
 *
 * ┌──────────────────────────────────────────────────────────────────────────┐
 * │  HOW SPRING i18n WORKS (class-level summary for students)                │
 * │                                                                          │
 * │  1. MessageSource bean (see I18nConfig)                                  │
 * │     Loads *.properties bundles from the classpath:                       │
 * │       messages.properties       → English (default / fallback)           │
 * │       messages_it.properties    → Italian                                │
 * │       messages_pl.properties    → Polish                                 │
 * │                                                                          │
 * │  2. LocaleResolver bean (see I18nConfig)                                 │
 * │     Decides the active Locale for every HTTP request.                    │
 * │     We use AcceptHeaderLocaleResolver, which reads the standard          │
 * │     "Accept-Language" HTTP header — the correct, standards-based way.    │
 * │                                                                          │
 * │  REQUEST FLOW                                                            │
 * │    HTTP request (with Accept-Language: it)                               │
 * │      → AcceptHeaderLocaleResolver resolves Locale("it")                  │
 * │      → Controller reads locale via request.getLocale()                   │
 * │      → MessageSource picks messages_it.properties                        │
 * │      → Italian translation returned to client                            │
 * └──────────────────────────────────────────────────────────────────────────┘
 *
 * ENDPOINTS (all public — no JWT required):
 *
 *   GET /messages                                     → all keys in English (default)
 *   GET /messages  [Accept-Language: it]              → all keys in Italian
 *   GET /messages  [Accept-Language: pl]              → all keys in Polish
 *   GET /messages/{key}                               → single key, language from header
 *   GET /messages/compare/{key}                       → one key in all three languages
 */
@AllArgsConstructor
@RestController
@RequestMapping("/messages")
public class MessageSourceController {

    /*
     * Spring auto-wires the MessageSource bean defined in I18nConfig.
     * We also inject TranslationService to show the "service layer" pattern.
     * Both are used in different endpoints so students can see both approaches.
     */
    private final MessageSource messageSource;
    private final TranslationService translationService;

    // All message keys that exist in the bundles.
    private static final String[] ALL_KEYS = {
            "greeting", "farewell", "thank_you",
            "employee.welcome", "employee.not_found",
            "employee.created", "employee.deleted",
            "app.name", "app.description", "app.language"
    };

    // -----------------------------------------------------------------------
    // Endpoint 1 – translate ALL keys for the resolved locale
    // -----------------------------------------------------------------------

    /**
     * Returns every message key translated into the language requested via
     * the standard HTTP Accept-Language header.
     *
     * AcceptHeaderLocaleResolver (configured in I18nConfig) reads the header
     * and sets the request locale before this method is called — so
     * request.getLocale() already returns the correct Locale.
     *
     * Example curl calls:
     *   curl http://localhost:8080/messages
     *   curl -H "Accept-Language: it" http://localhost:8080/messages
     *   curl -H "Accept-Language: pl" http://localhost:8080/messages
     *
     * @param request HttpServletRequest — locale already resolved by Spring
     */
    @GetMapping
    public Map<String, String> getAllMessages(HttpServletRequest request) {
        /*
         * request.getLocale() returns the locale resolved by AcceptHeaderLocaleResolver.
         *
         * KEY POINT for students:
         *   Do NOT hard-code new Locale("it") in a real application.
         *   Always read the locale from the request — the same endpoint then
         *   serves all languages without any if/else branching.
         */
        Locale locale = request.getLocale();

        Map<String, String> translations = new LinkedHashMap<>();
        translations.put("_locale", locale.toLanguageTag()); // show which locale was picked

        for (String key : ALL_KEYS) {
            /*
             * MessageSource.getMessage(String code, Object[] args, Locale locale)
             *
             *  code   – message key from the .properties file
             *  args   – positional arguments for MessageFormat patterns; null = none
             *  locale – the locale resolved from Accept-Language header
             */
            translations.put(key, messageSource.getMessage(key, null, locale));
        }

        return translations;
    }

    // -----------------------------------------------------------------------
    // Endpoint 2 – translate a SINGLE key (uses TranslationService)
    // -----------------------------------------------------------------------

    /**
     * Translates a single message key using the Accept-Language header locale.
     *
     * This endpoint intentionally uses {@link TranslationService} rather than
     * calling MessageSource directly — it shows students the service-layer pattern
     * and how to encapsulate MessageSource behind a service.
     *
     * Example curl calls:
     *   curl http://localhost:8080/messages/greeting
     *   curl -H "Accept-Language: pl" http://localhost:8080/messages/employee.not_found
     *   curl -H "Accept-Language: it" http://localhost:8080/messages/app.description
     *
     * @param key     the message key (path variable)
     * @param request used to obtain the locale from the Accept-Language header
     */
    @GetMapping("/{key}")
    public Map<String, String> getSingleMessage(
            @PathVariable String key,
            HttpServletRequest request) {

        Locale locale = request.getLocale();

        /*
         * TranslationService.translateMessage wraps MessageSource and adds:
         *  - null/blank key guard
         *  - NoSuchMessageException handling (returns key as fallback)
         *  - optional named-parameter substitution ({name}, {count}, …)
         */
        String translated = translationService.translateMessage(key, locale);

        return Map.of(
                "key",        key,
                "locale",     locale.toLanguageTag(),
                "translated", translated
        );
    }

    // -----------------------------------------------------------------------
    // Endpoint 3 – compare ALL THREE languages for one key side-by-side
    // -----------------------------------------------------------------------

    /**
     * Returns the translation of a single key in all three languages at once.
     *
     * This is a teaching endpoint — it explicitly builds three Locale objects
     * and calls MessageSource three times, making the locale → bundle mapping
     * clearly visible to students.
     *
     * Example curl calls:
     *   curl http://localhost:8080/messages/compare/greeting
     *   curl http://localhost:8080/messages/compare/app.description
     *   curl http://localhost:8080/messages/compare/employee.not_found
     *
     * @param key the message key to compare across all locales
     */
    @GetMapping("/compare/{key}")
    public Map<String, String> compareAllLanguages(@PathVariable String key) {

        /*
         * Locale objects correspond directly to .properties file suffixes:
         *
         *   Locale.ENGLISH   ("en") → messages.properties
         *   Locale.ITALIAN   ("it") → messages_it.properties
         *   new Locale("pl") ("pl") → messages_pl.properties
         */
        Locale english = Locale.ENGLISH;
        Locale italian = Locale.ITALIAN;
        Locale polish  = new Locale("pl");

        Map<String, String> comparison = new LinkedHashMap<>();
        comparison.put("key",     key);
        comparison.put("english", resolveOrFallback(key, english));
        comparison.put("italian", resolveOrFallback(key, italian));
        comparison.put("polish",  resolveOrFallback(key, polish));
        return comparison;
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    /**
     * Calls MessageSource and returns a human-friendly fallback on error.
     *
     * Even though we set useCodeAsDefaultMessage=true in I18nConfig, this helper
     * makes the fallback logic explicit and visible to students reading the code.
     */
    private String resolveOrFallback(String key, Locale locale) {
        try {
            return messageSource.getMessage(key, null, locale);
        } catch (NoSuchMessageException e) {
            return "[no translation for key: " + key + "]";
        }
    }
}
