package calendar.model.event;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import calendar.model.enumclass.DayOfWeekAbbreviation;

/**
 * Represents a rule for recurring events in a calendar system.
 * This class defines the days of the week on which the event occurs,
 * the number of repetitions, the end date for the recurrence, and the time
 * during which the event takes place.
 */
public class RecurringEventRule {
  private final Set<DayOfWeek> dayOfWeeks;
  private final int repetitionCount;
  private final LocalDate untilDate;
  private final LocalTime startTime;
  private final LocalTime endTime;
  private final boolean isAllDay;


  /**
   * Constructs a RecurringEventRule with the specified parameters.
   * @param day the days of the week on which the event occurs, represented as a string
   * @param repetitionCount the number of times the event repeats
   * @param untilDate the date until which the event repeats in this series.
   * @param startTime the start time of the event in the series
   * @param endTime the end time of the event in the series
   * @param isAllDay  whether the event is an all-day event
   */
  public RecurringEventRule(String day, int repetitionCount, LocalDate untilDate,
                            LocalTime startTime, LocalTime endTime, boolean isAllDay) {
    this.dayOfWeeks = this.parseDay(day);
    this.repetitionCount = repetitionCount;
    this.untilDate = untilDate;
    this.startTime = startTime;
    this.endTime = endTime;
    this.isAllDay = isAllDay;
  }

  /**
   * Parses the given string representing the days of the week into a set of enums.
   * Each character in the string should represent a day of the week.
   * (e.g., "MTWRFSU" for Monday to Sunday).
   *
   * @param day the string representing days of the week
   * @return a set of DayOfWeekAbbreviation corresponding to the characters in the string
   */
  private Set<DayOfWeek> parseDay(String day) {
    //empty enum set
    EnumSet<DayOfWeek> result = EnumSet.noneOf(DayOfWeek.class);
    //break string into char and convert to DayOfWeekAbbreviation
    for (char c : day.toCharArray()) {
      //convert char to DayOfWeekAbbreviation and add to result, will throw exception if invalid
      result.add(DayOfWeekAbbreviation.charToAbbr(c).getDayOfWeek());
    }
    return result;
  }

  /**
   * Generates a list of occurrence dates based on the start date and the recurrence rule.
   *
   * @param startDate the given date from which to start generating occurrences
   * @return a list of LocalDate representing the occurrence dates
   */
  public List<LocalDate> generateOccurrenceDate(LocalDate startDate) {
    List<LocalDate> dates = new ArrayList<>();
    LocalDate currentDate = startDate;
    int count = 0;

    while ((this.repetitionCount == 0 || count < this.repetitionCount)
            //no untilDate or current date is before untilDate
            && (this.untilDate == null || currentDate.isBefore(this.untilDate))) {
      if (this.dayOfWeeks.contains(currentDate.getDayOfWeek())) {
        dates.add(currentDate);
        count++;
      }
      currentDate = currentDate.plusDays(1);
    }
    return dates;
  }

  /**
   * Returns the set of days of the week on which the event occurs.
   * @return a set of DayOfWeekAbbreviation representing the days of the week
   */
  private Set<DayOfWeek> getDayOfWeeks() {
    return this.dayOfWeeks;
  }

  /**
   * Return the number of times this event repeats.
   * @return the repetition count.
   */
  private int getRepetitionCount() {
    return this.repetitionCount;
  }

  /**
   * Return the date until which this event repeats.
   * @return the until date, or null if it repeats indefinitely.
   */
  private LocalDate getUntilDate() {
    return this.untilDate;
  }

  /**
   * Return the start time of the event in the series.
   * @return the start time of the event.
   */
  private LocalTime getStartTime() {
    return this.startTime;
  }

  /**
   * Return the end time of the event in the series.
   * @return the end time of the event.
   */
  private LocalTime getEndTime() {
    return this.endTime;
  }

  /**
   * Check if the event is an all-day event.
   * @return true if the event is all-day, false otherwise.
   */
  private boolean isAllDay() {
    return this.isAllDay;
  }
}
