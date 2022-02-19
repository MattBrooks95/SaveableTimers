package brooks.SaveableTimers

import java.util.*

enum class Language(val value: String) {
    JAPANESE_LANGUAGE("ja"),
    ENGLISH_LANGUAGE("en"),
};

class AppState {
    fun languageToJapanese() {
        currentLanguage = Language.JAPANESE_LANGUAGE
    }

    fun languageToEnglish() {
        currentLanguage = Language.ENGLISH_LANGUAGE
    }

    fun currentLanguage(): Language {
        return currentLanguage
    }

    companion object {
        @Volatile private var instance: AppState? = null
        //TODO persist this setting, and set the locale when the app first loads
        @Volatile private var currentLanguage: Language = Language.ENGLISH_LANGUAGE;
        fun getInstance(): AppState {
            return instance?: AppState()
        }

    }
}