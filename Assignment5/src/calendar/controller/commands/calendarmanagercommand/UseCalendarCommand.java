package calendar.controller.commands.calendarmanagercommand;

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

  /**
   * Matches the inputted command with the existing command patterns for correct syntax. If a
   * match is found, the method calls the corresponding command from the match and displays
   * necessary information from the view when applicable. Otherwise, displays a message informing
   * the user of incorrect command syntax.
   * @param manager the calendar manager to apply this command to
   * @param view  the calendar view to apply this command to
   */
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
