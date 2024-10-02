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
    private final Map<LocalDate, Integer> purchaseDateToDailyRevenue; // Хранит доходы по датам


    public TicketOffice() {
        this.purchaseDateToDailyRevenue = new HashMap<>();
    }
/**
 * Данный метод ожидает на вход только положительные числа.
 * */
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

    public Integer getDailyRevenue(LocalDate date) { // Получить дату и стоимость
        return purchaseDateToDailyRevenue.get(date);
    }
}
