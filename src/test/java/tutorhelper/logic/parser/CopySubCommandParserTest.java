package tutorhelper.logic.parser;

import static tutorhelper.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutorhelper.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_SECOND_SUBJECT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_THIRD_STUDENT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_THIRD_SUBJECT;

import org.junit.Test;

import tutorhelper.logic.commands.CopySubCommand;
import tutorhelper.commons.core.Messages;

public class CopySubCommandParserTest {
    private CopySubCommandParser parser = new CopySubCommandParser();

    @Test
    public void parse_validArgs_returnsCopySubCommand() {
        assertParseSuccess(parser, "1 1 2",
                new CopySubCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, INDEX_SECOND_STUDENT));
        assertParseSuccess(parser, "2 3 1",
                new CopySubCommand(INDEX_SECOND_STUDENT, INDEX_THIRD_SUBJECT, INDEX_FIRST_STUDENT));
        assertParseSuccess(parser, "1 2 3",
                new CopySubCommand(INDEX_FIRST_STUDENT, INDEX_SECOND_SUBJECT, INDEX_THIRD_STUDENT));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, CopySubCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, CopySubCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1 1",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, CopySubCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "a 1",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, CopySubCommand.MESSAGE_USAGE));
    }
}
