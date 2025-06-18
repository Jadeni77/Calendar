package calendar.controller;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import calendar.controller.guicontroller.GUIController;
import calendar.model.calendarmanagerclass.CalendarManagerMockModel;
import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.model.event.Event;
import calendar.view.IGUIView;
import calendar.view.MockGUIView;

import static org.junit.Assert.assertEquals;

/**
 * This JUnit test class tests the functionality of the GUIController class, specifically its
 * ability to delegate commands to the model and view after an action in the view triggered the
 * controller.
 */
public class GUIControllerTest {
  private final StringBuilder viewLog = new StringBuilder();
  private StringBuilder managerLog = new StringBuilder();
  private GUIController controller;
  private String currentDate;

  /**
   * Initializes any classes and variables needed for testing.
   */
  @Before
  public void setUp() {
    IGUIView view = new MockGUIView(viewLog, "testEvent", "2025-06-15T09:30",
            "2025-06-15T11:30");
    ICalendarManager manager = new CalendarManagerMockModel(managerLog);
    this.controller = new GUIController(manager, view);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    this.currentDate = LocalDate.now().format(formatter);
  }

  /**
   * Tests that the controller's start() method properly sets up a new environment within the model
   * that is being displayed on the view.
   */
  @Test
  public void testStart() {
    controller.start();
    assertEquals("Calendar added: Default\n" +
            "Start date set to: \n" +
            currentDate + "\n" +
            "Start date queried\n" +
            "Printing events\n" +
            "Message displayed: Calendar application started.\n", this.viewLog.toString());
    assertEquals("Calendar created: Default, America/New_York\n" +
            "Current calendar changed: Default\n" +
            "Current calendar queried", this.managerLog.toString());
  }

  /**
   * Tests that the eventsUpdated() method properly handles refreshing the GUI after an event has
   * been updated.
   */
  @Test
  public void testEventsUpdated() {
    List<Event> eventList = new ArrayList<>();
    controller.eventsUpdated(eventList);
    assertEquals("Start date set to: \n" +
            currentDate + "\n" +
            "Start date queried\n" +
            "Printing events\n", this.viewLog.toString());
    assertEquals("Calendar created: Default, America/New_York\n" +
            "Current calendar changed: Default\n" +
            "Current calendar queried", this.managerLog.toString());
  }

  /**
   * Tests that the handleAddEvent() method properly manages the view and model when the user has
   * attempted to add an event through the GUI.
   */
  @Test
  public void testHandleAddEvent() {
    controller.handleAddEvent("Meeting", "2025-06-15T10:30", "2025-06-15T11:30",
            "description", "PHYSICAL", "PUBLIC");
    assertEquals("Message displayed: Added event: Meeting !\n", this.viewLog.toString());
    assertEquals("Calendar created: Default, America/New_York\n" +
            "Current calendar changed: Default\n" +
            "Current calendar queried", this.managerLog.toString());
  }

  /**
   * Tests that the handleEditEvent() method properly manages the view and model when the user has
   * attempted to edit an event through the GUI.
   */
  @Test
  public void testHandleEditEvent() {
    controller.handleEditEvent("subject", "hi");
    assertEquals("Selected event subject queried\n" +
            "Selected event start date/time queried\n" +
            "Selected event end date/time queried\n" +
            "Message displayed: Updated event: testEvent\n", this.viewLog.toString());
    assertEquals("Calendar created: Default, America/New_York\n" +
            "Current calendar changed: Default\n" +
            "Current calendar queried", this.managerLog.toString());
  }

  /**
   * Tests that the handleCreateCalendar() method properly manages the view and model when the
   * user has attempted to create a new calendar through the GUI.
   */
  @Test
  public void testHandleCreateCalendar() {
    controller.handleCreateCalendar("test3", "America/New_York");
    assertEquals("Calendar added: test3\n"
            + "Message displayed: Created calendar: test3\n", this.viewLog.toString());
    assertEquals("Calendar created: Default, America/New_York\n"
            + "Current calendar changed: Default\n"
            + "Current calendar queried"
            + "Calendar created: test3, America/New_York\n", this.managerLog.toString());
  }

  /**
   * Tests that the handleSwitchCalendar() method properly handles the control of the view when
   * the user tries to switch to a different calendar.
   */
  @Test
  public void testHandleSwitchCalendar() {
    controller.handleSwitchCalendar();
    assertEquals("Selected event calendar queried\n"
            + "Start date set to: \n"
            + currentDate + "\n"
            + "Start date queried\n"
            + "Printing events\n"
            + "Message displayed: Switched to calendar: selected calendar\n",
            this.viewLog.toString());
    assertEquals("Calendar created: Default, America/New_York\n"
            + "Current calendar changed: Default\n"
            + "Current calendar queriedCurrent calendar changed: selected calendar\n"
            + "Current calendar queried", this.managerLog.toString());
  }

  /**
   * Tests that the handleRefreshSchedule() method properly refreshes the schedule view after it
   * has been prompted to, either by an event being updated or the user attempting to refresh
   * themselves through the GUI.
   */
  @Test
  public void testHandleRefreshSchedule() {
    controller.handleRefreshSchedule();
    assertEquals("Start date queried\n" +
            "Printing events\n", this.viewLog.toString());
    assertEquals("Calendar created: Default, America/New_York\n" +
            "Current calendar changed: Default\n" +
            "Current calendar queried", this.managerLog.toString());
  }

  /**
   * Tests that the handlePrevMonth() method properly moves the displayed month in calendar form
   * back by one month within the application's "Month View".
   */
  @Test
  public void testHandlePrevMonth() {
    controller.handlePrevMonth();
    assertEquals("Months changed by: \n" +
            "-1\n", this.viewLog.toString());
    assertEquals("Calendar created: Default, America/New_York\n" +
            "Current calendar changed: Default\n" +
            "Current calendar queried", this.managerLog.toString());
  }

  /**
   * Tests that the handlePrevMonth() method properly moves the displayed month in calendar form
   * forward by one month within the application's "Month View".
   */
  @Test
  public void testHandleNextMonth() {
    controller.handleNextMonth();
    assertEquals("Months changed by: \n" +
            "1\n", this.viewLog.toString());
    assertEquals("Calendar created: Default, America/New_York\n" +
            "Current calendar changed: Default\n" +
            "Current calendar queried", this.managerLog.toString());
  }
}
