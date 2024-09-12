package se.martin.weather.json;

import se.martin.weather.model.Visibility;

import static se.martin.weather.model.Visibility.*;

/**
 * Simple class to map between Json values and Visibility values
 */
class VisibilityMapper {

    final static String NODE_ID = "visibility";

    static String toJsonValue(Visibility visibility) {
        return switch (visibility) {
            case Good -> "good";
            case Average -> "average";
            case Poor -> "poor";
            case Darkness -> "total utter darkness";
        };
    }

    static Visibility fromJsonValue(String jsonValue) {
        return switch (jsonValue) {
            case "good" -> Good;
            case "average" -> Average;
            case "poor" -> Poor;
            case "total utter darkness" -> Darkness;
            default -> null; // This case should not occur if schema validation enforced when writing Json
        };
    }
}
