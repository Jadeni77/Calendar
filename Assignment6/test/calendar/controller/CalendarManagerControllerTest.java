package calendar.controller;

import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.time.ZoneId;

import calendar.model.calendarclass.ICalendar;
import calendar.model.calendarclass.NewCalendarMockModel;
import calendar.model.calendarmanagerclass.CalendarManagerMockModel;
import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.view.ICalendarView;

import static org.junit.Assert.assertEquals;

/**
 * This JUnit test class tests that CalendarManagerController is able to process user input
 * correctly and send the correct information to its view and model, based on the entered
 * operation.
 */
public class CalendarManagerControllerTest extends AbstractControllerTest {
  private ICalendarManager calendarManager;

  /**
   * Initializes the mock view and model used during testing.
   */
  @Before
  public void setUp() {
    super.setUp();
    this.calendarManager = new CalendarManagerMockModel((NewCalendarMockModel) this.model);
  }

  /**
   * Creates a new CalendarManagerController object using the given fields.
   *
   * @param view the view to be used in the CalendarManagerController
   * @param in   the input to be read from in the CalendarManagerController
   * @return a new CalendarManagerController object
   */
  @Override
  public ICalendarController createController(ICalendarView view, Readable in) {
    return new CalendarManagerController(calendarManager, view, in);
  }

  /**
   * Creates a new NewCalendarMockModel object.
   *
   * @return a new NewCalendarMockModel object
   */
  @Override
  public ICalendar createCalendar(StringBuilder modelLog) {
    return new NewCalendarMockModel("test1", ZoneId.of("America/New_York"), modelLog);
  }

  /**
   * Tests that the controller shows the expected greeting and farewell messages upon starting
   * the program and entering the quit command.
   */
  @Test
  public void testGreetingQuit() {
    StringReader fakeInput = new StringReader("quit\n");
    ICalendarController controller = createController(view, fakeInput);
    controller.start();
    String expected = getFullExpectedOutput(
            "Welcome to the Calendar Program!",
            "Enter 'menu' to see a list of commands.",
            "Thank you for using the Calendar Program. Goodbye!"
    );
    assertEquals(expected, this.log.toString());
  }

  /**
   * Tests that the controller shows the expected greeting and farewell messages upon starting
   * the program, but never entering quit at the end.
   */
  @Test
  public void testGreetingNoQuit() {
    StringReader fakeInput = new StringReader("");
    ICalendarController controller = createController(view, fakeInput);
    controller.start();
    String expected = getFullExpectedOutput(
            "Welcome to the Calendar Program!",
            "Enter 'menu' to see a list of commands.",
            "Error: the 'quit' command was never entered. Quitting now..."
    );
    assertEquals(expected, this.log.toString());
  }

  /**
   * Tests that the entry of the command 'create calendar' sends the expected information to
   * the model and view.
   */
  @Test
  public void testCreateCalendar() {
    StringReader fakeInput = new StringReader("create calendar --name test1 --timezone "
            + "America/New_York\nquit");
    ICalendarController controller = createController(view, fakeInput);
    controller.start();
    String expected = getFullExpectedOutput(
            "Welcome to the Calendar Program!",
            "Enter 'menu' to see a list of commands.",
            "Created calendar 'test1' with timezone 'America/New_York'.",
            "Thank you for using the Calendar Program. Goodbye!"
    );
    assertEquals(expected, this.log.toString());
  }

  /**
   * Tests that the controller sends the expected information to the model and the view when
   * an 'edit calendar' command is entered.
   */
  @Test
  public void testEditCalendar() {
    StringReader fakeInput = new StringReader("create calendar --name test1 --timezone"
            + " America/New_York\nedit calendar --name test1 --property timezone "
            + "America/Los_Angeles\nquit");
    ICalendarController controller = createController(view, fakeInput);
    controller.start();
    String expected = getFullExpectedOutput(
            "Welcome to the Calendar Program!",
            "Enter 'menu' to see a list of commands.",
            "Created calendar 'test1' with timezone 'America/New_York'.",
            "Calendar 'test1' updated successfully. Property 'timezone' set to "
                    + "'America/Los_Angeles'.",
            "Thank you for using the Calendar Program. Goodbye!"
    );
    assertEquals(expected, this.log.toString());
  }

  /**
   * Tests that the controller sends the expected information to the model and the view when
   * a 'use calendar' command is entered.
   */
  @Test
  public void testUseCalendar() {
    StringReader fakeInput = new StringReader("create calendar --name test1 --timezone"
            + " America/New_York\nuse calendar --name test1\nquit");
    ICalendarController controller = createController(view, fakeInput);
    controller.start();
    String expected = getFullExpectedOutput(
            "Welcome to the Calendar Program!",
            "Enter 'menu' to see a list of commands.",
            "Created calendar 'test1' with timezone 'America/New_York'.",
            "Switched to calendar 'test1'.",
            "Thank you for using the Calendar Program. Goodbye!"
    );
    assertEquals(expected, this.log.toString());
  }

