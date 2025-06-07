package calendar.controller.commands.newcalendarcommand;

import calendar.controller.commands.CalendarCommand;
import calendar.model.calendarclass.ICalendar;
import calendar.model.calendarmanagerclass.CalendarManagerModel;
import calendar.view.ICalendarView;

public class EditCalendarCommand implements CalendarCommand {
  private final String calendarName;
  private final String timezone;
  private final String value;

  public EditCalendarCommand(String calendarName, String timezone, String value) {
    this.calendarName = calendarName;
    this.timezone = timezone;
    this.value = value;
  }

  @Override
  public void execute(ICalendar model, ICalendarView view) {
    if (model instanceof CalendarManagerModel) {
      CalendarManagerModel manager = (CalendarManagerModel) model;

      try {
        manager.editCalendar(calendarName, timezone, value);
        view.displayMessage("Calendar '" + calendarName + "' edited successfully.");
      } catch (Exception e) {
        view.displayException(new IllegalArgumentException("Error editing calendar: "
                + e.getMessage()));
      }
    } else {
      view.displayException(new IllegalArgumentException("Invalid model type." +
              " Expected CalendarManagerModel."));
    }

  }
}
