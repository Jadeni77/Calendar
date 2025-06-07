package calendar.controller.commands.newcalendarcommand;

import calendar.controller.commands.CalendarCommand;
import calendar.model.calendarclass.ICalendar;
import calendar.view.ICalendarView;

/**
 * Represents a command to create a new calendar with a specified name and timezone.
 * This command is part of the calendar application and allows users to create a new calendar
 * instance with the provided parameters.
 */
public class CreateCalendarCommand implements CalendarCommand {
  private final String name;
  private final String timezone;

  /**
   * Constructor for CreateCalendarCommand.
   * @param name the name of the calendar to be created
   * @param timezone the timezone of the calendar to be created
   */
  public CreateCalendarCommand(String name, String timezone) {
    this.name = name;
    this.timezone = timezone;
  }

  @Override
  public void execute(ICalendar model, ICalendarView view) {

  }
}
