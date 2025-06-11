package calendar.controller;

import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import calendar.model.calendarclass.MockModel;
import calendar.view.TextBasedView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This JUnit test class tests that the controller correctly handles incorrect user input and
 * sends the correct information to the view and model based on what operation the user entered.
 */
public class CalendarControllerTest {
  private StringBuilder log;
  private MockModel model;
  private TextBasedView view;

  /**
   * Initializes the mock view and model used during testing.
   */
  @Before
  public void setUp() {
    this.log = new StringBuilder();
    this.model = new MockModel();
    this.view = new TextBasedView(log);
  }

  /**
   * Returns the expected initial output of the controller, which includes the welcome message
   * and instructions for the user.
   *
   * @return the expected initial output as a String
   */
  private String getFullExpectedOutput(String... messages) {
    StringBuilder expected = new StringBuilder();
    for (String message : messages) {
      expected.append(message).append("\n");
    }
    return expected.toString();
  }

  /**
   * Tests that the controller correctly responds with error messages to an incomplete command
   * from the user input.
   */
  @Test
  public void testErrorMessage1() {
    StringReader fakeInput = new StringReader("create");
    CalendarController controller = new CalendarController(model, view, fakeInput);
    controller.start();
    String expected = getFullExpectedOutput(
            "An error was encountered:",
            "Please enter a full command."
    );
    assertEquals(expected, this.log.toString());
  }

  /**
   * Tests that the controller correctly responds with error messages to an unknown command
   * from the user input.
   */
  @Test
  public void testErrorMessage2() {
    StringReader fakeInput = new StringReader("replace syntax with bad");
    CalendarController controller2 = new CalendarController(model, view, fakeInput);
    controller2.start();
    String expected = getFullExpectedOutput(
            "An error was encountered:",
            "Unknown command: replace"
    );
    assertEquals(expected, this.log.toString());
  }

  /**
   * The below tests all test that each type of valid command that the user could input results
   * in the controller sending the correct information to the view and/or the model, based on
   * operations within the controller's start() method.
   */
  @Test
  public void testCreateSingleEvent() {
    StringReader fakeInput = new StringReader(
            "create event Meeting from 2023-10-01T10:00 to 2023-10-01T11:30");
    CalendarController controller = new CalendarController(model, view, fakeInput);
    controller.start();


    assertEquals("Single Event Created: Meeting, 2023-10-01T10:00, 2023-10-01T11:30\n",
            model.log.toString());
    assertTrue(log.toString().contains("Event created with subject 'Meeting', " +
            "start 2023-10-01T10:00, " +
            "and end 2023-10-01T11:30.\n"));
  }


  @Test
  public void testCreateRecurringEvent() {
    StringReader fakeInput = new StringReader(
            "create event Meeting from 2023-10-01T10:00 to 2023-10-01T11:30" +
                    " repeats MTR for 3 times");
    CalendarController controller = new CalendarController(model, view, fakeInput);
    controller.start();
    assertEquals("Recurring Event Created: Meeting, 2023-10-01T10:00, 2023-10-01T11:30," +
                    " on MTR, repeats 3 times\n",
            model.log.toString());
    assertTrue(log.toString().contains("Events created with subject 'Meeting'"));
  }

  @Test
  public void testCreateRecurringEvent2() {
    StringReader fakeInput = new StringReader(
            "create event Meeting from 2023-10-01T10:00 to 2023-10-01T11:30" +
                    " repeats MTR until 2023-10-07");
    CalendarController controller = new CalendarController(model, view, fakeInput);
    controller.start();
    assertEquals("Recurring Event Created: Meeting, 2023-10-01T10:00, 2023-10-01T11:30," +
                    " on MTR, until 2023-10-07\n",
            model.log.toString());
    assertTrue(log.toString().contains("Events created with subject 'Meeting'"));
  }

  @Test
  public void testCreateSingleAllDayEvent() {
    StringReader fakeInput = new StringReader(
            "create event Meeting2 on 2023-10-01");
    CalendarController controller = new CalendarController(model, view, fakeInput);
    controller.start();
    assertEquals("Single All-Day Event Created: Meeting2, 2023-10-01\n",
            model.log.toString());
    assertTrue(log.toString().contains("Event created with subject 'Meeting2'"));
  }

  @Test
  public void testCreateRecurringAllDayEvent() {
    StringReader fakeInput = new StringReader(
            "create event Meeting on 2023-10-01 repeats MTR for 3 times");
    CalendarController controller = new CalendarController(model, view, fakeInput);
    controller.start();
    assertEquals("Recurring Event Created: Meeting, on 2023-10-01," +
                    " on days MTR, repeats 3 times\n",
            model.log.toString());
    assertTrue(log.toString().contains("Events created with subject 'Meeting'"));
  }

