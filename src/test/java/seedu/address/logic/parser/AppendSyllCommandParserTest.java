package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_SUBJECT;

import org.junit.Test;

import seedu.address.logic.commands.AppendSyllCommand;
import seedu.address.model.subject.Syllabus;

public class AppendSyllCommandParserTest {

    private AppendSyllCommandParser parser = new AppendSyllCommandParser();

    @Test
    public void parse_validArgs_returnsAppendSyllMarkCommand() {
        assertParseSuccess(parser, "1 1 sy/Integration",
                new AppendSyllCommand(INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, Syllabus.makeSyllabus("Integration")));
        assertParseSuccess(parser, "2 3 sy/Kinetics",
                new AppendSyllCommand(INDEX_SECOND_PERSON, INDEX_THIRD_SUBJECT, Syllabus.makeSyllabus("Kinetics")));
        assertParseSuccess(parser, "1 2 sy/Molecular Biology",
                new AppendSyllCommand(INDEX_FIRST_PERSON, INDEX_SECOND_SUBJECT, Syllabus.makeSyllabus("Molecular Biology")));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AppendSyllCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AppendSyllCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AppendSyllCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "a 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AppendSyllCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1 1 test",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AppendSyllCommand.MESSAGE_USAGE));

        // Error on the syllabus parsing should show syllabus constraints instead
        assertParseFailure(parser, "1 1 sy/ ", Syllabus.MESSAGE_SYLLABUS_CONSTRAINTS);
    }

}
