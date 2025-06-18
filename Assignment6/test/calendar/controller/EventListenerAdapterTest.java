package calendar.controller;

import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import calendar.controller.guiadapter.EventListenerAdapter;
import calendar.controller.guicontroller.IGUIController;
import calendar.controller.guicontroller.MockGUIController;
import calendar.view.IGUIView;
import calendar.view.MockGUIView;

import static org.junit.Assert.assertEquals;

/**
 * This JUnit test class tests the functionality of the EventListenerAdapter class, specifically
 * its ability to delegate button action commands with their corresponding controller methods.
 */
public class EventListenerAdapterTest {
  private StringBuilder controllerLog;
  private StringBuilder viewLog;
  private EventListenerAdapter listenerAdapter;
  private IGUIController controller;
  private IGUIView view;
  private ActionEvent addEventAction;
  private ActionEvent editEventAction;
  private ActionEvent createCalendarAction;
  private ActionEvent switchCalendarAction;
  private ActionEvent refreshScheduleAction;
  private ActionEvent prevMonthAction;
  private ActionEvent nextMonthAction;

  /**
   * Initializes the fields and classes needed for testing.
   */
  @Before
  public void setUp() {
    controllerLog = new StringBuilder();
    viewLog = new StringBuilder();
    this.controller = new MockGUIController(controllerLog);
    this.view = new MockGUIView(viewLog);
    this.listenerAdapter = new EventListenerAdapter(controller, view);
    this.addEventAction = new ActionEvent(
            view, ActionEvent.ACTION_PERFORMED, "addEventButton");
    this.editEventAction = new ActionEvent(
            view, ActionEvent.ACTION_PERFORMED, "editEventButton");
    this.createCalendarAction = new ActionEvent(
            view, ActionEvent.ACTION_PERFORMED, "createCalendarButton");
    this.switchCalendarAction = new ActionEvent(
            view, ActionEvent.ACTION_PERFORMED, "switchCalendarButton");
    this.refreshScheduleAction = new ActionEvent(
            view, ActionEvent.ACTION_PERFORMED, "refreshScheduleButton");
    this.prevMonthAction = new ActionEvent(
            view, ActionEvent.ACTION_PERFORMED, "prevMonthButton");
    this.nextMonthAction = new ActionEvent(
            view, ActionEvent.ACTION_PERFORMED, "nextMonthButton");
  }

  /**
   * The tests below test that when the actionPerformed method is invoked with an ActionEvent, the
   * proper controller method is called based on the ActionEvent's action command.
   */
  @Test
  public void testAddEventButton() {
    listenerAdapter.actionPerformed(addEventAction);
    assertEquals("Add Event\n", controllerLog.toString());
  }

  @Test
  public void testEditEventButton() {
    listenerAdapter.actionPerformed(editEventAction);
    assertEquals("Edit Event\n", controllerLog.toString());
  }

  @Test
  public void testCreateCalendarButton() {
    listenerAdapter.actionPerformed(createCalendarAction);
    assertEquals("Create Calendar\n", controllerLog.toString());
  }

  @Test
  public void testSwitchCalendarButton() {
    listenerAdapter.actionPerformed(switchCalendarAction);
    assertEquals("Switch Calendar\n", controllerLog.toString());
  }

  @Test
  public void testRefreshScheduleButton() {
    listenerAdapter.actionPerformed(refreshScheduleAction);
    assertEquals("Refresh Schedule\n", controllerLog.toString());
  }

  @Test
  public void testPrevMonthButton() {
    listenerAdapter.actionPerformed(prevMonthAction);
    assertEquals("Previous Month\n", controllerLog.toString());
  }

  @Test
  public void testNextMonthButton() {
    listenerAdapter.actionPerformed(nextMonthAction);
    assertEquals("Next Month\n", controllerLog.toString());
  }
}
