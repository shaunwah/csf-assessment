package vttp2023.batch3.csf.assessment.cnserver.utilities;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public final class Utilities {
    public static JsonObject returnNewsId(String newsId) {
        return Json.createObjectBuilder()
                .add("newsId", newsId)
                .build();
    }
}
