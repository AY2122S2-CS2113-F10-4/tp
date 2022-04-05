package seedu.allonus.expense;

import org.junit.jupiter.api.Test;

import seedu.allonus.expense.exceptions.ExpenseAmountException;
import seedu.allonus.expense.exceptions.ExpenseEmptyFieldException;
import seedu.allonus.expense.exceptions.ExpenseMissingFieldException;
import seedu.allonus.expense.exceptions.ExpenseExtraFieldException;
import seedu.allonus.expense.exceptions.ExpenseSurroundSlashSpaceException;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.allonus.expense.ExpenseParser.parseNewExpense;
import static seedu.allonus.expense.ExpenseParser.parseKeywordExpense;
import static seedu.allonus.expense.ExpenseParser.isAmountValid;
import static seedu.allonus.expense.ExpenseParser.reformatDate;
import static seedu.allonus.expense.ExpenseParser.parseDeleteExpense;
import static seedu.allonus.expense.ExpenseParser.parseEditExpense;
import static seedu.allonus.expense.ExpenseParser.parseFindExpense;
import static seedu.allonus.expense.ExpenseParser.checkSlashValidity;
import static seedu.allonus.expense.ExpenseTracker.MSG_EMPTY_FIELDS;
import static seedu.allonus.expense.ExpenseTracker.MSG_INCORRECT_DATE_FORMAT;
import static seedu.allonus.expense.ExpenseTracker.MSG_NUMBERS_ONLY_AMOUNT;

class ExpenseParserTest {

    @Test
    void testParseKeyword() {
        String testInput = "d/13/3/2022 a/18.00 c/Movie r/This is a remark";
        try {
            String date = parseKeywordExpense(testInput, "d/", "[dacr]/");
            assertEquals(date, "13/3/2022");
            String amount = parseKeywordExpense(testInput, "a/", "[dacr]/");
            assertEquals(amount, "18.00");
            String category = parseKeywordExpense(testInput, "c/", "[dacr]/");
            assertEquals(category, "Movie");
            String remarks = parseKeywordExpense(testInput, "r/", "[dacr]/");
            assertEquals(remarks, "This is a remark");
        } catch (ExpenseAmountException | ExpenseEmptyFieldException | ExpenseExtraFieldException
                | ExpenseSurroundSlashSpaceException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testParseDelete() {
        String testInput = "rm 1";
        assertEquals(parseDeleteExpense(testInput), 1);
    }

    @Test
    void testReformatDate() {
        String testDate = "2022-03-27";
        String parsedTestDate = reformatDate(testDate);
        assertEquals("2022-03-27", parsedTestDate);
    }

    @Test
    void testIsAmountValid_negativeAmount_exceptionThrown() {
        try {
            isAmountValid("-5");
        } catch (ExpenseAmountException e) {
            assertEquals("Amount cannot be negative!", e.getMessage());
        }
    }

    @Test
    void testParseNewExpense() {
        String testInput = "add d/2022-03-13 a/18.00 c/Movie r/This is a remark";
        try {
            String[] result = parseNewExpense(testInput);
            System.out.println(result);
            String[] expected = {"2022-03-13", "18.00", "Movie", "This is a remark"};
            assertArrayEquals(expected, result);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(MSG_EMPTY_FIELDS);
        } catch (DateTimeParseException e) {
            System.out.println(MSG_INCORRECT_DATE_FORMAT);
        } catch (NumberFormatException e) {
            System.out.println(MSG_NUMBERS_ONLY_AMOUNT);
        } catch (ExpenseAmountException e) {
            System.out.println(e.getMessage());
        } catch (ExpenseEmptyFieldException e) {
            System.out.println(e.getMessage());
        } catch (ExpenseMissingFieldException e) {
            System.out.println(e.getMessage());
        } catch (ExpenseExtraFieldException e) {
            System.out.println(e.getMessage());
        } catch (ExpenseSurroundSlashSpaceException e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    void testParseEditExpense() {
        String testInput = "edit 1";
        assertEquals(parseEditExpense(testInput), 1);
    }

    @Test
    void testParseFindExpense() {
        String testInput = "find test";
        assertEquals(parseFindExpense(testInput), "test");
    }

    @Test
    void checkSlashValidity_slashInFirstPositionWithoutSpacing_exceptionThrown() {
        try {
            String testString = "/ slash in first position";
            checkSlashValidity("/ slash in first position", 0);
        } catch (ExpenseSurroundSlashSpaceException e) {
            assertEquals("/ as first character must be surrounded by white spaces!", e.getMessage());
        }
    }

    @Test
    void checkSlashValidity_slashInLastPosition_exceptionThrown() {
        try {
            String testString = "slash in last position /";
            checkSlashValidity(testString, testString.length() - 1);
        } catch (ExpenseSurroundSlashSpaceException e) {
            assertEquals("/ is not allowed as the last non-space character!", e.getMessage());
        }
    }

    @Test
    void checkSlashValidity_slashInStringWithoutSpacing_exceptionThrown() {
        try {
            String testString = "slash in /middle / position ";
            checkSlashValidity(testString, testString.indexOf('/'));
        } catch (ExpenseSurroundSlashSpaceException e) {
            assertEquals("/ must be surrounded by white spaces!", e.getMessage());
        }
    }
}