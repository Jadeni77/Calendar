package calendar.controller.commands;

import calendar.model.calendarclass.MockModel;
import calendar.view.ICalendarView;
import calendar.view.TextBasedView;

import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 * This class represents a testing class for the CreateCommand class in a calendar application.
 * It tests various scenarios of creating events, including single events, all-day events,
 * and recurring events with different parameters.
 */
public class CreateCommandTest {
  private MockModel model;
  private ICalendarView view;
  private StringWriter log;

  @Before
  public void setUp() {
    StringBuilder modelLog = new StringBuilder();
    this.model = new MockModel(modelLog);
    this.log = new StringWriter();
    this.view = new TextBasedView(log);
  }

  @Test
  public void testCreateSingleEvent() {
    CreateCommand cmd = new CreateCommand("Meeting from " +
            "2023-10-01T10:00 to 2023-10-01T11:30");
    cmd.execute(model, view);
    assertEquals("Single Event Created: Meeting, 2023-10-01T10:00, " +
            "2023-10-01T11:30\n", model.log.toString());

    assertEquals("Event created with subject 'Meeting', " +
            "start 2023-10-01T10:00, and end 2023-10-01T11:30.\n", log.toString());
  }

  @Test
  public void testCreateAllDayEvent() {
    CreateCommand cmd = new CreateCommand("Meeting on 2023-10-01");
    cmd.execute(model, view);
    assertEquals("Single All-Day Event Created: Meeting, " +
            "2023-10-01\n", model.log.toString());
    assertEquals("Event created with subject 'Meeting' on " +
            "date 2023-10-01.\n", log.toString());
  }

  @Test
  public void testCreateRecurringEventWithCount() {
    CreateCommand cmd = new CreateCommand("Meeting from 2023-10-01T10:00 " +
            "to 2023-10-01T11:30 repeats MWF for 5 times");
    cmd.execute(model, view);
    assertEquals("Recurring Event Created: Meeting, 2023-10-01T10:00, " +
            "2023-10-01T11:30, on MWF, repeats 5 times\n", model.log.toString());
  }

  @Test
  public void testCreateRecurringEventWithUntil() {
    CreateCommand cmd = new CreateCommand("Meeting from 2023-10-01T10:00 to " +
            "2023-10-01T11:30 repeats MWF until 2023-10-31");
    cmd.execute(model, view);
    assertEquals("Recurring Event Created: Meeting, 2023-10-01T10:00, " +
            "2023-10-01T11:30, on MWF, until 2023-10-31\n", model.log.toString());
  }

  @Test
  public void testCreateRecurringAllDayWithCount() {
    CreateCommand cmd = new CreateCommand("Meeting on 2023-10-01 repeats MWF " +
            "for 5 times");
    cmd.execute(model, view);
    assertEquals("Recurring Event Created: Meeting, on 2023-10-01, on days MWF, " +
            "repeats 5 times\n", model.log.toString());
  }

  @Test
  public void testCreateRecurringAllDayWithUntil() {
    CreateCommand cmd = new CreateCommand("Meeting on 2023-10-01 repeats MWF until" +
            " 2023-10-31");
    cmd.execute(model, view);
    assertEquals("Recurring Event Created: Meeting, on 2023-10-01, on days MWF, " +
            "until 2023-10-31\n", model.log.toString());
  }

  @Test
  public void testSubjectThatIsMoreThanOneWord() {
    CreateCommand cmd = new CreateCommand("\"Team Meeting\" from 2023-10-01T10:00 " +
            "to 2023-10-01T11:30");
    cmd.execute(model, view);
    assertEquals("Single Event Created: Team Meeting, 2023-10-01T10:00, " +
            "2023-10-01T11:30\n", model.log.toString());
  }

  @Test
  public void testInvalidCommand() {
    CreateCommand cmd = new CreateCommand("invalid command syntax");
    cmd.execute(model, view);
    assertEquals("Invalid command. Please provide valid event details.\n",
            log.toString());
  }
}