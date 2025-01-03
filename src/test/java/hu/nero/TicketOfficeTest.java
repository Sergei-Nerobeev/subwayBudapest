package hu.nero;

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
    @DisplayName("Добавление билета к стоимости дневной выручки - количество станций - ноль")
    void addRevenueTest_WhenStationsCountIsZero() {
        var ticketOffice = new TicketOffice();
        var testStationsAmount = 0;
        var testDate = LocalDate.now();
        var expectedRevenue = 20;

        ticketOffice.addRevenue(testStationsAmount);
        var actualRevenue = ticketOffice.getDailyRevenue(testDate);

        assertEquals(expectedRevenue, actualRevenue);
    }

    @Test
    @DisplayName("Добавление билета к стоимости дневной выручке не Null")
    void addRevenueTest_NotNull() {
        var ticketOffice = new TicketOffice();

        var testStationsAmount = 2;
        var testDate = LocalDate.now();
        var expectedRevenue = 30;

        ticketOffice.addRevenue(testStationsAmount);
        var actualRevenue = ticketOffice.getDailyRevenue(testDate);

        assertEquals(expectedRevenue, actualRevenue);
    }

    @Test
    @DisplayName("Добавление стоимости проездного билета к стоимости дневной выручке")
    void addRevenueMonthlyTicket_Success() {
        var ticketOffice = new TicketOffice();
        var testDate = LocalDate.now();
        var expectedRevenue = 3000;

        ticketOffice.addRevenueMonthlyTicket();
        var actualRevenue = ticketOffice.getDailyRevenue(testDate);

        assertEquals(expectedRevenue, actualRevenue);
    }
}
