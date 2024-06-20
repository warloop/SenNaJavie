package org.example.forum.enums;

public enum ActionType {
    ADD(1),
    EDIT(2),
    SHOW(3),
    HIDE(4),
    DELETED_BY_OWNER(5),
    BANNED(6),
    UNBANNED(7),
    DELETED_BY_MODERATOR(8);

    private final int value;

    // Konstruktor
    ActionType(int value) {
        this.value = value;
    }

    // Getter do pobierania wartości
    public int getValue() {
        return value;
    }
}