  /**
   * Tests that the controller sends the expected information to the model and the view when
   * a 'copy event' command is entered.
   */
  @Test
  public void testCopyEvent() {
    StringReader fakeInput = new StringReader(
            "create calendar --name test1 --timezone America/New_York\n"
                    + "use calendar --name test1\n"
                    + "create event meeting on 2023-10-01\n"
                    + "copy event meeting on 2023-10-01T08:00 --target test1 to 2023-10-02T08:00\n"
                    + "quit");
    ICalendarController controller = createController(view, fakeInput);
    controller.start();
    String expected = getFullExpectedOutput(
            "Welcome to the Calendar Program!",
            "Enter 'menu' to see a list of commands.",
            "Created calendar 'test1' with timezone 'America/New_York'.",
            "Switched to calendar 'test1'.",
            "Event created with subject 'meeting' on date 2023-10-01.",
            "Event 'meeting' copied successfully to calendar 'test1' at 2023-10-02T08:00.",
            "Thank you for using the Calendar Program. Goodbye!"
    );
    assertEquals(expected, this.log.toString());
  }

  /**
   * Tests that the controller sends the expected information to the model and the view when
   * a 'copy events on' command is entered.
   */
  @Test
  public void testCopyEventsOnDate() {
    StringReader fakeInput = new StringReader(
            "create calendar --name test1 --timezone America/New_York\n"
                    + "use calendar --name test1\n"
                    + "create event hello on 2023-10-01 repeats MTR for 3 times\n"
                    + "copy events on 2023-10-01 --target test1 to 2023-10-02\n"
                    + "quit");
    ICalendarController controller = createController(view, fakeInput);
    controller.start();
    String expected = getFullExpectedOutput(
            "Welcome to the Calendar Program!",
            "Enter 'menu' to see a list of commands.",
            "Created calendar 'test1' with timezone 'America/New_York'.",
            "Switched to calendar 'test1'.",
            "Events created with subject 'hello', starting on 2023-10-01, on days "
                    + "MTR for 3 times.",
            "Events on 2023-10-01 copied successfully to calendar 'test1' at 2023-10-02.",
            "Thank you for using the Calendar Program. Goodbye!"
    );
    assertEquals(expected, this.log.toString());
  }

  /**
   * Tests that the controller sends the expected information to the model and the view when
   * a 'copy events between' command is entered.
   */
  @Test
  public void testCopyEventsBetweenDates() {
    StringReader fakeInput = new StringReader(
            "create calendar --name test1 --timezone America/New_York\n"
                    + "use calendar --name test1\n"
                    + "create event hello on 2023-10-01 repeats MTR for 3 times\n"
                    + "copy events between 2023-10-01 and 2023-10-04 --target"
                    + " test1 to 2023-10-02\n"
                    + "quit");
    ICalendarController controller = createController(view, fakeInput);
    controller.start();
    String expected = getFullExpectedOutput(
            "Welcome to the Calendar Program!",
            "Enter 'menu' to see a list of commands.",
            "Created calendar 'test1' with timezone 'America/New_York'.",
            "Switched to calendar 'test1'.",
            "Events created with subject 'hello', starting on 2023-10-01, on days "
                    + "MTR for 3 times.",
            "Events between 2023-10-01 and 2023-10-04 copied successfully to calendar"
                    + " 'test1' at 2023-10-02.",
            "Thank you for using the Calendar Program. Goodbye!"
    );
    assertEquals(expected, this.log.toString());
  }

  /**
   * Tests that the controller sends the expected information to the model and the view when
   * the 'menu' command is entered.
   */
  @Test
  public void testMenu() {
    StringReader fakeInput = new StringReader("menu\nquit");
    ICalendarController controller = createController(view, fakeInput);
    controller.start();
    String expected = getFullExpectedOutput(
            "Welcome to the Calendar Program!",
            "Enter 'menu' to see a list of commands.",
            "Available operations:\n" +
                    "create event <eventSubject> from <dateStringTtimeString> to "
                    + "<dateStringTtimeString>\n"
                    + "create event <eventSubject> from <dateStringTtimeString> to "
                    + "<dateStringTtimeString> repeats <weekdays> for <N> times\n"
                    + "create event <eventSubject> from <dateStringTtimeString> to "
                    + "<dateStringTtimeString> repeats <weekdays> until <dateString>\n"
                    + "create event <eventSubject> on <dateString>\n"
                    + "create event <eventSubject> on <dateString> repeats <weekdays> "
                    + "for <N> times\n"
                    + "create event <eventSubject> on <dateString> repeats <weekdays> until "
                    + "<dateString>\n"
                    + "edit event <property> <eventSubject> from <dateStringTtimeString> to "
                    + "<dateStringTtimeString> with <NewPropertyValue>\n"
                    + "edit events <property> <eventSubject> from <dateStringTtimeString> "
                    + "with <NewPropertyValue>\n"
                    + "edit series <property> <eventSubject> from <dateStringTtimeString> with "
                    + "<NewPropertyValue>\n"
                    + "print events on <dateString>\n"
                    + "print events from <dateStringTtimeString> to <dateStringTtimeString>\n"
                    + "show status on <dateStringTtimeString>\n"
                    + "create calendar --name <calName> --timezone area/location\n"
                    + "edit calendar --name <name> --property <property> <value>\n"
                    + "use calendar --name <name>\n"
                    + "copy event <eventName> on <datetime> --target <calendar> to <datetime>\n"
                    + "copy events on <date> --target <calendar> to <date>\n"
                    + "copy events between <start> and <end> --target <calendar> to <date>\n"
                    + "menu (Show this menu)\n"
                    + "q or quit (Exit the program)",
            "Thank you for using the Calendar Program. Goodbye!"
    );
    assertEquals(expected, this.log.toString());
  }
}
