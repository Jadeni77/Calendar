package calendar.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import calendar.controller.commands.CalendarCommand;
import calendar.controller.commands.CreateCommand;
import calendar.controller.commands.EditCommand;
import calendar.controller.commands.MenuCommand;
import calendar.controller.commands.PrintCommand;
import calendar.controller.commands.ShowCommand;
import calendar.model.calendarclass.ICalendar;
import calendar.view.ICalendarView;

/**
 * This class represents the controller for virtual calculator application which creates, displays,
 * and manages events. This controller handles various types of command-line modes for user
 * interaction, delegating model activity and output to users.
 */
public class CalendarController implements ICalendarController {
  protected final Map<String, Function<Scanner, CalendarCommand>> knownCommands;
  protected final ICalendar model;
  private final ICalendarView view;
  private final Readable in;

  /**
   * Initializes a controller with the inputted model and view to which it will delegate
   * functionality and output, as well as the channel it will receive user input from.
   *
   * @param model the model to be controlled
   * @param view  the view to be controlled
   * @param in    the input channel to be read from
   */
  public CalendarController(ICalendar model, ICalendarView view, Readable in) {
    knownCommands = new HashMap<>();
    knownCommands.put("create", s -> new CreateCommand(s.nextLine()));
    knownCommands.put("edit", s -> new EditCommand(s.nextLine()));
    knownCommands.put("print", s -> new PrintCommand(s.nextLine()));
    knownCommands.put("show", s -> new ShowCommand(s.nextLine()));
    knownCommands.put("menu", s -> new MenuCommand());
    this.model = model;
    this.view = view;
    this.in = in;
  }

  /**
   * Starts the controller, which gives it control over the application. This method continues to
   * process user inputted user commands until the user inputs an exit command, in which case the
   * application will be terminated. This handles commands such as creating, editing, and viewing
   * events.
   */
  public void start() {
    CalendarCommand c;
    Scanner s = new Scanner(in);
    boolean quitEntered = false;

    view.displayMessage("Welcome to the Calendar Program!");
    view.displayMessage("Enter 'menu' to see a list of commands.");

    while (s.hasNext()) {
      String userInput = s.nextLine().trim();

      if (userInput.isEmpty()) {
        continue;
      }
      if (userInput.equals("menu")) {
        new MenuCommand().execute(this.model, this.view);
        continue;
      }
      if (userInput.equals("quit") || userInput.equals("q")) {
        view.displayMessage("Thank you for using the Calendar Program. Goodbye!");
        return;
      }
      String firstWord = "";
      boolean exceptionThrown = false;
      try {
        firstWord = userInput.substring(0, userInput.indexOf(" "));
      } catch (StringIndexOutOfBoundsException e) {
        exceptionThrown = true;
        view.displayException(new IllegalArgumentException("Please enter a full command."));
      }
      Function<Scanner, CalendarCommand> command =
              knownCommands.getOrDefault(firstWord, null);
      if (command == null && !exceptionThrown) {
        view.displayException(new IllegalArgumentException("Unknown command: " + firstWord));
      } else if (!exceptionThrown) {
        String arguments = userInput.substring(firstWord.length() + 1);
        Scanner argsScanner = new Scanner(arguments);

        c = command.apply(argsScanner);
        c.execute(this.model, this.view);
      }
    }
    if (!quitEntered) {
      this.view.displayMessage("Error: the 'quit' command was never entered. Quitting now...");
    }
  }


}