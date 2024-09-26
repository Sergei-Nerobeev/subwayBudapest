package hu.nero;

import java.util.HashMap;
import java.util.Map;

/*
 * Класс хранит информацию о доходах от продаж билетов за определенные даты.
 * */
public class TicketOffice {

    private final Map<String, Integer> dailyRevenue; // Хранит доходы по датам


    public TicketOffice() {
        this.dailyRevenue = new HashMap<>();
    }

    public void addRevenue(String date, int dailyIncome) { // Добавить дату и дневную выручку

        dailyRevenue.put(date, dailyIncome);
    }

    public Integer getDailyRevenue(String date) { // Получить дату и стоимость
        return dailyRevenue.get(date);
    }
}
