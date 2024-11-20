package hu.nero;

import hu.nero.exception.*;
import lombok.AccessLevel;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Subway {
    private static final int MAX_TICKET_NUMBER = 9999;
    private static final int VALIDITY_PERIOD_DAYS = 30;
    public static final int INTERVAL_NOT_FOUND = -1;
    @Getter(value = AccessLevel.PACKAGE)
    private final List<MonthlyTicket> monthlyTickets;
    private final Set<Line> lines;
    @Getter
    private final String cityName;
    private int ticketCounter = 0;

    public Subway(String cityName) {
        this.cityName = cityName;
        this.lines = new HashSet<>();
        this.monthlyTickets = new ArrayList<>();
    }

    public Line createNewLine(String newLineColor) {
        if (isLineWithThisColorExists(newLineColor)) {
            throw new ColorLineException(newLineColor + " already exists!");
        }
        var line = new Line(newLineColor, this);
        lines.add(line);
        return line;
    }

    boolean isLineWithThisColorExists(String newLineColor) {
        return lines.stream()
                .map(Line::getColor)
                .anyMatch(color -> color.equals(newLineColor));
    }

    boolean isStationNameExistsInAnyLine(String nameStation) {
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .map(Station::getName)
                .anyMatch(station -> station.equals(nameStation));
    }

    /**
     * Создание первой станции на линии
     */
    public Station createFirstStation(String lineColor, String stationName, List<Station> transferStations) {
        checkLineExists(lineColor);
        checkStationNameNotExists(stationName);
        Line line = getLine(lineColor);
        checkLineIsEmpty(line);
        var station =
                Station
                        .builder()
                        .name(stationName)
                        .line(line)
                        .transferStations(transferStations)
                        .subway(this)
                        .build();
        line.addStation(station);
        return station;
    }

    private static void checkLineIsEmpty(Line line) {
        if (!line.getStations().isEmpty()) {
            throw new LineNotEmptyException("Line " + line.getColor() + " is not empty!");
        }
    }

    private void checkStationNameNotExists(String stationName) {
        if (isStationNameExistsInAnyLine(stationName)) {
            throw new StationNameException(stationName + " station already exists!");
        }
    }

    private void checkLineExists(String lineColor) {
        if (!isLineWithThisColorExists(lineColor)) {
            throw new ColorLineException(lineColor + " Line color doesn't exist!");
        }
    }

    public Line getLine(String lineColor) {
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
    public Station createLastStation(
            String lineColor, String newNameOfStation, int timeToThePreviousStation, List<Station> transferStations) {
        checkStationNameNotExists(newNameOfStation);
        var currentLastStation = getLastStationInLine(lineColor);
        isStationLastInLine(currentLastStation);
        checkTimeToTheNextStation(timeToThePreviousStation);
        var newLastStation =
                Station
                        .builder()
                        .name(newNameOfStation)
                        .line(getLine(lineColor))
                        .transferStations(transferStations)
                        .subway(this)
                        .build();
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
    private void isStationLastInLine(Station station) {
        if (station.getNext() != null) {
            throw new PreviousAndNextStationException("The Station is not last!");
        }
    }

    /**
     * Проверка, что время перегона > 0
     */
    private void checkTimeToTheNextStation(int timeToNextStation) {
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
            if (transferStations == null) {
                continue;
            }
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
    int getInterval(Station stationOne, Station stationTwo) {
        if (areStationsNull(stationOne, stationTwo)) {
            return INTERVAL_NOT_FOUND;
        }
        var line = stationOne.getLine();
        var sameLine = stationTwo.getLine();
        if (!line.getColor().equals(sameLine.getColor())) {
            return INTERVAL_NOT_FOUND;
        }
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
        return INTERVAL_NOT_FOUND;
    }

    /**
     * Метод возвращает количество перегонов между двумя станциями на одной линии идя от конечной
     * к начальной по previous, либо возвращает -1, если конечная stationTwo стоит перед stationOne
     */
    int getIntervalFromLastStation(Station stationOne, Station stationTwo) {
        if (areStationsNull(stationOne, stationTwo)) {
            return INTERVAL_NOT_FOUND;
        }
        var line = stationOne.getLine();
        var sameLine = stationTwo.getLine();
        if (!line.getColor().equals(sameLine.getColor())) {
            return INTERVAL_NOT_FOUND;
        }
        int interval = 0;
        Station currentStation = stationTwo;
        while (currentStation != null) {
            if (currentStation.equals(stationOne)) {
                return interval;
            }
            currentStation = currentStation.getPrevious();
            interval++;
        }
        return INTERVAL_NOT_FOUND;
    }

    /**
     * Метод возвращает количество перегонов между двумя станциями на одной линии, вне зависимости от того,
     * стоит stationOne перед stationTwo или наоборот.
     */
    int getIntervalOnOneLine(Station stationOne, Station stationTwo) {
        int intervalOne = getInterval(stationOne, stationTwo);
        if (intervalOne != INTERVAL_NOT_FOUND) {
            return intervalOne;
        }
        int intervalTwo = getIntervalFromLastStation(stationOne, stationTwo);
        if (intervalTwo != INTERVAL_NOT_FOUND) {
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
            return INTERVAL_NOT_FOUND;
        }
        // если линии совпали:
        var line = start.getLine();
        var otherLine = finish.getLine();
        if (line.getColor().equals(otherLine.getColor())) {
            return getIntervalOnOneLine(start, finish);
        }
        // если линии не совпали:
        Station transferStation = getTransferStationIdentify(line.getColor(), otherLine.getColor());
        int intervalForFirstLine = getIntervalOnOneLine(start, transferStation);

        Station transferStation2 = getTransferStationIdentify(otherLine.getColor(), line.getColor());
        int intervalForSecondLine = getIntervalOnOneLine(finish, transferStation2);
        return intervalForFirstLine + intervalForSecondLine;
    }

    // метод генерации номера проездного билета
    String generateMonthlyTicketNumber() {
        if (ticketCounter > MAX_TICKET_NUMBER) {
            throw new SoldoutException("Today, all the monthly tickets are sold out");
        }
        var ticketNumber = String.format("a%04d", ticketCounter);
        ticketCounter++;
        return ticketNumber;
    }

    // метод создания проездного билета
    MonthlyTicket createMonthlyTicket() {
        var ticketNumber = generateMonthlyTicketNumber();
        var today = LocalDate.now();
        MonthlyTicket monthlyTicket = new MonthlyTicket(ticketNumber, today);
        monthlyTickets.add(monthlyTicket);
        return monthlyTicket;
    }

    // метод для тестирования:
    MonthlyTicket createMonthlyTicket(LocalDate date) {
        var ticketNumber = generateMonthlyTicketNumber();
        MonthlyTicket monthlyTicket = new MonthlyTicket(ticketNumber, date);
        monthlyTickets.add(monthlyTicket);
        return monthlyTicket;
    }

    // метод проверки проездного билета в базе проданных билетов
    public boolean isTicketInSystem(String ticketNumber) {
        for (MonthlyTicket monthlyTicket : monthlyTickets) {
            if (monthlyTicket.getTicketNumber().equals(ticketNumber)) {
                return true;
            }
        }
        return false;
    }

    // метод проверки кол-ва знаков номера проездного билета
    private boolean isValidCountOfNumbers(String ticketNumber) {
        return ticketNumber != null && ticketNumber.matches("a\\d{4}");
    }

    // метод проверки действительности проездного билета
    public boolean isValidMonthlyTicket(String ticketNumber, LocalDate checkDate) {
        return isValidCountOfNumbers(ticketNumber)
                && isTicketInSystem(ticketNumber)
                && isValidDate(ticketNumber, checkDate);
    }

    // метод проверки даты
    private boolean isValidDate(String ticketNumber, LocalDate checkDate) {
        MonthlyTicket foundTicket = null;
        for (MonthlyTicket monthlyTicket : monthlyTickets) {
            if (monthlyTicket.getTicketNumber().equals(ticketNumber)) {
                foundTicket = monthlyTicket;
                break;
            }
        }
        if (foundTicket == null) {
            return false;
        }
        var purchaseDate = foundTicket.getPurchaseDate();
        var expirationDate = foundTicket.getPurchaseDate().plusDays(VALIDITY_PERIOD_DAYS);
        return checkDate.isAfter(purchaseDate) && checkDate.isBefore(expirationDate) || checkDate.isEqual(purchaseDate);
    }

    // метод печати доходов всех касс со всех станций по дням в которые были продажи
    public String printDailyRevenueFromAllTicketOffices(LocalDate saleDate) {
        StringBuilder revenueReport = new StringBuilder();
        revenueReport.append("Print revenue by date: ").append(saleDate).append("\n");
        int totalRevenue = 0;
        for (Line line : lines) {
            var stations = line.getStations();
            for (Station station : stations) {
                var ticketOffice = station.getTicketOffice();
                var ticketOfficeRevenue = ticketOffice.getDailyRevenue(saleDate);
                if (ticketOfficeRevenue != null) {
                    totalRevenue += ticketOfficeRevenue;
                    revenueReport
                            .append(station.getName())
                            .append(": ")
                            .append(ticketOfficeRevenue)
                            .append("\n");
                }
            }
        }
        revenueReport.append("Total revenue is: ").append(totalRevenue);
        return revenueReport.toString();
    }

    @Override
    public String toString() {
        return "Subway{" + "cityName='" + cityName + '\'' + ", lines=" + lines.toString() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Subway subway = (Subway) o;
        return Objects.equals(cityName, subway.cityName) && Objects.equals(lines, subway.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityName, lines);
    }

}
