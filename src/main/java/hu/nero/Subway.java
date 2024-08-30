package hu.nero;

import hu.nero.exception.*;

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

    private static void checkLineIsEmpty(Line line) {
        if (line.getStations() != null && !line.getStations().isEmpty()) {
            throw new LineNotEmptyException("Line " + line.getColor() + " is not empty!");
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
     *
     * @param newNameOfStation terminal station
     */
    public Station createLastStation(String lineColor,
                                     String newNameOfStation,
                                     int timeToThePreviousStation,
                                     List<Station> transferStations) {
        checkStationNameNotExists(newNameOfStation); // проверка станция с таким именем не существует
        var currentLastStation = getLastStationInLine(lineColor); // текущая последняя станция Астория
        isStationLastInLine(currentLastStation);
        checkTimeToTheNextStation(timeToThePreviousStation);// Проверка, что время перегона > 0
        var newLastStation = new Station(newNameOfStation, getLine(lineColor), transferStations, this);
        Line line = getLine(lineColor);
        line.addStation(newLastStation);
        currentLastStation.setNext(newLastStation);
        currentLastStation.setTransitTimeInSeconds(timeToThePreviousStation);
        newLastStation.setPrevious(currentLastStation);
        return newLastStation;
    }

    /**
     * Получение последней станции в линии
     */
    private Station getLastStationInLine(String lineColor) {
        var line = getLine(lineColor);
        return line.getLastStation();
    }

    /**
     * Проверка на существование предыдущей станции
     *
     * @param currentLastStation lastStation
     */
    public void isStationLastInLine(Station currentLastStation) {
        if (currentLastStation.getPrevious() != null) {
            throw new PreviousAndNextStationException("The previous lastStation does not exist!");
        }
    }

    /**
     * Проверка, что время перегона > 0
     */
    public void checkTimeToTheNextStation(int timeToNextStation) {
        if (timeToNextStation <= 0) {
            throw new PreviousAndNextStationException("Time to the next Station < 0!");
        }
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
