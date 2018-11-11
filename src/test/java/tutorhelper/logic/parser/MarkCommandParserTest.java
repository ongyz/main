package tutorhelper.logic.parser;

import static tutorhelper.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutorhelper.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_SYLLABUS;
import static tutorhelper.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_SECOND_SUBJECT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_SECOND_SYLLABUS;
import static tutorhelper.testutil.TypicalIndexes.INDEX_THIRD_SUBJECT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_THIRD_SYLLABUS;

import org.junit.Test;

import tutorhelper.commons.core.Messages;
import tutorhelper.logic.commands.MarkCommand;

public class MarkCommandParserTest {
    private MarkCommandParser parser = new MarkCommandParser();

    @Test
    public void parse_validArgs_returnsMarkCommand() {
        assertParseSuccess(parser, "1 1 2",
                new MarkCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, INDEX_SECOND_SYLLABUS));
        assertParseSuccess(parser, "2 3 1",
                new MarkCommand(INDEX_SECOND_STUDENT, INDEX_THIRD_SUBJECT, INDEX_FIRST_SYLLABUS));
        assertParseSuccess(parser, "1 2 3",
                new MarkCommand(INDEX_FIRST_STUDENT, INDEX_SECOND_SUBJECT, INDEX_THIRD_SYLLABUS));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1 1",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "a 1",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
    }
}
