package calendar.controller;

import java.util.Scanner;
import java.util.function.Function;

import calendar.controller.commands.CalendarCommand;
import calendar.controller.commands.CreateCommand;
import calendar.controller.commands.EditCommand;
import calendar.controller.commands.newcalendarcommand.CopyEventCommand;
import calendar.controller.commands.newcalendarcommand.CreateCalendarCommand;
import calendar.controller.commands.newcalendarcommand.EditCalendarCommand;
import calendar.controller.commands.newcalendarcommand.InvalidCommand;
import calendar.controller.commands.newcalendarcommand.UseCalendarCommand;
import calendar.model.calendarclass.ICalendar;
import calendar.view.ICalendarView;

/**
 * Represents a controller for managing calendar commands, specifically for creating, editing,
 * using, and copying events and calendars.
 * This controller extends the base CalendarController to provide additional functionality
 * for handling new calendar-related commands.
 */
public class NewCalendarController extends CalendarController {
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

  }

  private Function<Scanner, CalendarCommand> createCommandFactory() {
    return s -> {
      System.out.println("DEBUG: Inside createCommandFactory lambda.");
      // s.hasNext() checks if there is a next token in the scanner input
      if (!s.hasNext()) {
        System.out.println("DEBUG: createCommandFactory: No arguments found."); // Add this

        return new InvalidCommand("Missing type after 'create'." +
                "Expected 'event' or 'calendar'.");
      }
      String type = s.next().trim().toLowerCase();
      String remainingArgs = s.nextLine().trim();
      System.out.println("DEBUG: createCommandFactory: Parsed type='" + type + "', remainingArgs='" + remainingArgs + "'"); // Add this


      switch (type) {
        case "event":
          System.out.println("DEBUG: createCommandFactory: Dispatching to CreateEventCommand."); // Add this

          return new CreateCommand(remainingArgs);
        case "calendar":
          System.out.println("DEBUG: createCommandFactory: Dispatching to CreateCalendarCommand."); // Add this

          return new CreateCalendarCommand(remainingArgs);
        default:
          System.out.println("DEBUG: createCommandFactory: Unknown type '" + type + "'."); // Add this

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

        return new EditCommand(fullEditArgs);

//        switch (type) {
//          case "event":
//            return new EditCommand(remainingArgs);
//          case "calendar":
//            return new EditCalendarCommand(remainingArgs);
//          default:
//            return new InvalidCommand("Unknown 'edit' type: '" + type + "'. " +
//                    "Expected 'event' or 'calendar'.");
//        }
        //create event NewSeries on 2022-01-01 repeats T for 3 times
        //edit series name NewSeries from 2022-01-01T11:00 with Bye
        //create calendar --name helloworld --timezone Africa/Abidjan
        //se calendar --name ByeWorld
      };
    }

    private Function<Scanner, CalendarCommand> useCommandFactory() {
    return s -> {
      String calendarName = s.nextLine().trim();
      if (calendarName.isEmpty()) {
        return new InvalidCommand("Missing calendar name after 'use'.");
      }
      return new UseCalendarCommand(calendarName);
    };
    }

    private Function<Scanner, CalendarCommand> copyCommandFactory() {
    return s -> {
      String fullCopyCommandArguments = s.nextLine().trim();
      if (fullCopyCommandArguments.isEmpty()) {
        return new InvalidCommand("Missing arguments for 'copy' command. " +
                "Please provide details for event, events on date, or events between dates.");
      }
      return new CopyEventCommand(fullCopyCommandArguments);
    };
    }


}