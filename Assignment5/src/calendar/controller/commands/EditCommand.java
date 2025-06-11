package calendar.controller.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import calendar.model.calendarclass.ICalendar;
import calendar.view.ICalendarView;

/**
 * Represents a command class to edit events or event series in the calendar.
 * This command can edit properties of a single event or a series of events.
 */
public class EditCommand implements CalendarCommand {
  private final String arguments;
  private static final Pattern EDIT_EVENT = Pattern.compile(
          "event (?<property>\\w+) \"?(?<subject>[^\"]+)\"? from " +
                  "(?<start>.+) to (?<end>.+) with (?<newValue>.+)");

  private static final Pattern EDIT_EVENTS = Pattern.compile(
          "events (?<property>\\w+) \"?(?<subject>[^\"]+)\"?" +
                  " from (?<start>.+) with (?<newValue>.+)");

  private static final Pattern EDIT_SERIES = Pattern.compile(
          "series (?<property>\\w+) \"?(?<subject>[^\"]+)\"?" +
                  " from (?<start>.+) with (?<newValue>.+)");

  /**
   * Constructs an EditCommand with the given arguments.
   *
   * @param arguments the command arguments, which specify the property to edit and its new value
   */
  public EditCommand(String arguments) {
    this.arguments = arguments;
  }

  /**
   * Matches the string representing the relevant part of a user inputted command to patterns that
   * represent different commands. If a match is found, calls the related method from the model
   * and/or view.
   */
  @Override
  public void execute(ICalendar model, ICalendarView view) {
    // logic for execute command
    if (this.arguments == null || this.arguments.isBlank()) {
      view.displayMessage("Invalid 'edit' command. Please provide arguments (e.g.," +
              " 'edit event <property>" +
              " \"<subject>\" from <start> to <end> with <newValue>').");
      return;
    }
    Matcher matcher;

    if ((matcher = EDIT_EVENT.matcher(this.arguments)).matches()) {
      parseEditSingleEvent(matcher, model, view);
    } else if ((matcher = EDIT_EVENTS.matcher(this.arguments)).matches()) {
      parseEditEventSeries(matcher, model, view, false);
    } else if ((matcher = EDIT_SERIES.matcher(this.arguments)).matches()) {
      parseEditEventSeries(matcher, model, view, true);
    } else {
      view.displayMessage("Invalid 'edit' command format. Please use 'edit event " +
              "<property> \"<subject>\" " + "from <start> to <end> with <newValue>' " +
              "or 'edit series <property> \"<subject>\" from <start> " + "with <newValue>'.");
    }
  }

  /**
   * Parses the edit command for a single event and updates its properties.
   *
   * @param matcher the matcher containing the parsed command
   * @param model   the calendar model to update the event in
   * @param view    the calendar view to display messages
   */
  private void parseEditSingleEvent(Matcher matcher, ICalendar model, ICalendarView view) {
    String property = matcher.group("property");
    String subject = matcher.group("subject");
    String startStr = matcher.group("start");
    String endStr = matcher.group("end");
    String newValue = matcher.group("newValue");
    try {
      model.editSingleEvent(property, subject, startStr, endStr, newValue);
      view.displayMessage("Event property '" + property + "' sucessfully edited to "
              + newValue + ".");
    } catch (Exception e) {
      view.displayException(e);
    }
  }

  /**
   * Parses the edit command for a series of events and updates their properties.
   *
   * @param matcher the matcher containing the parsed command
   * @param model   the calendar model to update the series in
   * @param view    the calendar view to display messages
   */
  private void parseEditEventSeries(Matcher matcher, ICalendar model,
                                    ICalendarView view, boolean editSeries) {
    String property = matcher.group("property");
    String subject = matcher.group("subject");
    String startStr = matcher.group("start");
    String newValue = matcher.group("newValue");
    try {
      model.editMultipleEvents(property, subject, startStr, newValue, editSeries);
      view.displayMessage("Event properties '" + property + "' sucessfully edited to "
              + newValue + ".");
    } catch (Exception e) {
      view.displayException(e);
    }
  }
}