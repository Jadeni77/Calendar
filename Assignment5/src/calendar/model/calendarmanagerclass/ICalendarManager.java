package calendar.model.calendarmanagerclass;

import calendar.model.calendarclass.ICalendar;

/**
 * This interface defines the operations for managing calendars within the application.
 * It allows for creating, editing, and retrieving calendars, as well as setting the current
 * active calendar.
 */
public interface ICalendarManager {

  /**
   * Create a new Calendar with the given name and zoneId.
   *
   * @param name   the name of the calendar being created
   * @param zoneId the zoneId of the calendar being created as a string
   */
  void createCalendar(String name, String zoneId);

  /**
   * Edit the given property of a calendar with the given name to the given new value.
   *
   * @param name     the name of the calendar
   * @param property the property of the calendar being edited
   * @param newValue the new value
   */
  void editCalendar(String name, String property, String newValue);

  /**
   * Set the Calendar with the given name as the current Calendar
   * that is being operated on.
   *
   * @param name the given name of a Calendar
   */
  void setCurrentCalendar(String name);

  /**
   * To retrieve the calendar that is currently  "active" or being "focused on"
   * by the user. It represents the user's immediate working context.
   *
   * @return the current calendar being in use
   */
  ICalendar getCurrentActiveCalendar();

  /**
   * To retrieve any specific calendar that matches the given name, regardless
   * of whether being currently in use or not. It serves as a direct lookup utility.
   *
   * @param name the given name of a Calendar
   * @return the target calendar with the given name.
   */
  ICalendar getTargetCalendar(String name);

  /**
   * Copies an event from one calendar to another on the given target date/time.
   *
   * @param eventName     the name of the event to be copied
   * @param startDateTime the start date and time of the event to be copied
   * @param calendarName  the name of the calendar that is being copied to
   * @param newDateTime   the target date and time for the copied event
   */
  void copyEvent(String eventName, String startDateTime, String calendarName, String newDateTime);

  /**
   * Copies all events on a specific date from one calendar to another, starting on the given
   * target date.
   *
   * @param date         the date of the events to be copied
   * @param calendarName the name of the calendar that is being copied to
   * @param newDate      the target starting date for the copied events
   */
  void copyEventsOnDate(String date, String calendarName, String newDate);

  /**
   * Copies all events between two dates from one calendar to another, starting on the given
   * target date.
   *
   * @param startDate    the start date of the events to be copied
   * @param endDate      the end date of the events to be copied
   * @param calendarName the name of the calendar that is being copied to
   * @param newDate      the target starting date for the copied events
   */
  void copyEventsBetweenDates(String startDate, String endDate, String calendarName,
                              String newDate);

}
