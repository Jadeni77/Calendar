package calendar.model.calendarclass;

import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import calendar.model.enumclass.EventStatus;
import calendar.model.enumclass.Location;
import calendar.model.event.Event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This JUnit test class tests the functionality of the methods within the calendar application's
 * model, including logic for creation, editing, and retrieval of events.
 */
public class CalendarModelTest {
  private CalendarModel calendarModel;
  private CalendarModel cm2;
  private DateTimeFormatter dateFormatter;
  private DateTimeFormatter dateTimeFormatter;
  private LocalDateTime ldt1;
  private LocalDateTime ldt2;
  private LocalDateTime ldt3;
  private LocalDateTime ldt4;
  private LocalDateTime ldt5;
  private LocalDateTime ldt6;
  private LocalDateTime ldt7;
  private LocalDateTime ldt8;

  /**
   * Initializes variables used during testing, including formatters, LocalDateTime objects, and
   * models with different values at initialization.
   */
  @Before
  public void setUp() {
    this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    this.ldt1 = LocalDateTime.parse("2025-06-05T08:00", dateTimeFormatter);
    this.ldt2 = LocalDateTime.parse("2025-06-05T17:00", dateTimeFormatter);
    this.ldt3 = LocalDateTime.parse("2025-06-07T08:00", dateTimeFormatter);
    this.ldt4 = LocalDateTime.parse("2025-06-07T17:00", dateTimeFormatter);
    this.ldt5 = LocalDateTime.parse("2025-06-09T08:00", dateTimeFormatter);
    this.ldt6 = LocalDateTime.parse("2025-06-09T17:00", dateTimeFormatter);
    this.ldt7 = LocalDateTime.parse("2025-06-12T08:00", dateTimeFormatter);
    this.ldt8 = LocalDateTime.parse("2025-06-12T17:00", dateTimeFormatter);
    this.calendarModel = new CalendarModel();
    this.cm2 = new CalendarModel();
    cm2.createSingleEvent("Party", "2025-10-27T10:30", "2025-10-27T15:30");
    cm2.createRecurringAllDayEvent("Work", "2025-06-05", "MRS", 4);
    cm2.createSingleEvent("Celebration", "2025-06-09T17:30", "2025-06-10T01:30");
  }

  /**
   * Tests that the getter methods for the dateFormatter and dateTimeFormatter fields of the
   * model retrieve the formatter objects correctly, and evaluations using this method have the
   * same result as if an actual formatter with the same pattern was used.
   */
  @Test
  public void testFormatterGetters() {
    String dateString = "2020-01-01";
    assertEquals(LocalDate.parse(dateString, dateFormatter),
            LocalDate.parse(dateString, calendarModel.getDateFormatter()));
    String dateTimeString = "2020-01-01T08:00";
    assertEquals(LocalDateTime.parse(dateTimeString, dateTimeFormatter),
            LocalDateTime.parse(dateTimeString, calendarModel.getDateTimeFormatter()));
    LocalDate localDate = LocalDate.parse("2025-10-27", dateFormatter);
    LocalDateTime localDateTime = LocalDateTime.parse("2025-10-27T10:35", dateTimeFormatter);
    assertEquals("2025-10-27", localDate.format(calendarModel.getDateFormatter()));
    assertEquals("2025-10-27T10:35", localDateTime.format(calendarModel.getDateTimeFormatter()));
  }

  /**
   * Tests that the getEvent() method correctly retrieves the event with the specified subject,
   * start time and end time.
   */
  @Test
  public void testGetEvent() {
    Event e1 = cm2.getEvent("Party", LocalDateTime.parse("2025-10-27T10:30", dateTimeFormatter),
            LocalDateTime.parse("2025-10-27T15:30", dateTimeFormatter));
    assertEquals("Party", e1.getSubject());
    assertEquals(LocalDateTime.parse("2025-10-27T10:30", dateTimeFormatter),
            e1.getStartDateTime());
    assertEquals(LocalDateTime.parse("2025-10-27T15:30", dateTimeFormatter),
            e1.getEndDateTime());
  }

