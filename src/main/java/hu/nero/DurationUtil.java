package hu.nero;

import java.time.Duration;
/*
*   Парсинг времени
* */
public class DurationUtil {
    public static Duration parseTimeToStation(String timeToStationText){
        return Duration.parse("PT" + timeToStationText);
    }
}
