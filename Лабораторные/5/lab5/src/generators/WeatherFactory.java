package generators;

import ontology.Weather;

import java.util.Random;

public enum WeatherFactory {
    ABSTRACT_WEATHER {
        @Override
        public Weather makeWeather() {
            final int temperature = RANDOM.nextInt(81) - 30; //-30-50
            final int humidity = RANDOM.nextInt(101); //0-100
            final int pressure = RANDOM.nextInt(21) + 750; //750-770

            return new Weather(
                    temperature,
                    humidity,
                    pressure,
                    getDescription(temperature)
            );
        }
    };

    private static final Random RANDOM = new Random();
    private static final int VERY_HOT_TEMP = 33;
    private static final int HOT_TEMP = 28;
    private static final int WARM_TEMP = 23;
    private static final int MILD_TEMP = 18;
    private static final int COOL_TEMP = 13;
    private static final int COLD_TEMP = 8;

    @SuppressWarnings({"IfMayBeConditional", "IfStatementWithTooManyBranches"})
    private static String getDescription(final int temperature) {
        if (temperature >= VERY_HOT_TEMP) {
            return "Very hot";
        } else if (temperature >= HOT_TEMP) {
            return "Hot";
        } else if (temperature >= WARM_TEMP) {
            return "Warm";
        } else if (temperature >= MILD_TEMP) {
            return "Mild";
        } else if (temperature >= COOL_TEMP) {
            return "Cool";
        } else if (temperature >= COLD_TEMP) {
            return "Cold";
        } else {
            return "Very cold";
        }
    }

    public abstract Weather makeWeather();
}
