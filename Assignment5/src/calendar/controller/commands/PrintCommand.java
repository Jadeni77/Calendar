package calendar.controller.commands;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import calendar.model.event.Event;
import calendar.model.calendarclass.ICalendar;
import calendar.view.ICalendarView;

/**
 * Represents a command class to print events from the calendar.
 * This command can print events on a specific date or within a date-time range.
 */
public class PrintCommand implements CalendarCommand {
  private final String arguments;
  private static final Pattern PRINT_EVENTS_ON = Pattern.compile(
          "events on (?<date>.+)");

  private static final Pattern PRINT_EVENTS_RANGE = Pattern.compile(
          "events from (?<start>.+) to (?<end>.+)");

  /**
   * Constructs a PrintCommand with the given arguments.
   * @param s the command arguments, which can be a date or a date range
   */
  public PrintCommand(String s) {
    arguments = s;
  }


  /**
   * Matches the string representing the relevant part of a user inputted command to patterns that
   * represent different commands. If a match is found, calls the related method from the model
   * and/or view.
   */
  @Override
  public void execute(ICalendar model, ICalendarView view) {
    // logic for print command
    if (arguments == null || arguments.isBlank()) {
      view.displayMessage("Invalid command. Please provide a date or range.");
      return;
    }

    Matcher matcher;
    if ((matcher = PRINT_EVENTS_ON.matcher(arguments)).matches()) {
      parsePrintEventsOnDate(matcher, model, view);
    } else if ((matcher = PRINT_EVENTS_RANGE.matcher(arguments)).matches()) {
      parsePrintEventsInRange(matcher, model, view);
    } else {
      view.displayMessage("Invalid command format. Please use 'print events on <date>' " +
              "or 'print events from <start> to <end>'.");
    }
  }

  /**
   * Prints events on a specific date.
   * @param matcher the matcher containing the date string
   * @param model the calendar model to retrieve events from
   * @param view the calendar view to display messages
   */
  private void parsePrintEventsOnDate(Matcher matcher, ICalendar model, ICalendarView view) {
    String dateStr = matcher.group("date");
    LocalDate date;
    try {
      date = LocalDate.parse(dateStr.trim(), model.getDateFormatter());
    } catch (Exception e) {
      view.displayMessage("Invalid date format. Please use 'yyyy-MM-dd'.");
      return;
    }

    List<Event> events = model.getEventsOnDate(date);
    if (events.isEmpty()) {
      view.displayMessage("No events found on " + date);
    } else {
      view.displayMessage("Events on " + date.format(model.getDateFormatter()) + ":");
      view.printEvents(events);
    }
  }

  /**
   * Prints events within a specified date-time range.
   * @param matcher the matcher containing the start and end date-time strings
   * @param model the calendar model to retrieve events from
   * @param view the calendar view to display messages
   */
  private void parsePrintEventsInRange(Matcher matcher, ICalendar model, ICalendarView view) {
    String startStr = matcher.group("start");
    String endStr = matcher.group("end");
    LocalDateTime start;
    LocalDateTime end;

    try {
      start = LocalDateTime.parse(startStr.trim(), model.getDateTimeFormatter());
      end = LocalDateTime.parse(endStr.trim(), model.getDateTimeFormatter());
    } catch (Exception e) {
      view.displayMessage("Invalid date-time format. Please use 'yyyy-MM-dd'T'HH:mm'.");
      return;
    }

    if (start.isAfter(end)) {
      view.displayMessage("Start date-time cannot be after end date-time.");
      return;
    }
    List<Event> events = model.getEventsInRange(start, end);
    if (events.isEmpty()) {
      view.displayMessage("No events found from " + start.format(model.getDateTimeFormatter()) +
              " to " + end.format(model.getDateTimeFormatter()));
    } else {
      view.displayMessage("Events from " + start.format(model.getDateTimeFormatter()) +
              " to " + end.format(model.getDateTimeFormatter()) + ":");
      view.printEvents(events);
    }

  }

}
