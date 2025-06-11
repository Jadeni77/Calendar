package calendar.controller.commands.newcalendarcommand;

import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.view.ICalendarView;

/**
 * Represents a command that displays the available operations in the calendar application.
 * This command provides a menu of actions that users can perform, such as creating events,
 * editing events, printing events, and showing the status of the calendar.
 */
public class MenuCommand implements CalendarManagerCommand {

  @Override
  public void execute(ICalendarManager manager, ICalendarView view) {
    view.displayMessage("Available operations:");
    view.displayMessage("create event <eventSubject> from <dateStringTtimeString> to" +
            " <dateStringTtimeString>");
    view.displayMessage("create event <eventSubject> from <dateStringTtimeString> to " +
            "<dateStringTtimeString> repeats <weekdays> for <N> times");
    view.displayMessage("create event <eventSubject> from <dateStringTtimeString> to " +
            "<dateStringTtimeString> repeats <weekdays> until <dateString>");
    view.displayMessage("create event <eventSubject> on <dateString>");
    view.displayMessage("create event <eventSubject> on <dateString> repeats <weekdays> " +
            "for <N> times");
    view.displayMessage("create event <eventSubject> on <dateString> repeats <weekdays> " +
            "until <dateString>");
    view.displayMessage("edit event <property> <eventSubject> from <dateStringTtimeString> " +
            "to <dateStringTtimeString> with <NewPropertyValue>");
    view.displayMessage("edit events <property> <eventSubject> from <dateStringTtimeString> " +
            "with <NewPropertyValue>");
    view.displayMessage("edit series <property> <eventSubject> from <dateStringTtimeString> " +
            "with <NewPropertyValue>");
    view.displayMessage("print events on <dateString>");
    view.displayMessage("print events from <dateStringTtimeString> to <dateStringTtimeString>");
    view.displayMessage("show status on <dateStringTtimeString>");
    //new line ------------
    view.displayMessage("create calendar --name <calName> --timezone area/location");
    view.displayMessage("edit calendar --name <name> --property <property> <value>");
    view.displayMessage("use calendar --name <name>");
    view.displayMessage("copy event <eventName> on <datetime> --target <calendar> to <datetime>");
    view.displayMessage("copy events on <date> --target <calendar> to <date>");
    view.displayMessage("copy events between <start> and <end> --target <calendar> to <date>");
    //------------------
    view.displayMessage("menu (Show this menu)");
    view.displayMessage("q or quit (Exit the program)");
  }
}