  /**
   * Tests that the getEventsOnDate() method correctly returns a list of all events that take
   * place on a certain day.
   */
  @Test
  public void testGetEventsOnDate() {
    List<Event> events = cm2.getEventsOnDate(LocalDate.parse("2025-10-27",
            dateFormatter));
    List<Event> events2 = cm2.getEventsOnDate(LocalDate.parse("2025-06-09",
            dateFormatter));
    assertEquals(1, events.size());
    Event e1 = cm2.getEvent("Party", LocalDateTime.parse("2025-10-27T10:30", dateTimeFormatter),
            LocalDateTime.parse("2025-10-27T15:30", dateTimeFormatter));
    assertEquals(e1, events.get(0));
    assertEquals(2, events2.size());
    Event e2 = cm2.getEvent("Work", ldt5, ldt6);
    Event e3 = cm2.getEvent("Celebration",
            LocalDateTime.parse("2025-06-09T17:30", dateTimeFormatter),
            LocalDateTime.parse("2025-06-10T01:30", dateTimeFormatter));
    assertEquals(e2, events2.get(0));
    assertEquals(e3, events2.get(1));
  }

  /**
   * Tests that the getEventsInRange() method correctly returns a list of all events that occur
   * between the given start and ending time.
   */
  @Test
  public void testGetEventsInRange() {
    List<Event> events = cm2.getEventsInRange(LocalDateTime.of(2025, 6, 5, 8, 0),
            LocalDateTime.of(2025, 6, 9, 11, 17));
    assertEquals(3, events.size());
    Event e1 = cm2.getEvent("Work", ldt1, ldt2);
    Event e2 = cm2.getEvent("Work", ldt3, ldt4);
    Event e3 = cm2.getEvent("Work", ldt5, ldt6);
    assertEquals(e1, events.get(0));
    assertEquals(e2, events.get(1));
    assertEquals(e3, events.get(2));
  }

  /**
   * Tests that the isBusy() method correctly determines if there is an event scheduled in the
   * model at the given date and time.
   */
  @Test
  public void testIsBusy() {
    assertTrue(cm2.isBusy(LocalDateTime.of(2025, 6, 5, 8, 0)));
    assertTrue(cm2.isBusy(LocalDateTime.of(2025, 6, 5, 9, 0)));
    assertFalse(cm2.isBusy(LocalDateTime.of(2025, 6, 4,
            11, 0)));
  }

  /**
   * Tests that the getEventsBySeriesId() method correctly returns a list of all the events in
   * a model with the given seriesId, if any.
   */
  @Test
  public void testGetEventsBySeriesId() {
    Event e1 = cm2.getEvent("Work", ldt1, ldt2);
    Event e2 = cm2.getEvent("Work", ldt3, ldt4);
    Event e3 = cm2.getEvent("Work", ldt5, ldt6);
    Event e4 = cm2.getEvent("Work", ldt7, ldt8);
    String seriesId = e1.getSeriesId();
    List<Event> events = cm2.getEventsBySeriesId(seriesId);
    assertEquals(4, events.size());
    assertTrue(events.contains(e1));
    assertTrue(events.contains(e2));
    assertTrue(events.contains(e3));
    assertTrue(events.contains(e4));
    List<Event> events2 = cm2.getEventsBySeriesId("hi");
    assertEquals(0, events2.size());
  }

  /**
   * Tests that the findEventsBySubjectAndStart() method correctly returns a list of all events
   * in the model with the given subject and starting dateTime.
   */
  @Test
  public void testFindEventsBySubjectAndStart() {
    List<Event> events = cm2.findEventsBySubjectAndStart("Work",
            LocalDateTime.parse("2025-06-05T08:00", dateTimeFormatter));
    assertEquals(1, events.size());
    Event e1 = cm2.getEvent("Work", ldt1, ldt2);
    assertEquals(e1, events.get(0));
  }

  /**
   * Tests that the createEvent() method correctly creates an Event object with the given
   * properties, and properly adds it to the model.
   */
  @Test
  public void testCreateEvent() {
    String subject = "Meeting";
    String start = "2023-10-01T10:00";
    String end = "2023-10-01T11:00";

    calendarModel.createSingleEvent(subject, start, end);

    LocalDateTime expectedStart = LocalDateTime.parse(start, dateTimeFormatter);
    LocalDateTime expectedEnd = LocalDateTime.parse(end, dateTimeFormatter);
    Event retrievedEvent = calendarModel.getEvent(subject, expectedStart, expectedEnd);
    assertEquals(subject, retrievedEvent.getSubject());
    assertEquals(expectedStart, retrievedEvent.getStartDateTime());
    assertEquals(expectedEnd, retrievedEvent.getEndDateTime());
  }

