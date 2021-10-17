package JavaBot;

import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;

public class WatsonTranslator implements Translator {
    LanguageTranslator languageTranslator;

    public WatsonTranslator(String apiKey, String url) {
        var authenticator = new IamAuthenticator(apiKey);
        languageTranslator = new LanguageTranslator("2018-05-01", authenticator);
        languageTranslator.setServiceUrl(url);
        var r = languageTranslator.listModels();
    }

    @Override
    public String translate(String word) {
        var translateOptions = new TranslateOptions.Builder()
                .addText(word)
                .modelId("en-es")
                .build();

        var result = languageTranslator
                .translate(translateOptions)
                .execute()
                .getResult();
        var translations = result.getTranslations();
        return null;
    }
}