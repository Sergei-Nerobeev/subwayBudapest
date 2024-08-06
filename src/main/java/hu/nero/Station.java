package hu.nero;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Station {
    private final String name;
    private Station previous;
    private Station next;
    private int transitTimeInSeconds;
    private final Line line;
    private final List<Station> transferStations;
    private final List<Double> intervals;
    private final Subway subway;

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
        this.intervals = new ArrayList<>();
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

    public int getTransitTimeInSeconds() {
        return transitTimeInSeconds;
    }

    public void setTransitTimeInSeconds(int transitTimeInSeconds) {
        this.transitTimeInSeconds = transitTimeInSeconds;
    }

    public List<Station> getTransferStations() {
        return transferStations;
    }

    public Line getLine() {
        return line;
    }

    public List<Double> getIntervals() {
        return intervals;
    }

    public Subway getSubway() {
        return subway;
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
                ", previous=" + previous +
                ", next=" + next +
                ", transitTimeInMinutesAndSeconds=" + transitTimeInSeconds +
                ", line=" + line.getColor() +
                ", transferStations=" + transferStations +
                ", subway=" + subway.getCityName() +
                '}';
    }
}
