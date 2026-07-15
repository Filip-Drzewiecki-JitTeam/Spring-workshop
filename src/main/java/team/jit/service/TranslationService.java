package team.jit.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

/**
 * TranslationService – thin wrapper around Spring's {@link MessageSource}.
 *
 * Why wrap MessageSource?
 *  • Centralises all fallback/error handling in one place.
 *  • Controllers stay clean — they never deal with NoSuchMessageException.
 *  • Makes it easy to add caching, metrics, or parameter substitution later.
 *
 * Spring Boot auto-wires the MessageSource bean defined in I18nConfig
 * (or the auto-configured one if you left I18nConfig empty).
 */
@Slf4j
@Service
@AllArgsConstructor
public class TranslationService {

    /** Spring's MessageSource — the central i18n contract. */
    private final MessageSource messageSource;

    // -----------------------------------------------------------------------
    // Public API
    // -----------------------------------------------------------------------

    /**
     * Translate a message key using the given locale.
     *
     * Workflow:
     *  1. Ask MessageSource to resolve the key for the locale.
     *  2. If the key doesn't exist, log an error and return the key itself
     *     so the caller always gets a non-null, human-readable string.
     *  3. If placeholders ({name}, {count}, …) were supplied, substitute them.
     *
     * @param key    message key, e.g. "greeting" or "employee.not_found"
     * @param params optional named parameters to inject into the message text
     * @param locale target locale — use {@link Locale#ENGLISH}, new Locale("it"), etc.
     * @return translated (and interpolated) message
     */
    public String translateMessage(String key, Map<String, String> params, Locale locale) {
        log.debug("Translating key='{}' for locale='{}'", key, locale);

        if (key == null || key.isBlank()) {
            log.warn("translateMessage called with blank key — returning empty string");
            return "";
        }

        try {
            /*
             * MessageSource.getMessage(String code, Object[] args, Locale locale)
             *
             *  code   – the key in messages.properties
             *  args   – positional parameters for MessageFormat patterns ({0}, {1}, …)
             *           We pass null here because we use named substitution instead.
             *  locale – determines which .properties file to use
             */
            String raw = messageSource.getMessage(key, null, locale);
            return replaceNamedParams(raw, params);

        } catch (NoSuchMessageException e) {
            /*
             * Thrown when useCodeAsDefaultMessage = false AND the key is absent.
             * Because we set useCodeAsDefaultMessage = true in I18nConfig we normally
             * never reach here, but we guard against it anyway for robustness.
             */
            log.error("No translation found for key='{}' in locale='{}'", key, locale);
            return key; // return the key itself as a last resort
        }
    }

    /**
     * Convenience overload — translates without any parameter substitution.
     */
    public String translateMessage(String key, Locale locale) {
        return translateMessage(key, null, locale);
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    /**
     * Replaces named placeholders in the message text.
     *
     * Convention: placeholders are written as {paramName} in the .properties file.
     * Example:
     *   messages.properties:  welcome=Hello, {name}!
     *   params map:           {"name" -> "Alice"}
     *   result:               "Hello, Alice!"
     *
     * Note: Spring's built-in MessageFormat uses positional {0} arguments.
     * Named placeholders like {name} are a deliberate teaching choice here —
     * they make property files more readable, at the cost of a tiny manual replace.
     */
    private String replaceNamedParams(String message, Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return message;
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return message;
    }
}

