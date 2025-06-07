package calendar.view;

import java.util.List;

import calendar.model.event.Event;

/**
 * This interface represents the view of the calendar application. It contains the methods needed
 * to display information to the user within the view.
 */
public interface ICalendarView {

  /**
   * Displays a message to the user.
   * @param message The message to be displayed
   */
  void displayMessage(String message);

  /**
   * Displays an exception message to the user.
   * @param e The exception to be displayed
   */
  void displayException(Exception e);

  /**
   * This prints a bulleted list of the given events with their start time, end time, and location
   * (if applicable).
   * @param events a list with the Events to be printed
   */
  void printEvents(List<Event> events);

  /**
   * Based on a boolean variable that represents if there is a scheduled event at a certain
   * date or time, prints "busy" if there is, and "available" if not.
   * @param status whether there is a scheduled event at a certain date and time
   */
  void showStatusOnDayTime(boolean status);
}
