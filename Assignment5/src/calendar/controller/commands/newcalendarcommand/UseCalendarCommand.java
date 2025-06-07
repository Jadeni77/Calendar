package calendar.controller.commands.newcalendarcommand;

import calendar.controller.commands.CalendarCommand;
import calendar.model.calendarclass.ICalendar;
import calendar.model.calendarmanagerclass.CalendarManagerModel;
import calendar.view.ICalendarView;

public class UseCalendarCommand implements CalendarCommand {
  private final String calendarName;

  public UseCalendarCommand(String calendarName) {
    this.calendarName = calendarName;
  }

  @Override
  public void execute(ICalendar model, ICalendarView view) {
    if (model instanceof CalendarManagerModel) {
      CalendarManagerModel manager = (CalendarManagerModel) model;

      try {
        manager.setCurrentCalendar(calendarName);
        view.displayMessage("Switched to calendar '" + calendarName + "'.");
      } catch (IllegalArgumentException e) {
        view.displayException(new IllegalArgumentException("Error switching to calendar: "
                + e.getMessage()));
      }
    } else {
      view.displayException(new IllegalArgumentException("Invalid model type." +
              " Expected CalendarManagerModel."));
    }

  }
}
