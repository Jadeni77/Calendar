package calendar.controller.commands.calendarmanagercommand;

import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import calendar.model.calendarmanagerclass.CalendarManagerModel;
import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.view.ICalendarView;
import calendar.view.TextBasedView;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the InvalidCommand functionality in a calendar application.
 * It checks if the command correctly displays an error message when an invalid command is executed.
 */
public class InvalidCommandTest {
  private ICalendarManager manager;
  private ICalendarView view;
  private StringWriter log;

  @Before
  public void setUp() {
    this.manager = new CalendarManagerModel();
    this.log = new StringWriter();
    this.view = new TextBasedView(log);
  }

  @Test
  public void testInvalidCommand() {
    CalendarManagerCommand cmd = new InvalidCommand("invalid command");
    cmd.execute(manager, view);
    String expected = "An error was encountered:\n" +
            "invalid command\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testEmptyCommand() {
    CalendarManagerCommand cmd = new InvalidCommand(" ");
    cmd.execute(manager, view);
    String expected = "An error was encountered:\n" +
            " \n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testNull() {
    CalendarManagerCommand cmd = new InvalidCommand(null);
    cmd.execute(manager, view);
    String expected = "An error was encountered:\n" +
            "null\n";
    assertEquals(expected, log.toString());
  }

}
