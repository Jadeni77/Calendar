package calendar.view;

import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents the GUI (graphical user interface) view for a virtual calendar application. It
 * supports a schedule view where it can display calendar events and a monthly view to select
 * the date from which the schedule view starts. It also supports actions such as editing and
 * selecting events, creating and switching calendars, and refreshing the page, the options for
 * all of which are presented to the user as components of the GUI such as buttons or text fields.
 */
public interface IGUIView extends ICalendarView {

  /**
   * Sets the given ActionListener as a listener for any GUI components that could initiate
   * events upon user interaction, allowing the view to relay these events and their details to
   * the listener.
   * @param listener the given ActionListener
   */
  void setListeners(ActionListener listener);

  /**
   * Adds the calendar with the given name to be included in the drop-down menu for selecting
   * calendars within the GUI.
   * @param name the name of the calendar
   */
  void addCalendar(String name);

  /**
   * Retrieves the subject of the selected event.
   * @return the subject of the selected event as a String
   */
  String getSelectedEventSubject();

  /**
   * Retrieves the start date/time of the selected event.
   * @return the start date/time of the selected event as a String
   */
  String getSelectedEventStart();

  /**
   * Retrieves the end date/time of the selected event.
   * @return the end date/time of the selected event as a String
   */
  String getSelectedEventEnd();

  /**
   * Returns the name of the currently selected calendar within the view.
   * @return the name of the selected calendar.
   */
  String getSelectedCalendar();

  /**
   * Upon interaction with the element used for adding events, shows a popup window prompting
   * the user to input relevant information into its components.
   * @return a list containing the inputted information as Strings.
   */
  List<String> showAddEventDialog();

  /**
   * Upon interaction with the element used for editing events, shows a popup window prompting
   * the user to input relevant information into its components.
   * @return a list containing the inputted information as Strings.
   */
  List<String> showEditEventDialog();

  /**
   * Upon interaction with the element used for creating calendars, shows a popup window prompting
   * the user to input relevant information into its components.
   * @return a list containing the inputted information as Strings.
   */
  List<String> showCreateCalendarDialog();

  /**
   * Changes the month in the GUI's monthly view by the specified amount of months. If the amount
   * is positive, the month will advance, and if the amount is negative the month will decrease.
   * @param direction the direction of the change in number of months
   */
  void changeMonth(int direction);

  /**
   * Selects the tab for the schedule view so that it may be displayed in the GUI if it has not
   * been selected already.
   */
  void switchToScheduleView();

  /**
   * Retrieves the current date from which events are displayed of the schedule view of the GUI.
   * @return the start date of the view
   */
  String getStartDate();

  /**
   * Sets the start date of the view to the given LocalDate.
   * @param startDate the date to be set as the start date
   */
  void setStartDate(LocalDate startDate);

  /**
   * Updates the displayed dates according to the given date, meant to be the new starting date.
   * @param date the new start date of the view
   */
  void showEventsForDate(LocalDate date);

}
