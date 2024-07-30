package hu.nero;

import hu.nero.exception.ColorLineException;
import hu.nero.exception.LineNotEmptyException;
import hu.nero.exception.StationNameException;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Subway {
    private String cityName;
    private Set<Line> lines;

    public Subway(String cityName) {
        this.cityName = cityName;
        this.lines = new HashSet<>();
    }

    public Line createNewLine(String newLineColor) {
        if (isLineWithThisColorExists(newLineColor)) {
            throw new ColorLineException(newLineColor + " already exists!");
        }
        Line line = new Line(newLineColor, this);
        lines.add(line);
        return line;
    }

    public boolean isLineWithThisColorExists(String newLineColor) {
        return lines.stream().anyMatch(line -> line.getColor().equals(newLineColor));
    }

    public boolean isStationNameExistsInAnyLine(String nameStation) {
        for (Line line : lines) {
            for (Station station : line.getStations()) {
                if (station.getName().equals(nameStation)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Создание первой станции на линии
     */
    public Station createFirstStation(String lineColor,
                                      String stationName,
                                      List<Station> transferStations) {
        checkLineExists(lineColor);
        checkStationNameNotExists(stationName);
        Line line = getLine(lineColor);
        checkLineIsEmpty(line);

        Station station = new Station(stationName, line, transferStations, this);
        line.addStation(station);

        return station;
    }

    static void checkLineIsEmpty(Line line) {
        if (line.getStations() != null && !line.getStations().isEmpty()) {
            throw new LineNotEmptyException(line + " is not empty");
        }
    }

    void checkStationNameNotExists(String stationName) {
        if (isStationNameExistsInAnyLine(stationName)) {
            throw new StationNameException(stationName + " station already exists!");
        }
    }

    private void checkLineExists(String lineColor) {
        if (!isLineWithThisColorExists(lineColor)) {
            throw new ColorLineException(lineColor + " Line color doesn't exist!");
        }
    }

    Line getLine(String lineColor) {
        Line line = null;
        for (Line l : lines) {
            if (l.getColor().equals(lineColor)) {
                line = l;
                break;
            }
        }
        return line;
    }

    /**
     * Создание последней станции на линии
     */
    public Station createTerminalStation(String lineColor,
                                         String newNameOfStation,
                                         Duration travelTimeToPrevious,
                                         List<Station> transferStations) {
        checkStationNameNotExists(newNameOfStation);
        checkPreviousStationOnLine();
        var terminalStation = new Station(newNameOfStation, getLine(lineColor), transferStations, this);
        terminalStation.setPrevious(); //todo
        return terminalStation;
    }

    /**
     * Проверка на существование предыдущей станции
     */
    public void checkPreviousStationOnLine(Line line) {
        if (line.getStations() != null && // todo
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Set<Line> getLines() {
        return lines;
    }

    @Override
    public String toString() {
        return cityName;
    }

    public void setLines(Set<Line> lines) {
        this.lines = lines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subway subway = (Subway) o;
        return Objects.equals(cityName, subway.cityName) && Objects.equals(lines, subway.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityName, lines);
    }
}
