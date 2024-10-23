package hu.nero;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static hu.nero.TestDataProvider.createDataForTestSubway;
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

    @Test
    @DisplayName("Продажа проездного билета - возвращает Not Null - позитивный сценарий")
    void sellMonthlyTicketTest_NotNull() {
        var budapest = createDataForTestSubway("Budapest");
        var stationTest = budapest.getLine("Red").getStation("Astoria");

        var actualMonthlyTicket = stationTest.sellMonthlyTicket();

        assertNotNull(actualMonthlyTicket);
    }

    @Test
    @DisplayName("Продажа проездного билета - возвращает билет - позитивный сценарий")
    void sellMonthlyTicketTest_Success() {
        var budapest = createDataForTestSubway("Budapest");
        var testDate = LocalDate.now();
        var expectedMonthlyTicket = new MonthlyTicket("a0000", testDate);
        var stationTest = budapest.getLine("Red").getStation("Astoria");

        var actualMonthlyTicket = stationTest.sellMonthlyTicket();

        assertEquals(expectedMonthlyTicket, actualMonthlyTicket);
    }

    @Test
    @DisplayName("Продление проездного билета - возвращает билет плюс 30 дней - корректная дата,позитивный сценарий")
    void extendMonthlyTicketTest_Success() {
        var budapest = createDataForTestSubway("Budapest");
        var actualTicket = budapest.createMonthlyTicket();
        var astoria = budapest.getLine("Red").getStation("Astoria");

        astoria.extendMonthlyTicket(actualTicket.ticketNumber());
        // создать метод для прошлого и будущего 5 шт
        assertTrue(budapest.isValidMonthlyTicket(actualTicket.ticketNumber(),LocalDate.now()));
//        assertTrue(budapest.isValidMonthlyTicket(actualTicket.ticketNumber(),LocalDate.now().plusDays(30)));
        assertFalse(budapest.isValidMonthlyTicket(actualTicket.ticketNumber(),LocalDate.now().plusDays(31)));
    }



}
