package calendar.controller.commands;

import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import calendar.model.calendarclass.MockModel;
import calendar.view.ICalendarView;
import calendar.view.TextBasedView;

import static org.junit.Assert.assertEquals;

/**
 * This class represents a testing class for the EditCommand class in a caldendar application.
 * It tests various scenarios of editing events, including single events, multiple events,
 * and event series with different parameters.
 */
public class EditCommandTest {
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
  public void testEditSingleEvent() {
    EditCommand cmd = new EditCommand("event subject Meeting " +
            "from 2023-10-01T10:00 to 2023-10-01T11:30 with NewMeeting");
    cmd.execute(model, view);
    assertEquals("Single Event Updated: subject, Meeting, 2023-10-01T10:00, " +
            "2023-10-01T11:30, NewMeeting\n", model.log.toString());
    assertEquals("Event property 'subject' sucessfully edited to NewMeeting.\n",
            log.toString());
  }

  @Test
  public void testEditMultipleEvents() {
    EditCommand cmd = new EditCommand("events start Meeting from 2023-10-01T10:00 with" +
            " 2023-10-01T11:00");
    cmd.execute(model, view);
    assertEquals("Multiple Events Updated: start, Meeting, 2023-10-01T10:00, " +
                    "2023-10-01T11:00, false\n",
            model.log.toString());
    assertEquals("Event properties 'start' sucessfully edited to 2023-10-01T11:00.\n",
            log.toString());

  }

  @Test
  public void testEditEventSeries() {
    EditCommand cmd = new EditCommand("series location Meeting from 2023-10-01T10:00 with OFFICE");
    cmd.execute(model,view);
    assertEquals("Multiple Events Updated: location, Meeting, 2023-10-01T10:00, OFFICE, true\n",
            model.log.toString());
    assertEquals("Event properties 'location' sucessfully edited to OFFICE.\n",
            log.toString());
  }

  @Test
  public void testSubjectThatIsMoreThanOneWord() {
    EditCommand cmd = new EditCommand("event subject \"Meeting with Team\" " +
            "from 2023-10-01T10:00 to 2023-10-01T11:30 with A New Meeting Subject");
    cmd.execute(model, view);
    assertEquals("Single Event Updated: subject, Meeting with Team, " +
            "2023-10-01T10:00, 2023-10-01T11:30, A New Meeting Subject\n",
            model.log.toString());
    assertEquals("Event property 'subject' sucessfully edited to A New Meeting Subject.\n",
            log.toString());

  }

  @Test
  public void testInvalidEditCommand() {
    EditCommand cmd = new EditCommand("edit event subject Meeting from " +
            "2023-10-01T10:00 to 2023-10-01T11:30");
    cmd.execute(model, view);
    assertEquals("", model.log.toString());
    assertEquals("Invalid 'edit' command format. Please use 'edit event " +
            "<property> \"<subject>\" from <start> to <end> with <newValue>' " +
            "or 'edit series <property> \"<subject>\" from <start> with <newValue>'.\n",
            log.toString());
  }

  @Test
  public void testInvalidCommand() {
    EditCommand cmd = new EditCommand("invalid command syntax");
    cmd.execute(model, view);
    assertEquals("", model.log.toString());
    assertEquals("Invalid 'edit' command format. Please use 'edit event <property> " +
            "\"<subject>\" from <start> to <end> with <newValue>' or 'edit series <property> " +
            "\"<subject>\" from <start> with <newValue>'.\n", log.toString());
  }



}
