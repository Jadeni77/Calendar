package calendar.controller.commands.newcalendarcommand;

import calendar.controller.commands.CalendarCommand;
import calendar.model.calendarclass.ICalendar;
import calendar.model.calendarmanagerclass.CalendarManagerModel;
import calendar.view.ICalendarView;

/**
 * Represents a command to create a new calendar with a specified name and timezone.
 * This command is part of the calendar application and allows users to create a new calendar
 * instance with the provided parameters.
 */
public class CreateCalendarCommand implements CalendarCommand {
  private final String calendarName;
  private final String timezone;

  /**
   * Constructor for CreateCalendarCommand.
   * @param calendarName the name of the calendar to be created
   * @param timezone the timezone of the calendar to be created
   */
  public CreateCalendarCommand(String calendarName, String timezone) {
    this.calendarName = calendarName;
    this.timezone = timezone;
  }

  @Override
  public void execute(ICalendar model, ICalendarView view) {
    if (model instanceof CalendarManagerModel) {
      CalendarManagerModel manager = (CalendarManagerModel) model;

      try {
        manager.createCalendar(calendarName, timezone);
        view.displayMessage("Calendar '" + calendarName + "' created successfully.");
      } catch (Exception e) {
        view.displayException(new IllegalArgumentException("Error creating calendar: "
                + e.getMessage()));
      }
    } else {
      view.displayException(new IllegalArgumentException("Invalid model type." +
              " Expected CalendarManagerModel."));
    }

  }
}
