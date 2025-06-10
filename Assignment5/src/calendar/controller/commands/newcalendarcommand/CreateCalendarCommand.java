package calendar.controller.commands.newcalendarcommand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.view.ICalendarView;

/**
 * Represents a command to create a new calendar with a specified name and timezone.
 * This command is part of the calendar application and allows users to create a new calendar
 * instance with the provided parameters.
 */
public class CreateCalendarCommand implements CalendarManagerCommand {
  private final String arguments;
  private static final Pattern CREATE_CALENDAR = Pattern.compile(
          "--name \"?(?<name>[^\"]+)\"? --timezone (?<timezone>.+)");

  /**
   * Constructs a CreateCalendarCommand with the given arguments.
   * @param arguments the command arguments, which specify the calendar name and timezone
   */
  public CreateCalendarCommand(String arguments) {
    this.arguments = arguments;
  }

  @Override
  public void execute(ICalendarManager manager, ICalendarView view) {
    if (arguments == null || arguments.isBlank()) {
      view.displayException(new IllegalArgumentException("Invalid arguments."));
      return;
    }
    String trimmedArguments = arguments.trim();
    Matcher matcher = CREATE_CALENDAR.matcher(trimmedArguments);

    if (matcher.matches()) {
      this.parseCreateCalendar(matcher, manager, view);
    } else {
      view.displayMessage("Invalid 'create calendar' command format. " +
              "Please use 'create calendar --name \"<name>\" --timezone <timezone>'.");
    }
  }

  /**
   * Parses the matcher to extract calendar name and timezone, and creates a new calendar.
   * @param matcher the matcher containing the calendar creation details
   * @param manager the calendar manager model to create the calendar
   * @param view the view to display messages or exceptions
   */
  private void parseCreateCalendar(Matcher matcher, ICalendarManager manager,
                                   ICalendarView view) {
    String calendarName = matcher.group("name");
    String timezone = matcher.group("timezone");

    try {
      manager.createCalendar(calendarName.trim(), timezone.trim());
      view.displayMessage("Calendar '" + calendarName + "' created successfully.");
    } catch (IllegalArgumentException e) {
      view.displayException(new IllegalArgumentException("Error creating calendar: "
              + e.getMessage()));
    }
  }
}
