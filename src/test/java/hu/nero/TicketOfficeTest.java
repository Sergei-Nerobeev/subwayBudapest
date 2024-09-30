package hu.nero;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TicketOfficeTest {

    @Test
    void addRevenueTest_Success() {
        var ticketOffice = new TicketOffice();
        var testStationsAmount = 5;
        var testDate = LocalDate.now();
        var expectedRevenue = 45;

        ticketOffice.addRevenue(testStationsAmount);
        var actualRevenue = ticketOffice.getDailyRevenue(testDate);

        assertEquals(expectedRevenue, actualRevenue);
    }

    @Test
    void addRevenueTest_Zero() {
        var ticketOffice = new TicketOffice();
        var testStationsAmount = 0;
        var testDate = LocalDate.now();
        var expectedRevenue = 20;

        ticketOffice.addRevenue(testStationsAmount);
        var actualRevenue = ticketOffice.getDailyRevenue(testDate);

        assertEquals(expectedRevenue, actualRevenue);
    }

    @Test
    @DisplayName("Добавление билета к стоимости дневной выручке не Налл")
    void addRevenueTest_NotNull() {
        var ticketOffice = new TicketOffice();
        ticketOffice.addRevenue(2);
        var testStationsAmount = 2;
        var testDate = LocalDate.now();
        var expectedRevenue = 60;

        ticketOffice.addRevenue(testStationsAmount);
        var actualRevenue = ticketOffice.getDailyRevenue(testDate);

        assertEquals(expectedRevenue, actualRevenue);
    }
}
