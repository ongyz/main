package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;
import seedu.address.logic.commands.GroupCommand;
import seedu.address.model.tuitiontiming.TuitionTimingContainsKeywordsPredicate;

public class GroupCommandParserTest {

    private GroupCommandParser parser = new GroupCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        // empty string
        assertParseFailure(parser, " ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, GroupCommand.MESSAGE_USAGE));

        // does not match day regex
        assertParseFailure(parser, "MONDAY",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, GroupCommand.MESSAGE_USAGE));

        // does not match time regex
        assertParseFailure(parser, "1200",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, GroupCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsGroupCommand() {
        // no leading and trailing whitespaces
        // valid day regex
        GroupCommand expectedGroupCommand =
                new GroupCommand(
                        new TuitionTimingContainsKeywordsPredicate("Monday"), true, false);
        assertParseSuccess(parser, "Monday", expectedGroupCommand);

        // valid time regex
        expectedGroupCommand =
                new GroupCommand(
                        new TuitionTimingContainsKeywordsPredicate("12:00pm"), true, false);
        assertParseSuccess(parser, "12:00pm", expectedGroupCommand);
    }
}
