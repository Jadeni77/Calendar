package calendar.model.calendarclass;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import calendar.model.enumclass.EventStatus;
import calendar.model.enumclass.DayOfWeekAbbreviation;
import calendar.model.enumclass.Location;
import calendar.model.event.Event;
import calendar.model.event.RecurringEventRule;

/**
 * This class represent a calendar model that implements the ICalendar interface.
 * It manages events, allows adding, retrieving, updating, and removing events,
 * and supports recurring events through series IDs.
 */
public class CalendarModel implements ICalendar {
  protected final Map<String, Event> events;
  protected final Map<String, Set<Event>> seriesEvents; //use set bc cannot have duplicate events
  protected final DateTimeFormatter dateFormatter;
  protected final DateTimeFormatter dateTimeFormatter;

  /**
   * Constructor for CalendarModel.
   * Initializes the events and seriesEvents to empty maps.
   */
  public CalendarModel() {
    this.events = new HashMap<>();
    this.seriesEvents = new HashMap<>();
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

  /**
   * Adds a single event to the calendar.
   *
   * @param event the event to be added
   */
  public void addEvent(Event event) {
    //generate a unique ID for the event based on its subject and time
    String id = this.eventKey(event);

    if (this.events.containsKey(id)) {
      throw new IllegalArgumentException("Event already exists with the same subject and time.");
    }
    this.events.put(id, event);

    if (event.getSeriesId() != null) {
      this.seriesEvents.computeIfAbsent(event.getSeriesId(), k -> new HashSet<>()).add(event);
    }
  }

  /**
   * Adds multiple events to the calendar.
   *
   * @param events the list of events to be added
   */
  private void addMultipleEvents(List<Event> events) {
    for (Event e : events) {
      this.addEvent(e);
    }
  }

  @Override
  public Event getEvent(String subject, LocalDateTime startDateTime, LocalDateTime endDateTime) {
    return this.events.get(eventKey(subject, startDateTime, endDateTime));
  }

  @Override
  public List<Event> getEventsOnDate(LocalDate dateTime) {
    LocalDateTime start = dateTime.atStartOfDay();
    LocalDateTime end = dateTime.atTime(23, 59, 59);
    return this.getEventsInRange(start, end);
  }

  /**
   * Returns all events that take place between two date-times, including events that only
   * partially occur in the range and are overlapping with it.
   *
   * @param startDateTime the starting date time of the range
   * @param endDateTime   the ending date time of the range
   * @return a list of Events which take place on the given date
   */
  @Override
  public List<Event> getEventsInRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    List<Event> result = new ArrayList<>();

    for (Event e : this.events.values()) {
      if (e.getStartDateTime().isBefore(endDateTime) &&
              e.getEndDateTime().isAfter(startDateTime)) {
        result.add(e);
      }
    }
    result.sort(Comparator.comparing(event -> event.getStartDateTime()));
    return result;
  }

