package calendar.model.calendarmanagerclass;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRulesException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import calendar.model.calendarclass.NewCalendarModel;
import calendar.model.event.Event;

/**
 * CalendarManager represents a manager for handling multiple calendars.
 * It allows creating, editing, and managing calendars,
 */
public class CalendarManagerModel implements ICalendarManager {
  private final Map<String, NewCalendarModel> calendars;
  private String currentCalendarName;

  /**
   * Constructs a CalendarManagerModel with an empty calendar map.
   */
  public CalendarManagerModel() {
    this.calendars = new HashMap<>();
  }

  @Override
  public void createCalendar(String name, String zoneId) {
    if (this.calendars.containsKey(name)) {
      throw new IllegalArgumentException("Calendar already exists");
    }
    ZoneId parseZone;
    try {
      parseZone = ZoneId.of(zoneId);
    } catch (DateTimeException e) {
      throw new IllegalArgumentException("Invalid or unsupported timezone ID: '" + zoneId + "'. " +
              "Please use IANA Time Zone Database format (e.g., 'America/New_York').");
    }

    calendars.put(name, new NewCalendarModel(name, parseZone));
  }

  @Override
  public void editCalendar(String nameOfCalendarToEdit, String property, String newValue) {
    NewCalendarModel targetCalendar = this.getTargetCalendar(nameOfCalendarToEdit);

    switch (property) {
      case "name":
        editNameHelp(nameOfCalendarToEdit, targetCalendar, newValue);
//        if (calendars.containsKey(newValue)) {
//          throw new IllegalArgumentException("New Calendar name already exists");
//        }
//        calendars.remove(nameOfCalendarToEdit);
//        targetCalendar.setName(newValue);
//        calendars.put(newValue, targetCalendar);
//
//        if (nameOfCalendarToEdit.equals(currentCalendarName)) {
//          currentCalendarName = newValue; // Update current calendar name if it was changed
//        }
        break;
      case "timezone":
        editTimeZoneHelp(targetCalendar, newValue);
//        ZoneId newZoneId;
//        try {
//          newZoneId = ZoneId.of(newValue);
//        } catch (ZoneRulesException e) {
//          throw new IllegalArgumentException("Invalid or unsupported timezone ID: '"
//                  + newValue + "'. " + "Please use IANA Time Zone Database format " +
//                  "(e.g., 'America/New_York').");
//        }
//        targetCalendar.setTimeZone(newZoneId);
        break;
      default:
        throw new IllegalArgumentException("Unsupported property '" + property
                + "' for calendar editing. " + "Valid properties are: 'name', 'timezone'.");
    }
  }

  /**
   * Helper method to edit the calendar's name.
   * @param nameOfCalendarToEdit the name of the calendar to edit
   * @param targetCalendar the calendar model to edit
   * @param newValue the new name for the calendar
   */
  private void editNameHelp(String nameOfCalendarToEdit, NewCalendarModel
          targetCalendar, String newValue) {
    if (calendars.containsKey(newValue)) {
      throw new IllegalArgumentException("New Calendar name already exists");
    }
    calendars.remove(nameOfCalendarToEdit);
    targetCalendar.setName(newValue);
    calendars.put(newValue, targetCalendar);

    if (nameOfCalendarToEdit.equals(currentCalendarName)) {
      currentCalendarName = newValue; // Update current calendar name if it was changed
    }
  }

  /**
   * Helper method to edit the calendar's time zone.
   * @param targetCalendar the calendar model to edit
   * @param newValue the new time zone ID for the calendar
   */
  private void editTimeZoneHelp(NewCalendarModel targetCalendar, String newValue) {
    ZoneId newZoneId;
    try {
      newZoneId = ZoneId.of(newValue);
    } catch (ZoneRulesException e) {
      throw new IllegalArgumentException("Invalid or unsupported timezone ID: '"
              + newValue + "'. " + "Please use IANA Time Zone Database format " +
              "(e.g., 'America/New_York').");
    }
    targetCalendar.setTimeZone(newZoneId);
  }

  @Override
  public void setCurrentCalendar(String name) {
    if (calendars.containsKey(name)) {
      currentCalendarName = name;
    } else {
      throw new IllegalArgumentException("Calendar '" + name + "' not found.");
    }

  }

