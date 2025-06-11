package calendar.controller;

import org.junit.Before;
import org.junit.Test;

import java.time.ZoneId;

import calendar.model.calendarclass.ICalendar;
import calendar.model.calendarclass.MockModel;
import calendar.model.calendarclass.NewCalendarMockModel;
import calendar.model.calendarmanagerclass.CalendarManagerMockModel;
import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.view.ICalendarView;
import calendar.view.TextBasedView;

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
   * @param view the view to be used in the CalendarManagerController
   * @param in the input to be read from in the CalendarManagerController
   * @return a new CalendarManagerController object
   */
  @Override
  public ICalendarController createController(ICalendarView view, Readable in) {
    return new CalendarManagerController(calendarManager, view, in);
  }

  /**
   * Creates a new NewCalendarMockModel object
   * @return a new NewCalendarMockModel object
   */
  @Override
  public ICalendar createCalendar(StringBuilder modelLog) {
    return new NewCalendarMockModel("test1", ZoneId.of("America/New_York"), modelLog);
  }

  /**
   * TODO add tests for things unique to ManagerController :)
   * Tests ...
   */
  @Test
  public void test() {
    // code
  }
}
