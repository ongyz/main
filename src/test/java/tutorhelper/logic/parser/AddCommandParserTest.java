package tutorhelper.logic.parser;

import static tutorhelper.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static tutorhelper.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static tutorhelper.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static tutorhelper.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static tutorhelper.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static tutorhelper.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static tutorhelper.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static tutorhelper.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static tutorhelper.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static tutorhelper.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static tutorhelper.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static tutorhelper.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static tutorhelper.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static tutorhelper.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static tutorhelper.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static tutorhelper.logic.commands.CommandTestUtil.SUBJECT_DESC_AMY;
import static tutorhelper.logic.commands.CommandTestUtil.SUBJECT_DESC_BOB;
import static tutorhelper.logic.commands.CommandTestUtil.TAG_DESC_EXAM;
import static tutorhelper.logic.commands.CommandTestUtil.TAG_DESC_WEAK;
import static tutorhelper.logic.commands.CommandTestUtil.TAG_DESC_WEAK_EXAM;
import static tutorhelper.logic.commands.CommandTestUtil.TUITION_TIMING_DESC_AMY;
import static tutorhelper.logic.commands.CommandTestUtil.TUITION_TIMING_DESC_BOB;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_TAG_EXAM;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_TAG_WEAK;
import static tutorhelper.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutorhelper.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutorhelper.testutil.TypicalStudents.AMY;
import static tutorhelper.testutil.TypicalStudents.BOB;

import org.junit.Test;

import tutorhelper.commons.core.Messages;
import tutorhelper.logic.commands.AddCommand;
import tutorhelper.model.student.Address;
import tutorhelper.model.student.Email;
import tutorhelper.model.student.Name;
import tutorhelper.model.student.Phone;
import tutorhelper.model.student.Student;
import tutorhelper.model.tag.Tag;
import tutorhelper.testutil.StudentBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Student expectedStudent = new StudentBuilder(BOB).withTags(VALID_TAG_EXAM).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + SUBJECT_DESC_BOB + TUITION_TIMING_DESC_BOB + TAG_DESC_EXAM,
                new AddCommand(expectedStudent));

        // multiple tags - all accepted
        Student expectedStudentMultipleTags = new StudentBuilder(BOB).withTags(VALID_TAG_EXAM, VALID_TAG_WEAK)
                .build();
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + SUBJECT_DESC_BOB + TUITION_TIMING_DESC_BOB + TAG_DESC_WEAK_EXAM,
                new AddCommand(expectedStudentMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Student expectedStudent = new StudentBuilder(AMY).withTags().build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + SUBJECT_DESC_AMY + TUITION_TIMING_DESC_AMY, new AddCommand(expectedStudent));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_ADDRESS_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB
                + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + SUBJECT_DESC_BOB + TUITION_TIMING_DESC_BOB
                + TAG_DESC_WEAK + TAG_DESC_EXAM, Name.MESSAGE_NAME_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC
                + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + SUBJECT_DESC_BOB + TUITION_TIMING_DESC_BOB
                + TAG_DESC_WEAK + TAG_DESC_EXAM, Phone.MESSAGE_PHONE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB
                + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB + SUBJECT_DESC_BOB + TUITION_TIMING_DESC_BOB
                + TAG_DESC_WEAK + TAG_DESC_EXAM, Email.MESSAGE_EMAIL_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB
                        + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC + SUBJECT_DESC_BOB + TUITION_TIMING_DESC_BOB
                        + TAG_DESC_WEAK + TAG_DESC_EXAM, Address.MESSAGE_ADDRESS_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB
                + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + SUBJECT_DESC_BOB + TUITION_TIMING_DESC_BOB + INVALID_TAG_DESC
                + VALID_TAG_EXAM, Tag.MESSAGE_TAG_CONSTRAINTS);

        // two invalid values, only first invalid subjectName reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB
                + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                + SUBJECT_DESC_BOB + TUITION_TIMING_DESC_BOB, Name.MESSAGE_NAME_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB
                + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + SUBJECT_DESC_BOB + TUITION_TIMING_DESC_BOB + TAG_DESC_WEAK + TAG_DESC_EXAM,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        // multiple names - invalid name
        assertParseFailure(parser, NAME_DESC_AMY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + SUBJECT_DESC_BOB + TUITION_TIMING_DESC_BOB + TAG_DESC_EXAM,
                Name.MESSAGE_NAME_CONSTRAINTS);

        // multiple phones - invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_AMY + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + SUBJECT_DESC_BOB + TUITION_TIMING_DESC_BOB + TAG_DESC_EXAM,
                Phone.MESSAGE_PHONE_CONSTRAINTS);

        // multiple emails - last email accepted
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_AMY + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + SUBJECT_DESC_BOB + TUITION_TIMING_DESC_BOB + TAG_DESC_EXAM,
                Email.MESSAGE_EMAIL_CONSTRAINTS);
    }
}
