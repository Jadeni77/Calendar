package calendar.controller.commands.newcalendarcommand;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import calendar.controller.commands.CalendarCommand;
import calendar.model.calendarclass.ICalendar;
import calendar.model.calendarmanagerclass.CalendarManagerModel;
import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.view.ICalendarView;

/**
 * Represents a command to switch to a specified calendar by name.
 * This command is part of the calendar application and allows users to switch
 * the current calendar context to the one specified by the user.
 */
public class EditCalendarCommand implements CalendarManagerCommand {
  private final String arguments;
  private static final Pattern EDIT_CALENDAR = Pattern.compile(
          "--name \"?(?<name>[^\"]+)\"? --property " +
                  "(?<property>\\w+) (?<newValue>.+)");

  /**
   * Constructs an EditCalendarCommand with the given arguments.
   * @param arguments the command arguments, which specify the calendar name,
   */
  public EditCalendarCommand(String arguments) {
    this.arguments = arguments;
  }

  @Override
  public void execute(ICalendarManager manager, ICalendarView view) {
    if (arguments == null || arguments.isBlank()) {
      view.displayException(new IllegalArgumentException("Invalid arguments."));
      return;
    }
    String trimmedArguments = arguments.trim();
    Matcher matcher = EDIT_CALENDAR.matcher(trimmedArguments);

    if (matcher.matches()) {
      this.parseEditCalendar(matcher, manager, view);
    } else {
      view.displayMessage("Invalid 'edit calendar' command format. Please use 'edit calendar " +
              "--name \"<name>\" --property <property> <value>'.");
    }

  }

  /**
   * Parses the matcher to extract calendar name, property, and new value,
   * @param matcher the matcher containing the calendar edit details
   * @param manager the calendar manager model to edit the calendar
   * @param view the view to display messages or exceptions
   */
  private void parseEditCalendar(Matcher matcher, ICalendarManager manager,
                                 ICalendarView view) {
    String calendarName = matcher.group("name");
    String property = matcher.group("property");
    String value = matcher.group("newValue").trim();

    try {
      manager.editCalendar(calendarName.trim(), property.trim(), value.trim());
      view.displayMessage("Calendar '" + calendarName + "' updated successfully.");
    } catch (IllegalArgumentException e) {
      view.displayException(new IllegalArgumentException("Error editing calendar: "
              + e.getMessage()));
    }
  }
}
