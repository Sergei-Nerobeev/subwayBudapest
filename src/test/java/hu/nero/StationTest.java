package hu.nero;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StationTest {
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
        Station arena = subway.createLastStation(redLineColor, "Arena", 5, null);
        arena.addTransferStation(deakFerencTer);
        deakFerencTer.addTransferStation(arena);
        return subway;
    }



    @Test
    void sellTicketTest_Success() {
        var subway = createDataForTestSubway("Budapest");
        var intervalTest = 2;
        var redLine = subway.getLine("Red");
        var start = redLine.getStation("Astoria");
        var finish = redLine.getStation("Arena");
        var stationTestData = redLine.getStation("Astoria");

        var expected = "Ticket: " + start.getName() + " " +
                finish.getName() + " " + "interval: " + intervalTest;
        var actual = stationTestData.sellTicket(start, finish);

        assertEquals(expected, actual);
    }


}