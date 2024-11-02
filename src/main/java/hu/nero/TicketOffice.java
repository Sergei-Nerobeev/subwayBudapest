package hu.nero;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/*
 * Класс хранит информацию о доходах от продаж билетов за определенные даты.
 * */
public class TicketOffice {
    private static final int TAX = 20; // Налог с каждого билета
    private static final int COST_OF_ONE_STATION_RIDE = 5; // Коэффициент 5
    private static final int COST_OF_MONTHLY_TICKET = 3000;
    private final Map<LocalDate, Integer> purchaseDateToDailyRevenue; // Хранит доходы по датам


    public TicketOffice() {
        this.purchaseDateToDailyRevenue = new HashMap<>();
    }

    /**
     * Данный метод ожидает на вход только положительные числа.
     */
    public void addRevenue(int stationsAmount) { // Добавляет дату покупки и дневную выручку
        var today = LocalDate.now();
        var ticketPrice = (stationsAmount * COST_OF_ONE_STATION_RIDE) + TAX;
        var revenue = purchaseDateToDailyRevenue.get(today);
        if (revenue == null) {
            purchaseDateToDailyRevenue.put(today, ticketPrice);
        } else {
            purchaseDateToDailyRevenue.put(today, revenue + ticketPrice);
        }
    }

    // Метод добавляет стоимость проездного в доход кассы.
    public void addRevenueMonthlyTicket() {
        var purchaseDate = LocalDate.now();
        var revenue = purchaseDateToDailyRevenue.get(purchaseDate);
        if (revenue == null) {
            purchaseDateToDailyRevenue.put(purchaseDate, COST_OF_MONTHLY_TICKET);
        } else {
            purchaseDateToDailyRevenue.put(purchaseDate, revenue + COST_OF_MONTHLY_TICKET);
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
