package tutorhelper.logic.parser;

import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorhelper.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutorhelper.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_SECOND_SUBJECT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_THIRD_STUDENT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_THIRD_SUBJECT;

import org.junit.Test;

import tutorhelper.logic.commands.DeleteSubCommand;

public class DeleteSubCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteSubCommand.MESSAGE_USAGE);

    private DeleteSubCommandParser parser = new DeleteSubCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteSubCommand() {
        assertParseSuccess(parser, "1 2", new DeleteSubCommand(INDEX_FIRST_STUDENT, INDEX_SECOND_SUBJECT));
        assertParseSuccess(parser, "2 3", new DeleteSubCommand(INDEX_SECOND_STUDENT, INDEX_THIRD_SUBJECT));
        assertParseSuccess(parser, "3 1", new DeleteSubCommand(INDEX_THIRD_STUDENT, INDEX_FIRST_SUBJECT));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Negative student index
        assertParseFailure(parser, "-1 1", MESSAGE_INVALID_FORMAT);

        // Zero student index
        assertParseFailure(parser, "0 1", MESSAGE_INVALID_FORMAT);

        // Invalid student index
        assertParseFailure(parser, "a", MESSAGE_INVALID_FORMAT);

        // Missing subject index
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);

        // Negative subject index
        assertParseFailure(parser, "1 -1", MESSAGE_INVALID_FORMAT);

        // Zero subject index
        assertParseFailure(parser, "1 0", MESSAGE_INVALID_FORMAT);

        // Invalid subject index
        assertParseFailure(parser, "1 a", MESSAGE_INVALID_FORMAT);
    }
}
