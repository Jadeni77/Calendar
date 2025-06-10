package calendar.model.calendarmanagerclass;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.zone.ZoneRulesException;
import java.util.HashMap;
import java.util.Map;

import calendar.model.calendarclass.CalendarModel;
import calendar.model.calendarclass.ICalendar;
import calendar.model.calendarclass.NewCalendarModel;

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
    } catch (ZoneRulesException e) {
      throw new IllegalArgumentException("Invalid or unsupported timezone ID: '" + zoneId + "'. " +
              "Please use IANA Time Zone Database format (e.g., 'America/New_York').");
    }

    calendars.put(name, new NewCalendarModel(name, parseZone));
  }

  @Override
  public void editCalendar(String nameOfCalendarToEdit, String property, String newValue) {
    NewCalendarModel targetCalendar = this.getTargetCalendar(nameOfCalendarToEdit);
    System.out.println(targetCalendar.getName());
    System.out.println(targetCalendar.getEventsInRange(LocalDateTime.MIN, LocalDateTime.MAX));

    switch (property) {
      case "name":
        if (calendars.containsKey(newValue)) {
          throw new IllegalArgumentException("New Calendar name already exists");
        }
        calendars.remove(nameOfCalendarToEdit);
        targetCalendar.setName(newValue);
        calendars.put(newValue, targetCalendar);

        if (nameOfCalendarToEdit.equals(currentCalendarName)) {
          currentCalendarName = newValue; // Update current calendar name if it was changed
        }
        break;
      case "timezone":
        ZoneId newZoneId;
        try {
          newZoneId = ZoneId.of(newValue);
        } catch (ZoneRulesException e) {
          throw new IllegalArgumentException("Invalid or unsupported timezone ID: '"
                  + newValue + "'. " + "Please use IANA Time Zone Database format " +
                  "(e.g., 'America/New_York').");
        }
        System.out.println("MANAGER REACHED");
        targetCalendar.setTimeZone(newZoneId);
        System.out.println("DEBUG: " + targetCalendar.getEventsInRange(LocalDateTime.MIN, LocalDateTime.MAX));
        break;
      default:
        throw new IllegalArgumentException("Unsupported property '" + property
                + "' for calendar editing. " + "Valid properties are: 'name', 'timezone'.");
    }
  }

  @Override
  public void setCurrentCalendar(String name) {
    if (calendars.containsKey(name)) {
      currentCalendarName = name;
    } else {
      throw new IllegalArgumentException("Calendar '" + name + "' not found.");
    }

  }

  @Override
  public ICalendar getCurrentActiveCalendar() {
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
  public void copyEvent(String eventName, String startDateTime, String calendarName, String newDateTime) {

  }

  @Override
  public void copyEventsOnDate(String date, String calendarName, String newDate) {

  }

  @Override
  public void copyEventsBetweenDates(String startDate, String endDate, String calendarName, String newDate) {

  }
}
