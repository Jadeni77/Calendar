package calendar.controller;

/**
 * This interface represents a controller for a calendar software. It interprets user-inputted
 * commands, as well as coordinating the activity of the model and view.
 */
public interface ICalendarController {
  /**
   * Starts the controller, giving it control of the application, where it will read input
   * and perform actions with the model or the view based on these inputted commands.
   */
  void start();
}