package calendar.controller.guicontroller;

import calendar.controller.ICalendarController;

/**
 * Represents the controller for the GUI (graphical user interface) of the calendar application.
 * This interface contains the commands that the controller should be able to initiate in
 * response to user actions within the view. This includes adding events and calendars, editing
 * events, switching calendars, refreshing the schedule view, and navigating through the monthly
 * view.
 */
public interface IGUIController extends ICalendarController {

  /**
   * Handles the creation of a new event in the currently active calendar using the given fields.
   * This method is triggered when the user presses the button to create an event, enters the
   * information, and confirms the action.
   * @param subject the subject of the event to be created
   * @param start the starting date/time of the event to be created
   * @param end the ending date/time of the event to be created
   * @param description the description of the event to be created
   * @param location the location of the event to be created
   * @param status the status of the event to be created
   */
  void handleAddEvent(String subject, String start, String end, String description,
                      String location, String status);

  /**
   * Handles the editing of an event within the active calendar. This method is triggered when the
   * user selects an event and presses the button to edit the event, enters the information, and
   * confirms it.
   * @param editedProperty the property of the selected event to be edited
   * @param newValue the value to replace the current one for the given property
   */
  void handleEditEvent(String editedProperty, String newValue);

  /**
   * Handles the creation of a new calendar within the application. This method is triggered if
   * the user presses the button to create the calendar and follows the prompts.
   * @param calendarName the name of the new calendar
   * @param timeZone the timezone of the new calendar
   */
  void handleCreateCalendar(String calendarName, String timeZone);

  /**
   * Handles switching from one selected calendar to another.
   */
  void handleSwitchCalendar();

  /**
   * Handles refreshing the schedule view based on its current starting date. This ensures that
   * any recently made changes are reflected within the view.
   */
  void handleRefreshSchedule();

  /**
   * Navigates to the previous month in the month view, triggered by the press of a 'previous'
   * button within the GUI.
   */
  void handlePrevMonth();

  /**
   * Navigates to the next month in the month view, triggered by the press of a 'next' button
   * within the GUI.
   */
  void handleNextMonth();
}
