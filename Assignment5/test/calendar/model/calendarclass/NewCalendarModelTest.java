package calendar.model.calendarclass;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import calendar.model.event.Event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This JUnit test class tests the functionality of the methods within the
 * NewCalendarModel class.
 */
public class NewCalendarModelTest extends AbstractCalendarModelTest {
  private NewCalendarModel c;
  private NewCalendarModel c2;
  LocalDateTime start;
  LocalDateTime end;

  /**
   * Initializes any variables or fields necessary for testing the functionality of unique
   * properties from the NewCalendarModel class.
   */
  @Before
  public void setUp() {
    super.setUp();
    this.c = new NewCalendarModel("test1", ZoneId.of("America/New_York"));
    this.start = LocalDateTime.of(2025, 1, 1, 4, 30);
    this.end = LocalDateTime.of(2025, 1, 1, 7, 30);
    c.createSingleEvent("meeting", start.toString(), end.toString());
    this.c2 = new NewCalendarModel("test2", ZoneId.of("America/Los_Angeles"));
    this.c2.createRecurringEvent("meeting", start.toString(), end.toString(), "MWF", 3);
  }

  /**
   * Constructs a new NewCalendarModel object.
   * @return an NewCalendarModel object
   */
  @Override
  public ICalendar createCalendarModel() {
    return new NewCalendarModel("test1", ZoneId.of("America/New_York"));
  }

  /**
   * Tests that setting a calendar's time zone to something earlier than its current one works
   * as expected.
   */
  @Test
  public void testSetTimeZoneBackwards() {
    assertEquals(1, c.getEventsOnDate(LocalDate.of(2025, 1, 1)).size());
    c.setTimeZone(ZoneId.of("America/Los_Angeles"));
    assertEquals(1, c.getEventsOnDate(LocalDate.of(2025, 1, 1)).size());
    Event event = c.getEventsOnDate(LocalDate.of(2025, 1, 1)).get(0);
    assertEquals(start.minusHours(3), event.getStartDateTime());
    assertEquals(end.minusHours(3), event.getEndDateTime());
  }

  /**
   * Tests that setting a calendar's timezone to something later than its current one works
   * properly.
   */
  @Test
  public void testSetTimeZoneForward() {
    assertEquals(1, c.getEventsOnDate(LocalDate.of(2025, 1, 1)).size());
    c.setTimeZone(ZoneId.of("America/Fortaleza"));
    assertEquals(1, c.getEventsOnDate(LocalDate.of(2025, 1, 1)).size());
    Event event = c.getEventsOnDate(LocalDate.of(2025, 1, 1)).get(0);
    assertEquals(start.plusHours(2), event.getStartDateTime());
    assertEquals(end.plusHours(2), event.getEndDateTime());
  }

  /**
   * Tests that setting a calendar's timezone to another timezone where it causes a single event
   * to span multiple days is allowed.
   */
  @Test
  public void testSetTimeZoneSingleSpan() {
    c.setTimeZone(ZoneId.of("Pacific/Honolulu"));
    assertEquals(1, c.getEventsOnDate(LocalDate.of(2025, 1, 1)).size());
    Event event = c.getEventsOnDate(LocalDate.of(2025, 1, 1)).get(0);
    assertEquals(start.minusHours(5), event.getStartDateTime());
    assertEquals(end.minusHours(5), event.getEndDateTime());
  }

  /**
   * Tests that changing a calendar's timezone to where it causes a series event to span multiple
   * days throws an error.
   */
  @Test
  public void testSetTimeZoneSeriesSpan() {
    try {
      c2.setTimeZone(ZoneId.of("Asia/Dili"));
      fail("Should have thrown an exception.");
    } catch (Exception e) {
      assertEquals("A series event cannot be edited to span multiple days.",
              e.getMessage());
    }
  }
}
