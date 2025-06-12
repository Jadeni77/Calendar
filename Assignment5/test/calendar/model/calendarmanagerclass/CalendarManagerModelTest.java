package calendar.model.calendarmanagerclass;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import calendar.model.calendarclass.NewCalendarModel;
import calendar.model.event.Event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This JUnit test class tests the functionality of the methods within the ICalendarManager
 * interface, relating to creation, editing, copying, and use of calendars within a calendar
 * manager.
 */
public class CalendarManagerModelTest {
  private CalendarManagerModel cmm;
  private DateTimeFormatter dateTimeFormatter;

  /**
   * Initializes any variables, fields, or instances of classes needed for testing.
   */
  @Before
  public void setUp() {
    this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    this.cmm = new CalendarManagerModel();
  }

  /**
   * Tests that the createCalendar() function properly creates a calendar with the specified name
   * and timezone, as well as that the getTargetCalendar() method properly returns the desired
   * calendar from the model.
   */
  @Test
  public void testCreateAndTargetCalendar() {
    cmm.createCalendar("test1", "America/New_York");
    cmm.createCalendar("test2", "America/Los_Angeles");
    NewCalendarModel test1 = cmm.getTargetCalendar("test1");
    assertEquals("test1", test1.getName());
    assertEquals(ZoneId.of("America/New_York"), test1.getTimeZone());
    NewCalendarModel test2 = cmm.getTargetCalendar("test2");
    assertEquals("test2", test2.getName());
    assertEquals(ZoneId.of("America/Los_Angeles"), test2.getTimeZone());
  }

