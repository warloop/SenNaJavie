package org.example.forum.enums;

/**
 * Enum przechowujące wszystkie możliwe stany reprezentujące akcje wykonane na
 * obiektach zarządzalnych przez zewnętrznych użytkowników aplikacji.
 *
 * Używane w => @see org.example.forum.repositories.ActionRepository oraz org.example.forum.services.ActionService
 *
 * @see db.migration V1.0.8__Create_action_types_tables.sql
 * @author Artur Leszczak
 * @version 1.0.0
 */
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

    // Konstruktor enuma
    ActionType(int value) {
        this.value = value;
    }

    // Getter do pobierania wartości enuma
    public int getValue() {
        return value;
    }
}