  @Test
  public void testCreateRecurringAllDayEvent2() {
    StringReader fakeInput = new StringReader(
            "create event Meeting on 2023-10-01 repeats MTR until 2023-10-07");
    CalendarController controller = new CalendarController(model, view, fakeInput);
    controller.start();
    assertEquals("Recurring Event Created: Meeting, on 2023-10-01," +
                    " on days MTR, until 2023-10-07\n",
            model.log.toString());
    assertTrue(log.toString().contains("Events created with subject 'Meeting'"));
  }

  @Test
  public void testEditSingleEvent() {
    StringReader fakeInput = new StringReader(
            "edit event subject Meeting from 2023-10-01T10:00 " +
                    "to 2023-10-01T11:30 with NewMeeting\n" +
                    "quit\n");
    CalendarController controller = new CalendarController(model, view, fakeInput);
    controller.start();

    assertEquals("Single Event Updated: subject, Meeting, " +
                    "2023-10-01T10:00, 2023-10-01T11:30, NewMeeting\n",
            model.log.toString());
    assertTrue(log.toString().contains("Event property 'subject' " +
            "sucessfully edited to NewMeeting.\n"));
  }

  @Test
  public void testEditMultipleEvents() {
    StringReader fakeInput = new StringReader(
            "edit events subject Meeting from 2023-10-01T10:00 with NewMeeting\n" +
                    "quit\n");
    CalendarController controller = new CalendarController(model, view, fakeInput);
    controller.start();

    assertEquals("Multiple Events Updated: subject, Meeting, " +
                    "2023-10-01T10:00, NewMeeting, false\n",
            model.log.toString());
    assertTrue(log.toString().contains("Event properties 'subject' " +
            "sucessfully edited to NewMeeting."));
  }

  @Test
  public void testEditSeriesEvents() {
    StringReader fakeInput = new StringReader(
            "edit series subject Meeting from 2023-10-01T10:00 with NewMeeting\n" +
                    "quit\n");
    CalendarController controller = new CalendarController(model, view, fakeInput);
    controller.start();

    assertEquals("Multiple Events Updated: subject, Meeting, " +
                    "2023-10-01T10:00, NewMeeting, true\n",
            model.log.toString());
    assertTrue(log.toString().contains("Event properties 'subject' " +
            "sucessfully edited to NewMeeting.\n"));
  }

  @Test
  public void testPrintEventsOnDay() {
    StringReader fakeInput = new StringReader("print events on 2023-10-01\nquit\n");
    CalendarController controller = new CalendarController(model, view, fakeInput);
    controller.start();
    assertEquals("Event(s) Queried for date: 2023-10-01\n", model.log.toString());
  }

  @Test
  public void testPrintEventsInRange() {
    StringReader fakeInput = new StringReader(
            "print events from 2023-10-01T00:00 to 2023-10-07T23:59\nquit\n");
    CalendarController controller = new CalendarController(model, view, fakeInput);
    controller.start();

    assertEquals("Events Queried from Start: " +
                    "2023-10-01T00:00, to End: 2023-10-07T23:59\n",
            model.log.toString());
  }

  @Test
  public void testShowStatus() {
    StringReader fakeInput = new StringReader("show status on 2023-10-01T10:00");
    CalendarController controller = new CalendarController(model, view, fakeInput);
    controller.start();
    assertEquals("Check for business: 2023-10-01T10:00\n", model.log.toString());
    assertTrue(log.toString().contains("Status at 2023-10-01T10:00:"));

  }

  @Test
  public void testSingleWordCommand() {
    StringReader fakeInput = new StringReader("q");
    CalendarController controller = new CalendarController(model, view, fakeInput);
    controller.start();
    assertTrue(log.toString().contains("An error was encountered:\n" +
            "Please enter a full command."));
  }

  @Test
  public void testUnknownCommand() {
    StringReader fakeInput = new StringReader("unknown command");
    CalendarController controller = new CalendarController(model, view, fakeInput);
    controller.start();
    assertTrue(log.toString().contains("Unknown command: unknown"));
  }

  @Test
  public void testInvalidCreateCommand() {
    StringReader fakeInput = new StringReader("create");
    CalendarController controller = new CalendarController(model, view, fakeInput);
    controller.start();
    assertTrue(log.toString().contains("An error was encountered:"));


  }
}
