package hu.nero;

import hu.nero.exception.LineNotEmptyException;
import org.junit.jupiter.api.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@DisplayName("Тестирование методов класса Subway")
class SubwayTest {

    public Subway createTestSubway(String cityName) {
        var redLineColor = "Red";
        var blueLineColor = "Blue";
        var yellowLineColor = "Yellow";
        Subway subway = new Subway(cityName);
        Line redLine = new Line(redLineColor, subway);
        Line blueLine = new Line(blueLineColor, subway);
        Line yellowLine = new Line(yellowLineColor, subway);
        List<Station> transferStations = new ArrayList<>();
        transferStations.add(null);
        Set<Line> lines = new HashSet<>();
        lines.add(redLine);
        lines.add(blueLine);
        lines.add(yellowLine);
        Station oktogon = new Station("Oktogon", yellowLine, transferStations, subway);
        Station opera = new Station("Opera", yellowLine, transferStations, subway);
        Station bajzaUtca = new Station("Bajza Utca", yellowLine, transferStations, subway);
        yellowLine.addStation(oktogon);
        yellowLine.addStation(opera);
        yellowLine.addStation(bajzaUtca);
        Station astoria = new Station("Astoria", redLine, transferStations, subway);
        Station keleti = new Station("Keleti", redLine, transferStations, subway);
        Station puscasFerencArena = new Station("Puscas Ferenc Arena", redLine, transferStations, subway);
        redLine.addStation(astoria);
        redLine.addStation(keleti);
        redLine.addStation(puscasFerencArena);
        Station klinikak = new Station("Klinikak", blueLine, transferStations, subway);
        Station nepLiget = new Station("NepLiget", blueLine, transferStations, subway);
        Station lehelTer = new Station("LehelTer", blueLine, transferStations, subway);
        blueLine.addStation(klinikak);
        blueLine.addStation(nepLiget);
        blueLine.addStation(lehelTer);
        Station transDeakFerencTer = new Station("Deak Ferenc Ter", bajzaUtca, astoria, 2, yellowLine, subway);
        transferStations.add(transDeakFerencTer);
        yellowLine.addStation(transDeakFerencTer);
        subway.setLines(lines);
        return subway;
    }

    public Subway createDataForTestSubway(String cityName) {
        var yellowLineColor = "Yellow";
        var redLineColor = "Red";
        Subway subway = new Subway(cityName);
        subway.createNewLine(yellowLineColor);
        subway.createNewLine(redLineColor);

        subway.createFirstStation(yellowLineColor, "Oktogon", null);
        subway.createLastStation(yellowLineColor, "Opera", 4, null);
        subway.createLastStation(yellowLineColor, "Bajza utca", 5, null);
        Station deakFerencTer = subway.createLastStation(yellowLineColor, "Deak Ferenc Ter", 6, null);

        subway.createFirstStation(redLineColor, "Astoria", null);
        subway.createLastStation(redLineColor, "Keleti", 4, null);
        Station pfa = subway.createLastStation(redLineColor, "Puscas Ferenc Arena", 5, null);
        pfa.addTransferStation(deakFerencTer);
        deakFerencTer.addTransferStation(pfa);
        return subway;
    }

    @DisplayName("isLineWithThisColorExists - correct data - Line color exist!")
    @Test
    void isColorExistsInlines_TrueTest() {
        var budapest = createTestSubway("Budapest");
        var colorLine = "Green";
        budapest.createNewLine(colorLine);

        boolean actual = budapest.isLineWithThisColorExists(colorLine);

        Assertions.assertTrue(actual);
    }

    @DisplayName("isStationNameExistsInAnyLine - correct data - station already exists!")
    @Test
    void isStationNameInlinesTest() {
        var stationName = "Astoria";
        var budapest = createTestSubway("Budapest");
        var greenLine = budapest.createNewLine("Green");
        var station = new Station(stationName, greenLine, new ArrayList<>(), budapest);
        greenLine.addStation(station);

        boolean actual = budapest.isStationNameExistsInAnyLine(stationName);

        Assertions.assertTrue(actual);
    }

