package calendar.model.event;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This JUnit test class tests the functionality of the methods
 * within the RecurringEventRule class.
 */
public class RecurringEventRuleTest {
  RecurringEventRule rule;

  /**
   * Initializes the variables needed for testing.
   */
  @Before
  public void setUp() {
    rule = new RecurringEventRule("MTR", 4, null,
            LocalTime.of(9,0), LocalTime.of(11,0), false);
  }

  /**
   * Tests that the generateOccurrenceDate() method produces a list of events that correctly
   * reflect when an event following the RecurringEventRule would occur.
   */
  @Test
  public void testGenerateOccurrenceDate() {
    List<LocalDate> dates = rule.generateOccurrenceDate(LocalDate.of(2025, 6, 2));
    List<LocalDate> expectedDates = Arrays.asList(LocalDate.of(2025, 6, 2),
            LocalDate.of(2025, 6, 3), LocalDate.of(2025, 6, 5),
            LocalDate.of(2025, 6, 9));
    assertEquals(expectedDates, dates);
  }
}
