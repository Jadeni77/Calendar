package calendar.controller;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import calendar.controller.commands.calendarmanagercommand.MenuCommand;
import calendar.controller.commands.calendarmanagercommand.CalendarManagerCommand;
import calendar.controller.commands.calendarmanagercommand.CopyEventCommand;
import calendar.controller.commands.calendarmanagercommand.CreateCalendarCommand;
import calendar.controller.commands.calendarmanagercommand.EditCalendarCommand;
import calendar.controller.commands.calendarmanagercommand.UseCalendarCommand;
import calendar.model.calendarclass.ICalendar;
import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.view.ICalendarView;

/**
 * Represents a controller for managing calendar commands, specifically for creating, editing,
 * using, and copying events and calendars.
 */
public class CalendarManagerController implements ICalendarController {
  protected final Map<String, Function<Scanner, CalendarManagerCommand>> knownCommands;
  protected final ICalendarManager manager;
  protected final ICalendarView view;
  protected final Readable in;

  /**
   * Initializes a controller with the inputted manager and view to which it will delegate
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

  /**
   * Starts the controller, which gives it control over the application. This method continuously
   * takes in commands until the quit command is entered or the end of the file is reached, in
   * which case the application will be terminated. This method handles operations related to
   * individual calendars such as creation, editing, use, and copying events from one to another,
   * in addition to the menu command. If a command does not match one of these, it will be
   * delegated to the CalendarController with the selected calendar (from the 'use' command). If
   * there is no selected calendar, an error will be displayed.
   */
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
}