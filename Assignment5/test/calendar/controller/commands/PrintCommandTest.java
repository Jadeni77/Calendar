package calendar.controller.commands;

import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import calendar.model.calendarclass.MockModel;
import calendar.view.ICalendarView;
import calendar.view.TextBasedView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the PrintCommand functionality in a calendar application.
 * It checks if the command correctly queries events based on different date formats
 * and handles invalid command formats gracefully.
 */
public class PrintCommandTest {
  private MockModel model;
  private ICalendarView view;
  private StringWriter log;

  @Before
  public void setUp() {
    StringBuilder modelLog = new StringBuilder();
    this.model = new MockModel(modelLog);
    log = new StringWriter();
    view = new TextBasedView(log);
  }

  @Test
  public void testPrintEventsOnDate() {
    PrintCommand cmd = new PrintCommand("events on 2023-10-01");
    cmd.execute(model, view);
    assertEquals("Event(s) Queried for date: 2023-10-01\n", model.log.toString());
    assertEquals("No events found on 2023-10-01", log.toString().trim());

  }

  @Test
  public void testPrintEventsInRange() {
    PrintCommand cmd = new PrintCommand("events from 2023-10-01T08:00 to 2023-10-05T17:00");
    cmd.execute(model, view);
    assertEquals("Events Queried from Start: 2023-10-01T08:00, to End: " +
            "2023-10-05T17:00\n", model.log.toString());
    assertEquals("No events found from 2023-10-01T08:00 to" +
            " 2023-10-05T17:00", log.toString().trim());
  }

  @Test
  public void testEmptyCommand() {
    PrintCommand cmd = new PrintCommand("");
    cmd.execute(model, view);
    assertTrue(log.toString().contains("Invalid command. Please provide a date or range."));
  }

  @Test
  public void testInvalidCommand() {
    PrintCommand cmd = new PrintCommand("invalid command syntax");
    cmd.execute(model, view);
    assertTrue(log.toString().contains("Invalid command format"));
  }
}
