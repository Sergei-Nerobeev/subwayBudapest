package hu.nero;

import hu.nero.exception.LineNotEmptyException;
import org.junit.jupiter.api.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        subway.setLines(lines);
        return subway;
    }

    @DisplayName("isLineWithThisColorExists - correct data - Line color exist!")
    @Test
    void isColorExistsInlines_True() {
        var budapest = createTestSubway("Budapest");
        var colorLine = "Green";
        budapest.createNewLine(colorLine);

        boolean actual = budapest.isLineWithThisColorExists(colorLine);

        Assertions.assertTrue(actual);
    }

    @DisplayName("isStationNameExistsInAnyLine - correct data - station already exists!")
    @Test
    void isStationNameInlines() {
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
    void newLineIsCreated() {
        var budapest = createTestSubway("Budapest");

        var line = budapest.createNewLine("Green");

        Assertions.assertNotNull(line);
    }

    @DisplayName("checkLineIsEmpty - not correct data - LineNotEmptyException")
    @Test
    void checkLineIsEmptyTest_notCorrect() throws NoSuchMethodException {
        Subway budapest = createTestSubway("Budapest");
        Line line = budapest.createNewLine("Green");
        List<Station> transferStations = new ArrayList<>();
        var station = new Station("Rome", line, transferStations, budapest);
        line.addStation(station);
        Method checkLineIsEmptyMethod = Subway.class.getDeclaredMethod("checkLineIsEmpty", Line.class);
        checkLineIsEmptyMethod.setAccessible(true);

        Exception exception = Assertions.assertThrows(InvocationTargetException.class, () -> { // check exception type!
            checkLineIsEmptyMethod.invoke(budapest, line);
        });
        Throwable cause = exception.getCause();
        Assertions.assertInstanceOf(LineNotEmptyException.class, cause,
                "Expected LineNotEmptyException but got: " + cause);
    }

    @DisplayName("createNewLine - correct data - new line created") // stackOverFlow debug
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
    void testCreateFirstStationSuccess() {
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
    void createTerminalStationTest() {
        Subway subway = new Subway("Budapest");
        subway.createNewLine("Red");
        subway.createNewLine("Blue");
        subway.createNewLine("Yellow");
        Station firstStation = subway.createFirstStation("Red", "Astoria", null);
        Station lastStation = subway.createTerminalStation("Red"," Blah Luisa Ter",120 ,null);
        Assertions.assertEquals(firstStation.getNext(), lastStation);
        Assertions.assertEquals(lastStation.getPrevious(), firstStation);
        Assertions.assertNull(firstStation.getPrevious());
        Assertions.assertNull(lastStation.getNext());

    }
}