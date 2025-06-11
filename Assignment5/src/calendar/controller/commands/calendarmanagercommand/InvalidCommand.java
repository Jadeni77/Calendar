package calendar.controller.commands.calendarmanagercommand;

import calendar.controller.commands.CalendarCommand;
import calendar.model.calendarclass.ICalendar;
import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.view.ICalendarView;

/**
 * Represents a command that handles invalid commands in the calendar application.
 * This command is used to display an error message when the user inputs an invalid command.
 */
public class InvalidCommand implements CalendarManagerCommand {
  private final String errorMessage;

  /**
   * Constructs an InvalidCommand with the specified error message.
   *
   * @param errorMessage the message to be displayed when the command is invalid
   */
  public InvalidCommand(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  /**
   * Displays the InvalidCommand's error message, which represents an error resulting from
   * an invalid command input.
   * @param model the calendar model to apply this command to
   * @param view  the calendar view to apply this command to
   */
  @Override
  public void execute(ICalendarManager model, ICalendarView view) {
    view.displayException(new IllegalArgumentException(errorMessage));
  }
}
