package team.jit.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

/**
 * Internationalisation (i18n) configuration.
 *
 * Two beans are registered here:
 *
 *  1. MessageSource  – loads translation bundles from the classpath.
 *                      Spring looks for:
 *                          messages.properties          (English fallback)
 *                          messages_it.properties       (Italian)
 *                          messages_pl.properties       (Polish)
 *
 *  2. LocaleResolver – decides WHICH locale to use for an incoming request.
 *                      We use AcceptHeaderLocaleResolver, which reads the
 *                      standard HTTP "Accept-Language" header.
 *
 * HOW TO SELECT A LANGUAGE IN YOUR HTTP CLIENT:
 *   Add the standard Accept-Language header to your request:
 *
 *     Accept-Language: en   →  English  (also the default when header is absent)
 *     Accept-Language: it   →  Italian
 *     Accept-Language: pl   →  Polish
 *
 * NOTE: Spring Boot auto-configures a basic MessageSource for free when it
 *       finds messages.properties on the classpath. We declare the beans
 *       explicitly here for educational purposes — so students can see and
 *       tweak every setting.
 */
@Configuration
public class I18nConfig implements WebMvcConfigurer {

    /**
     * Primary MessageSource bean.
     *
     * ResourceBundleMessageSource delegates to Java's ResourceBundle mechanism,
     * which maps a locale to a .properties file on the classpath:
     *
     *   Locale.ENGLISH / Locale.ROOT → messages.properties
     *   new Locale("it")             → messages_it.properties
     *   new Locale("pl")             → messages_pl.properties
     *
     * defaultEncoding = UTF-8 ensures Polish characters (ą, ę, …) are read correctly.
     * useCodeAsDefaultMessage = true means an unknown key is returned as-is instead of
     * throwing NoSuchMessageException — handy during development.
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("messages");          // classpath:messages*.properties
        source.setDefaultEncoding("UTF-8");
        source.setUseCodeAsDefaultMessage(true); // return key itself when translation missing
        return source;
    }

    /**
     * LocaleResolver – picks the locale for every HTTP request.
     *
     * AcceptHeaderLocaleResolver reads the standard HTTP "Accept-Language" header.
     * We restrict supported locales to EN, IT and PL; anything else falls back
     * to English so we never end up with an untranslated bundle.
     *
     * This is the correct, standards-compliant approach — no custom query
     * parameters, no session cookies, just plain HTTP.
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setSupportedLocales(List.of(
                Locale.ENGLISH,      // en
                Locale.ITALIAN,      // it
                new Locale("pl")     // pl
        ));
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }
}
