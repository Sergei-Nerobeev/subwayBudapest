package hu.nero;

import hu.nero.exception.LineNotEmptyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static hu.nero.TestDataProvider.createDataForTestSubway;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование методов класса Subway")
class SubwayTest {

    @DisplayName("isLineWithThisColorExists - correct data - Line color exist!")
    @Test
    void isColorExistsInlines_TrueTest() {
        var budapest = createDataForTestSubway("Budapest");
        var colorLine = "Green";
        budapest.createNewLine(colorLine);

        boolean actual = budapest.isLineWithThisColorExists(colorLine);

        Assertions.assertTrue(actual);
    }

    @DisplayName("isStationNameExistsInAnyLine - correct data - station already exists!")
    @Test
    void isStationNameInlinesTest() {
        var stationName = "Astoria";
        var budapest = createDataForTestSubway("Budapest");
        var greenLine = budapest.createNewLine("Green");
        var station = Station.builder().name(stationName).line(greenLine).subway(budapest).build();
        greenLine.addStation(station);

        boolean actual = budapest.isStationNameExistsInAnyLine(stationName);

        Assertions.assertTrue(actual);
    }

    @DisplayName("createNewLine - correct data - new line created")
    @Test
    void newLineIsCreatedTest() {
        var budapest = createDataForTestSubway("Budapest");

        var line = budapest.createNewLine("Green");

        Assertions.assertNotNull(line);
    }

    @DisplayName("checkLineIsEmpty - not correct data - LineNotEmptyException")
    @Test
    void checkLineIsEmptyTest() throws NoSuchMethodException {
        Subway budapest = createDataForTestSubway("Budapest");
        Line line = budapest.createNewLine("Green");
        var station = Station.builder().name("Roma").line(line).subway(budapest).build();
        line.addStation(station);
        Method checkLineIsEmptyMethod = Subway.class.getDeclaredMethod("checkLineIsEmpty", Line.class);
        checkLineIsEmptyMethod.setAccessible(true);

        InvocationTargetException exception = Assertions.assertThrows(InvocationTargetException.class, () -> {
            checkLineIsEmptyMethod.invoke(budapest, line);
        }, "Ожидалось исключение LineEmptyException");

        Throwable cause = exception.getCause();
        assertInstanceOf(LineNotEmptyException.class, cause, "Expected LineNotEmptyException but: " + cause.getClass().getName());

        assertEquals("Line " + line.getColor() + " is not empty!", cause.getMessage());
    }

    @DisplayName("createNewLine - correct data - new line created")
    @Test
    void createNewLineTest() {
        var colorLine = "Green";
        var cityName = "Budapest";
        Subway subway = createDataForTestSubway(cityName);
        Line line = subway.createNewLine(colorLine);

        Assertions.assertNotNull(line);
        assertEquals(colorLine, line.getColor());
    }

    @DisplayName("createFirstStation - correct data - first station created")
    @Test
    void createFirstStationSuccessTest() {
        Subway subway = createDataForTestSubway("Rome");
        var greenLine = new Line("Green", subway);
        List<Station> transferStations = new ArrayList<>();
        subway.createNewLine("Green");

        Station newStation = subway.createFirstStation(greenLine.getColor(), "Ors Vezer Tere", transferStations);

        Assertions.assertNotNull(newStation);
        assertEquals(newStation.getLine().getColor(), greenLine.getColor());
        assertEquals(transferStations, newStation.getTransferStations());
    }

    @Test
    void createLastStationTest() {
        Subway subway = new Subway("Budapest");
        subway.createNewLine("Red");
        Station firstStation = subway.createFirstStation("Red", "Astoria", null);

        Station lastStation = subway.createLastStation("Red", " Blah Luisa Ter", 120, null);

        assertEquals(lastStation.getPrevious(), firstStation);
        Assertions.assertNull(firstStation.getPrevious());
        Assertions.assertNull(lastStation.getNext());
        assertEquals(firstStation.getNext(), lastStation);
        assertEquals(firstStation.getTransitTimeInSeconds(), 120);
        Assertions.assertTrue(subway.isStationNameExistsInAnyLine(" Blah Luisa Ter"));
    }

