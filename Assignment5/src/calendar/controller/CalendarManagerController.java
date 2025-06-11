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
   * @param view    the view to be controlled
   * @param in      the input channel to be read from
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
      //cannot move to private because need to use return;
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
      this.ifMatchCommand(matchedCommand, userInput);
    }
    this.checkQuitEnteredInFile(quitEntered);
  }

  /**
   * Checks if the command matched any known commands and executes it.
   * If no command matched, it attempts to create a new calendar controller
   * with the provided user input.
   *
   * @param matchedCommand whether a known command was matched
   * @param userInput      the user input string
   */
  private void ifMatchCommand(boolean matchedCommand, String userInput) {
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

  /**
   * Checks if the 'quit' command was entered in the file.
   * If not, it displays an error message indicating that the program is quitting.
   *
   * @param quitEntered whether the 'quit' command was entered
   */
  private void checkQuitEnteredInFile(boolean quitEntered) {
    if (!quitEntered) {
      this.view.displayMessage("Error: the 'quit' command was never entered. Quitting now...");
    }
  }

}