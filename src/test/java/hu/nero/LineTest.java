package hu.nero;


import hu.nero.exception.StationNameException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DisplayName("Testing Line.class methods")
class LineTest {

    @Test
    void testAddStationToStationsList() {
        var subway = new Subway("Budapest");
        var yellowLine = new Line("Red", subway);
        List<Station> transferStations = new ArrayList<>();
        var stationExpected = new Station("Oktogon", yellowLine, transferStations, subway);
        var stationActual = new Station("Oktogon", yellowLine, transferStations, subway);

        yellowLine.addStation(stationExpected);

        Assertions.assertEquals(stationExpected, stationActual);
    }

    @Test
    void testAddStation_DoesNotAddDuplicates() {
        var subway = new Subway("Budapest");
        var yellowLine = new Line("Red", subway);
        List<Station> transferStations = new ArrayList<>();
        var stationExpected = new Station("Oktogon", yellowLine, transferStations, subway);
        var stationActual = new Station("Oktogon", yellowLine, transferStations, subway);

        yellowLine.addStation(stationExpected);

        StationNameException exception = Assertions.assertThrows(StationNameException.class, () -> {
            yellowLine.addStation(stationActual);
        });
        Assertions.assertEquals("The station already exists on the list", exception.getMessage());
        Assertions.assertEquals(1, yellowLine.getStations().size());
        Assertions.assertTrue(yellowLine.getStations().contains(stationExpected));
    }

    @Test
    void testAddStation_HandlesNullPreviousStation() {
        var subway = new Subway("Budapest");
        var yellowLine = new Line("Red", subway);
        List<Station> transferStations = new ArrayList<>();
        var stationExpected = new Station("Oktogon", yellowLine, transferStations, subway);
        stationExpected.setPrevious(null);
        var stationActual = new Station("Nyugati", yellowLine, transferStations, subway);

        yellowLine.addStation(stationExpected);
        yellowLine.addStation(stationActual);

        Assertions.assertEquals(2, yellowLine.getStations().size());


    }
}