    @Test
    void getTransferStationIdentifyTest_Success() {
        var budapest = createDataForTestSubway("Budapest");
        var red = "Red";
        var yellow = "Yellow";
        String stationExpected = "Deak Ferenc Ter";

        Station stationActual = budapest.getTransferStationIdentify(yellow, red);

        assertEquals(stationExpected, stationActual.getName());
    }

    @Test
    void getIntervalTestSuccess() {
        var budapest = createDataForTestSubway("Budapest");
        var stationsYellowLine = budapest.getLine("Yellow").getStations();
        var stationsRedLine = budapest.getLine("Red").getStations();
        Station oktogon = stationsYellowLine.get(0);
        Station opera = stationsYellowLine.get(1);
        Station baiza = stationsYellowLine.get(2);
        Station deak = stationsYellowLine.get(3);
        Station astoria = stationsRedLine.get(0);
        Station keleti = stationsRedLine.get(1);
        Station arena = stationsRedLine.get(2);

        int expected1 = 1;
        int expected2 = 2;
        int expected3 = 3;

        int actualYellow = budapest.getInterval(oktogon, opera);
        int actualRed = budapest.getInterval(astoria, arena);

        assertEquals(expected1, actualYellow);
        assertEquals(expected2, actualRed);
    }

    @Test
    void getIntervalFromLastStationTest_Success() {
        var budapest = createDataForTestSubway("Budapest");
        var stationsYellowLine = budapest.getLine("Yellow").getStations();
        var stationsRedLine = budapest.getLine("Red").getStations();
        Station oktogon = stationsYellowLine.get(0);
        Station opera = stationsYellowLine.get(1);
        Station baiza = stationsYellowLine.get(2);
        Station deak = stationsYellowLine.get(3);
        Station astoria = stationsRedLine.get(0);
        Station keleti = stationsRedLine.get(1);
        Station arena = stationsRedLine.get(2);

        int expected1 = 1;
        int expected2 = 2;
        int expected3 = 3;

        int actualYellow = budapest.getIntervalFromLastStation(oktogon, opera);
        int actualRed = budapest.getIntervalFromLastStation(astoria, arena);

        assertEquals(expected1, actualYellow);
        assertEquals(expected2, actualRed);
    }