  /**
   * Tests that two events with the same subject, start, and end time cannot be created, and upon
   * the attempt to create a second, identical object, an exception is thrown.
   */
  @Test
  public void testCreateDuplicateEvent() {
    String subject = "Meeting";
    String start = "2023-10-01T10:00";
    String end = "2023-10-01T11:00";

    calendarModel.createSingleEvent(subject, start, end);
    try {
      calendarModel.createSingleEvent(subject, start, end);
    } catch (IllegalArgumentException e) {
      assertEquals("Event already exists with the same subject and time.",
              e.getMessage());
    }
  }

  /**
   * Tests that the createRecurringEvent() method properly creates and add recurring events that
   * occur on certain days of the week for a specified number of times.
   */
  @Test
  public void testCreateRecurringEvent() {
    CalendarModel model = new CalendarModel();

    String subject = "Baseball";
    String start = "2025-06-02T03:00";
    String end = "2025-06-02T05:30";
    String weekdays = "MTW";
    int repeats = 3;

    model.createRecurringEvent(subject, start, end, weekdays, repeats);

    List<Event> events = model.getEventsInRange(LocalDateTime.parse(start, dateTimeFormatter),
            LocalDateTime.parse(end, dateTimeFormatter).plusWeeks(1));
    assertFalse(events.isEmpty());

    for (int i = 0; i < repeats; i++) {
      LocalDateTime expectedStart = LocalDateTime.parse(start).plusDays(i);
      LocalDateTime expectedEnd = LocalDateTime.parse(end).plusDays(i);

      Event e = model.getEvent(subject, expectedStart, expectedEnd);
      assertEquals(subject, e.getSubject());
      assertEquals(expectedStart, e.getStartDateTime());
      assertEquals(expectedEnd, e.getEndDateTime());
      assertNotNull(e.getSeriesId());
    }

    DateTimeFormatter formatter = model.getDateTimeFormatter();
    LocalDateTime s = LocalDateTime.parse(start, formatter);
    LocalDateTime e = LocalDateTime.parse(end, formatter).plusWeeks(2);

    String seriesId = null;
    for (Event event : events) {
      if (event != null) {
        if (seriesId == null) {
          seriesId = event.getSeriesId();
        }
        assertEquals(event.getSeriesId(), seriesId);
      } else {
        fail("Event not found.");
      }
    }

  }

  /**
   * Tests that the second version of the createRecurringEvent() method properly creates and adds
   * Event objects that are part of a recurring series of events that occur on certain days of the
   * week until a specified date.
   */
  @Test
  public void testCreateRecurringEvent2() {
    CalendarModel model = new CalendarModel();

    String subject = "Baseball";
    String start = "2025-06-02T03:00";
    String end = "2025-06-02T05:30";
    String weekdays = "MTW";
    String until = "2025-06-13";

    model.createRecurringEvent(subject, start, end, weekdays, until);

    List<Event> events = model.getEventsInRange(LocalDateTime.parse(start, dateTimeFormatter),
            LocalDateTime.parse(end, dateTimeFormatter).plusWeeks(2));
    assertFalse(events.isEmpty());

    LocalDateTime currentStart = LocalDateTime.parse(start, dateTimeFormatter);
    LocalDateTime currentEnd = LocalDateTime.parse(end, dateTimeFormatter);
    String seriesId = null;
    while (!currentStart.isAfter(LocalDate.parse(until, dateFormatter).
            atTime(3, 0))) {
      if (currentStart.getDayOfWeek() == DayOfWeek.MONDAY
              || currentStart.getDayOfWeek() == DayOfWeek.TUESDAY
              || currentStart.getDayOfWeek() == DayOfWeek.WEDNESDAY) {
        Event e = model.getEvent(subject, currentStart, currentEnd);
        assertEquals(subject, e.getSubject());
        assertEquals(currentStart, e.getStartDateTime());
        assertEquals(currentEnd, e.getEndDateTime());
        if (seriesId == null) {
          seriesId = e.getSeriesId();
        } else {
          assertEquals(e.getSeriesId(), seriesId);
        }
      }
      currentStart = currentStart.plusDays(1);
      currentEnd = currentEnd.plusDays(1);
    }
  }

