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
public class ShowCommandTest {
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
  public void testShowStatus() {
    ShowCommand cmd = new ShowCommand("status on 2023-10-01T10:00");
    cmd.execute(model, view);
    assertEquals("Check for business: 2023-10-01T10:00\n", model.log.toString());
    assertTrue(log.toString().contains("Status at 2023-10-01T10:00:"));
  }

  @Test
  public void testInvalidCommand() {
    ShowCommand cmd = new ShowCommand("invalid command syntax");
    cmd.execute(model, view);
    assertTrue(log.toString().contains("Invalid command format"));
  }
}

