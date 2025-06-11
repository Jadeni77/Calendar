package calendar.controller.commands.calendarmanagercommand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.view.ICalendarView;

/**
 * This class represents a command class to copy events from one calendar to another.
 * This command can copy single events, or multiple events on a given date or date range.
 */
public class CopyEventCommand implements CalendarManagerCommand {
  private final String arguments;
  private static final Pattern COPY_EVENT = Pattern.compile(
          "event \"?(?<eventName>[^\"]+)\"? on (?<startDateTime>.+)" +
                  " --target \"?(?<calendarName>[^\"]+)\"? to (?<newDateTime>.+)"
  );
  private static final Pattern COPY_EVENTS_ON_DATE = Pattern.compile(
          "events on (?<dateString>.+) --target \"?(?<calendarName>[^\"]+)\"? to (?<newDate>.+)"
  );
  private static final Pattern COPY_EVENTS_BETWEEN_DATES = Pattern.compile(
          "events between (?<startDate>.+) and (?<endDate>.+)" +
                  " --target \"?(?<calendarName>[^\"]+)\"? to (?<newDate>.+)"
  );

  /**
   * Constructs a CopyEventCommand with the given arguments.
   *
   * @param arguments the command arguments, which specify the event name, date, time,
   *                  target calendar name, and new date and time for the copied event.
   */
  public CopyEventCommand(String arguments) {
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
    Matcher matcher;

    if ((matcher = COPY_EVENT.matcher(trimmedArguments)).matches()) {
      parseCopyEvent(matcher, manager, view);
    } else if ((matcher = COPY_EVENTS_ON_DATE.matcher(trimmedArguments)).matches()) {
      parseCopyEventsOnDate(matcher, manager, view);
    } else if ((matcher = COPY_EVENTS_BETWEEN_DATES.matcher(trimmedArguments)).matches()) {
      parseCopyEventsBetweenDates(matcher, manager, view);
    } else {
      view.displayMessage("Invalid 'copy event' command format. " +
              "Please use one of the following:\n" +
              "1. copy event \"<eventName>\" on <startDateTime> --target " +
              "\"<calendarName>\" to <newDateTime>\n" +
              "2. copy events on <dateString> --target \"<calendarName>\"" +
              " to <newDate>\n" +
              "3. copy events between <startDate> and <endDate> --target " +
              "\"<calendarName>\" to <newDate>");
    }
  }

  /**
   * Parses the matcher to extract event name, start date and time, target calendar name,
   * and new date and time for the copied event.
   *
   * @param matcher the matcher containing the copy event details
   * @param manager the calendar manager model to copy the event
   * @param view    the view to display messages or exceptions
   */
  private void parseCopyEvent(Matcher matcher, ICalendarManager manager, ICalendarView view) {
    String eventName = matcher.group("eventName");
    String startDateTime = matcher.group("startDateTime");
    String calendarName = matcher.group("calendarName");
    String newDateTime = matcher.group("newDateTime");
    try {
      manager.copyEvent(eventName.trim(), startDateTime.trim(), calendarName.trim(), newDateTime.trim());
      view.displayMessage("Event '" + eventName + "' copied successfully to " +
              "calendar '" + calendarName + "' at " + newDateTime + ".");
    } catch (IllegalArgumentException e) {
      view.displayException(new IllegalArgumentException("Error copying event: " + e.getMessage()));
    }
  }

  /**
   * Parses the matcher to extract date, target calendar name, and new date for copying events.
   *
   * @param matcher the matcher containing the copy events on date details
   * @param manager the calendar manager model to copy the events
   * @param view   the view to display messages or exceptions
   */
  private void parseCopyEventsOnDate(Matcher matcher, ICalendarManager manager,
                                     ICalendarView view) {
    String date = matcher.group("dateString");
    String calendarName = matcher.group("calendarName");
    String newDate = matcher.group("newDate");

    try {
      manager.copyEventsOnDate(date, calendarName, newDate);
      view.displayMessage("Events on " + date + " copied successfully to " +
              "calendar '" + calendarName + "' at " + newDate + ".");
    } catch (IllegalArgumentException e) {
      view.displayException(new IllegalArgumentException("Error copying events: " + e.getMessage()));
    }
  }

  /**
   * Parses the matcher to extract start date, end date, target calendar name,
   * and new date for copying events between two dates.
   *
   * @param matcher the matcher containing the copy events between dates details
   * @param manager the calendar manager model to copy the events
   * @param view  the view to display messages or exceptions
   */
  private void parseCopyEventsBetweenDates(Matcher matcher, ICalendarManager manager,
                                           ICalendarView view) {
    String startDate = matcher.group("startDate");
    String endDate = matcher.group("endDate");
    String calendarName = matcher.group("calendarName");
    String newDate = matcher.group("newDate");

    try {
      manager.copyEventsBetweenDates(startDate, endDate, calendarName, newDate);
      view.displayMessage("Events between " + startDate + " and " + endDate +
              " copied successfully to calendar '" + calendarName + "' at " + newDate + ".");
    } catch (IllegalArgumentException e) {
      view.displayException(new IllegalArgumentException("Error copying events: " + e.getMessage()));
    }

  }

}
