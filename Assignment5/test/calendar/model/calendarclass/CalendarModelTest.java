package calendar.model.calendarclass;

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