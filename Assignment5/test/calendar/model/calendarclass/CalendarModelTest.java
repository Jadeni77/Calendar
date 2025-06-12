package calendar.model.calendarclass;

import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

// TODO abstract tests

/**
 * This JUnit test class tests the functionality of the methods within the calendar application's
 * model, including logic for creation, editing, and retrieval of events. This is done through
 * the tests written in AbstractCalendarModelTest.
 */
public class CalendarModelTest extends AbstractCalendarModelTest {

  /**
   * Constructs a new CalendarModel object.
   *
   * @return an CalendarModel object
   */
  @Override
  public ICalendar createCalendarModel() {
    return new CalendarModel();
  }

}