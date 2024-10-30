package hu.nero;

import java.time.LocalDate;

public final class TestDataProvider {
    public static Subway createDataForTestSubway(String cityName) {
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
        Station arena = subway.createLastStation(redLineColor, "Arena", 5, null);
        arena.addTransferStation(deakFerencTer);
        deakFerencTer.addTransferStation(arena);
        return subway;
    }
}
