package hu.nero;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Station {
    private final String name;
    private Station previous;
    private Station next;
    private int transitTimeInSeconds; //время перегона только до следующей станции
    private final Line line;
    private List<Station> transferStations;
    private final Subway subway;
    public TicketOffice ticketOffice;

    public Station(String name,
                   Station previous,
                   Station next,
                   int transitTimeInSeconds,
                   Line line,
                   Subway subway) {
        this.name = name;
        this.previous = previous;
        this.next = next;
        this.transitTimeInSeconds = transitTimeInSeconds;
        this.line = line;
        this.transferStations = new ArrayList<>();
        this.subway = subway;
    }

    public Station(String name,
                   Line line,
                   List<Station> transferStations,
                   Subway subway) {
        this(name,
                null,
                null,
                0,
                line,
                subway);
    }
    /**
     * Конструктор для создания объекта станции.
     *
     * @param name Имя станции.
     * @param line Линия, к которой принадлежит станция.
     * @param subway Система метро, к которой относится станция.
     * @param ticketOffice Касса для продажи билетов на станции.
     */
    public Station(String name, Line line, Subway subway,TicketOffice ticketOffice) {
        this.name = name;
        this.line = line;
        this.subway = subway;
        this.ticketOffice = ticketOffice;
    }

    public String getName() {
        return name;
    }

    public Station getPrevious() {
        return previous;
    }

    public void setPrevious(Station previous) {
        this.previous = previous;
    }

    public Station getNext() {
        return next;
    }

    public void setNext(Station next) {
        this.next = next;
    }

    public int getTransitTimeInSeconds() { //получение время перегона только до следующей станции
        return transitTimeInSeconds;
    }

    public void setTransitTimeInSeconds(int transitTimeInSeconds) { //замена время перегона только до следующей станции
        this.transitTimeInSeconds = transitTimeInSeconds;
    }

    public List<Station> getTransferStations() {
        return transferStations;
    }

    public Line getLine() {
        return line;
    }


    public Subway getSubway() {
        return subway;
    }

    public void addTransferStation(Station station) {
        if (transferStations == null) {
            transferStations = new ArrayList<>();
        }
        transferStations.add(station);
    }

    public void sellTicket(Station start, Station finish) { // Метод продажи билетов
        if (start == null || finish == null) {
            throw new IllegalArgumentException("Stations cannot be null.");
        }
        if (start.equals(finish)) {
            throw new IllegalArgumentException("Start station cannot be the same as finish station.");
        }
        int interval = getSubway().getIntervalFromDifferentLines(start, finish); // Получаем интервал между станциями
        ticketOffice.addRevenue(interval);

    }

    private String convertStringToLocalDate(String date) { // Метод конвертирует Строку в формат ЛокалДата по паттерну.
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(date, dateTimeFormatter);
            return localDateTime.toString();
        } catch (DateTimeException dateTimeException) {
            return "Invalid date format. Please use dd.MM.yyyy.";
        }
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
