package calendar.model.calendarclass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import calendar.model.event.Event;

/**
 * This interface defines the methods for managing a calendar system.
 */
public interface ICalendar {


  /**
   * Retrieves an event based on its subject and start and end date-time.
   *
   * @param subject       the subject of the event
   * @param startDateTime the start date and time of the event
   * @param endDateTime   the end date and time of the event
   * @return the event if found, otherwise null
   */
  Event getEvent(String subject, LocalDateTime startDateTime, LocalDateTime endDateTime);

  /**
   * Retrieves all events on a specific date.
   *
   * @param dateTime the date to check for events
   * @return a list of events on that date
   */
  List<Event> getEventsOnDate(LocalDate dateTime);

  /**
   * Retrieves all events within a specified date-time range.
   *
   * @param startDateTime the start date and time of the range
   * @param endDateTime   the end date and time of the range
   * @return a list of events within that range
   */
  List<Event> getEventsInRange(LocalDateTime startDateTime, LocalDateTime endDateTime);

  /**
   * Checks if there are any events scheduled at a specific date-time.
   *
   * @param time the date and time to check
   * @return true if there is an event at that time, false otherwise
   */
  boolean isBusy(LocalDateTime time);

  /**
   * Retrieves all events associated with a specific series ID.
   *
   * @param seriesId the ID of the series
   * @return a list of events that belong to that series
   */
  List<Event> getEventsBySeriesId(String seriesId);

  /**
   * Finds events by subject and start date.
   *
   * @param subject the subject of the event
   * @param date    the date to search for events
   * @return a list of events that match the subject and start date
   */
  List<Event> findEventsBySubjectAndStart(String subject, LocalDateTime date);

  /**
   * Creates a single event using the provided fields.
   * @param eventSubject the subject of the event
   * @param startDateTime the start time and date of the event
   * @param endDateTime the ending time and date of the event
   */
  void createSingleEvent(String eventSubject, String startDateTime, String endDateTime);

  /**
   * Creates a recurring event using the provided fields that repeats on specified days for a
   * certain number of times.
   * @param eventSubject the subject of the event
   * @param startDateTime the start time and date of the event
   * @param endDateTime the ending time and date of the event
   * @param weekdays the days of the week on which the event occurs
   * @param repeats how many times the event repeats
   */
  void createRecurringEvent(String eventSubject, String startDateTime, String endDateTime,
                            String weekdays, int repeats);

  /**
   * Creates a recurring event using the provided fields that repeats on specified days until
   * a specified date.
   * @param eventSubject the subject of the event
   * @param startDateTime the start time and date of the event
   * @param endDateTime the ending time and date of the event
   * @param weekdays the days of the week on which the event occurs
   * @param untilDate the day the event cannot occur past
   */
  void createRecurringEvent(String eventSubject, String startDateTime, String endDateTime,
                            String weekdays, String untilDate);

  /**
   * Creates an all-day event (which starts at 8:00 AM EST and ends at 5:00 PM EST) on the given
   * date.
   * @param eventSubject the subject of the event
   * @param onDate the date of the event
   */
  void createAllDayEvent(String eventSubject, String onDate);

  /**
   * Creates a recurring all-day event (which starts at 8:00 AM EST and ends at 5:00 PM EST) on the
   * given date, that repeats on specified days until a specified date.
   * @param eventSubject the subject of the event
   * @param onDate the date of the event
   * @param weekdays the days of the week on which the event occurs
   * @param untilDate the day the event cannot occur past
   */
  void createRecurringAllDayEvent(String eventSubject, String onDate,
                                  String weekdays, String untilDate);

  /**
   * Creates a recurring all-day event (which starts at 8:00 AM EST and ends at 5:00 PM EST) on the
   * given date, that repeats on specified days for a certain number of times.
   * @param eventSubject the subject of the event
   * @param onDate the date of the event
   * @param weekdays the days of the week on which the event occurs
   * @param repeats how many times the event repeats
   */
  void createRecurringAllDayEvent(String eventSubject, String onDate,
                                  String weekdays, int repeats);

  /**
   * Edits the given property of a single event with the given subject, start time, and end time,
   * replacing it with the given new value.
   * @param property the property of the event to be edited
   * @param eventSubject the subject of the event
   * @param startDateTime the start time of the event
   * @param endDateTime the end time of the event
   * @param newValue the new value replacing the current one for the given property
   */
  void editSingleEvent(String property, String eventSubject, String startDateTime,
                       String endDateTime, String newValue);

  /**
   * Edits the given properties of multiple events with the given subject and start time, replacing
   * them with the given new value.
   * @param property the property of the event to be edited
   * @param eventSubject the subject of the event
   * @param startDateTime the start time of the event
   * @param newValue the new value replacing the current one for the given property
   * @param editSeries whether this method should aim to edit a series
   */
  void editMultipleEvents(String property, String eventSubject, String startDateTime,
                          String newValue, boolean editSeries);

  /**
   * Returns the date formatter for the implementation of a Calendar object.
   * @return a DateFormatter which contains the formatting of a date for a Calendar implementation.
   */
  DateTimeFormatter getDateFormatter();

  /**
   * Returns the date-time formatter for the implementation of a Calendar object.
   * @return a DateTimeFormatter which contains the formatting of a date-time for a Calendar
   *     implementation.
   */
  DateTimeFormatter getDateTimeFormatter();
}
