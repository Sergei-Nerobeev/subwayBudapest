package hu.nero;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StationTest {

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

        Station station = subway.createFirstStation(redLineColor, "Astoria", null);
        subway.createLastStation(redLineColor, "Keleti", 4, null);
        Station arena = subway.createLastStation(redLineColor, "Arena", 5, null);
        arena.addTransferStation(deakFerencTer);
        deakFerencTer.addTransferStation(arena);
        return subway;
    }



}
