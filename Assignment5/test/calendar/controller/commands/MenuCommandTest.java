package calendar.controller.commands;

import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import calendar.controller.commands.newcalendarcommand.MenuCommand;
import calendar.model.calendarclass.MockModel;
import calendar.model.calendarmanagerclass.CalendarManagerModel;
import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.view.ICalendarView;
import calendar.view.TextBasedView;

import static org.junit.Assert.assertTrue;

/**
 * This class tests the MenuCommand functionality in a calendar application.
 * It checks if the command correctly displays the menu options to the user.
 */
public class MenuCommandTest {
  private ICalendarManager manager;
  private ICalendarView view;
  private StringWriter log;

  @Before
  public void setUp() {
    this.manager = new CalendarManagerModel();
    this.log = new StringWriter();
    this.view = new TextBasedView(log);
  }

  @Test
  public void testMenuDisplay() {
    MenuCommand cmd = new MenuCommand();
    cmd.execute(manager, view);
    assertTrue(log.toString().contains("create event"));
    assertTrue(log.toString().contains("edit event"));
    assertTrue(log.toString().contains("print events"));
    assertTrue(log.toString().contains("show status"));
    assertTrue(log.toString().contains("menu (Show this menu)"));
    assertTrue(log.toString().contains("q or quit (Exit the program)"));
  }

}