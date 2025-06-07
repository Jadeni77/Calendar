package calendar.model.enumclass;

import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This JUnit test class tests the functionality of the methods within the
 * DayOfWeekAbbreviation enum.
 */
public class DayOfWeekAbbreviationTest {
  private DayOfWeekAbbreviation abbr1;
  private DayOfWeekAbbreviation abbr2;
  private DayOfWeekAbbreviation abbr3;

  /**
   * Initializes data used to test methods.
   */
  @Before
  public void setUp() {
    this.abbr1 = DayOfWeekAbbreviation.M;
    this.abbr2 = DayOfWeekAbbreviation.W;
    this.abbr3 = DayOfWeekAbbreviation.S;
  }

  /**
   * Tests that the getDayOfWeek() method returns the correct DayOfWeek enum to correspond
   * with each DayOfWeekAbbreviation.
   */
  @Test
  public void testGetDayOfWeek() {
    assertEquals(DayOfWeek.MONDAY, abbr1.getDayOfWeek());
    assertEquals(DayOfWeek.WEDNESDAY, abbr2.getDayOfWeek());
    assertEquals(DayOfWeek.SATURDAY, abbr3.getDayOfWeek());
    assertEquals(DayOfWeek.TUESDAY, DayOfWeekAbbreviation.T.getDayOfWeek());
    assertEquals(DayOfWeek.THURSDAY, DayOfWeekAbbreviation.R.getDayOfWeek());
    assertEquals(DayOfWeek.FRIDAY, DayOfWeekAbbreviation.F.getDayOfWeek());
    assertEquals(DayOfWeek.SUNDAY, DayOfWeekAbbreviation.U.getDayOfWeek());
  }

  /**
   * Tests that the charToAbbr() method can properly convert characters that represent the
   * days of the week into DayOfWeekAbbreviations, and throws an error if a character not
   * representative of a day is inputted.
   */
  @Test
  public void testCharToAbbr() {
    assertEquals(DayOfWeekAbbreviation.M, DayOfWeekAbbreviation.charToAbbr('M'));
    assertEquals(DayOfWeekAbbreviation.R, DayOfWeekAbbreviation.charToAbbr('R'));
    assertEquals(DayOfWeekAbbreviation.F, DayOfWeekAbbreviation.charToAbbr('F'));
    assertEquals(DayOfWeekAbbreviation.U, DayOfWeekAbbreviation.charToAbbr('U'));
    try {
      DayOfWeekAbbreviation.charToAbbr('Q');
      fail("This test should not reach this line.");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid day abbreviation!", e.getMessage());
    }
  }
}
