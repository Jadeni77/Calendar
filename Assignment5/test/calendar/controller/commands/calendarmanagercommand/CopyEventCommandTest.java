package calendar.controller.commands.calendarmanagercommand;

import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import calendar.model.calendarclass.ICalendar;
import calendar.model.calendarmanagerclass.CalendarManagerModel;
import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.model.event.Event;
import calendar.view.ICalendarView;
import calendar.view.TextBasedView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the CopyEventCommand functionality in a calendar application.
 * It checks if the command correctly copies an event from one calendar to another,
 * including handling errors and ensuring the event is copied with the correct details.
 */
public class CopyEventCommandTest {
  private ICalendarManager manager;
  private ICalendarView view;
  private StringWriter log;

  @Before
  public void setUp() {
    this.manager = new CalendarManagerModel();
    this.log = new StringWriter();
    this.view = new TextBasedView(log);

    manager.createCalendar("Old Calendar", "America/New_York");
    manager.createCalendar("New Calendar", "Europe/London");
    manager.createCalendar("Another Calendar", "America/Los_Angeles");
  }

  @Test
  public void testSingleEventCopy() {
    manager.setCurrentCalendar("Old Calendar");
    ICalendar oldCalendar = manager.getCurrentActiveCalendar();
    oldCalendar.createSingleEvent("Team Meeting", "2024-10-10T10:00", "2024-10-10T11:00");

    CalendarManagerCommand cmd = new CopyEventCommand("event Team Meeting " +
            "on 2024-10-10T10:00 --target New Calendar to 2024-10-11T20:00");
    cmd.execute(manager, view);
    String expected = "Event 'Team Meeting' copied successfully to calendar 'New Calendar' " +
            "at 2024-10-11T20:00.";
    assertTrue(log.toString().contains(expected));
  }

  @Test
  public void testCopyEventsOnDate() {
    manager.setCurrentCalendar("Old Calendar");
    ICalendar oldCalendar = manager.getCurrentActiveCalendar();
    oldCalendar.createAllDayEvent("Project Deadline", "2023-10-19");

    CalendarManagerCommand cmd = new CopyEventCommand("events on 2023-10-19 " +
            "--target Another Calendar to 2023-10-20");
    cmd.execute(manager, view);
    String expected = "Events on 2023-10-19 copied successfully to calendar 'Another Calendar' " +
            "at 2023-10-20.";
    assertTrue(log.toString().contains(expected));
  }

  @Test
  public void testCopyEventsBetweenDates() {
    manager.setCurrentCalendar("Old Calendar");
    ICalendar oldCalendar = manager.getCurrentActiveCalendar();
    oldCalendar.createRecurringEvent("Recurr", "2024-10-01T09:00", "2024-10-01T10:00",
            "MT", 3);

    CalendarManagerCommand cmd = new CopyEventCommand("events between 2024-10-01 and 2024-10-01 " +
            "--target New Calendar to 2024-10-04");
    cmd.execute(manager, view);
    String expected = "Events between 2024-10-01 and 2024-10-01 copied successfully to " +
            "calendar 'New Calendar' at 2024-10-04.";

    assertTrue(log.toString().contains(expected));
  }


  @Test
  public void testInvalidCommandSyntax() {
    manager.setCurrentCalendar("Old Calendar");
    ICalendar oldCalendar = manager.getCurrentActiveCalendar();
    oldCalendar.createSingleEvent("Team Meeting", "2024-10-10T10:00", "2024-10-10T11:00");

    CalendarManagerCommand cmd = new CopyEventCommand("event Team Meeting on" +
            " 2024-10-10T10:00 --target New Calendar to");
    cmd.execute(manager, view);
    String expected = "Invalid 'copy event' command format. Please use one of the following:\n" +
            "1. copy event \"<eventName>\" on <startDateTime> --target \"<calendarName>\" to " +
            "<newDateTime>\n" + "2. copy events on <dateString> --target \"<calendarName>\" " +
            "to <newDate>\n" + "3. copy events between <startDate> and <endDate> --target " +
            "\"<calendarName>\" to <newDate>";
    assertTrue(log.toString().contains(expected));
  }

  @Test
  public void testCopyToNonExistentCalendar() {
    manager.setCurrentCalendar("Old Calendar");
    ICalendar oldCalendar = manager.getCurrentActiveCalendar();
    oldCalendar.createSingleEvent("Team Meeting", "2024-10-10T10:00", "2024-10-10T11:00");

    CalendarManagerCommand cmd = new CopyEventCommand("event Team Meeting on" +
            " 2024-10-10T10:00 --target NonExistent Calendar to 2024-10-11T20:00");
    cmd.execute(manager, view);

    String expected = "An error was encountered:\n" +
            "Error copying event: Calendar 'NonExistent Calendar' not found.";
    System.out.println(log.toString());
    assertTrue(log.toString().contains(expected));

  }


  @Test
  public void testVerifyTimezoneConversionDuringCopy() {
    manager.setCurrentCalendar("Old Calendar");
    ICalendar oldCalendar = manager.getCurrentActiveCalendar();
    // Event in New York: 2024-10-10T10:00 to 2024-10-10T11:00
    oldCalendar.createSingleEvent("Team Meeting", "2024-10-10T10:00", "2024-10-10T11:00");

    // Copy to London, using the same instant in time
    CalendarManagerCommand cmd = new CopyEventCommand("event Team Meeting on" +
            " 2024-10-10T10:00 --target New Calendar to 2024-10-10T15:00");
    cmd.execute(manager, view);

    ICalendar newCalendar = manager.getTargetCalendar("New Calendar");
    List<Event> events = newCalendar.findEventsBySubjectAndStart(
            "Team Meeting", LocalDateTime.parse("2024-10-10T15:00"));

    Event copiedEvent = events.get(0);

    // Get the original and copied event instants
    ZoneId nyZone = ZoneId.of("America/New_York");
    ZoneId londonZone = ZoneId.of("Europe/London");
    ZonedDateTime originalStart = oldCalendar.getEvent(
            "Team Meeting",
            LocalDateTime.parse("2024-10-10T10:00"),
            LocalDateTime.parse("2024-10-10T11:00")
    ).getStartDateTime().atZone(nyZone);
    ZonedDateTime copiedStart = copiedEvent.getStartDateTime().atZone(londonZone);

    // Assert that the instants are equal
    assertEquals("Copied event should represent the same instant in time",
            originalStart.toInstant(), copiedStart.toInstant());
  }

  @Test
  public void testNull() {
    CalendarManagerCommand cmd = new CopyEventCommand(null);
    cmd.execute(manager, view);
    String expected = "An error was encountered:\n" +
            "Invalid arguments.\n";
    assertTrue(log.toString().contains(expected));
  }
}

