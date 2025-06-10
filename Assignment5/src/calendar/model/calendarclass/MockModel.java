package calendar.model.calendarclass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import calendar.model.event.Event;

/**
 * This class represents a mock calendar model, which logs the data inputted to each function
 * to a StringBuilder in order to verify that data has been received correctly.
 */
public class MockModel implements ICalendar {
  public StringBuilder log;
  private final DateTimeFormatter dateFormatter;
  private final DateTimeFormatter dateTimeFormatter;

  /**
   * Constructor for MockModel.
   * Initializes the events and seriesEvents to empty maps.
   */
  public MockModel() {
    this.log = new StringBuilder();
    this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
  }

  @Override
  public DateTimeFormatter getDateTimeFormatter() {
    return this.dateTimeFormatter;
  }

  @Override
  public DateTimeFormatter getDateFormatter() {
    return this.dateFormatter;
  }

  @Override
  public void addEvent(Event event) {

  }

  /**
   * Logs the inputted data for this method into this Model's log StringBuilder.
   * @param subject       the subject of the event
   * @param startDateTime the start date and time of the event
   * @param endDateTime   the end date and time of the event
   * @return a dummy Event
   */
  public Event getEvent(String subject, LocalDateTime startDateTime, LocalDateTime endDateTime) {
    log.append("Event Queried: ").append(subject)
            .append(", ").append(startDateTime.format(dateTimeFormatter))
            .append(", ").append(endDateTime.format(dateTimeFormatter)).append("\n");
    return new Event(subject, startDateTime, endDateTime);
  }

  /**
   * Logs the inputted data for this method into this Model's log StringBuilder.
   * @param dateTime the date to check for events
   * @return a dummy List of Events
   */
  @Override
  public List<Event> getEventsOnDate(LocalDate dateTime) {
    log.append("Event(s) Queried for date: ").append(dateTime.format(dateFormatter))
            .append("\n");
    return new ArrayList<>();
  }

