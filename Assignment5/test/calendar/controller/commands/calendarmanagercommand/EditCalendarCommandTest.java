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
 * This class tests the EditCalendarCommand functionality in a calendar application.
 * It checks if the command correctly edits an existing calendar's properties.
 */
public class EditCalendarCommandTest {
  private ICalendarManager manager;
  private ICalendarView view;
  private StringWriter log;
  private CalendarManagerCommand createCalendar;
  private CalendarManagerCommand useCalendar;

  @Before
  public void setUp() {
    this.manager = new CalendarManagerModel();
    this.log = new StringWriter();
    this.view = new TextBasedView(log);

    this.createCalendar = new CreateCalendarCommand("--name Old Name " +
            "--timezone America/New_York");
    createCalendar.execute(manager, view);

    this.useCalendar = new UseCalendarCommand("--name \"Old Name\"");
    useCalendar.execute(manager, view);
  }

  @Test
  public void testEditCalendarName() {
    CalendarManagerCommand cmd = new EditCalendarCommand("--name Old Name --property" +
            " name New Name");
    cmd.execute(manager, view);
    String expected = "Calendar 'Old Name' updated successfully. Property " +
            "'name' set to 'New Name'.\n";
    assertTrue(log.toString().contains(expected));
  }

  @Test
  public void testEditCalendarTimezone() {
    CalendarManagerCommand cmd = new EditCalendarCommand("--name Old Name --property" +
            " timezone Europe/London");
    cmd.execute(manager, view);
    String expected = "Calendar 'Old Name' updated successfully. Property " +
            "'timezone' set to 'Europe/London'.\n";
    assertTrue(log.toString().contains(expected));
  }

  @Test
  public void testInvalidPropertyName() {
    CalendarManagerCommand cmd = new EditCalendarCommand("--name Old Name --property" +
            " invalidProperty Some Value");
    cmd.execute(manager, view);
    String expected = "An error was encountered:\n" +
            "Error editing calendar: Unsupported property 'invalidProperty' " +
            "for calendar editing. Valid properties are: 'name', 'timezone'.";
    assertTrue(log.toString().contains(expected));
  }

  @Test
  public void TestInvalidTimezoneDuringUpdate() {
    CalendarManagerCommand cmd = new EditCalendarCommand("--name Old Name " +
            "--property timezone Invalid/Timezone");
    cmd.execute(manager, view);
    String expected = "An error was encountered:\n" +
            "Error editing calendar: Invalid or unsupported timezone ID: 'Invalid/Timezone'. " +
            "Please use IANA Time Zone Database format (e.g., 'America/New_York').\n";
    assertTrue(log.toString().contains(expected));

  }

  @Test
  public void testEditNonExistentCalendar() {
    CalendarManagerCommand cmd = new EditCalendarCommand("--name Non Existent --property" +
            " name New Name");
    cmd.execute(manager, view);
    String expected = "An error was encountered:\n" +
            "Error editing calendar: Calendar 'Non Existent' not found.";
    assertTrue(log.toString().contains(expected));
  }

  @Test
  public void testDuplicateNameDuringRename() {
    CalendarManagerCommand createSecondCalendar = new CreateCalendarCommand(
            "--name New Calendar --timezone America/New_York");
    createSecondCalendar.execute(manager, view);

    //the modification
    CalendarManagerCommand cmd = new EditCalendarCommand("--name Old Name --property" +
            " name New Calendar");
    cmd.execute(manager, view);
    String expected = "An error was encountered:\n" +
            "Error editing calendar: New Calendar name already exists";
    assertTrue(log.toString().contains(expected));
  }

  @Test
  public void testNull() {
    CalendarManagerCommand cmd = new EditCalendarCommand(null);
    cmd.execute(manager, view);
    String expected = "An error was encountered:\n" +
            "Invalid arguments.";
    assertTrue(log.toString().contains(expected));
  }

}
