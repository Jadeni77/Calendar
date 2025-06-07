package calendar;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

import calendar.controller.CalendarController;
import calendar.model.calendarclass.CalendarModel;
import calendar.model.calendarclass.ICalendar;
import calendar.view.ICalendarView;
import calendar.view.TextBasedView;

/**
 * The driver of the calendar application.
 * It initializes the model, view, and controller based on the command line arguments.
 */
public class App {
  /**
   * The main method of the calendar application.
   * It accepts command line arguments to determine the mode (interactive or headless)
   * and initializes the model, view, and controller accordingly.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    try {
      if (args.length < 2 || !args[0].equalsIgnoreCase("--mode")) {
        System.out.println("Usage: java calendar.App --mode interactive");
        System.out.println("       java calendar.App --mode headless <commands-file>");
        return;
      }

      String mode = args[1].toLowerCase();
      StringBuilder log = new StringBuilder();
      ICalendar model = new CalendarModel();
      Reader input;
      ICalendarView view = null;
      CalendarController controller = null;

      if ("interactive".equals(mode)) {
        view = new TextBasedView(System.out);
        input = new InputStreamReader(System.in);
      } else if ("headless".equals(mode)) {
        if (args.length < 3) {
          System.out.println("Missing commands file for headless mode");
          return;
        }
        view = new TextBasedView(log);
        input = new FileReader("res/" + args[2]);
      } else {
        System.out.println("Invalid mode: " + mode);
        return;
      }
      controller = new CalendarController(model, view, input);
      controller.start();
      if ("headless".equals(mode)) {
        System.out.println("Log:\n" + log);
      }
    } catch (Exception e) {
      System.err.println("Fatal error: " + e.getMessage());
      e.printStackTrace();
    }
  }

}
//