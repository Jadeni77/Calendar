package calendar;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

import calendar.controller.CalendarManagerController;
import calendar.controller.guiadapter.EventListenerAdaptor;
import calendar.controller.guicontroller.GUIController;
import calendar.controller.guicontroller.IGUIController;
import calendar.model.calendarmanagerclass.CalendarManagerModel;
import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.view.GUIView;
import calendar.view.ICalendarView;
import calendar.view.IGUIView;
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
//      StringBuilder log = new StringBuilder();
//      ICalendarManager manager = new CalendarManagerModel();
//      IGUIView guiView;
//      IGUIController guiController;
//      EventListenerAdaptor adaptor;
//
//      Reader input;
//      ICalendarView view;
//      CalendarManagerController controller;

      if ("interactive".equals(mode)) {

        ICalendarManager manager = new CalendarManagerModel();
        GUIView view = new GUIView();
        IGUIController controller = new GUIController(manager, view);

        // Register controller as listener for view actions
        view.setAddEventButtonListener(e -> controller.handleAddEvent());
        view.setEditEventButtonListener(e -> controller.handleEditEvent());
        view.setCreateCalendarButtonListener(e -> controller.handleCreateCalendar());

        view.setSwitchCalendarListener(e -> controller.handleSwitchCalendar());
        view.setRefreshButtonListener(e -> controller.handleRefreshSchedule());
        view.setPrevMonthListener(e -> controller.handlePrevMonth());
        view.setNextMonthListener(e -> controller.handleNextMonth());

        // Start the application
        controller.start();
        view.setVisible(true);

      }

//      } else if ("headless".equals(mode)) {
//        if (args.length < 3) {
//          System.out.println("Missing commands file for headless mode");
//          return;
//        }
//        view = new TextBasedView(log);
//        input = new FileReader("res/" + args[2]);
//        controller = new CalendarManagerController(manager, view, input);
//        controller.start();
//        System.out.println("Log:\n" + log);
//      }
      else {
        System.out.println("Invalid mode: " + mode);
      }
    } catch (Exception e) {
      System.err.println("Fatal error: " + e.getMessage());
      e.printStackTrace();
    }
  }

}
//