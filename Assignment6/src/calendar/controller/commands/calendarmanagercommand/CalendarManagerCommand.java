package calendar.controller.commands.calendarmanagercommand;

import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.view.ICalendarView;

/**
 * Represents a command that can be executed by a calendar manager.
 * Each command represents a specific action to be carried out on an ICalendarManager, and
 * has a specific effect on the manager, both determined by each implementation.
 */
public interface CalendarManagerCommand {
  /**
   * Executes the command on the given manager.
   *
   * @param manager the calendar manager to apply this command to
   * @param view  the calendar view to apply this command to
   */
  void execute(ICalendarManager manager, ICalendarView view);
}
