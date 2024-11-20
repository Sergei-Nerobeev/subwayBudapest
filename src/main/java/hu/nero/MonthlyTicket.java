package hu.nero;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
public class MonthlyTicket {
    private final String ticketNumber;
    @Setter(value = AccessLevel.PACKAGE)
    private LocalDate purchaseDate;

    public MonthlyTicket(String ticketNumber, LocalDate purchaseDate) {
        this.ticketNumber = ticketNumber;
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


