package calendar.controller.commands;

import calendar.model.calendarclass.ICalendar;
import calendar.view.ICalendarView;

/**
 * Represents a command that can be executed by a calendar model.
 * Each command represents a specific action to be carried out on a {@code CalendarModel}, and
 * has a specific effect on the model, both determined by each implementation.
 */
public interface CalendarCommand {
  /**
   * Executes the command on the given model.
   *
   * @param model the calendar model to apply this command to
   * @param view  the calendar view to apply this command to
   */
  void execute(ICalendar model, ICalendarView view);
}