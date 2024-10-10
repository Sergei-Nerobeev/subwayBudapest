package hu.nero;

public enum Months {
    JAN("31"), FEB("28"), MAR("31"),APR("30"), MAY("31"),
    JUN("30"), JUL("31"), AUG("31"), SEN("30"), OKT("31"),
    NOV("30"), DEC("31");

    private final String dayOfMonth;

    Months(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }
}