  /**
   * Tests that the createRecurringEvent() method does not allow recurring events to be created
   * when they span more than one day.
   */
  @Test
  public void testCreateRecurringEventExceptions() {
    CalendarModel model = new CalendarModel();

    String subject = "Baseball";
    String start = "2025-06-02T03:00";
    String end = "2025-06-03T05:30";
    String weekdays = "MTW";
    int repeats = 3;
    String until = "2025-06-13";

    try {
      model.createRecurringEvent(subject, start, end, weekdays, until);
    } catch (IllegalArgumentException e) {
      assertEquals("Recurring events must not span multiple days.", e.getMessage());
    }
    try {
      model.createRecurringEvent(subject, start, end, weekdays, repeats);
    } catch (IllegalArgumentException e) {
      assertEquals("Recurring events must not span multiple days.", e.getMessage());
    }
  }

  /**
   * Tests that the createAllDayEvent() method properly creates and adds an all-day event to
   * the model.
   */
  @Test
  public void testCreateAllDayEvent() {
    String subject = "Meeting";
    String on = "2023-10-01";

    calendarModel.createAllDayEvent(subject, on);

    LocalDate expectedStart = LocalDate.parse(on, dateFormatter);
    LocalDateTime start = expectedStart.atTime(8, 0);
    LocalDateTime end = expectedStart.atTime(17, 0);
    Event retrievedEvent = calendarModel.getEvent(subject, start, end);
    assertEquals(subject, retrievedEvent.getSubject());
    assertEquals(start, retrievedEvent.getStartDateTime());
    assertEquals(end, retrievedEvent.getEndDateTime());
  }

  /**
   * Tests that the createRecurringAllDayEvent() method properly creates and adds a series of
   * recurring all-day events that occur on certain days of the week, for a specified number of
   * times before they stop occurring.
   */
  @Test
  public void testCreateAllDayRecurringEvent() {
    CalendarModel model = new CalendarModel();

    String subject = "Baseball";
    String on = "2025-06-02";
    String weekdays = "MTW";
    int repeats = 4;

    model.createRecurringAllDayEvent(subject, on, weekdays, repeats);

    LocalDate currentDate = LocalDate.parse(on, dateFormatter);
    LocalDateTime expectedStart = currentDate.atTime(8, 0);
    List<Event> events = model.getEventsInRange(expectedStart, expectedStart.plusWeeks(2));
    assertFalse(events.isEmpty());
    String seriesId = null;
    while (repeats > 0) {
      if (currentDate.getDayOfWeek() == DayOfWeek.MONDAY
              || currentDate.getDayOfWeek() == DayOfWeek.TUESDAY
              || currentDate.getDayOfWeek() == DayOfWeek.WEDNESDAY) {
        Event e = model.findEventsBySubjectAndStart(subject, currentDate.atTime(8,
                0)).get(0);
        assertEquals(subject, e.getSubject());
        assertEquals(currentDate.atTime(8, 0), e.getStartDateTime());
        assertEquals(currentDate.atTime(17, 0), e.getEndDateTime());
        if (seriesId == null) {
          seriesId = e.getSeriesId();
        } else {
          assertEquals(e.getSeriesId(), seriesId);
        }
        repeats--;
      }
      currentDate = currentDate.plusDays(1);
    }
  }

