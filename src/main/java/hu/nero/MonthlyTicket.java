package hu.nero;

import java.time.LocalDate;

public record MonthlyTicket(String ticketNumber, LocalDate purchaseDate) {

    @Override
    public String ticketNumber() {
        return ticketNumber;
    }

    @Override
    public LocalDate purchaseDate() {
        return purchaseDate;
    }

    @Override
    public String toString() {
        return "MonthlyTicket{" +
                "ticketNumber='" + ticketNumber + '\'' +
                ", purchaseDate=" + purchaseDate +
                '}';
    }
}
