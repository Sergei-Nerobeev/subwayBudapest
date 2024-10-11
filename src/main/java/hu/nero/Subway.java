package hu.nero;

import hu.nero.exception.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Subway {
    private String cityName;
    private Set<Line> lines;
    private int ticketCounter = 0;
    private static final int MAX_TICKET_NUMBER = 9999;
    private static final int CURRENT_YEAR = 2024;
    private static final int VALIDITY_PERIOD_DAYS = 30;
    private final List<MonthlyTicket> monthlyTickets;

    public Subway(String cityName) {
        this.cityName = cityName;
        this.lines = new HashSet<>();
        this.monthlyTickets = new ArrayList<>();
    }

    public List<MonthlyTicket> getMonthlyTickets() {
        return monthlyTickets;
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
        if (!line.getStations().isEmpty()) {
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
     * Получение последней станции в линии.
     */
    private Station getLastStationInLine(String lineColor) {
        var line = getLine(lineColor);
        return line.getLastStation();
    }

    /**
     * Метод проверяет, что переданная в него станция является последней на линии.
     *
     * @param station lastStation
     */
    public void isStationLastInLine(Station station) {
        if (station.getNext() != null) {
            throw new PreviousAndNextStationException("The Station is not last!");
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

    /**
     * Метод определения станции на пересадку
     */
    public Station getTransferStationIdentify(String startColor, String endColor) {
        Line startLine = getLine(startColor);
        for (Station station : startLine.getStations()) {
            List<Station> transferStations = station.getTransferStations();
            for (Station transferStation : transferStations) {
                String color = transferStation.getLine().getColor();
                if (color.equals(endColor)) {
                    return station;
                }
            }
        }
        throw new StationNameException("No transfer station");
    }

    /**
     * Метод возвращает количество перегонов между двумя станциями на одной линии, либо возвращает -1,
     * если конечная stationTwo стоит перед stationOne
     */
    public int getInterval(Station stationOne, Station stationTwo) {
        if (areStationsNull(stationOne, stationTwo))
            return -1;
        var line = stationOne.getLine();
        var sameLine = stationTwo.getLine();
        if (!line.getColor().equals(sameLine.getColor()))
            return -1;
        int interval = 0;
        boolean foundStart = false;
        for (Station station : line.getStations()) {
            if (station.equals(stationOne)) {
                foundStart = true;
            }
            if (foundStart) {
                if (station.equals(stationTwo)) {
                    return interval;
                }
                interval++;
            }
        }
        return -1;
    }

    /**
     * Метод возвращает количество перегонов между двумя станциями на одной линии идя от конечной
     * к начальной по previous, либо возвращает -1, если конечная stationTwo стоит перед stationOne
     */
    public int getIntervalFromLastStation(Station stationOne, Station stationTwo) {
        if (areStationsNull(stationOne, stationTwo))
            return -1;
        var line = stationOne.getLine();
        var sameLine = stationTwo.getLine();
        if (!line.getColor().equals(sameLine.getColor()))
            return -1;
        int interval = 0;
        Station currentStation = stationTwo;
        while (currentStation != null) {
            if (currentStation.equals(stationOne)) {
                return interval;
            }
            currentStation = currentStation.getPrevious();
            interval++;
        }
        return -1;
    }

    /**
     * Метод возвращает количество перегонов между двумя станциями на одной линии, вне зависимости от того,
     * стоит stationOne перед stationTwo или наоборот.
     */
    public int getIntervalOnOneLine(Station stationOne, Station stationTwo) {
        int intervalOne = getInterval(stationOne, stationTwo);
        if (intervalOne != -1) {
            return intervalOne;
        }
        int intervalTwo = getIntervalFromLastStation(stationOne, stationTwo);
        if (intervalTwo != -1) {
            return intervalTwo;
        }
        throw new RuntimeException("Neither interval found between the specified stations.");
    }

    /**
     * Проверка на null
     */
    private static boolean areStationsNull(Station stationOne, Station stationTwo) {
        return stationOne == null || stationTwo == null;
    }

    /**
     * Метод считает количество перегонов между двумя станциями на разных линиях
     */
    public int getIntervalFromDifferentLines(Station start, Station finish) {
        if (areStationsNull(start, finish) || start.equals(finish)) {
            return -1;
        }
        // если линии совпали:
        var line = start.getLine();
        var otherLine = finish.getLine();
        if (line.getColor().equals(otherLine.getColor())) {
            return getIntervalOnOneLine(start, finish);
        }
        // если линии не совпали:
        Station transferStation =
                getTransferStationIdentify(line.getColor(), otherLine.getColor());
        int intervalForFirstLine = getIntervalOnOneLine(start, transferStation);

        Station transferStation2 =
                getTransferStationIdentify(otherLine.getColor(), line.getColor());
        int intervalForSecondLine = getIntervalOnOneLine(finish, transferStation2);
        return intervalForFirstLine + intervalForSecondLine;
    }

    // метод генерации номера проездного билета
    public String generateMonthlyTicketNumber() {
        if (ticketCounter > MAX_TICKET_NUMBER) {
            throw new RuntimeException("Today, all the monthly tickets are sold out");
        }
        var ticketNumber = String.format("a%04d", ticketCounter);
        ticketCounter++;
        return ticketNumber;
    }

    // метод создания проездного билета
    public MonthlyTicket createMonthlyTicket() {
        var ticketNumber = generateMonthlyTicketNumber();
        var today = LocalDate.now();
        MonthlyTicket monthlyTicket = new MonthlyTicket(ticketNumber, today);
        monthlyTickets.add(monthlyTicket);
        return monthlyTicket;
    }

    // метод проверки проездного билета в базе проданных билетов
    public boolean isTicketInSystem(String ticketNumber) {
        for (MonthlyTicket monthlyTicket : monthlyTickets) {
            if (monthlyTicket.ticketNumber().equals(ticketNumber)) {
                return true;
            }
        }
        throw new RuntimeException("Ticket unavailable! Because the ticket's not in the system.");
    }

    // метод проверки кол-ва знаков номера проездного билета
    private boolean isValidCountOfNumbers(String ticketNumber) {
        if (ticketNumber != null && ticketNumber.matches("a\\d{4}")) {
            return true;
        }
        throw new RuntimeException("Number of ticket is not valid! Because: " + ticketNumber + "is wrong!");
    }

    // метод проверки действительности проездного билета
    public boolean isValidMonthlyTicket(String ticketNumber, LocalDate checkDate) {
        if (isValidYear(checkDate)
                && isValidCountOfNumbers(ticketNumber)
                && isTicketInSystem(ticketNumber)
                && isValidDate(checkDate)) {
            return true;
        }
        throw new RuntimeException("Your ticket is not valid!");
    }

    // метод проверки даты
    private boolean isValidDate(LocalDate date) { //todo?
        for (MonthlyTicket monthlyTicket : monthlyTickets) {
            LocalDate purchaseDate = monthlyTicket.purchaseDate();
            LocalDate expirationDate = monthlyTicket.purchaseDate().plusDays(VALIDITY_PERIOD_DAYS);
            if (date.isBefore(purchaseDate) || date.isAfter(expirationDate)) {
                throw new RuntimeException("Your ticket is not valid!");
            }
        }
        return true;
    }

    // метод проверки года
    private boolean isValidYear(LocalDate today) {
        return today.getYear() == CURRENT_YEAR;
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
        return "Subway{" +
                "cityName='" + cityName + '\'' +
                ", lines=" + lines.toString() +
                '}';
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