  /**
   * Tests that the createRecurringEventAllDay() method properly creates and adds a series of
   * recurring all-day events that occur on certain days of the week until the specified date.
   */
  @Test
  public void testCreateAllDayRecurringEvent2() {
    CalendarModel model = new CalendarModel();

    String subject = "Baseball";
    String on = "2025-06-02";
    String weekdays = "MTW";
    String until = "2025-06-13";

    model.createRecurringAllDayEvent(subject, on, weekdays, until);
    LocalDate currentDate = LocalDate.parse(on, dateFormatter);
    LocalDateTime expectedStart = currentDate.atTime(8, 0);
    List<Event> events = model.getEventsInRange(expectedStart, expectedStart.plusWeeks(2));
    assertFalse(events.isEmpty());
    String seriesId = null;
    while (currentDate.isBefore(LocalDate.parse(until, dateFormatter))) {
      if (currentDate.getDayOfWeek() == DayOfWeek.MONDAY
              || currentDate.getDayOfWeek() == DayOfWeek.TUESDAY
              || currentDate.getDayOfWeek() == DayOfWeek.WEDNESDAY) {
        Event e = model.findEventsBySubjectAndStart(subject, currentDate.atTime(8, 0))
                .get(0);
        assertEquals(subject, e.getSubject());
        assertEquals(currentDate.atTime(8, 0), e.getStartDateTime());
        assertEquals(currentDate.atTime(17, 0), e.getEndDateTime());
        if (seriesId == null) {
          seriesId = e.getSeriesId();
        } else {
          assertEquals(e.getSeriesId(), seriesId);
        }
      }
      currentDate = currentDate.plusDays(1);
    }
  }

