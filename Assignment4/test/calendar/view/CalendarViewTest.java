package calendar.view;

import org.junit.Before;
import org.junit.Test;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import calendar.model.enumclass.Location;
import calendar.model.event.Event;

import static org.junit.Assert.assertEquals;

/**
 * This JUnit test class tests the functionality of the methods within the calendar's
 * implementation of the view, including displaying messages to users, printing queried events,
 * and showing the availability of a certain dateTime.
 */
public class CalendarViewTest {
  ICalendarView view;
  StringBuilder sb;
  Event e1;
  Event e2;
  Event e3;
  List<Event> eventList;

  /**
   * Initializes variables used during testing, including Event objects with different contents
   * and the view object and its StringBuilder log used throughout the tests.
   */
  @Before
  public void setUp() {
    this.sb = new StringBuilder();
    this.view = new TextBasedView(sb);
    this.e1 = new Event.EventBuilder()
            .subject("Subject 1")
            .startDateTime(LocalDateTime.of(2023, 10, 1, 10, 0))
            .endDateTime(LocalDateTime.of(2023, 10, 1, 20, 0))
            .build();
    this.e2 = new Event.EventBuilder()
            .subject("blahblahblah")
            .startDateTime(LocalDateTime.of(2025, 6, 4, 14, 15))
            .endDateTime(LocalDateTime.of(2025, 6, 4, 17, 30))
            .location(Location.PHYSICAL)
            .build();
    this.e3 = new Event.EventBuilder()
            .subject("Sub. 3")
            .startDateTime(LocalDateTime.of(2025, 4, 17, 11, 27))
            .isAllDayEvent(true)
            .build();
    this.eventList = new ArrayList<>();
  }

  /**
   * Tests that the displayMessage() method correctly displays a textual message to the user.
   */
  @Test
  public void testDisplayMessage() {
    view.displayMessage("Hello World!");
    assertEquals("Hello World!\n", sb.toString());
  }

  /**
   * Tests that the displayException() method correctly displays the message of the given
   * exception in text form to the user.
   */
  @Test
  public void testDisplayException() {
    view.displayException(new Exception("message"));
    assertEquals("An error was encountered:\nmessage\n", sb.toString());
  }

  /**
   * Tests that an event without a specified location is properly printed.
   */
  @Test
  public void testPrintEventNoLocation() {
    eventList.add(e1);
    view.printEvents(eventList);
    assertEquals("\u2022Subject 1 - Starts: 2023-10-01T10::00, Ends: 2023-10-01T08::00\n",
            sb.toString());
  }

  /**
   * Tests that an event with a specified location is properly printed.
   */
  @Test
  public void testPrintEventWithLocation() {
    eventList.add(e2);
    view.printEvents(eventList);
    assertEquals("\u2022blahblahblah - Starts: 2025-06-04T02::15, Ends: 2025-06-04T05::30"
            + ", Location: PHYSICAL\n", sb.toString());
  }

  /**
   * Tests that the result of printing multiple events at once is as expected.
   */
  @Test
  public void testPrintMultipleEvents() {
    eventList.add(e1);
    eventList.add(e2);
    eventList.add(e3);
    view.printEvents(eventList);
    assertEquals("\u2022Subject 1 - Starts: 2023-10-01T10::00, Ends: 2023-10-01T08::00\n"
            + "\u2022blahblahblah - Starts: 2025-06-04T02::15, Ends: " +
            "2025-06-04T05::30, Location: PHYSICAL\n"
            + "\u2022Sub. 3 - Starts: 2025-04-17T08::00, Ends: " +
            "2025-04-17T05::00\n", sb.toString());
  }

  /**
   * Tests that the showStatusOnDayTime() method displays the given boolean value properly, which
   * represents a query from the model for whether there is an event scheduled at a certain time.
   */
  @Test
  public void testShowStatusOnDayTime() {
    view.showStatusOnDayTime(true);
    assertEquals("Busy\n", sb.toString());
    view.showStatusOnDayTime(false);
    assertEquals("Busy\nAvailable\n", sb.toString());
  }
}
