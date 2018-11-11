package tutorhelper.logic.parser;

import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_SYLLABUS_DIFFERENTIATION;
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

import tutorhelper.logic.commands.EditSyllCommand;
import tutorhelper.model.subject.Syllabus;

public class EditSyllCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditSyllCommand.MESSAGE_USAGE);

    private EditSyllCommandParser parser = new EditSyllCommandParser();

    @Test
    public void parse_missingParts_failure() {

        // missing all index syllabus present
        assertParseFailure(parser, "sy/Integration", MESSAGE_INVALID_FORMAT);

        // missing two index syllabus present
        assertParseFailure(parser, "1 sy/Integration", MESSAGE_INVALID_FORMAT);

        // missing one index syllabus present
        assertParseFailure(parser, "1 1 sy/Integration", MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-1 1 1 sy/Integration", MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0 1 1 sy/Integration", MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 1 1 Integration", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 1 1 s/Integration", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Syllabus syllabus = new Syllabus(VALID_SYLLABUS_DIFFERENTIATION, true);

        assertParseSuccess(parser, "1 1 2 sy/Differentiation",
                new EditSyllCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, INDEX_SECOND_SYLLABUS, syllabus));
        assertParseSuccess(parser, "2 3 1 sy/Differentiation",
                new EditSyllCommand(INDEX_SECOND_STUDENT, INDEX_THIRD_SUBJECT, INDEX_FIRST_SYLLABUS, syllabus));
        assertParseSuccess(parser, "1 2 3 sy/Differentiation",
                new EditSyllCommand(INDEX_FIRST_STUDENT, INDEX_SECOND_SUBJECT, INDEX_THIRD_SYLLABUS, syllabus));
    }

}
