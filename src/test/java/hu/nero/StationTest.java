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

        assertEquals("Stations can not be null.", exception.getMessage());
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

        assertEquals("Start station can not be the same as finish station.", exception.getMessage());
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
    @DisplayName("Продление проездного билета - билет плюс 30 дней - корректная дата,позитивный сценарий")
    void extendMonthlyTicketTest_Success() {
        var budapest = createDataForTestSubway("Budapest");
        var testDate = LocalDate.now();
        var actualTicket = budapest.createMonthlyTicket();
        var astoria = budapest.getLine("Red").getStation("Astoria");

        astoria.extendMonthlyTicket(actualTicket.getTicketNumber(), testDate);

        assertTrue(budapest.isValidMonthlyTicket(actualTicket.getTicketNumber(), LocalDate.now()));
        assertTrue(budapest.isValidMonthlyTicket(actualTicket.getTicketNumber(), LocalDate.now().plusDays(29)));
    }

    @Test
    @DisplayName("Продление проездного билета - билет плюс 30 дней - некорректная дата,негативный сценарий")
    void extendMonthlyTicketTest_NoSuccess() {
        var budapest = createDataForTestSubway("Budapest");
        var testDatePlusTwoDays = LocalDate.now().plusDays(2);
        var actualTicket = budapest.createMonthlyTicket();
        var astoria = budapest.getLine("Red").getStation("Astoria");

        astoria.extendMonthlyTicket(actualTicket.getTicketNumber(), testDatePlusTwoDays);

        assertFalse(budapest.isValidMonthlyTicket(actualTicket.getTicketNumber(), LocalDate.now()));
        assertFalse(budapest.isValidMonthlyTicket(actualTicket.getTicketNumber(), LocalDate.now().plusDays(32)));
    }

    @Test
    @DisplayName("Продление проездного билета - срок действия билета прошел - некорректная дата,негативный сценарий")
    void extendMonthlyTicketTest_NoSuccessValidityPeriod() {
        var budapest = createDataForTestSubway("Budapest");
        var testDateStartNow = LocalDate.now();
        var dateMinusDays = LocalDate.now().minusDays(1000000000);
        var actualTicket = budapest.createMonthlyTicket(dateMinusDays);
        var astoria = budapest.getLine("Red").getStation("Astoria");

        astoria.extendMonthlyTicket(actualTicket.getTicketNumber(), testDateStartNow);

        assertTrue(budapest.isValidMonthlyTicket(actualTicket.getTicketNumber(), testDateStartNow));
    }

    @Test
    @DisplayName("Продление проездного билета - тест даты - плюс 2 года,позитивный сценарий")
    void extendMonthlyTicketTest_Success_StartDate() {
        var budapest = createDataForTestSubway("Budapest");
        var testDateStartPlusOneYear = LocalDate.now().plusYears(2);
        var actualTicket = budapest.createMonthlyTicket();
        var astoria = budapest.getLine("Red").getStation("Astoria");

        astoria.extendMonthlyTicket(actualTicket.getTicketNumber(), testDateStartPlusOneYear);

        assertTrue(budapest.isValidMonthlyTicket(actualTicket.getTicketNumber(), testDateStartPlusOneYear));
    }
}
