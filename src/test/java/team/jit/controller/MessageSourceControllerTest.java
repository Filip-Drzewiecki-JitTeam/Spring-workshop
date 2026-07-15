package team.jit.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link MessageSourceController}.
 *
 * Key concepts demonstrated:
 *  - /messages endpoint is PUBLIC — no JWT / Authorization header needed
 *  - Language is selected via the standard "Accept-Language" HTTP header
 *  - When the header is absent, English is returned (default locale)
 *  - The /compare/{key} endpoint always returns all three languages at once
 */
@SpringBootTest
@AutoConfigureMockMvc
class MessageSourceControllerTest {

    @Autowired
    MockMvc mvc;

    // =========================================================================
    // GET /messages  — all keys
    // =========================================================================

    @Nested
    @DisplayName("GET /messages — all keys")
    class GetAllMessages {

        @Test
        @DisplayName("No Accept-Language header → English (default)")
        void noHeader_returnsEnglish() throws Exception {
            mvc.perform(get("/messages"))
                    .andExpect(status().isOk())
                    // _locale field shows which bundle was resolved
                    .andExpect(jsonPath("$._locale").value("en"))
                    .andExpect(jsonPath("$.greeting").value("Hello, World!"))
                    .andExpect(jsonPath("$.farewell").value("Goodbye!"))
                    .andExpect(jsonPath("$.thank_you").value("Thank you!"))
                    .andExpect(jsonPath("$['app.language']").value("Language: English"));
        }

