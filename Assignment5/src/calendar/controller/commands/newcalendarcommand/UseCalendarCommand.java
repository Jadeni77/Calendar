package calendar.controller.commands.newcalendarcommand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.view.ICalendarView;

/**
 * Represents a command to switch to a specified calendar by name.
 * This command is part of the calendar application and allows users to switch
 * the current calendar context to the one specified by the user.
 */
public class UseCalendarCommand implements CalendarManagerCommand {
  private final String arguments;
  private static final Pattern USE_CALENDAR = Pattern.compile(
          "--name \"?(?<name>[^\"]+)\"?"
  );

  /**
   * Constructs a UseCalendarCommand with the given arguments.
   * @param arguments the command arguments, which specify the calendar name to switch to
   */
  public UseCalendarCommand(String arguments) {
    this.arguments = arguments;
  }

  @Override
  public void execute(ICalendarManager manager, ICalendarView view) {
    if (arguments == null || arguments.isBlank()) {
      view.displayException(new IllegalArgumentException("Invalid arguments."));
      return;
    }
    String trimmedArguments = arguments.trim();
    Matcher matcher = USE_CALENDAR.matcher(trimmedArguments);

    if (matcher.matches()) {
      this.parseUseCalendar(matcher, manager, view);
    } else {
      view.displayMessage("Invalid 'use calendar' command format. Please use 'use calendar " +
              "--name \"<name>\"'.");
    }
  }

  /**
   * Parses the matcher to extract the calendar name and switches to that calendar.
   * @param matcher the matcher containing the calendar name
   * @param manager the calendar manager model to switch calendars
   * @param view the view to display messages or exceptions
   */
  private void parseUseCalendar(Matcher matcher, ICalendarManager manager,
                                ICalendarView view) {
    String calendarName = matcher.group("name");

    try {
      manager.setCurrentCalendar(calendarName.trim());
      view.displayMessage("Switched to calendar '" + calendarName + "'.");
    } catch (IllegalArgumentException e) {
      view.displayException(new IllegalArgumentException("Error switching to calendar: "
              + e.getMessage()));
    }
  }
}
