package hu.nero;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class DateUtils {

    private String convertStringToLocalDate(String date) { // Метод конвертирует Строку в формат ЛокалДата по паттерну.
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
            return localDate.toString();
        } catch (DateTimeException dateTimeException) {
            return "Invalid date format. Please use dd.MM.yyyy.";
        }
    }
}
