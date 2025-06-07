package calendar.model.enumclass;

import java.time.DayOfWeek;

/**
 * Enum representing the days of the week with single-character abbreviations.
 * Each abbreviation corresponds to a specific DayOfWeek enum value.
 */
public enum DayOfWeekAbbreviation {
  M(DayOfWeek.MONDAY),
  T(DayOfWeek.TUESDAY),
  W(DayOfWeek.WEDNESDAY),
  R(DayOfWeek.THURSDAY),
  F(DayOfWeek.FRIDAY),
  S(DayOfWeek.SATURDAY),
  U(DayOfWeek.SUNDAY);

  private final DayOfWeek dayOfWeek;

  /**
   * Constructor to associate each abbreviation with its corresponding DayOfWeek.
   *
   * @param dayOfWeek the DayOfWeek associated with the abbreviation
   */
  DayOfWeekAbbreviation(DayOfWeek dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

  /**
   * Gets the DayOfWeek associated with this abbreviation.
   *
   * @return the DayOfWeek corresponding to this abbreviation.
   */
  public DayOfWeek getDayOfWeek() {
    return this.dayOfWeek;
  }

  /**
   * Converts a given character to its corresponding DayOfWeekAbbreviation.
   *
   * @param c the given character representing a day of the week.
   * @return the DayOfWeekAbbreviation corresponding to the character.
   */
  public static DayOfWeekAbbreviation charToAbbr(char c) {
    for (DayOfWeekAbbreviation abr : values()) {
      if (abr.name().charAt(0) == Character.toUpperCase(c)) {
        return abr;
      }
    }
    throw new IllegalArgumentException("Invalid day abbreviation!");
  }

}
