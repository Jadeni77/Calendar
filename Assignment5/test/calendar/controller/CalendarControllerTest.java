package calendar.controller;


import calendar.model.calendarclass.ICalendar;
import calendar.model.calendarclass.MockModel;
import calendar.view.ICalendarView;

/**
 * This JUnit test class tests that the controller correctly handles incorrect user input and
 * sends the correct information to the view and model based on what operation the user entered.
 */
public class CalendarControllerTest extends AbstractControllerTest {

  /**
   * Creates a new CalendarController object using the given fields.
   *
   * @param view the view to be used in the CalendarController
   * @param in   the input to be read from in the CalendarController
   * @return a new CalendarController object
   */
  @Override
  public ICalendarController createController(ICalendarView view, Readable in) {
    return new CalendarController(this.model, view, in);
  }

  /**
   * Creates a new MockModel object.
   *
   * @return a new MockModel object
   */
  @Override
  public ICalendar createCalendar(StringBuilder modelLog) {
    return new MockModel(modelLog);
  }
}
