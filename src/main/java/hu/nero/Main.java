package hu.nero;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        var redLineColor = "Red";
        var blueLineColor = "Blue";
        var yellowLineColor = "Yellow";
        Subway subway = new Subway("Budapest");
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
        Station puscasFerencArena = new Station("Puscas Ferenc Arena", redLine, transferStations, subway);
        Station keleti = new Station("Keleti", astoria, puscasFerencArena,0.0, redLine, subway);
        Station astoria = new Station("Astoria", null, keleti, 0.0, redLine, subway);

//        redLine.addStation(astoria);
//        redLine.addStation(keleti);
//        redLine.addStation(puscasFerencArena);
        Station klinikak = new Station("Klinikak", blueLine, transferStations, subway);
        Station nepLiget = new Station("NepLiget", blueLine, transferStations, subway);
        Station lehelTer = new Station("LehelTer", blueLine, transferStations, subway);
        blueLine.addStation(klinikak);
        blueLine.addStation(nepLiget);
        blueLine.addStation(lehelTer);
        Station transDeakFerencTer = new Station("Deak Ferenc Ter", bajzaUtca, astoria, 2, yellowLine, subway);
        transferStations.add(transDeakFerencTer);
        subway.setLines(lines);

        subway.createFirstStation(redLineColor, astoria.getName(), transferStations);
        redLine.addStation(keleti);
        subway.createTerminalStation(redLineColor, puscasFerencArena.getName(), 2.2, transferStations);
        System.out.println(subway.getLines());
    }
}