  /**
   * Tests that the editSingleEvent() method is able to properly edit the possible fields of a
   * given Event within the model.
   */
  @Test
  public void testEditSingleEvent() {
    LocalDateTime partyStart = LocalDateTime.parse("2025-10-27T10:30", dateTimeFormatter);
    LocalDateTime partyEnd = LocalDateTime.parse("2025-10-27T15:30", dateTimeFormatter);
    Event e1 = cm2.getEvent("Party", partyStart, partyEnd);
    assertEquals("Party", e1.getSubject());
    cm2.editSingleEvent("subject", "Party",
            "2025-10-27T10:30", "2025-10-27T15:30", "woohoo");
    Event e2 = cm2.getEvent("woohoo", partyStart, partyEnd);
    assertEquals("woohoo", e2.getSubject());
    assertFalse(cm2.getEventsInRange(LocalDateTime.parse("2023-10-27T10:30",
                    dateTimeFormatter),
            LocalDateTime.parse("2027-10-27T10:30", dateTimeFormatter)).contains(e1));
    cm2.editSingleEvent("description", "woohoo",
            partyStart.format(dateTimeFormatter),
            partyEnd.format(dateTimeFormatter), "12345");
    assertEquals("12345", cm2.getEvent("woohoo",
            partyStart, partyEnd).getDescription());
    cm2.editSingleEvent("location", "woohoo",
            partyStart.format(dateTimeFormatter),
            partyEnd.format(dateTimeFormatter), "physical");
    assertEquals(Location.PHYSICAL, cm2.getEvent("woohoo",
            partyStart, partyEnd).getLocation());
    assertEquals(EventStatus.PUBLIC, cm2.getEvent("woohoo",
            partyStart, partyEnd).getStatus());
    cm2.editSingleEvent("status", "woohoo",
            partyStart.format(dateTimeFormatter),
            partyEnd.format(dateTimeFormatter), "PRIVATE");
    assertEquals(EventStatus.PRIVATE, cm2.getEvent("woohoo",
            partyStart, partyEnd).getStatus());
    cm2.editSingleEvent("start", "woohoo",
            partyStart.format(dateTimeFormatter),
            partyEnd.format(dateTimeFormatter), "2025-10-27T09:30");
    LocalDateTime newStart = LocalDateTime.parse("2025-10-27T09:30", dateTimeFormatter);
    assertEquals(newStart, cm2.getEvent("woohoo", newStart, partyEnd).getStartDateTime());
    cm2.editSingleEvent("end", "woohoo", "2025-10-27T09:30",
            partyEnd.format(dateTimeFormatter), "2025-10-27T15:30");
    LocalDateTime newEnd = LocalDateTime.parse("2025-10-27T15:30", dateTimeFormatter);
    assertEquals(newEnd, cm2.getEvent("woohoo", newStart, newEnd).getEndDateTime());
    assertFalse(cm2.getEventsInRange(LocalDateTime.parse("2023-10-27T10:30",
                            dateTimeFormatter),
                    LocalDateTime.parse("2027-10-27T10:30", dateTimeFormatter))
            .contains(cm2.getEvent("woohoo", partyStart, partyEnd)));

    try {
      cm2.editSingleEvent("subject", "Party",
              "2025-10-27T10:30", "2025-10-27T5:30", "woohoo");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid date format. Please use 'yyyy-MM-dd'T'HH:mm'.",
              e.getMessage());
    }
    try {
      cm2.editSingleEvent("subject", "ehhh",
              "2025-10-27T10:30", "2025-10-27T19:30", "woohoo");
    } catch (IllegalArgumentException e) {
      assertEquals("No event found with subject 'ehhh' starting at 2025-10-27 " +
              "and ending at 2025-10-27.", e.getMessage());
    }
  }

  /**
   * Tests that the editMultipleEvents() edits a single event that is not a part of a series, or
   * the event and any events that occur after it in the series that the event is in, if so. This
   * is when the editSeries field is set to false, meaning that the method is not intending to edit
   * an entire series.
   */
  @Test
  public void testEditMultipleEvents() {
    // acts as single method if not part of series
    LocalDateTime partyStart = LocalDateTime.parse("2025-10-27T10:30", dateTimeFormatter);
    LocalDateTime partyEnd = LocalDateTime.parse("2025-10-27T15:30", dateTimeFormatter);
    Event e1 = cm2.getEvent("Party", partyStart, partyEnd);
    assertEquals("Party", e1.getSubject());
    cm2.editMultipleEvents("subject", "Party", "2025-10-27T10:30",
            "woohoo", false);
    Event e2 = cm2.getEvent("woohoo", partyStart, partyEnd);
    assertEquals("woohoo", e2.getSubject());
    assertFalse(cm2.getEventsInRange(LocalDateTime.parse("2023-10-27T10:30",
                    dateTimeFormatter),
            LocalDateTime.parse("2027-10-27T10:30", dateTimeFormatter)).contains(e1));

    assertEquals("Work", cm2.getEvent("Work", ldt1, ldt2).getSubject());
    assertEquals("Work", cm2.getEvent("Work", ldt3, ldt4).getSubject());
    assertEquals("Work", cm2.getEvent("Work", ldt5, ldt6).getSubject());
    assertEquals("Work", cm2.getEvent("Work", ldt7, ldt8).getSubject());
    // changes current and future events if part of series
    cm2.editMultipleEvents("subject", "Work", "2025-06-09T08:00",
            "123", false);
    assertEquals("Work", cm2.getEvent("Work", ldt1, ldt2).getSubject());
    assertEquals("Work", cm2.getEvent("Work", ldt3, ldt4).getSubject());
    assertEquals("123", cm2.getEvent("123", ldt5, ldt6).getSubject());
    assertEquals("123", cm2.getEvent("123", ldt7, ldt8).getSubject());
    assertNull(cm2.getEvent("Work", ldt5, ldt6));
    assertNull(cm2.getEvent("Work", ldt7, ldt8));
    cm2.editMultipleEvents("end", "123", "2025-06-12T08:00",
            "2025-06-13T10:00", false);
    LocalDateTime newTime = LocalDateTime.parse("2025-06-13T10:00", dateTimeFormatter);
    assertEquals("Work", cm2.getEvent("Work", ldt1, ldt2).getSubject());
    assertEquals("Work", cm2.getEvent("Work", ldt3, ldt4).getSubject());
    assertEquals("123", cm2.getEvent("123", ldt5, ldt6).getSubject());
    assertEquals("123", cm2.getEvent("123", ldt7, newTime).getSubject());
  }

  /**
   * Tests that the editMultipleEvents() edits a single event that is not a part of a series, or
   * any events in the same series if so. This is when the editSeries field is set to false,
   * meaning that the method IS intending to edit an entire series.
   */
  @Test
  public void testEditMultipleEventsSeries() {
    // acts as single method if not part of series
    LocalDateTime partyStart = LocalDateTime.parse("2025-10-27T10:30", dateTimeFormatter);
    LocalDateTime partyEnd = LocalDateTime.parse("2025-10-27T15:30", dateTimeFormatter);
    Event e1 = cm2.getEvent("Party", partyStart, partyEnd);
    assertEquals("Party", e1.getSubject());
    cm2.editMultipleEvents("subject", "Party", "2025-10-27T10:30",
            "woohoo", true);
    Event e2 = cm2.getEvent("woohoo", partyStart, partyEnd);
    assertEquals("woohoo", e2.getSubject());
    assertFalse(cm2.getEventsInRange(LocalDateTime.parse("2023-10-27T10:30",
                    dateTimeFormatter),
            LocalDateTime.parse("2027-10-27T10:30", dateTimeFormatter)).contains(e1));

    assertEquals("Work", cm2.getEvent("Work", ldt1, ldt2).getSubject());
    assertEquals("Work", cm2.getEvent("Work", ldt3, ldt4).getSubject());
    assertEquals("Work", cm2.getEvent("Work", ldt5, ldt6).getSubject());
    assertEquals("Work", cm2.getEvent("Work", ldt7, ldt8).getSubject());
    // changes current and future events if part of series
    cm2.editMultipleEvents("subject", "Work", "2025-06-09T08:00",
            "123", true);
    assertEquals("123", cm2.getEvent("123", ldt1, ldt2).getSubject());
    assertEquals("123", cm2.getEvent("123", ldt3, ldt4).getSubject());
    assertEquals("123", cm2.getEvent("123", ldt5, ldt6).getSubject());
    assertEquals("123", cm2.getEvent("123", ldt7, ldt8).getSubject());
    assertNull(cm2.getEvent("Work", ldt1, ldt2));
    assertNull(cm2.getEvent("Work", ldt3, ldt4));
    assertNull(cm2.getEvent("Work", ldt5, ldt6));
    assertNull(cm2.getEvent("Work", ldt7, ldt8));
    cm2.editMultipleEvents("end", "123", "2025-06-09T08:00",
            "2025-06-13T10:00", true);
    LocalDateTime newTime = LocalDateTime.parse("2025-06-13T10:00", dateTimeFormatter);
    assertEquals("123", cm2.getEvent("123", ldt1, newTime).getSubject());
    assertEquals("123", cm2.getEvent("123", ldt3, newTime).getSubject());
    assertEquals("123", cm2.getEvent("123", ldt5, newTime).getSubject());
    assertEquals("123", cm2.getEvent("123", ldt7, newTime).getSubject());

  }

  /**
   * Tests various exceptions thrown by the editMultipleEvents() method related to correcting
   * user input.
   */
  @Test
  public void testEditMultipleEventsExceptions() {
    try {
      cm2.editMultipleEvents("subject", "Party",
              "2025-10-27T10:30", "new", false);
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid date format. Please use 'yyyy-MM-dd'T'HH:mm'.",
              e.getMessage());
    }
    try {
      cm2.editMultipleEvents("subject", "123", // Non-existent subject
              "2025-10-27T10:30", "new", false);
      fail("Expected IllegalArgumentException for no events found, but none was thrown.");
    } catch (IllegalArgumentException e) {
      assertEquals("No events found with subject '123' starting at 2025-10-27T10:30.",
              e.getMessage());
    }

    cm2.createSingleEvent("Fun", "2025-10-27T10:30",
            "2025-10-27T16:30");
    cm2.createSingleEvent("Fun", "2025-10-27T10:30",
            "2025-10-27T17:30");
    try {
      cm2.editMultipleEvents("subject", "Fun",
              "2025-10-27T10:30", "new", true);
    } catch (IllegalArgumentException e) {
      assertEquals("Multiple events found with subject 'Fun' and start time " +
              "at 2025-10-27T10:30." +
              " More than one event cannot be edited at once.", e.getMessage());
    }
  }

  /**
   * Tests that the methods related to editing events throw general exceptions about ensuring
   * correct user input.
   */
  @Test
  public void testOtherEditExceptions() {
    try {
      cm2.editSingleEvent("wrong", "Party", "2025-10-27T10:30",
              "2025-10-27T15:30", "new");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid property 'wrong'. Valid properties are: subject, start," +
              " end, description, location, status.", e.getMessage());
    }
    try {
      cm2.editMultipleEvents("start", "Party",
              "2025-10-27T10:30", "string", false);
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid value for property 'start': string. " +
              "Please ensure the value is correct.", e.getMessage());
    }
  }
}