package calendar.model.calendarclass;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Represents a calendar model with a name and time zone.
 * This class provides methods to convert between LocalDateTime and ZonedDateTime
 * in the context of the calendar's time zone.
 */
public class NewCalendarModel extends CalendarModel {
  private String name;
  private ZoneId timeZone;

  /**
   * Constructor for a NewCalendarModel.
   *
   * @param name     the name of the calendar
   * @param timeZone the time zone of the calendar
   */
  public NewCalendarModel(String name, ZoneId timeZone) {
    this.name = name;
    this.timeZone = timeZone;
  }

  /**
   * Get the name of the calendar.
   *
   * @return the name of the calendar
   */
  public String getName() {
    return this.name;
  }

  /**
   * Set the name of the calendar.
   *
   * @param name the new name of the calendar
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the time zone of the calendar.
   *
   * @return the time zone of the calendar
   */
  public ZoneId getTimeZone() {
    return this.timeZone;
  }

  /**
   * Set the time zone of the calendar.
   *
   * @param timeZone the new time zone of the calendar
   */
  public void setTimeZone(ZoneId timeZone) {
    this.timeZone = timeZone;
  }

  /**
   * Convert a given LocalDateTime to a ZonedDateTime in the calendar's time zone.
   *
   * @param local the given LocalDateTime to convert
   * @return the ZonedDateTime in the calendar's time zone
   */
  public ZonedDateTime toCalendarTime(LocalDateTime local) {
    return ZonedDateTime.of(local, timeZone);
  }

  /**
   * Convert a given ZonedDateTime to a LocalDateTime in the calendar's time zone.
   *
   * @param zone the given ZonedDateTime to convert
   * @return the LocalDateTime in the calendar's time zone
   */
  public LocalDateTime fromCalendarTime(ZonedDateTime zone) {
    return zone.withZoneSameInstant(timeZone).toLocalDateTime();
  }
}