  /**
   * To retrieve the calendar that is currently  "active" or being "focused on"
   * by the user. It represents the user's immediate working context.
   * If there is no current active calendar, or it is not in the manager's list of calendars,
   * this method will throw an exception prompting the user to select a calendar before proceeding.
   * @return the current calendar being in use
   */
  @Override
  public NewCalendarModel getCurrentActiveCalendar() {
    if (currentCalendarName == null || !calendars.containsKey(currentCalendarName)) {
      throw new IllegalStateException("No active calendar is currently selected. Please use" +
              " the command 'use calendar --name <calendar name>' to select a calendar first.");
    }
    return calendars.get(currentCalendarName);
  }

  @Override
  public NewCalendarModel getTargetCalendar(String name) {
    NewCalendarModel targetCalendar = calendars.get(name);
    if (!calendars.containsKey(name)) {
      throw new IllegalArgumentException("Calendar '" + name + "' not found.");
    }
    return targetCalendar;
  }

  @Override
  public void copyEvent(String eventName, String startDateTime, String calendarName,
                        String newDateTime) {
    NewCalendarModel sourceCalendar = this.getCurrentActiveCalendar();
    NewCalendarModel targetCalendar = this.getTargetCalendar(calendarName);

    LocalDateTime sourceStartTime = LocalDateTime.parse(startDateTime,
            sourceCalendar.getDateTimeFormatter());
    //find events with the name and start time
    List<Event> events = sourceCalendar.findEventsBySubjectAndStart(eventName, sourceStartTime);
    if (events.isEmpty()) {
      throw new IllegalArgumentException("Event '" + eventName + "' not found on " +
              sourceStartTime + " in calendar '" + sourceCalendar.getName() + "'.");
    }
    Event originalEvent = events.get(0);

    // Convert source start/end to Instant
    ZonedDateTime sourceStartZdt = originalEvent.getStartDateTime()
            .atZone(sourceCalendar.getTimeZone());
    ZonedDateTime sourceEndZdt = originalEvent.getEndDateTime()
            .atZone(sourceCalendar.getTimeZone());
    Instant startInstant = sourceStartZdt.toInstant();
    Instant endInstant = sourceEndZdt.toInstant();

    // Convert instants to target calendar's timezone
    ZonedDateTime newStartZdt = ZonedDateTime.ofInstant(startInstant, targetCalendar.getTimeZone());
    ZonedDateTime newEndZdt = ZonedDateTime.ofInstant(endInstant, targetCalendar.getTimeZone());

    Event newEvent = shiftHelp(originalEvent, newStartZdt, newEndZdt);

    targetCalendar.addEvent(newEvent);
  }

  /**
   * Shifts an event to a new start time while maintaining its duration.
   *
   * @param event        the event to be shifted
   * @param newStartTime the new start time for the event
   * @return a new Event object with updated start and end times
   */
  private Event shiftEvent(Event event, ZonedDateTime newStartTime, ZonedDateTime newEndTime) {

    return this.shiftHelp(event, newStartTime, newEndTime);


//    return new Event.EventBuilder()
//            .subject(event.getSubject())
//            .startDateTime(newStartTime)
//            .endDateTime(newEndTime)
//            .description(event.getDescription())
//            .location(event.getLocation())
//            .status(event.getStatus())
//            .seriesId(event.getSeriesId())
//            .isAllDayEvent(event.getIsAllDayEvent())
//            .build();
  }

