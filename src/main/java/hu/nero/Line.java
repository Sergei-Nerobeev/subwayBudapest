package hu.nero;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Line {
    private String color;
    private final List<Station> stations;
    private Subway subway;

    public Line(String color, Subway subway) {
        this.color = color;
        this.subway = subway;
        this.stations = new ArrayList<>();
    }

    public void addStation(Station station) {
        stations.add(station);

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Subway getSubway() {
        return subway;
    }

    public void setSubway(Subway subway) {
        this.subway = subway;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Station getLastStation() {
        if (stations.isEmpty()) {
            return null;
        }
        return stations.get(stations.size() - 1);
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
        assert color != null;
        assert subway != null;
        return "Line{" +
                "color='" + color + '\'' +
                ", stations=" + stations.size() +
                ", subway name=" + subway.getCityName() +
                '}';
    }


}
