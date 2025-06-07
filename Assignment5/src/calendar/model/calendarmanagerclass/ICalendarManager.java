package calendar.model.calendarmanagerclass;

import java.util.Calendar;

import calendar.model.calendarclass.ICalendar;

public interface ICalendarManager {

  /**
   * Create a new Calendar with the given name and zoneId.
   * @param name the name of the calendar being created
   * @param zoneId the zoneId of the calendar being created as a string
   */
  void createCalendar(String name, String zoneId);

  /**
   * Edit the given property of a calendar with the given name to a new given value.
   * @param name the name of the calendar
   * @param property the property of the calendar being edited
   * @param newValue the new value
   */
  void editCalendar(String name, String property, String newValue);

  /**
   * Set the Calendar that matches the given name as the current Calendar
   * that is being operated.
   * @param name the given name of a Calendar
   */
  void setCurrentCalendar(String name);

  /**
   * To retrieve the calendar that is currently being "active" or "focs on"
   * by the user. It represents the user's immediate working context.
   * @return the current calendar being in use
   */
  ICalendar getCurrentActiveCalendar();

  /**
   * To retrieve any specific calendar that matches the given name, regardless
   * of whether being currently in use or not. It serves as a direct lookup utility.
   * @param name the given name of a Calendar
   * @return the target calendar with the given name.
   *
   */
  ICalendar getTargetCalendar(String name);
}
