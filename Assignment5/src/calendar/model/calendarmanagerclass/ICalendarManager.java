package calendar.model.calendarmanagerclass;

import calendar.model.calendarclass.NewCalendarModel;

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
   * Edit the given property of a calendar with the given name to a new given value.
   *
   * @param name     the name of the calendar
   * @param property the property of the calendar being edited
   * @param newValue the new value
   */
  void editCalendar(String name, String property, String newValue);

  /**
   * Set the Calendar that matches the given name as the current Calendar
   * that is being operated.
   *
   * @param name the given name of a Calendar
   */
  void setCurrentCalendar(String name);

  /**
   * To retrieve the calendar that is currently being "active" or "focs on"
   * by the user. It represents the user's immediate working context.
   *
   * @return the current calendar being in use
   */
  NewCalendarModel getCurrentActiveCalendar();

  /**
   * To retrieve any specific calendar that matches the given name, regardless
   * of whether being currently in use or not. It serves as a direct lookup utility.
   *
   * @param name the given name of a Calendar
   * @return the target calendar with the given name.
   */
  NewCalendarModel getTargetCalendar(String name);

  /**
   * To copy an event from one calendar to another.
   *
   * @param eventName     the name of the event to be copied
   * @param startDateTime the start date and time of the event to be copied
   * @param calendarName  the name of the calendar that is being copied to
   * @param newDateTime   the new date and time for the copied event
   */
  void copyEvent(String eventName, String startDateTime, String calendarName, String newDateTime);

  /**
   * To copy all events on a specific date from one calendar to another.
   *
   * @param date         the date of the events to be copied
   * @param calendarName the name of the calendar that is being copied to
   * @param newDate      the new date for the copied events
   */
  void copyEventsOnDate(String date, String calendarName, String newDate);

  /**
   * To copy all events between two dates from one calendar to another.
   *
   * @param startDate    the start date of the events to be copied
   * @param endDate      the end date of the events to be copied
   * @param calendarName the name of the calendar that is being copied to
   * @param newDate      the new date for the copied events
   */
  void copyEventsBetweenDates(String startDate, String endDate, String calendarName,
                              String newDate);


}