  @Override
  public void copyEventsOnDate(String date, String calendarName, String newDate) {
    NewCalendarModel sourceCalendar = this.getCurrentActiveCalendar();
    NewCalendarModel targetCalendar = this.getTargetCalendar(calendarName);

    LocalDate originalDate = LocalDate.parse(date, sourceCalendar.getDateFormatter());
    LocalDate newStartDate = LocalDate.parse(newDate, targetCalendar.getDateFormatter());

    List<Event> eventsOnDate = sourceCalendar.getEventsOnDate(originalDate);

    for (Event event : eventsOnDate) {
      ZonedDateTime sourceStartZdt = event.getStartDateTime().atZone(sourceCalendar.getTimeZone());
      ZonedDateTime sourceEndZdt = event.getEndDateTime().atZone(sourceCalendar.getTimeZone());

      // Calculate the time-of-day offset from the original date
      int startHour = sourceStartZdt.getHour();
      int startMinute = sourceStartZdt.getMinute();
      int endHour = sourceEndZdt.getHour();
      int endMinute = sourceEndZdt.getMinute();

      // Set new start/end in target timezone with the new date and same time-of-day
      ZonedDateTime newStartZdt = ZonedDateTime.of(newStartDate.getYear(),
              newStartDate.getMonthValue(), newStartDate.getDayOfMonth(),
              startHour, startMinute, 0, 0, targetCalendar.getTimeZone());
      ZonedDateTime newEndZdt = ZonedDateTime.of(newStartDate.getYear(),
              newStartDate.getMonthValue(), newStartDate.getDayOfMonth(),
              endHour, endMinute, 0, 0, targetCalendar.getTimeZone());

      // Adjust duration if event crosses midnight
      if (sourceEndZdt.toLocalDate().isAfter(sourceStartZdt.toLocalDate())) {
        newEndZdt = newEndZdt.plusDays(
                sourceEndZdt.toLocalDate().toEpochDay() - sourceStartZdt.toLocalDate().toEpochDay()
        );
      }
      Event newEvent = this.shiftHelp(event, newStartZdt, newEndZdt);
      targetCalendar.addEvent(newEvent);
    }

  }

  @Override
  public void copyEventsBetweenDates(String startDate, String endDate, String calendarName,
                                     String newDate) {
    NewCalendarModel sourceCalendar = this.getCurrentActiveCalendar();
    NewCalendarModel targetCalendar = this.getTargetCalendar(calendarName);
    LocalDate originalStartDate = LocalDate.parse(startDate, sourceCalendar.getDateFormatter());
    LocalDate originalEndDate = LocalDate.parse(endDate, sourceCalendar.getDateFormatter());
    LocalDate newStartDate = LocalDate.parse(newDate, targetCalendar.getDateFormatter());
    // Calculate the number of days to shift the events
    long shift = newStartDate.toEpochDay() - originalStartDate.toEpochDay();
    List<Event> eventsInRange = sourceCalendar.getEventsInRange(
            originalStartDate.atStartOfDay(),
            originalEndDate.atTime(23, 59, 59));

    for (Event event : eventsInRange) {
      // Get original start/end as ZonedDateTime in source timezone
      ZonedDateTime sourceStartZdt = event.getStartDateTime().atZone(sourceCalendar.getTimeZone());
      ZonedDateTime sourceEndZdt = event.getEndDateTime().atZone(sourceCalendar.getTimeZone());

      // Shift the date by the calculated number of days
      ZonedDateTime shiftedStartZdt = sourceStartZdt.plusDays(shift);
      ZonedDateTime shiftedEndZdt = sourceEndZdt.plusDays(shift);

      // Convert to target timezone, preserving the same instant
      ZonedDateTime newStartZdt = shiftedStartZdt.withZoneSameInstant(targetCalendar.getTimeZone());
      ZonedDateTime newEndZdt = shiftedEndZdt.withZoneSameInstant(targetCalendar.getTimeZone());

      Event newEvent = this.shiftHelp(event, newStartZdt, newEndZdt);
      targetCalendar.addEvent(newEvent);
    }
  }

  /**
   * Helper method to create a new Event with updated start and end times.
   * @param event the original event to shift
   * @param newStartZdt the new start time for the event
   * @param newEndZdt the new end time for the event
   * @return a new Event object with updated start and end times
   */
  private Event shiftHelp(Event event, ZonedDateTime newStartZdt, ZonedDateTime newEndZdt) {
    return new Event.EventBuilder()
            .subject(event.getSubject())
            .startDateTime(newStartZdt.toLocalDateTime())
            .endDateTime(newEndZdt.toLocalDateTime())
            .description(event.getDescription())
            .location(event.getLocation())
            .status(event.getStatus())
            .seriesId(event.getSeriesId())
            .isAllDayEvent(event.getIsAllDayEvent())
            .build();
  }

}