    @Test
    void getIntervalOnOneLineTest_Success() {
        var budapest = createDataForTestSubway("Budapest");
        var stationsYellowLine = budapest.getLine("Yellow").getStations();
        var stationsRedLine = budapest.getLine("Red").getStations();
        Station oktogon = stationsYellowLine.get(0);
        Station opera = stationsYellowLine.get(1);
        Station baiza = stationsYellowLine.get(2);
        Station deak = stationsYellowLine.get(3);
        Station astoria = stationsRedLine.get(0);
        Station keleti = stationsRedLine.get(1);
        Station arena = stationsRedLine.get(2);

        int expected1 = 0;
        int expected2 = 0;
        String expectedMessage = "Neither interval found between the specified stations.";

        int actualYellow = budapest.getIntervalOnOneLine(opera, opera);
        int actualRed = budapest.getIntervalOnOneLine(arena, arena);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            budapest.getIntervalOnOneLine(opera, null);
        });

        assertEquals(expected1, actualYellow);
        assertEquals(expected2, actualRed);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void getIntervalFromDifferentLinesTest_Success() {
        var budapest = createDataForTestSubway("Budapest");
        var stationsYellowLine = budapest.getLine("Yellow").getStations();
        var stationsRedLine = budapest.getLine("Red").getStations();
        Station oktogon = stationsYellowLine.get(0);
        Station astoria = stationsRedLine.get(0);

        int expectedTotalIntervalNegative = -1;
        int expectedTotalIntervalPositive = 5;
        int actualTotalInterval = budapest.getIntervalFromDifferentLines(oktogon, astoria);
        int actualTotalIntervalNull = budapest.getIntervalFromDifferentLines(oktogon, null);

        assertNotEquals(expectedTotalIntervalNegative, actualTotalInterval);
        assertEquals(expectedTotalIntervalPositive, actualTotalInterval);
        assertEquals(expectedTotalIntervalNegative, actualTotalIntervalNull);
    }

    @Test
    @DisplayName("Генерация номера проездного билета - позитивный сценарий")
    void generateMonthlyTicketNumberTest_Success() {
        var budapest = createDataForTestSubway("Budapest");

        String expected = "a0000";
        String expectedTwo = "a0001";
        String actual = budapest.generateMonthlyTicketNumber();
        String actualTwo = budapest.generateMonthlyTicketNumber();

        assertEquals(expected, actual);
        assertEquals(expectedTwo, actualTwo);
    }

    @Test
    @DisplayName("Генерация номера проездного билета - проверка количества знаков номера")
    void generateMonthlyTicketNumberTest_CheckThrowException() {
        var budapest = createDataForTestSubway("Budapest");
        for (int i = 0; i < 10000; i++) {
            budapest.generateMonthlyTicketNumber();
        }

        var runtimeException = assertThrows(
                RuntimeException.class,
                budapest::generateMonthlyTicketNumber);

        assertEquals("Today, all the monthly tickets are sold out", runtimeException.getMessage());
    }

    @Test
    @DisplayName("Создание проездного билета - билет Not Null - позитивный сценарий")
    void createMonthlyTicket_ForTest_NotNull() {
        var budapest = createDataForTestSubway("Budapest");

        var actualMonthlyTicket = budapest.createMonthlyTicket();

        assertNotNull(actualMonthlyTicket);
    }

    @Test
    @DisplayName("Создание проездного билета - сравнение данных билета - позитивный сценарий")
    void createMonthlyTicket_ForTest_Success() {
        var budapest = createDataForTestSubway("Budapest");
        var testDate = LocalDate.now();
        var expectedMonthlyTicket = new MonthlyTicket("a0000", testDate);

        var actualMonthlyTicket = budapest.createMonthlyTicket();

        assertEquals(expectedMonthlyTicket, actualMonthlyTicket);
    }

    @Test
    @DisplayName("Создание проездного билета - добавление билетов в список - позитивный сценарий")
    void createMonthlyTicketTest_AddingMonthlyTicketsToList_SuccessForTest() {
        var budapest = createDataForTestSubway("Budapest");
        var testDate = LocalDate.now();
        var expectedMonthlyTicket = new MonthlyTicket("a0000", testDate);
        var expectedMonthlyTicket2 = new MonthlyTicket("a0001", testDate);
        budapest.createMonthlyTicket();
        budapest.createMonthlyTicket();
        var actualMonthlyTicket = budapest.getMonthlyTickets().getFirst();
        var actualMonthlyTicket2 = budapest.getMonthlyTickets().get(1);

        assertEquals(expectedMonthlyTicket, actualMonthlyTicket);
        assertEquals(expectedMonthlyTicket2, actualMonthlyTicket2);
    }

    @Test
    @DisplayName("Есть ли проверяемый билет в базе проданных - позитивный сценарий")
    void isTicketInSystemTest_Success() throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        var budapest = createDataForTestSubway("Budapest");
        budapest.createMonthlyTicket();
        var expectedNumber = "a0000";

        Method method = Subway.class.getDeclaredMethod("isTicketInSystem", String.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(budapest, expectedNumber));
    }

    @Test
    @DisplayName("Валидна ли дата проездного билета - негативный сценарий - дата проверки на один день раньше даты покупки #1")
    void isValidDateDateTest_NoSuccess_OneDayBeforeCheck() {
        var budapest = createDataForTestSubway("Budapest");
        var createDate = LocalDate.now();
        var checkDate = LocalDate.now().minusDays(1);
        var testTicket = budapest.createMonthlyTicket(createDate);
        var testTicketNumber = testTicket.getTicketNumber();

        boolean isTicketValid = budapest.isValidMonthlyTicket(testTicketNumber, checkDate);

        assertFalse(isTicketValid);
    }

    @Test
    @DisplayName("Валидна ли дата проездного билета - позитивный сценарий - дата покупки в рамках срока действия #2")
    void isValidDateDateTest_Success_Purchase_InValidityPeriod() {
        var budapest = createDataForTestSubway("Budapest");
        var checkDate = LocalDate.now().plusDays(15);
        var testTicket = budapest.createMonthlyTicket(LocalDate.now());
        var testTicketNumber = testTicket.getTicketNumber();

        boolean isTicketValid = budapest.isValidMonthlyTicket(testTicketNumber, checkDate);

        assertTrue(isTicketValid);
    }

    @Test
    @DisplayName("Валидна ли дата проездного билета - негативный сценарий - дата покупки за рамками срока действия #3")
    void isValidDateTest_NoSuccess_PurchaseDateIsTicketNotInValidityPeriod() {
        var budapest = createDataForTestSubway("Budapest");
        var checkDate = LocalDate.now().plusDays(50);
        var testTicket = budapest.createMonthlyTicket(LocalDate.now());
        var testTicketNumber = testTicket.getTicketNumber();

        boolean isTicketValid = budapest.isValidMonthlyTicket(testTicketNumber, checkDate);

        assertFalse(isTicketValid);
    }

    @Test
    @DisplayName("Валидна ли дата проездного билета - негативный сценарий - дата проверки в день окончания срока #4")
    void isValidDateDateTest_NoSuccess_CheckInLastDay() {
        var budapest = createDataForTestSubway("Budapest");
        var checkDate = LocalDate.now().plusDays(30);
        var testTicket = budapest.createMonthlyTicket(LocalDate.now());
        var testTicketNumber = testTicket.getTicketNumber();

        boolean isTicketValid = budapest.isValidMonthlyTicket(testTicketNumber, checkDate);

        assertFalse(isTicketValid);
    }

    @Test
    @DisplayName("Валидна ли дата проездного билета - позитивный сценарий - дата покупки равна дате начала срока действия #5")
    void isValidDateDateTest_Success_PurchaseDate_Equals_Check() {
        var budapest = createDataForTestSubway("Budapest");
        var checkDate = LocalDate.now();
        var createDate = LocalDate.now();
        var newMonthlyTicket = budapest.createMonthlyTicket(createDate);
        var testTicketNumber = newMonthlyTicket.getTicketNumber();

        boolean isTicketValid = budapest.isValidMonthlyTicket(testTicketNumber, checkDate);

        assertTrue(isTicketValid);
    }

    @Test
    @DisplayName("Печать доходов за день касс со всех станций метро по дням когда были продажи")
    void isPrintAnything() {
        var budapest = createDataForTestSubway("Budapest");
        var saleDate = LocalDate.now();
        var astoria = budapest.getLine("Red").getStation("Astoria");
        var oktogon = budapest.getLine("Yellow").getStation("Oktogon");
        astoria.sellMonthlyTicket();
        oktogon.sellMonthlyTicket();
        oktogon.sellTicket(astoria, oktogon);
        String expectedDailyRevenueReport = """
                Print revenue by date: $DATE
                Oktogon: 3045
                Astoria: 3000
                Total revenue is: 6045""".replace("$DATE", saleDate.toString());

        String actualDailyRevenueReport = budapest.printDailyRevenueFromAllTicketOffices(saleDate);
        System.out.println(actualDailyRevenueReport);

        assertEquals(expectedDailyRevenueReport, actualDailyRevenueReport);
    }

    @Test
    @DisplayName("Печать доходов за день касс со всех станций метро по дням когда нет продаж")
    void isPrintAnything_WithoutSales() {
        var budapest = createDataForTestSubway("Budapest");
        var saleDate = LocalDate.now();
        String expectedDailyRevenueReport = """
                Print revenue by date: $DATE
                Total revenue is: 0""".replace("$DATE", saleDate.toString());

        String actualDailyRevenueReport = budapest.printDailyRevenueFromAllTicketOffices(saleDate);
        System.out.println(actualDailyRevenueReport);

        assertEquals(expectedDailyRevenueReport, actualDailyRevenueReport);

    }

}