  @Override
  public boolean isBusy(LocalDateTime time) {
    for (Event e : this.events.values()) {
      if (!e.getEndDateTime().isBefore(time) && !e.getStartDateTime().isAfter(time)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public List<Event> getEventsBySeriesId(String seriesId) {
    return new ArrayList<>(this.seriesEvents.getOrDefault(seriesId, Set.of()));
  }

  private void updateEvent(Event oldEvent, Event newEvent) {
    removeEvent(oldEvent);
    addEvent(newEvent);
  }

  /**
   * Removes the given event from the model.
   *
   * @param event the event to be removed
   */
  private void removeEvent(Event event) {
    this.events.remove(eventKey(event));
    //if the event is part of a series
    if (event.getSeriesId() != null) {
      //take a set containing the given event out of the seriesEvents map
      Set<Event> seriesEvents = this.seriesEvents.get(event.getSeriesId());
      //if the set is found
      if (seriesEvents != null) {
        //remove the event from the set
        seriesEvents.remove(event);
        //if the set is empty after removal, remove the series from the map
        if (seriesEvents.isEmpty()) {
          this.seriesEvents.remove(event.getSeriesId());
        }
      }
    }
  }

  @Override
  public List<Event> findEventsBySubjectAndStart(String subject, LocalDateTime date) {
    List<Event> result = new ArrayList<>();

    for (Event e : this.events.values()) {
      if (e.getSubject().equals(subject) && e.getStartDateTime().equals(date)) {
        result.add(e);
      }
    }
    return result;
  }

  /**
   * Generates a unique key for the event based on its subject and time.
   *
   * @param event the event to generate the key for
   * @return a unique string key for the event
   */
  private String eventKey(Event event) {
    return eventKey(event.getSubject(), event.getStartDateTime(), event.getEndDateTime());
  }

  /**
   * Generates a unique key for the event based on its subject and time.
   *
   * @param subject       the subject of the event
   * @param startDateTime the start date and time of the event
   * @param endDateTime   the end date and time of the event
   * @return a unique string key for the event
   */
  private String eventKey(String subject, LocalDateTime startDateTime, LocalDateTime endDateTime) {
    return subject + "|" + startDateTime + "|" + endDateTime;
  }

  @Override
  public void createSingleEvent(String eventSubject, String startDateTime, String endDateTime) {
    LocalDateTime start = LocalDateTime.parse(startDateTime, dateTimeFormatter);
    LocalDateTime end = LocalDateTime.parse(endDateTime, dateTimeFormatter);
    Event event = new Event.EventBuilder()
            .subject(eventSubject)
            .startDateTime(start)
            .endDateTime(end)
            .build();
    this.addEvent(event);
  }

  /**
   * Creates a recurring event using the provided fields that repeats on specified days for\
   * a certain number of times. Throws an exception if the given start time and end time are
   * more than 1 day apart, as recurring events cannot be longer than 1 day.
   *
   * @param eventSubject  the subject of the event
   * @param startDateTime the start time and date of the event
   * @param endDateTime   the ending time and date of the event
   * @param weekdays      the days of the week on which the event occurs
   * @param repeats       how many times the event repeats
   */
  @Override
  public void createRecurringEvent(String eventSubject, String startDateTime, String endDateTime,
                                   String weekdays, int repeats) {
    LocalDateTime start = LocalDateTime.parse(startDateTime, dateTimeFormatter);
    LocalDateTime end = LocalDateTime.parse(endDateTime, dateTimeFormatter);

    if (!start.toLocalDate().equals(end.toLocalDate())) {
      throw new IllegalArgumentException("Recurring events must not span multiple days.");
    }

    List<Event> recurringEvents = new ArrayList<>();
    String seriesId = UUID.randomUUID().toString();

    List<DayOfWeek> daysList = abbreviationsToWeekdays(weekdays);
    int i = 0;
    while (repeats > 0) {
      LocalDateTime currentStart = start.plusDays(i);
      LocalDateTime currentEnd = end.plusDays(i);
      if (daysList.contains(currentStart.getDayOfWeek())) {
        Event.EventBuilder builder = new Event.EventBuilder()
                .subject(eventSubject)
                .status(EventStatus.PUBLIC)
                .seriesId(seriesId);

        builder.startDateTime(currentStart).endDateTime(currentEnd).isAllDayEvent(false);
        recurringEvents.add(builder.build());
        repeats--;
      }
      i++;
    }
    this.addMultipleEvents(recurringEvents);
  }

  /**
   * Creates a recurring event using the provided fields that repeats on specified days for\
   * a certain number of times. Throws an exception if the given start time and end time are
   * more than 1 day apart, as recurring events cannot be longer than 1 day.
   *
   * @param eventSubject  the subject of the event
   * @param startDateTime the start time and date of the event
   * @param endDateTime   the ending time and date of the event
   * @param weekdays      the days of the week on which the event occurs
   * @param untilDate     the day the event cannot occur past
   */
  @Override
  public void createRecurringEvent(String eventSubject, String startDateTime, String endDateTime,
                                   String weekdays, String untilDate) {
    LocalDateTime start = LocalDateTime.parse(startDateTime, dateTimeFormatter);
    LocalDateTime end = LocalDateTime.parse(endDateTime, dateTimeFormatter);

    if (!start.toLocalDate().equals(end.toLocalDate())) {
      throw new IllegalArgumentException("Recurring events must not span multiple days.");
    }

    LocalDate until = LocalDate.parse(untilDate, dateFormatter);

    Duration daysDifference = Duration.between(start, end);
    int days = (int) daysDifference.toDays();

    RecurringEventRule rule = new RecurringEventRule(weekdays, days,
            until, start.toLocalTime(), end.toLocalTime(), false);

    List<Event> recurringEvent = new ArrayList<>();
    List<LocalDate> occurrenceDates = rule.generateOccurrenceDate(start.toLocalDate());
    String seriesId = UUID.randomUUID().toString();

    for (LocalDate date : occurrenceDates) {
      Event.EventBuilder builder = new Event.EventBuilder()
              .subject(eventSubject)
              .status(EventStatus.PUBLIC)
              .seriesId(seriesId);

      LocalDateTime currentStart = LocalDateTime.of(date, start.toLocalTime());
      LocalDateTime currentEnd = LocalDateTime.of(date, end.toLocalTime());

      builder.startDateTime(currentStart).endDateTime(currentEnd).isAllDayEvent(false);
      recurringEvent.add(builder.build());
    }
    this.addMultipleEvents(recurringEvent);
  }

  @Override
  public void createAllDayEvent(String eventSubject, String onDate) {
    LocalDate date = LocalDate.parse(onDate, dateFormatter);
    LocalDateTime start = date.atTime(8, 0);
    LocalDateTime end = date.atTime(17, 0);

    Event event = new Event.EventBuilder()
            .subject(eventSubject)
            .startDateTime(start)
            .endDateTime(end)
            .isAllDayEvent(true)
            .build();
    this.addEvent(event);
  }

  @Override
  public void createRecurringAllDayEvent(String eventSubject, String onDate,
                                         String weekdays, String untilDate) {
    LocalDate date = LocalDate.parse(onDate, dateFormatter);
    LocalDate uDate = LocalDate.parse(untilDate, dateFormatter);

    int days = (int) ChronoUnit.DAYS.between(date, uDate);

    RecurringEventRule rule = new RecurringEventRule(weekdays, days,
            uDate, LocalTime.of(8, 0), LocalTime.of(17, 0), true);
    List<LocalDate> occurrenceDates = rule.generateOccurrenceDate(date);

    List<Event> recurringEvent = new ArrayList<>();
    String seriesId = UUID.randomUUID().toString();

    for (LocalDate d : occurrenceDates) {
      Event.EventBuilder builder = new Event.EventBuilder()
              .subject(eventSubject)
              .status(EventStatus.PUBLIC)
              .seriesId(seriesId);

      builder.startDateTime(d.atStartOfDay()).isAllDayEvent(true);

      recurringEvent.add(builder.build());
    }
    this.addMultipleEvents(recurringEvent);
  }

  @Override
  public void createRecurringAllDayEvent(String eventSubject, String onDate,
                                         String weekdays, int repeats) {
    LocalDate date = LocalDate.parse(onDate, this.dateFormatter);

    RecurringEventRule rule = new RecurringEventRule(weekdays, repeats,
            null, LocalTime.of(8, 0), LocalTime.of(17, 0),
            true);

    List<LocalDate> occurrenceDates = rule.generateOccurrenceDate(date);
    List<Event> recurringEvent = new ArrayList<>();
    String seriesId = UUID.randomUUID().toString();

    for (LocalDate dates : occurrenceDates) {
      Event.EventBuilder builder = new Event.EventBuilder()
              .subject(eventSubject)
              .status(EventStatus.PUBLIC)
              .seriesId(seriesId);

      builder.startDateTime(dates.atStartOfDay()).isAllDayEvent(true);

      recurringEvent.add(builder.build());
    }
    this.addMultipleEvents(recurringEvent);
  }

  /**
   * Edits the given property of a single event with the given subject, start time, and end time,
   * replacing it with the given new value. Handles exceptions related to incorrect input and
   * the absence of any relevant events to edit.
   *
   * @param property      the property of the event to be edited
   * @param eventSubject  the subject of the event
   * @param startDateTime the start time of the event
   * @param endDateTime   the end time of the event
   * @param newValue      the new value replacing the current one for the given property
   */
  @Override
  public void editSingleEvent(String property, String eventSubject, String startDateTime,
                              String endDateTime, String newValue) {
    LocalDateTime startTime = null;
    LocalDateTime endTime = null;

    try {
      startTime = LocalDateTime.parse(startDateTime, dateTimeFormatter);
      endTime = LocalDateTime.parse(endDateTime, dateTimeFormatter);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid date format. Please use 'yyyy-MM-dd'T'HH:mm'.");
    }

    Event targetEvent = getEvent(eventSubject, startTime, endTime);

    if (targetEvent == null) {
      throw new IllegalArgumentException("No event found with subject '" + eventSubject + "' " +
              "starting at " + startTime.format(dateFormatter) + " " +
              "and ending at " + endTime.format(dateFormatter) + ".");
    }

    editEventHelper(targetEvent, property, newValue);
  }

  /**
   * Edits the given properties of multiple events with the given subject and start time,
   * replacing them with the given new value. Handles exceptions related to incorrect input and
   * the absence of any relevant events to edit.
   *
   * @param property      the property of the event to be edited
   * @param eventSubject  the subject of the event
   * @param startDateTime the start time of the event
   * @param newValue      the new value replacing the current one for the given property
   * @param editSeries    whether this method should aim to edit a series
   */
  @Override
  public void editMultipleEvents(String property, String eventSubject,
                                 String startDateTime, String newValue, boolean editSeries) {
    LocalDateTime startTime = null;

    try {
      startTime = LocalDateTime.parse(startDateTime, dateTimeFormatter);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid date format. Please use 'yyyy-MM-ddTHH:mm'.");
    }
    List<Event> targetEvents = findEventsBySubjectAndStart(eventSubject, startTime);
    if (targetEvents.isEmpty()) {
      throw new IllegalArgumentException("No events found with subject '" + eventSubject + "' "
              + "starting at " + startTime.format(dateTimeFormatter) + ".");
    } else if (targetEvents.size() > 1) {
      throw new IllegalArgumentException("Multiple events found with subject '" + eventSubject
              + "' and start time at " + startTime.format(dateTimeFormatter) + ". More than one"
              + " event cannot be edited at once.");
    }
    Event targetEvent = targetEvents.get(0);

    String seriesId = null;
    if (targetEvent.getSeriesId() != null && !targetEvent.getSeriesId().isEmpty()) {
      seriesId = targetEvent.getSeriesId();
    }
    if (seriesId == null) {
      editEventHelper(targetEvent, property, newValue);
    } else {
      List<Event> seriesEvents = getEventsBySeriesId(seriesId);
      for (Event seriesEvent : seriesEvents) {
        if (editSeries) {
          editEventHelper(seriesEvent, property, newValue);
        } else if (seriesEvent.getStartDateTime().isEqual(targetEvent.getStartDateTime())
                || seriesEvent.getStartDateTime().isAfter(targetEvent.getStartDateTime())) {
          editEventHelper(seriesEvent, property, newValue);
        }
      }
    }
  }

  /**
   * Helper method for editEvent that checks for the property being edited and
   * replace the oldValue with the given newValue.
   *
   * @param targetEvent the event being edited.
   * @param property    the property of event being edited.
   * @param newValue    the newValue of this edit.
   */
  private void editEventHelper(Event targetEvent, String property, String newValue) {
    Event.EventBuilder newEventBuilder = new Event.EventBuilder()
            .subject(targetEvent.getSubject())
            .startDateTime(targetEvent.getStartDateTime())
            .endDateTime(targetEvent.getEndDateTime())
            .description(targetEvent.getDescription())
            .location(targetEvent.getLocation())
            .status(targetEvent.getStatus())
            .seriesId(targetEvent.getSeriesId())
            .isAllDayEvent(targetEvent.getIsAllDayEvent());

    try {
      switch (property) {
        case "subject":
          newEventBuilder.subject(newValue);
          break;
        case "start":
          LocalDateTime newStart = LocalDateTime.parse(newValue, dateTimeFormatter);
          newEventBuilder.startDateTime(newStart);
          break;
        case "end":
          LocalDateTime newEnd = LocalDateTime.parse(newValue, dateTimeFormatter);
          newEventBuilder.endDateTime(newEnd).seriesId(UUID.randomUUID().toString());
          break;
        case "description":
          newEventBuilder.description(newValue);
          break;
        case "location":
          //convert location to enum
          newEventBuilder.location(Location.valueOf(newValue.trim().toUpperCase()));
          break;
        case "status":
          newEventBuilder.status(EventStatus.valueOf(newValue.trim().toUpperCase()));
          break;
        default:
          throw new IllegalArgumentException("Invalid property '" + property + "'. " +
                  "Valid properties are: subject, start, end, description, location, status.");
      }
      Event newEvent = newEventBuilder.build();
      updateEvent(targetEvent, newEvent);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid value for property '" + property + "': "
              + newValue + ". " + "Please ensure the value is correct.");
    }
  }

  /**
   * Abbreviate the given string into weekday abbreviations.
   *
   * @param abbreviations the given string as weekdays
   * @return a list of abbreviated weekdays
   */
  private List<DayOfWeek> abbreviationsToWeekdays(String abbreviations) {
    ArrayList<DayOfWeek> weekdays = new ArrayList<>();
    for (int i = 0; i < abbreviations.length(); i++) {
      char c = abbreviations.charAt(i);
      DayOfWeek day = DayOfWeekAbbreviation.charToAbbr(c).getDayOfWeek();
      weekdays.add(day);
    }
    return weekdays;
  }
}