        @Test
        @DisplayName("Accept-Language: it → Italian translations")
        void italianHeader_returnsItalian() throws Exception {
            mvc.perform(get("/messages")
                    .header("Accept-Language", "it"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$._locale").value("it"))
                    .andExpect(jsonPath("$.greeting").value("Ciao, Mondo!"))
                    .andExpect(jsonPath("$.farewell").value("Arrivederci!"))
                    .andExpect(jsonPath("$.thank_you").value("Grazie!"))
                    .andExpect(jsonPath("$['app.language']").value("Lingua: Italiano"));
        }

        @Test
        @DisplayName("Accept-Language: pl → Polish translations")
        void polishHeader_returnsPolish() throws Exception {
            mvc.perform(get("/messages")
                    .header("Accept-Language", "pl"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$._locale").value("pl"))
                    .andExpect(jsonPath("$.greeting").value("Witaj, świecie!"))
                    .andExpect(jsonPath("$.farewell").value("Do widzenia!"))
                    .andExpect(jsonPath("$.thank_you").value("Dziękuję!"))
                    .andExpect(jsonPath("$['app.language']").value("Język: Polski"));
        }

        @Test
        @DisplayName("Unsupported locale (e.g. fr) → falls back to English")
        void unsupportedLocale_fallsBackToEnglish() throws Exception {
            mvc.perform(get("/messages")
                    .header("Accept-Language", "fr"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.greeting").value("Hello, World!"));
        }

        @Test
        @DisplayName("No JWT required — endpoint is public")
        void noJwt_returns200() throws Exception {
            // This would return 401 on a secured endpoint.
            // Verifying /messages is accessible without any Authorization header.
            mvc.perform(get("/messages"))
                    .andExpect(status().isOk());
        }
    }

    // =========================================================================
    // GET /messages/{key}  — single key via TranslationService
    // =========================================================================

    @Nested
    @DisplayName("GET /messages/{key} — single key")
    class GetSingleMessage {

        @Test
        @DisplayName("English greeting — returns key, locale and translated value")
        void englishGreeting_returnsExpectedJson() throws Exception {
            mvc.perform(get("/messages/greeting"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.key").value("greeting"))
                    .andExpect(jsonPath("$.locale").value("en"))
                    .andExpect(jsonPath("$.translated").value("Hello, World!"));
        }

        @Test
        @DisplayName("Accept-Language: it — Italian single key")
        void italianSingleKey_returnsItalianValue() throws Exception {
            mvc.perform(get("/messages/farewell")
                    .header("Accept-Language", "it"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.key").value("farewell"))
                    .andExpect(jsonPath("$.locale").value("it"))
                    .andExpect(jsonPath("$.translated").value("Arrivederci!"));
        }

        @Test
        @DisplayName("Accept-Language: pl — Polish single key")
        void polishSingleKey_returnsPolishValue() throws Exception {
            mvc.perform(get("/messages/thank_you")
                    .header("Accept-Language", "pl"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.key").value("thank_you"))
                    .andExpect(jsonPath("$.locale").value("pl"))
                    .andExpect(jsonPath("$.translated").value("Dziękuję!"));
        }

        @Test
        @DisplayName("Unknown key — returns the key itself as fallback (useCodeAsDefaultMessage=true)")
        void unknownKey_returnsKeyAsFallback() throws Exception {
            mvc.perform(get("/messages/this.key.does.not.exist"))
                    .andExpect(status().isOk())
                    // Because useCodeAsDefaultMessage=true in I18nConfig,
                    // MessageSource returns the key string itself instead of throwing.
                    .andExpect(jsonPath("$.translated").value("this.key.does.not.exist"));
        }

        @Test
        @DisplayName("Employee message keys are resolved correctly in all languages")
        void employeeKeys_allLanguages() throws Exception {
            // English
            mvc.perform(get("/messages/employee.not_found"))
                    .andExpect(jsonPath("$.translated").value("Employee not found."));

            // Italian
            mvc.perform(get("/messages/employee.not_found")
                    .header("Accept-Language", "it"))
                    .andExpect(jsonPath("$.translated").value("Dipendente non trovato."));

            // Polish
            mvc.perform(get("/messages/employee.not_found")
                    .header("Accept-Language", "pl"))
                    .andExpect(jsonPath("$.translated").value("Pracownik nie został znaleziony."));
        }
    }

    // =========================================================================
    // GET /messages/compare/{key}  — all three languages side-by-side
    // =========================================================================

    @Nested
    @DisplayName("GET /messages/compare/{key} — all three languages")
    class CompareAllLanguages {

        @Test
        @DisplayName("compare/greeting — returns key + three translations in one response")
        void compareGreeting_returnsAllThreeLanguages() throws Exception {
            mvc.perform(get("/messages/compare/greeting"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.key").value("greeting"))
                    .andExpect(jsonPath("$.english").value("Hello, World!"))
                    .andExpect(jsonPath("$.italian").value("Ciao, Mondo!"))
                    .andExpect(jsonPath("$.polish").value("Witaj, świecie!"));
        }

        @Test
        @DisplayName("compare/app.language — language label differs per bundle")
        void compareAppLanguage_showsDifferentLabels() throws Exception {
            mvc.perform(get("/messages/compare/app.language"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.english").value("Language: English"))
                    .andExpect(jsonPath("$.italian").value("Lingua: Italiano"))
                    .andExpect(jsonPath("$.polish").value("Język: Polski"));
        }

        @Test
        @DisplayName("compare/{unknownKey} — all three values are the key itself (fallback)")
        void compareUnknownKey_allFallbackToKey() throws Exception {
            mvc.perform(get("/messages/compare/no.such.key"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.key").value("no.such.key"))
                    .andExpect(jsonPath("$.english").value("no.such.key"))
                    .andExpect(jsonPath("$.italian").value("no.such.key"))
                    .andExpect(jsonPath("$.polish").value("no.such.key"));
        }

        @Test
        @DisplayName("Accept-Language header is irrelevant — /compare always returns all three")
        void compareIgnoresAcceptLanguageHeader() throws Exception {
            // Even if the caller sends Accept-Language: it, the compare endpoint
            // always resolves all three locales explicitly, ignoring the header.
            mvc.perform(get("/messages/compare/farewell")
                    .header("Accept-Language", "it"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.english").value("Goodbye!"))
                    .andExpect(jsonPath("$.italian").value("Arrivederci!"))
                    .andExpect(jsonPath("$.polish").value("Do widzenia!"));
        }
    }
}

