package hu.nero;

import java.time.LocalDate;
import java.util.Objects;

public class MonthlyTicket {
    private String ticketNumber;
    private LocalDate purchaseDate;

    public MonthlyTicket(String ticketNumber, LocalDate purchaseDate) {
        this.ticketNumber = ticketNumber;
        this.purchaseDate = purchaseDate;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyTicket that = (MonthlyTicket) o;
        return Objects.equals(ticketNumber, that.ticketNumber) && Objects.equals(purchaseDate, that.purchaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketNumber, purchaseDate);
    }

    @Override
    public String toString() {
        return "MonthlyTicket{" +
                "ticketNumber='" + ticketNumber + '\'' +
                ", purchaseDate=" + purchaseDate +
                '}';
    }
}