  /**
   * Tests that the createCalendar() function properly throws errors encountered during the
   * creation of a calendar.
   */
  @Test
  public void testCreateCalendarExceptions() {
    cmm.createCalendar("test1", "America/New_York");
    try {
      cmm.createCalendar("test1", "America/New_York");
    } catch (IllegalArgumentException e) {
      assertEquals("Calendar already exists", e.getMessage());
    }
    try {
      cmm.createCalendar("test2", "abc123");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid or unsupported timezone ID: 'abc123'. Please use "
              + "IANA Time Zone Database format (e.g., 'America/New_York').", e.getMessage());
    }
  }

  /**
   * Tests that the editCalendar() function properly modifies the properties of the specified
   * calendar.
   */
  @Test
  public void testEditCalendar() {
    cmm.createCalendar("test1", "America/New_York");
    NewCalendarModel test1 = cmm.getTargetCalendar("test1");
    assertEquals("test1", test1.getName());
    assertEquals(ZoneId.of("America/New_York"), test1.getTimeZone());
    cmm.editCalendar("test1", "name", "hello");
    try {
      NewCalendarModel test1Gone = cmm.getTargetCalendar("test1");
    } catch (IllegalArgumentException e) {
      assertEquals("Calendar 'test1' not found.", e.getMessage());
    }
    assertEquals("hello", test1.getName());
    assertEquals(ZoneId.of("America/New_York"), test1.getTimeZone());
    cmm.editCalendar("hello", "timezone", "America/Los_Angeles");
    assertEquals("hello", test1.getName());
    assertEquals(ZoneId.of("America/Los_Angeles"), test1.getTimeZone());
    try {
      cmm.editCalendar("hello", "hello", "America/New_York");
    } catch (IllegalArgumentException e) {
      assertEquals("Unsupported property 'hello' for calendar editing. "
              + "Valid properties are: 'name', 'timezone'.", e.getMessage());
    }
  }

  /**
   * Tests that the setCurrentCalendar() and getCurrentActiveCalendar() properly set and retrieve
   * a manager's current active calendar.
   */
  @Test
  public void testGetSetActiveCalendar() {
    cmm.createCalendar("test1", "America/New_York");
    cmm.createCalendar("test2", "America/Los_Angeles");
    NewCalendarModel test1 = cmm.getTargetCalendar("test1");
    NewCalendarModel test2 = cmm.getTargetCalendar("test2");
    try {
      cmm.getCurrentActiveCalendar();
    } catch (IllegalStateException e) {
      assertEquals("No active calendar is currently selected. Please use the command 'use "
              + "calendar --name <calendar name>' to select a calendar first.", e.getMessage());
    }
    cmm.setCurrentCalendar("test2");
    NewCalendarModel currentCalendar = cmm.getCurrentActiveCalendar();
    assertEquals("test2", currentCalendar.getName());
    assertEquals(ZoneId.of("America/Los_Angeles"), currentCalendar.getTimeZone());
  }

  /**
   * Tests that any functionality related to events cannot be used until a calendar is selected.
   */
  @Test
  public void testActiveCalendarCheck() {
    cmm.createCalendar("test1", "America/New_York");
    cmm.createCalendar("test2", "America/Los_Angeles");
    NewCalendarModel test1 = cmm.getTargetCalendar("test1");
    test1.createSingleEvent("event1", "2025-06-11T03:30", "2025-06-11T05:30");
    try {
      cmm.copyEvent("event1", "2025-06-11T03:30", "test2", "2025-06-11T03:30");
    } catch (IllegalStateException e) {
      assertEquals("No active calendar is currently selected. Please use the command 'use"
              + " calendar --name <calendar name>' to select a calendar first.", e.getMessage());
    }
  }

  /**
   * Tests that the copyEvent() method properly copies a single event from one calendar to another
   * on the given date/time.
   */
  @Test
  public void testCopyEvent() {
    cmm.createCalendar("test1", "America/New_York");
    cmm.createCalendar("test2", "America/Los_Angeles");
    cmm.setCurrentCalendar("test1");
    NewCalendarModel test1 = cmm.getTargetCalendar("test1");
    NewCalendarModel test2 = cmm.getTargetCalendar("test2");
    assertTrue(test2.getEventsOnDate(LocalDate.of(2025, 6, 10)).isEmpty());
    test1.createSingleEvent("event1", "2025-06-11T00:30", "2025-06-11T05:30");
    cmm.copyEvent("event1", "2025-06-11T00:30", "test2", "2025-06-11T03:30");
    // 2025-06-11T03:30 in NY is 2025-06-10T00:30 in LA (same instant)
    assertEquals(1, test2.getEventsOnDate(LocalDate.of(2025, 6, 10)).size());
    Event copiedEvent = test2.getEventsOnDate(LocalDate.of(2025, 6, 10)).get(0);
    assertEquals("event1", copiedEvent.getSubject());
    assertEquals(LocalDateTime.parse("2025-06-10T21:30", dateTimeFormatter),
            copiedEvent.getStartDateTime());
    assertEquals(LocalDateTime.parse("2025-06-11T02:30", dateTimeFormatter),
            copiedEvent.getEndDateTime());

    test1.createSingleEvent("event2", "2025-06-11T04:30", "2025-06-11T05:30");
    cmm.copyEvent("event2", "2025-06-11T04:30", "test2", "2025-06-12T07:30");
    assertEquals(2, test2.getEventsOnDate(LocalDate.of(2025, 6, 11)).size());
    Event copiedEvent2 = test2.getEventsOnDate(LocalDate.of(2025, 6, 11)).get(1);
    assertEquals("event2", copiedEvent2.getSubject());
    assertEquals(LocalDateTime.parse("2025-06-11T01:30", dateTimeFormatter),
            copiedEvent2.getStartDateTime());
    assertEquals(LocalDateTime.parse("2025-06-11T02:30", dateTimeFormatter),
            copiedEvent2.getEndDateTime());
  }


  /**
   * Tests that the copyEventsOnDate() method properly copies all the events that occur on a
   * given date from one calendar to another to the given target date.
   */
  @Test
  public void testCopyEventsOnDate() {
    cmm.createCalendar("test1", "America/New_York");
    cmm.createCalendar("test2", "America/Los_Angeles");
    cmm.setCurrentCalendar("test1");
    NewCalendarModel test1 = cmm.getTargetCalendar("test1");
    NewCalendarModel test2 = cmm.getTargetCalendar("test2");
    test1.createSingleEvent("event1", "2025-06-11T03:30", "2025-06-11T05:30");
    test1.createRecurringEvent("class", "2025-06-11T13:00", "2025-06-11T15:00", "MWR", 1);
    cmm.copyEventsOnDate("2025-06-11", "test2", "2025-06-12");
    assertEquals(2, test2.getEventsOnDate(LocalDate.of(2025, 6, 12)).size());
    Event copiedEvent = test2.getEventsOnDate(LocalDate.of(2025, 6, 12)).get(0);
    Event copiedEvent2 = test2.getEventsOnDate(LocalDate.of(2025, 6, 12)).get(1);
    assertEquals("event1", copiedEvent.getSubject());
    assertEquals(LocalDateTime.parse("2025-06-12T00:30", dateTimeFormatter),
            copiedEvent.getStartDateTime());
    assertEquals(LocalDateTime.parse("2025-06-12T02:30", dateTimeFormatter),
            copiedEvent.getEndDateTime());
    assertEquals("class", copiedEvent2.getSubject());
    assertEquals(LocalDateTime.parse("2025-06-12T10:00", dateTimeFormatter),
            copiedEvent2.getStartDateTime());
    assertEquals(LocalDateTime.parse("2025-06-12T12:00", dateTimeFormatter),
            copiedEvent2.getEndDateTime());
  }

  /**
   * Tests that the copyEventsBetweenDates() method properly copies all the events that occur
   * between the given start and end date, starting on the given target date.
   */
  @Test
  public void testCopyEventsBetweenDates() {
    cmm.createCalendar("test1", "America/New_York");
    cmm.createCalendar("test2", "America/Los_Angeles");
    cmm.setCurrentCalendar("test1");
    NewCalendarModel test1 = cmm.getTargetCalendar("test1");
    NewCalendarModel test2 = cmm.getTargetCalendar("test2");
    test1.createRecurringEvent("work", "2025-06-11T13:00", "2025-06-11T15:00", "MWR", 3);
    cmm.copyEventsBetweenDates("2025-06-11", "2025-06-13", "test2", "2025-06-12");
    List<Event> copiedEvents = test2.getEventsInRange(LocalDateTime.of(2025, 6, 11, 0, 0),
            LocalDateTime.of(2025, 6, 13, 23, 59));
    assertEquals(2, copiedEvents.size());
    Event copiedEvent = copiedEvents.get(0);
    Event copiedEvent2 = copiedEvents.get(1);
    assertEquals("work", copiedEvent.getSubject());
    assertEquals(LocalDateTime.parse("2025-06-12T10:00", dateTimeFormatter),
            copiedEvent.getStartDateTime());
    assertEquals(LocalDateTime.parse("2025-06-12T12:00", dateTimeFormatter),
            copiedEvent.getEndDateTime());
    assertEquals("work", copiedEvent2.getSubject());
    assertEquals(LocalDateTime.parse("2025-06-13T10:00", dateTimeFormatter),
            copiedEvent2.getStartDateTime());
    assertEquals(LocalDateTime.parse("2025-06-13T12:00", dateTimeFormatter),
            copiedEvent2.getEndDateTime());
  }
}
