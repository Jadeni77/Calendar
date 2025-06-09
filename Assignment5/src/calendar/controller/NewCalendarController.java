package calendar.controller;

import java.util.Scanner;
import java.util.function.Function;

import calendar.controller.commands.CalendarCommand;
import calendar.controller.commands.CreateCommand;
import calendar.controller.commands.EditCommand;
import calendar.controller.commands.PrintCommand;
import calendar.controller.commands.newcalendarcommand.CopyEventCommand;
import calendar.controller.commands.newcalendarcommand.CreateCalendarCommand;
import calendar.controller.commands.newcalendarcommand.EditCalendarCommand;
import calendar.controller.commands.newcalendarcommand.InvalidCommand;
import calendar.controller.commands.newcalendarcommand.UseCalendarCommand;
import calendar.model.calendarclass.ICalendar;
import calendar.model.calendarmanagerclass.CalendarManagerModel;
import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.view.ICalendarView;

/**
 * Represents a controller for managing calendar commands, specifically for creating, editing,
 * using, and copying events and calendars.
 * This controller extends the base CalendarController to provide additional functionality
 * for handling new calendar-related commands.
 */
public class NewCalendarController extends CalendarController {
  private ICalendarManager manager;

  /**
   * Initializes a controller with the inputted model and view to which it will delegate
   * functionality and output, as well as the channel it will receive user input from.
   *
   * @param model the model to be controlled
   * @param view  the view to be controlled
   * @param in    the input channel to be read from
   */
  public NewCalendarController(ICalendar model, ICalendarView view, Readable in) {
    super(model, view, in);
    knownCommands.put("create", createCommandFactory());
    knownCommands.put("edit", editCommandFactory());
    knownCommands.put("use", useCommandFactory());
    knownCommands.put("copy", copyCommandFactory());
    knownCommands.put("print",  printCommandFactory());

    if (!(model instanceof ICalendarManager)) {
      throw new IllegalArgumentException("The provided model must implement ICalendarManager.");
    }
    this.manager = (ICalendarManager) model;
  }

  private Function<Scanner, CalendarCommand> printCommandFactory() {
    return s -> {
      if (manager.getCurrentActiveCalendar() == null) {
        return new InvalidCommand("No calendar selected. Use 'use <calendar>' first.");
      }

      String fullPrintCommandArguments = s.nextLine().trim();
      if (fullPrintCommandArguments.isEmpty()) {
        return new InvalidCommand("Missing arguments for 'print' command. " +
                "Please provide details for event, events on date, or events between dates.");
      }
      return new PrintCommand(fullPrintCommandArguments);
    };
  }

  private Function<Scanner, CalendarCommand> createCommandFactory() {
    return s -> {
      if (!s.hasNext()) {
        return new InvalidCommand("Missing type after 'create'." +
                "Expected 'event' or 'calendar'.");
      }
      String type = s.next().trim().toLowerCase();
      String remainingArgs = s.nextLine().trim();

      switch (type) {
        case "event":
          if (manager.getCurrentActiveCalendar() == null) {
            return new InvalidCommand("No calendar selected. Use 'use <calendar>' first.");
          }
          return new CreateCommand(remainingArgs);
        case "calendar":
          return new CreateCalendarCommand(remainingArgs);
        default:
          return new InvalidCommand("Unknown 'create' type: '" + type + "'. " +
                  "Expected 'event' or 'calendar'.");
      }
    };
  }

    private Function<Scanner, CalendarCommand> editCommandFactory() {
      return s -> {
        if (!s.hasNext()) {
          return new InvalidCommand("Missing type after 'edit'." +
                  "Expected 'event' or 'calendar'.");
        }
        String type = s.next().trim().toLowerCase();
        String remainingArgs = s.nextLine().trim();

        String fullEditArgs = type + " " + remainingArgs;

        switch (type) {
          case "event":
            if (manager.getCurrentActiveCalendar() == null) {
              return new InvalidCommand("No calendar selected. Use 'use <calendar>' first.");
            }
            return new EditCommand(fullEditArgs);
          case "calendar":
            return new EditCalendarCommand(remainingArgs);
          default:
            return new InvalidCommand("Unknown 'edit' type: '" + type + "'. " +
                    "Expected 'event' or 'calendar'.");
        }
      };
    }

    private Function<Scanner, CalendarCommand> useCommandFactory() {
    return s -> {
      String calendarName = s.nextLine().trim();
      if (calendarName.isEmpty()) {
        return new InvalidCommand("Missing calendar name after 'use'.");
      }
//
//      ICalendar found;
//      //type cast --------------------------
//      if (model instanceof CalendarManagerModel) {
//        found = ((CalendarManagerModel) model).findCalendarByName(calendarName);
//        if (found == null) {
//          return new InvalidCommand("No calendar found with name: '" + calendarName + "'.");
//        }
//        this.activeCalendar = found;
//      }
      return new UseCalendarCommand(calendarName);
    };
    }

    private Function<Scanner, CalendarCommand> copyCommandFactory() {
    return s -> {
      if (manager.getCurrentActiveCalendar() == null) {
        return new InvalidCommand("No calendar selected. Use 'use <calendar>' first.");
      }
      String fullCopyCommandArguments = s.nextLine().trim();
      if (fullCopyCommandArguments.isEmpty()) {
        return new InvalidCommand("Missing arguments for 'copy' command. " +
                "Please provide details for event, events on date, or events between dates.");
      }
      return new CopyEventCommand(fullCopyCommandArguments);
    };
    }


}