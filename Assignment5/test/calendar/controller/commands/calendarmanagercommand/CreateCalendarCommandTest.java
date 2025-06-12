package calendar.controller.commands.calendarmanagercommand;

import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import calendar.model.calendarmanagerclass.CalendarManagerModel;
import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.view.ICalendarView;
import calendar.view.TextBasedView;

import static org.junit.Assert.assertTrue;

/**
 * This class tests the UseCalendarCommand functionality in a calendar application.
 * It checks if the command correctly switches to a specified calendar.
 */
public class CreateCalendarCommandTest {
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
  public void testCreateCalendar() {
    CalendarManagerCommand cmd = new CreateCalendarCommand("--name \"Work Calendar\" " +
            "--timezone America/New_York");
    cmd.execute(manager, view);
    String expected = "Created calendar 'Work Calendar' with timezone 'America/New_York'.\n";
    assertTrue(log.toString().contains(expected));
  }

  @Test
  public void duplicateCalendarName() {
    CalendarManagerCommand cmd = new CreateCalendarCommand("--name \"Existing Calendar\" " +
            "--timezone Europe/London");
    cmd.execute(manager, view);
    cmd = new CreateCalendarCommand("--name \"Existing Calendar\" " +
            "--timezone Asia/Tokyo");
    cmd.execute(manager, view);
    String expected = "An error was encountered:\n" +
            "Error creating calendar: Calendar already exists";
    assertTrue(log.toString().contains(expected));
  }

  @Test
  public void testInvalidTimeZoneFormat() {
    CalendarManagerCommand cmd = new CreateCalendarCommand("--name \"Invalid TZ Calendar\"" +
            " --timezone Invalid/Timezone");
    cmd.execute(manager, view);
    String expected = "An error was encountered:\n" +
            "Error creating calendar: Invalid or unsupported timezone ID: 'Invalid/Timezone'. " +
            "Please use IANA Time Zone Database format (e.g., 'America/New_York').\n";
    assertTrue(log.toString().contains(expected));
  }

  @Test
  public void testMissingRequiredParameters() {
    CalendarManagerCommand cmd = new CreateCalendarCommand("--name \"Incomplete Calendar\"");
    cmd.execute(manager, view);
    String expected = "Invalid 'create calendar' command format. " +
            "Please use 'create calendar --name \"<name>\" --timezone <timezone>'.";
    assertTrue(log.toString().contains(expected));
  }

  @Test
  public void testLongCalendarName() {
    String longName = "This is a very long calendar name that exceeds the typical length " +
            "and should be tested for handling in the calendar manager.";
    CalendarManagerCommand cmd = new CreateCalendarCommand("--name \"" + longName + "\" " +
            "--timezone America/New_York");
    cmd.execute(manager, view);
    String expected = "Created calendar '" + longName + "' with timezone 'America/New_York'.\n";
    assertTrue(log.toString().contains(expected));
  }

  @Test
  public void testNull() {
    CalendarManagerCommand cmd = new CreateCalendarCommand(null);
    cmd.execute(manager, view);
    String expected = "An error was encountered:\n" +
            "Invalid arguments.\n";
    assertTrue(log.toString().contains(expected));
  }
}
