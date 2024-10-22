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
        var actualTicketNumber = "a0234";
        var testDateNow = LocalDate.now();
        var testDateTomorrow = DateUtils.convertStringToLocalDate("20.10.2024");
        var expectedDate = DateUtils.convertStringToLocalDate("10.11.2024");
        var stationTest = budapest.getLine("Red").getStation("Astoria");

        var extendedMonthlyTicket = stationTest.extendMonthlyTicket(actualTicketNumber, testDateTomorrow);
        var actualDate = extendedMonthlyTicket.purchaseDate();

        assertEquals(expectedDate, actualDate);
    }

    @Test
    @DisplayName("Продление проездного билета - возвращает исключение - некорректная дата,негативный сценарий")
    void extendMonthlyTicketTest_ThrowsException() {
        var budapest = createDataForTestSubway("Budapest");
        var actualTicketNumber = "a0234";
        var expectedTestDate = DateUtils.convertStringToLocalDate("09.11.2024");
        var wrongTestDate = DateUtils.convertStringToLocalDate("01.10.2024");
        var stationTest = budapest.getLine("Red").getStation("Astoria");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> {
                    stationTest.extendMonthlyTicket(actualTicketNumber, wrongTestDate);
                });

        assertEquals("The date must be at least as recent as today.", exception.getMessage());
    }

}
