package hu.nero;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Line {
    private final List<Station> stations;
    private final String color;
    private final Subway subway;

    public Line(String color, Subway subway) {
        this.color = color;
        this.subway = subway;
        this.stations = new ArrayList<>();
    }

    public void addStation(Station station) {
        stations.add(station);

    }

    public Station getLastStation() {
        if (stations.isEmpty()) {
            return null;
        }
        return stations.getLast();
    }

    public Station getStation(String searchStation) {
        for (Station station : stations) {
            if (station.getName().equals(searchStation)) {
                return station;
            }
        }
        return null;
    }

    private List<String> getStationNames() {
        return stations.stream()
                .map(Station::getName)
                .toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(color, line.color) && Objects.equals(stations, line.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, stations);
    }

    @Override
    public String toString() {
        return "Line{" +
                "color='" + color + '\'' +
                ", stations=" + getStationNames() +
                ", subway name=" + subway.getCityName() +
                '}';
    }
}
