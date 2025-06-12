package calendar.controller.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import calendar.model.calendarclass.ICalendar;
import calendar.view.ICalendarView;

/**
 * Represents a command class to create events in the calendar.
 * This command can create single events, all-day events, recurring events,
 * and all-day recurring events based on the provided arguments.
 */
public class CreateCommand implements CalendarCommand {
  private final String arguments;
  private static final Pattern CREATE_SINGLE = Pattern.compile(
          "\"?(?<subject>[^\"]+)\"? from (?<start>.+) to (?<end>.+)");

  private static final Pattern CREATE_ALL_DAY = Pattern.compile(
          "\"?(?<subject>[^\"]+)\"? on (?<date>.+)");

  private static final Pattern CREATE_RECURRING = Pattern.compile(
          "\"?(?<subject>[^\"]+)\"? from (?<start>.+) to " +
                  "(?<end>.+) repeats (?<days>[A-Z]+) (for (?<count>\\d+) times|until " +
                  "(?<until>.+))");

  private static final Pattern CREATE_ALL_DAY_RECURRING = Pattern.compile(
          "\"?(?<subject>[^\"]+)\"? on (?<date>.+) repeats (?<days>[A-Z]+) " +
                  "(for (?<count>\\d+) times|until (?<until>.+))");

  /**
   * Constructs a CreateCommand with the given arguments.
   *
   * @param s the command arguments, which specify the event details
   */
  public CreateCommand(String s) {
    this.arguments = s;
  }

  /**
   * Matches the string representing the relevant part of a user inputted command to patterns that
   * represent different commands. If a match is found, calls the related method from the model
   * and/or view.
   */
  @Override
  public void execute(ICalendar model, ICalendarView view) {
    if (arguments == null || arguments.isBlank()) {
      view.displayMessage("Invalid command. Please provide valid event details.");
      return;
    }
    String trimmedArguments = arguments.trim();
    Matcher matcher;

    if ((matcher = CREATE_RECURRING.matcher(trimmedArguments)).matches()) {
      parseCreateRecurring(matcher, model, view);
    } else if ((matcher = CREATE_ALL_DAY_RECURRING.matcher(trimmedArguments)).matches()) {
      parseCreateAllDayRecurring(matcher, model, view);
    } else if ((matcher = CREATE_SINGLE.matcher(trimmedArguments)).matches()) {
      parseCreateSingle(matcher, model, view);
    } else if ((matcher = CREATE_ALL_DAY.matcher(trimmedArguments)).matches()) {
      parseCreateAllDay(matcher, model, view);
    } else {
      view.displayMessage("Invalid command. Please provide valid event details.");
    }
  }

  /**
   * Parses the matcher for a single event creation command and calls the model to create the event.
   *
   * @param matcher the matcher containing the parsed command details
   * @param model   the calendar model to create the event in
   * @param view    the calendar view to display messages
   */
  private void parseCreateSingle(Matcher matcher, ICalendar model, ICalendarView view) {
    String subject = matcher.group("subject");
    String start = matcher.group("start");
    String end = matcher.group("end");
    model.createSingleEvent(subject, start, end);
    view.displayMessage("Event created with subject '" + subject + "', start " + start
            + ", and end " + end + ".");
  }

  /**
   * Parses the matcher for an all-day event creation command
   * and calls the model to create the event.
   *
   * @param matcher the matcher containing the parsed command details
   * @param model   the calendar model to create the event in
   * @param view    the calendar view to display messages
   */
  private void parseCreateAllDay(Matcher matcher, ICalendar model, ICalendarView view) {
    String subject = matcher.group("subject");
    String date = matcher.group("date");
    model.createAllDayEvent(subject, date);
    view.displayMessage("Event created with subject '" + subject + "' on date " + date + ".");
  }

  /**
   * Parses the matcher for a recurring event creation command and
   * calls the model to create the events.
   *
   * @param matcher the matcher containing the parsed command details
   * @param model   the calendar model to create the events in
   * @param view    the calendar view to display messages
   */
  private void parseCreateRecurring(Matcher matcher, ICalendar model, ICalendarView view) {
    String subject = matcher.group("subject");
    String days = matcher.group("days");
    String start = null;
    String end = null;

    if (matcher.group("start") != null) {
      start = matcher.group("start");
      end = matcher.group("end");
    }
    try {
      this.parseCreateRecurHelper(matcher, model, view, subject, days, start, end);
    } catch (IllegalArgumentException e) {
      view.displayException(e);
    }
  }

  /**
   * Helper method to parse the matcher for a recurring event creation command.
   *
   * @param matcher the matcher containing the parsed command details
   * @param model   the calendar model to create the events in
   * @param view    the calendar view to display messages
   * @param subject the subject of the event
   * @param days    the days of the week on which the event occurs
   * @param start   the start date and time of the event
   * @param end     the end date and time of the event
   */
  private void parseCreateRecurHelper(Matcher matcher, ICalendar model, ICalendarView view,
                                      String subject, String days, String start, String end) {
    int count;
    String until;

    if (matcher.group("count") != null) {
      count = Integer.parseInt(matcher.group("count"));
      model.createRecurringEvent(subject, start, end, days, count);
      view.displayMessage("Events created with subject '" + subject + "', with first start " +
              "and end at " + start + " and " + end + ", on days " + days + " for "
              + count + " times.");
    } else if (matcher.group("until") != null) {
      until = matcher.group("until");
      model.createRecurringEvent(subject, start, end, days, until);
      view.displayMessage("Events created with subject '" + subject + "', with first start " +
              "and end at " + start + " and " + end + ", on days " + days
              + " until " + until + ".");
    }
  }


  /**
   * Parses the matcher for an all-day recurring event creation command
   * and calls the model to create the events.
   *
   * @param matcher the matcher containing the parsed command details
   * @param model   the calendar model to create the events in
   * @param view    the calendar view to display messages
   */
  private void parseCreateAllDayRecurring(Matcher matcher, ICalendar model, ICalendarView view) {
    String subject = matcher.group("subject");
    String onDate = matcher.group("date");
    String days = matcher.group("days");

    int count;
    String until;

    if (matcher.group("count") != null) {
      count = Integer.parseInt(matcher.group("count"));
      model.createRecurringAllDayEvent(subject, onDate, days, count);
      view.displayMessage("Events created with subject '" + subject + "', starting on " + onDate
              + ", on days " + days + " for " + count + " times.");
    } else if (matcher.group("until") != null) {
      until = matcher.group("until");
      model.createRecurringAllDayEvent(subject, onDate, days, until);
      view.displayMessage("Events created with subject '" + subject + "', starting on " + onDate
              + ", on days " + days + " until " + until + ".");
    }
  }
}