    @DisplayName("createNewLine - correct data - new line created")
    @Test
    void newLineIsCreatedTest() {
        var budapest = createTestSubway("Budapest");

        var line = budapest.createNewLine("Green");

        Assertions.assertNotNull(line);
    }

    @DisplayName("checkLineIsEmpty - not correct data - LineNotEmptyException")
    @Test
    void checkLineIsEmptyTest() throws NoSuchMethodException {
        Subway budapest = createTestSubway("Budapest");
        Line line = budapest.createNewLine("Green");
        var station = new Station("Roma", null, null, 120, line, budapest);
        line.addStation(station);
        Method checkLineIsEmptyMethod = Subway.class.getDeclaredMethod("checkLineIsEmpty", Line.class);
        checkLineIsEmptyMethod.setAccessible(true);

        InvocationTargetException exception = Assertions.assertThrows(InvocationTargetException.class, () -> {
            checkLineIsEmptyMethod.invoke(budapest, line);
        }, "Ожидалось исключение LineEmptyException");

        Throwable cause = exception.getCause();
        assertInstanceOf(LineNotEmptyException.class, cause, "Expected LineNotEmptyException but: " + cause.getClass().getName());

        Assertions.assertEquals("Line " + line.getColor() + " is not empty!", cause.getMessage());
    }

    @DisplayName("createNewLine - correct data - new line created")
    @Test
    void createNewLineTest() {
        var colorLine = "Green";
        var cityName = "Budapest";
        Subway subway = createTestSubway(cityName);
        Line line = subway.createNewLine(colorLine);

        Assertions.assertNotNull(line);
        Assertions.assertEquals(colorLine, line.getColor());
    }

    @DisplayName("createFirstStation - correct data - first station created")
    @Test
    void createFirstStationSuccessTest() {
        Subway subway = createTestSubway("Rome");
        var greenLine = new Line("Green", subway);
        List<Station> transferStations = new ArrayList<>();
        subway.createNewLine("Green");

        Station newStation = subway.createFirstStation(greenLine.getColor(), "Ors Vezer Tere", transferStations);

        Assertions.assertNotNull(newStation);
        Assertions.assertEquals(newStation.getLine().getColor(), greenLine.getColor());
        Assertions.assertEquals(transferStations, newStation.getTransferStations());
    }

    @Test
    void createLastStationTest() {
        Subway subway = new Subway("Budapest");
        subway.createNewLine("Red");
        Station firstStation = subway.createFirstStation("Red", "Astoria", null);

        Station lastStation = subway.createLastStation("Red", " Blah Luisa Ter", 120, null);

        Assertions.assertEquals(lastStation.getPrevious(), firstStation);
        Assertions.assertNull(firstStation.getPrevious());
        Assertions.assertNull(lastStation.getNext());
        Assertions.assertEquals(firstStation.getNext(), lastStation);
        Assertions.assertEquals(firstStation.getTransitTimeInSeconds(), 120);
        Assertions.assertTrue(subway.isStationNameExistsInAnyLine(" Blah Luisa Ter"));
    }

    @Test
    void getTransferStationIdentifyTest_Success() {
        var budapest = createDataForTestSubway("Budapest");
        var red = "Red";
        var yellow = "Yellow";
        String stationExpected = "Deak Ferenc Ter";

        Station stationActual = budapest.getTransferStationIdentify(yellow, red);

        Assertions.assertEquals(stationExpected, stationActual.getName());
    }

    @Test
    void getIntervalTest_Success() {
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

        Assertions.assertEquals(expected1, actualYellow);
        Assertions.assertEquals(expected2, actualRed);
    }
    @Test
    void getIntervalFromLastStationTest_Success(){
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

        Assertions.assertEquals(expected1, actualYellow);
        Assertions.assertEquals(expected2, actualRed);
    }
}