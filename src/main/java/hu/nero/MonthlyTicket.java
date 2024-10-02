package hu.nero;

import java.util.Objects;

public class MonthlyTicket {

    private String number = "a000";
    private final int cost = 3000;

    public MonthlyTicket(String number) {
        this.number = number;
    }

    public int getCost() {
        return cost;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyTicket that = (MonthlyTicket) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, cost);
    }

    @Override
    public String toString() {
        return "MonthlyTicket{" +
                "number='" + number + '\'' +
                ", cost=" + cost +
                '}';
    }
}
