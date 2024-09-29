package hu.nero;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/*
 * Класс хранит информацию о доходах от продаж билетов за определенные даты.
 * */
public class TicketOffice {
    private final static int TAX = 20; // Налог с каждого билета
    private final static int COST_OF_ONE_STATION_RIDE = 5; // Коэффициент 5
    private final Map<String, Integer> dailyRevenue; // Хранит доходы по датам


    public TicketOffice() {
        this.dailyRevenue = new HashMap<>();
    }

    public void addRevenue(int interval) { // Добавить дату и дневную выручку
        LocalDate localDate = LocalDate.now();
        int total = (interval * COST_OF_ONE_STATION_RIDE) + TAX;
        dailyRevenue.put(String.valueOf(localDate), total);
    }

    public Integer getDailyRevenue(String date) { // Получить дату и стоимость
        return dailyRevenue.get(date);
    }
}
