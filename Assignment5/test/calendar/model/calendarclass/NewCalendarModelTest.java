package calendar.model.calendarclass;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import calendar.model.event.Event;

import static org.junit.Assert.assertEquals;

/**
 * This JUnit test class tests the functionality of the methods within the
 * NewCalendarModel class.
 */
public class NewCalendarModelTest {
  private NewCalendarModel c;

  @Before
  public void setUp() {
    this.c = new NewCalendarModel("test1", ZoneId.of("America/New_York"));
  }

  @Test
  public void testSetTimeZone() {
    LocalDateTime start = LocalDateTime.of(2025, 1, 1, 4, 30);
    LocalDateTime end = LocalDateTime.of(2025, 1, 1, 7, 30);
    c.createSingleEvent("meeting", start.toString(), end.toString());
    assertEquals(1, c.getEventsOnDate(LocalDate.of(2025, 1, 1))
            .size());
    c.setTimeZone(ZoneId.of("America/Los_Angeles"));
    assertEquals(1, c.getEventsOnDate(LocalDate.of(2025, 1, 1))
            .size());
    Event event = c.getEventsOnDate(LocalDate.of(2025, 1, 1)).get(0);
    assertEquals(start.minusHours(3), event.getStartDateTime());
    assertEquals(end.minusHours(3), event.getEndDateTime());
  }
}
