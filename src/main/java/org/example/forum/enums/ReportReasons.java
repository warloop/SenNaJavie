package org.example.forum.enums;

public enum ReportReasons {
    SEARWORD(1),
    THREATS(2),
    HARASSMENT(3),
    SPAM(4),
    OTHER(5);

    private final int value;

    // Konstruktor enuma
    ReportReasons(int value) {
        this.value = value;
    }

    // Getter do pobierania wartości enuma
    public int getValue() {
        return value;
    }
}