  /**
   * Logs the inputted data for this method into this Model's log StringBuilder.
   * @param startDateTime the start date and time of the range
   * @param endDateTime   the end date and time of the range
   * @return a dummy List of Events
   */
  @Override
  public List<Event> getEventsInRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    log.append("Events Queried from Start: ").append(startDateTime.format(dateTimeFormatter))
            .append(", to End: ").append(endDateTime.format(dateTimeFormatter)).append("\n");
    return new ArrayList<>();
  }

  /**
   * Logs the inputted data for this method into this Model's log StringBuilder.
   * @param time the date and time to check
   * @return a dummy boolean
   */
  @Override
  public boolean isBusy(LocalDateTime time) {
    log.append("Check for business: ").append(time.format(dateTimeFormatter)).append("\n");
    return false;
  }

  /**
   * Logs the inputted data for this method into this Model's log StringBuilder.
   * @param seriesId the ID of the series
   * @return a dummy List of Events
   */
  @Override
  public List<Event> getEventsBySeriesId(String seriesId) {
    log.append("Series ID Queried: ").append(seriesId).append("\n");
    return new ArrayList<>();
  }

  /**
   * Logs the inputted data for this method into this Model's log StringBuilder.
   * @param subject the subject of the event
   * @param date    the date to search for events
   * @return a dummy List of Events
   */
  @Override
  public List<Event> findEventsBySubjectAndStart(String subject, LocalDateTime date) {
    log.append("Event Queried by Subject and Start Time: ").append(subject).append(", ")
            .append(date.format(dateTimeFormatter)).append("\n");
    return new ArrayList<>();
  }

  /**
   * Logs the inputted data for this method into this Model's log StringBuilder.
   * @param eventSubject  the subject of the event to be created
   * @param startDateTime the start date as a formatted String
   * @param endDateTime   the end date as a formatted String
   */
  public void createSingleEvent(String eventSubject, String startDateTime, String endDateTime) {
    log.append("Single Event Created: ").append(eventSubject).append(", ")
            .append(startDateTime).append(", ").append(endDateTime).append("\n");
  }

  /**
   * Logs the inputted data for this method into this Model's log StringBuilder.
   * @param eventSubject the subject of the event
   * @param startDateTime the start time and date of the event
   * @param endDateTime the ending time and date of the event
   * @param weekdays the days of the week on which the event occurs
   * @param repeats how many times the event repeats
   */
  @Override
  public void createRecurringEvent(String eventSubject, String startDateTime, String endDateTime,
                                   String weekdays, int repeats) {
    log.append("Recurring Event Created: ").append(eventSubject).append(", ")
            .append(startDateTime).append(", ").append(endDateTime).append(", on ")
            .append(weekdays).append(", repeats ").append(repeats).append(" times").append("\n");
  }

  /**
   * Logs the inputted data for this method into this Model's log StringBuilder.
   * @param eventSubject the subject of the event
   * @param startDateTime the start time and date of the event
   * @param endDateTime the ending time and date of the event
   * @param weekdays the days of the week on which the event occurs
   * @param untilDate the day the event cannot occur past
   */
  public void createRecurringEvent(String eventSubject, String startDateTime, String endDateTime,
                                   String weekdays, String untilDate) {
    log.append("Recurring Event Created: ").append(eventSubject).append(", ")
            .append(startDateTime).append(", ").append(endDateTime).append(", on ")
            .append(weekdays).append(", until ").append(untilDate).append("\n");
  }

  /**
   * Logs the inputted data for this method into this Model's log StringBuilder.
   * @param eventSubject the subject of the event
   * @param onDate the date of the event
   */
  @Override
  public void createAllDayEvent(String eventSubject, String onDate) {
    log.append("Single All-Day Event Created: ").append(eventSubject).append(", ")
            .append(onDate).append("\n");
  }

  /**
   * Logs the inputted data for this method into this Model's log StringBuilder.
   * @param eventSubject the subject of the event
   * @param onDate the date of the event
   * @param weekdays the days of the week on which the event occurs
   * @param untilDate the day the event cannot occur past
   */
  @Override
  public void createRecurringAllDayEvent(String eventSubject, String onDate, String weekdays,
                                         String untilDate) {
    log.append("Recurring Event Created: ").append(eventSubject).append(", on ")
            .append(onDate).append(", on days ").append(weekdays).append(", until ")
            .append(untilDate).append("\n");
  }

  /**
   * Logs the inputted data for this method into this Model's log StringBuilder.
   * @param eventSubject the subject of the event
   * @param onDate the date of the event
   * @param weekdays the days of the week on which the event occurs
   * @param repeats how many times the event repeats
   */
  @Override
  public void createRecurringAllDayEvent(String eventSubject, String onDate, String weekdays,
                                         int repeats) {
    log.append("Recurring Event Created: ").append(eventSubject).append(", on ")
            .append(onDate).append(", on days ").append(weekdays).append(", repeats ")
            .append(repeats).append(" times").append("\n");
  }

  /**
   * Logs the inputted data for this method into this Model's log StringBuilder.
   * @param property the property of the event to be edited
   * @param eventSubject the subject of the event
   * @param startDateTime the start time of the event
   * @param endDateTime the end time of the event
   * @param newValue the new value replacing the current one for the given property
   */
  @Override
  public void editSingleEvent(String property, String eventSubject, String startDateTime,
                              String endDateTime, String newValue) {
    log.append("Single Event Updated: ").append(property).append(", ").append(eventSubject)
            .append(", ").append(startDateTime).append(", ").append(endDateTime).append(", ")
            .append(newValue).append("\n");
  }

  /**
   * Logs the inputted data for this method into this Model's log StringBuilder.
   * @param property the property of the event to be edited
   * @param eventSubject the subject of the event
   * @param startDateTime the start time of the event
   * @param newValue the new value replacing the current one for the given property
   * @param editSeries whether this method should aim to edit a series
   */
  @Override
  public void editMultipleEvents(String property, String eventSubject, String startDateTime,
                                 String newValue, boolean editSeries) {
    log.append("Multiple Events Updated: ").append(property).append(", ").append(eventSubject)
            .append(", ").append(startDateTime).append(", ").append(newValue).append(", ")
            .append(editSeries).append("\n");
  }
}
