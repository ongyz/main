package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_SUBJECT;

import org.junit.Test;

import seedu.address.logic.commands.CopySubCommand;

public class CopySubCommandParserTest {
    private CopySubCommandParser parser = new CopySubCommandParser();

    @Test
    public void parse_validArgs_returnsCopySubCommand() {
        assertParseSuccess(parser, "1 1 2",
                new CopySubCommand(INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_SECOND_PERSON));
        assertParseSuccess(parser, "2 3 1",
                new CopySubCommand(INDEX_SECOND_PERSON, INDEX_THIRD_SUBJECT, INDEX_FIRST_PERSON));
        assertParseSuccess(parser, "1 2 3",
                new CopySubCommand(INDEX_FIRST_PERSON, INDEX_SECOND_SUBJECT, INDEX_THIRD_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopySubCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopySubCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopySubCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "a 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopySubCommand.MESSAGE_USAGE));
    }
}
