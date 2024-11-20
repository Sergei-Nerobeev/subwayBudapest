package hu.nero;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/*
 * Класс хранит информацию о доходах от продаж билетов за определенные даты.
 * */
public class TicketOffice {
    private static final int TAX = 20;
    private static final int COST_OF_ONE_STATION_RIDE = 5;
    private static final int COST_OF_MONTHLY_TICKET = 3000;
    private final Map<LocalDate, Integer> purchaseDateToDailyRevenue; // Хранит доходы по датам


    public TicketOffice() {
        this.purchaseDateToDailyRevenue = new HashMap<>();
    }

    /**
     * Данный метод ожидает на вход только положительные числа.
     */
    void addRevenue(int stationsAmount) { // Добавляет дату покупки и дневную выручку
        var ticketPrice = (stationsAmount * COST_OF_ONE_STATION_RIDE) + TAX;
        updateTodayRevenue(ticketPrice);
    }

    // Метод добавляет стоимость проездного в доход кассы.
    void addRevenueMonthlyTicket() {
        updateTodayRevenue(COST_OF_MONTHLY_TICKET);
    }

    private void updateTodayRevenue(int ticketPrice) {
        var today = LocalDate.now();
        var revenue = purchaseDateToDailyRevenue.get(today);
        if (revenue == null) {
            purchaseDateToDailyRevenue.put(today, ticketPrice);
        } else {
            purchaseDateToDailyRevenue.put(today, revenue + ticketPrice);
        }
    }

    public Integer getDailyRevenue(LocalDate date) { // Получить дневную выручку
        return purchaseDateToDailyRevenue.get(date);
    }

    @Override
    public String toString() {
        return "TicketOffice{" +
                "purchaseDateToDailyRevenue=" + purchaseDateToDailyRevenue +
                '}';
    }
}
