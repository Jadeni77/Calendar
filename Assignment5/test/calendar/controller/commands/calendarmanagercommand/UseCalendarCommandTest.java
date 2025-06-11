package calendar.controller.commands.calendarmanagercommand;

import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import calendar.model.calendarmanagerclass.CalendarManagerModel;
import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.view.ICalendarView;
import calendar.view.TextBasedView;

import static org.junit.Assert.assertTrue;

public class UseCalendarCommandTest {
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
  public void testError() {
    CalendarManagerCommand cmd = new UseCalendarCommand("test");
    cmd.execute(manager, view);
    String expected = "Invalid 'use calendar' command format. Please use " +
            "'use calendar --name \"<name>\"'.";
    assertTrue(log.toString().contains(expected));
  }

  @Test
  public void testUseCalendar() {
    CalendarManagerCommand createCalendar = new CreateCalendarCommand(
            "--name \"Test Calendar\" --timezone America/New_York");
    createCalendar.execute(manager, view);
    CalendarManagerCommand cmd = new UseCalendarCommand("--name \"Test Calendar\"");
    cmd.execute(manager, view);
    assertTrue(log.toString().contains("Switched to calendar 'Test Calendar'.\n"));
  }

  @Test
  public void testNull() {
    CalendarManagerCommand cmd = new UseCalendarCommand(null);
    cmd.execute(manager, view);
    String expected = "An error was encountered:\n" +
    "Invalid arguments.\n";
    assertTrue(log.toString().contains(expected));
  }

}