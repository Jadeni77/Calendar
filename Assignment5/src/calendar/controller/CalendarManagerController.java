package calendar.controller;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import calendar.controller.commands.newcalendarcommand.MenuCommand;
import calendar.controller.commands.newcalendarcommand.CalendarManagerCommand;
import calendar.controller.commands.newcalendarcommand.CopyEventCommand;
import calendar.controller.commands.newcalendarcommand.CreateCalendarCommand;
import calendar.controller.commands.newcalendarcommand.EditCalendarCommand;
import calendar.controller.commands.newcalendarcommand.UseCalendarCommand;
import calendar.model.calendarclass.ICalendar;
import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.view.ICalendarView;

/**
 * TODO change (maybe??)
 * Represents a controller for managing calendar commands, specifically for creating, editing,
 * using, and copying events and calendars.
 * This controller extends the base CalendarController to provide additional functionality
 * for handling new calendar-related commands.
 */
public class CalendarManagerController implements ICalendarController {
  protected final Map<String, Function<Scanner, CalendarManagerCommand>> knownCommands;
  protected final ICalendarManager manager;
  protected final ICalendarView view;
  protected final Readable in;

  /**
   * TODO change this description
   * Initializes a controller with the inputted model and view to which it will delegate
   * functionality and output, as well as the channel it will receive user input from.
   *
   * @param manager the manager to be processed
   * @param view  the view to be controlled
   * @param in    the input channel to be read from
   */
  public CalendarManagerController(ICalendarManager manager, ICalendarView view, Readable in) {
    knownCommands = new HashMap<>();
    this.manager = manager;
    this.view = view;
    this.in = in;
    knownCommands.put("create calendar", s -> new CreateCalendarCommand(s.nextLine()));
    knownCommands.put("edit calendar", s -> new EditCalendarCommand(s.nextLine()));
    knownCommands.put("use calendar", s -> new UseCalendarCommand(s.nextLine()));
    knownCommands.put("copy", s -> new CopyEventCommand(s.nextLine()));
    knownCommands.put("menu", s -> new MenuCommand());
  }

  @Override
  public void start() {
    Scanner scanner = new Scanner(in);
    CalendarManagerCommand c;
    boolean quitEntered = false;

    view.displayMessage("Welcome to the Calendar Program!");
    view.displayMessage("Enter 'menu' to see a list of commands.");


    while (scanner.hasNext()) {
      String userInput = scanner.nextLine().trim();

      if (userInput.isEmpty()) {
        continue;
      }
      if (userInput.equals("menu")) {
        new MenuCommand().execute(this.manager, this.view);
        continue;
      }
      if (userInput.equals("quit") || userInput.equals("q")) {
        view.displayMessage("Thank you for using the Calendar Program. Goodbye!");
        return;
      }

      boolean matchedCommand = false;
      for (String command : knownCommands.keySet()) {
        if (userInput.startsWith(command)) {
          matchedCommand = true;
          Function<Scanner, CalendarManagerCommand> cmd =
                  knownCommands.get(command);
          String arguments = userInput.substring(command.length() + 1);
          Scanner argsScanner = new Scanner(arguments);

          c = cmd.apply(argsScanner);
          c.execute(this.manager, this.view);
          break;
        }
      }
      if (!matchedCommand) {
        try {
          ICalendar selectedModel = manager.getCurrentActiveCalendar();
          Reader newReader = new StringReader(userInput);
          ICalendarController controller = new CalendarController(selectedModel, view, newReader);
          controller.start();
        } catch (IllegalStateException e) {
          this.view.displayException(e);
        }
      }
    }
    if (!quitEntered) {
      this.view.displayMessage("Error: the 'quit' command was never entered. Quitting now...");
    }
  }

  /*
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

      switch (type) {
        case "event":
          return  new EditCommand(fullEditArgs);
        case "calendar":
          return new EditCalendarCommand(remainingArgs);
        default:
          return new InvalidCommand("Unknown 'edit' type: '" + type + "'. " +
                  "Expected 'event' or 'calendar'.");
      }
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
  */

}