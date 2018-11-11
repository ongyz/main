package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_SUBJECT;

import org.junit.Test;

import seedu.address.logic.commands.AddSyllCommand;
import seedu.address.model.subject.Syllabus;

import java.util.ArrayList;
import java.util.List;

public class AddSyllCommandParserTest {

    private AddSyllCommandParser parser = new AddSyllCommandParser();

    @Test
    public void parse_validArgs_returnsAddSyllCommand() {
        List<Syllabus> syllabusList = new ArrayList<>();
        syllabusList.add(Syllabus.makeSyllabus("Integration"));
        assertParseSuccess(parser, "1 1 sy/Integration",
                new AddSyllCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, syllabusList));
        syllabusList.set(0, Syllabus.makeSyllabus("Kinetics"));
        assertParseSuccess(parser, "2 3 sy/Kinetics",
                new AddSyllCommand(INDEX_SECOND_STUDENT, INDEX_THIRD_SUBJECT, syllabusList));
        syllabusList.set(0, Syllabus.makeSyllabus("Molecular Biology"));
        assertParseSuccess(parser, "1 2 sy/Molecular Biology",
                new AddSyllCommand(INDEX_FIRST_STUDENT, INDEX_SECOND_SUBJECT, syllabusList));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSyllCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSyllCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSyllCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "a 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSyllCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1 1 test",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSyllCommand.MESSAGE_USAGE));

        // Error on the syllabus parsing should show syllabus constraints instead
        assertParseFailure(parser, "1 1 sy/ ", Syllabus.MESSAGE_SYLLABUS_CONSTRAINTS);
    }

}
