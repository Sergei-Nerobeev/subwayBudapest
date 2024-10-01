package hu.nero;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public final class DateUtils {
    // Метод конвертирует Строку в формат ЛокалДата по паттерну.
    public static LocalDate convertStringToLocalDate(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(date, dateTimeFormatter);
    }
}
