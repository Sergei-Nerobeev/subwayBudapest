package hu.nero;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class DateUtils {

    private LocalDate convertStringToLocalDate(String date) { // Метод конвертирует Строку в формат ЛокалДата по паттерну.
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(date, dateTimeFormatter);
    }
}
