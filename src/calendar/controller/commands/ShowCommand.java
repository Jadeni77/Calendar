package calendar.controller.commands;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import calendar.model.calendarclass.ICalendar;
import calendar.view.ICalendarView;

/**
 * Represents a command class to show the status of a specific date and time in the calendar.
 * The command checks if the specified date and time is busy or free.
 */
public class ShowCommand implements CalendarCommand {
  private final String arguments;
  private static final Pattern SHOW_STATUS = Pattern.compile(
          "status on (?<datetime>.+)");

  /**
   * Constructs a ShowCommand with the given arguments.
   * @param s the command arguments, which should specify a date and time
   */
  public ShowCommand(String s) {
    arguments = s;
  }

  /**
   * Matches the string representing the relevant part of a user inputted command to patterns that
   * represent different commands. If a match is found, calls the related method from the model
   * and/or view.
   */
  @Override
  public void execute(ICalendar model, ICalendarView view) {
    // logic for show command
    if (arguments == null || arguments.isBlank()) {
      view.displayMessage("Invalid command. Please provide a date and time.");
      return;
    }
    String trimmedArguments = arguments.trim();
    Matcher matcher = SHOW_STATUS.matcher(trimmedArguments);

    if (matcher.matches()) {
      parseShowStatus(matcher, model, view);
    } else {
      view.displayMessage("Invalid command format. Please use 'show status on <dateTime>'.");
    }
  }

  /**
   * Parses the show status command and checks if the specified date and time is busy or free.
   *
   * @param matcher the matcher containing the parsed command
   * @param model   the calendar model to check the status against
   * @param view    the calendar view to display the status
   */
  private void parseShowStatus(Matcher matcher, ICalendar model, ICalendarView view) {
    String dateTimeStr = matcher.group("datetime");
    LocalDateTime time;
    try {
      time = LocalDateTime.parse(dateTimeStr, model.getDateTimeFormatter());
    } catch (Exception e) {
      view.displayMessage("Invalid date and time format. Please use 'yyyy-MM-dd'T'HH:mm'.");
      return;
    }
    boolean busy = model.isBusy(time);
    view.displayMessage("Status at " + time + ": ");
    view.showStatusOnDayTime(busy);
  }
}
