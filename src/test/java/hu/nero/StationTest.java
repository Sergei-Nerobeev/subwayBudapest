package hu.nero;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static hu.nero.Util.createDataForTestSubway;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing Station class methods")
class StationTest {

    @Test
    @DisplayName("Продажа билета - позитивный сценарий - корректные тестовые данные на одной линии")
    void sellTicketTest_Success() {
        var subway = createDataForTestSubway("Budapest");
        var intervalDataTest = 2;
        var redLineDataTest = subway.getLine("Red");
        var stationDataTest = redLineDataTest.getStation("Astoria");

        var start = redLineDataTest.getStation("Astoria");
        var finish = redLineDataTest.getStation("Arena");

        var expected = "Ticket: " + start.getName() + " " +
                finish.getName() + " " + "interval: " + intervalDataTest;
        var actual = stationDataTest.sellTicket(start, finish);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Продажа билета - позитивный сценарий - корректные тестовые данные на разных линиях")
    void sellTicketTestOtherLines_Success() {
        var subway = createDataForTestSubway("Budapest");
        var intervalDataTest = 4;
        var redLineDataTest = subway.getLine("Red");
        var yellowLineDataTest = subway.getLine("Yellow");
        var stationDataTest = redLineDataTest.getStation("Astoria");

        var start = redLineDataTest.getStation("Astoria");
        var finish = yellowLineDataTest.getStation("Opera");

        var expected = "Ticket: " + start.getName() + " " +
                finish.getName() + " " + "interval: " + intervalDataTest;
        var actual = stationDataTest.sellTicket(start, finish);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Продажа билета - негативный сценарий - начальная станция null")
    void sellTicketTest_Null() {
        var subway = createDataForTestSubway("Budapest");
        var redLineDataTest = subway.getLine("Red");
        var stationDataTest = redLineDataTest.getStation("Astoria");
        var startNull = redLineDataTest.getStation(null);
        var finish = redLineDataTest.getStation("Arena");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    stationDataTest.sellTicket(startNull, finish);
                });

        assertEquals("Stations cannot be null.", exception.getMessage());
    }

    @Test
    @DisplayName("Продажа билета - негативный сценарий - названия станций одинаковые")
    void sellTicketTest_SameStation() {
        var subway = createDataForTestSubway("Budapest");
        var redLineDataTest = subway.getLine("Red");
        var stationDataTest = redLineDataTest.getStation("Astoria");
        var start = redLineDataTest.getStation("Astoria");
        var finish = redLineDataTest.getStation("Astoria");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    stationDataTest.sellTicket(start, finish);
                });

        assertEquals("Start station cannot be the same as finish station.", exception.getMessage());
    }

}
