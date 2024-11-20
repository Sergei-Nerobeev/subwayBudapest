package hu.nero;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@DisplayName("Testing Line.class methods")
class LineTest {

    @Test
    void testAddStationToStationsList() {
        var subway = new Subway("Budapest");
        var yellowLine = new Line("Red", subway);
        var stationExpected = Station.builder().name("Oktogon").line(yellowLine).subway(subway).build();

        yellowLine.addStation(stationExpected);
        var stationActual = yellowLine.getStation("Oktogon");

        Assertions.assertEquals(stationExpected, stationActual);
    }

    @Test
    void testAddStation_DoesNotAddDuplicates() {
        var subway = new Subway("Budapest");
        var yellowLine = new Line("Red", subway);
        var stationExpected = Station.builder().name("Oktogon").line(yellowLine).subway(subway).build();

        yellowLine.addStation(stationExpected);

        Assertions.assertEquals(1, yellowLine.getStations().size());
        Assertions.assertTrue(yellowLine.getStations().contains(stationExpected));
    }

    @Test
    void testAddStation_HandlesNullPreviousStation() {
        var subway = new Subway("Budapest");
        var yellowLine = new Line("Red", subway);
        var station1 = Station.builder().name("Oktogon").line(yellowLine).subway(subway).build();
        station1.setPrevious(null);
        var station2 = Station.builder().name("Nyugati").line(yellowLine).subway(subway).build();

        yellowLine.addStation(station1);
        yellowLine.addStation(station2);

        Assertions.assertEquals(2, yellowLine.getStations().size());

    }
}