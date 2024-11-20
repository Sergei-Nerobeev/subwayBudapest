package hu.nero;

import hu.nero.exception.TicketNotFoundException;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
public class Station {
    private final String name;
    private final Line line;
    private final TicketOffice ticketOffice = new TicketOffice();
    private final Subway subway;
    @Setter
    private Station previous;
    @Setter
    private Station next;
    //замена время перегона только до следующей станции
    /**
     * Время перегона только до следующей станции
     */
    @Setter
    private int transitTimeInSeconds;
    private List<Station> transferStations = new ArrayList<>();

    public void addTransferStation(Station station) {
        if (transferStations == null) {
            transferStations = new ArrayList<>();
        }
        transferStations.add(station);
    }

    /**
     * Метод продажи билетов (добавляет стоимость билета в кассу)
     * 
     * @param start начальная станция
     * @param finish конечная станция
     * @return сообщение о билете - поездка с одной станции на другую, интервал
     */
    public String sellTicket(Station start, Station finish) {
        if (start == null || finish == null) {
            throw new IllegalArgumentException("Stations can not be null.");
        }
        if (start.equals(finish)) {
            throw new IllegalArgumentException("Start station can not be the same as finish station.");
        }
        var interval = getSubway().getIntervalFromDifferentLines(start, finish);
        ticketOffice.addRevenue(interval);
        return "Ticket: " + start.getName() + " " + finish.getName() + " " + "interval: " + interval;
    }

    /**
     * Метод продажи проездных билетов
     * 
     * @return созданный месячный билет
     */
    public MonthlyTicket sellMonthlyTicket() { 
        ticketOffice.addRevenueMonthlyTicket();
        return subway.createMonthlyTicket();
    }

    /**
     * Метод продления действия проездного на 30 дней с момента покупки
     *
     */
    public void extendMonthlyTicket(String ticketNumber, LocalDate startDate) {
        MonthlyTicket foundTicket = null;
        List<MonthlyTicket> monthlyTickets = subway.getMonthlyTickets();
        for (MonthlyTicket monthlyTicket : monthlyTickets) {
            if (monthlyTicket.getTicketNumber().equals(ticketNumber)) {
                foundTicket = monthlyTicket;
                break;
            }
        }
        if (foundTicket == null) {
            throw new TicketNotFoundException("Ticket not found!");
        }
        foundTicket.setPurchaseDate(startDate);
        ticketOffice.addRevenueMonthlyTicket();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(name, station.name)
                && Objects.equals(previous, station.previous)
                && Objects.equals(next, station.next)
                && Objects.equals(transitTimeInSeconds, station.transitTimeInSeconds)
                && Objects.equals(line, station.line)
                && Objects.equals(transferStations, station.transferStations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, previous, next, transitTimeInSeconds, line, transferStations);
    }

    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                ", previous=" + previous.getName() +
                ", next=" + next.getName() +
                ", transitTimeInMinutesAndSeconds=" + transitTimeInSeconds +
                ", line=" + line.getColor() +
                ", transferStations=" + transferStations.toString() +
                ", subway=" + subway.getCityName() +
                '}';
    }
}
