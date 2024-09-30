package hu.nero;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TicketOfficeTest {

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

    @Test
    void sellTicketTest_Success() {
        TicketOffice ticketOffice = new TicketOffice();
        var budapest = createDataForTestSubway("Budapest");
        var stationsYellowLine = budapest.getLine("Yellow").getStations();
        var stationsRedLine = budapest.getLine("Red").getStations();
        Station oktogon = stationsYellowLine.get(0);
        Station astoria = stationsRedLine.get(0);

        ticketOffice.addRevenue(2);
        var expectedInfo = oktogon.sellTicket(oktogon, astoria,ticketOffice);
        var actualInfo = "Ticket: Oktogon Astoria interval: 5";


        Assertions.assertEquals(expectedInfo,actualInfo);

    }


}
