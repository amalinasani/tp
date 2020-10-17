package seedu.duke.parser;

import seedu.duke.command.Command;
import seedu.duke.command.ExitCommand;
import seedu.duke.command.IncorrectCommand;
import seedu.duke.command.add.AddCommand;
import seedu.duke.command.delete.DeleteCommand;
import seedu.duke.command.done.DoneCommand;
import seedu.duke.command.edit.EditCommand;
import seedu.duke.command.edit.EditModuleCommand;
import seedu.duke.command.edit.EditTaskCommand;
import seedu.duke.command.help.HelpCommand;
import seedu.duke.command.list.ListCommand;

import java.security.InvalidParameterException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static seedu.duke.util.ExceptionMessage.MESSAGE_INVALID_COMMAND_WORD;
import static seedu.duke.util.ExceptionMessage.MESSAGE_INVALID_PARAMETERS;
import static seedu.duke.util.Message.MESSAGE_CHECK_COMMAND_FORMAT;
import static seedu.duke.util.Message.MESSAGE_EMPTY_INPUT;
import static seedu.duke.util.Message.MESSAGE_INVALID_COMMAND_FORMAT;

public class Parser {
    public enum TypeOfEntries {
        TASK, MODULE
    }

    /**
     * Used for initial separation of command word and args.
     */
//    private static final Pattern BASIC_COMMAND_FORMAT =
//            Pattern.compile("(?<commandWord>\\S+)" + "((?<digit>\\s+\\d+)?)"
//                    + "((?<commandFlag>.*-\\S+)?)"  + "((?<parameters>.*)?)");

    private static final String COMMAND_WORD_GROUP = "commandWord";
    protected static final String COMMAND_FLAG_GROUP = "commandFlag";
    private static final String PARAMETERS_GROUP = "parameters";
    private static final String NUMBER_GROUP = "digit";
    protected static final String TASK_NAME_GROUP = "taskName";
    private static final String MODULE_GROUP = "moduleCode";
    protected static final String DATE_IDENTIFIER_GROUP = "by";
    protected static final String DUE_DATE_GROUP = "dueDate";

    public static final String MODULE_PREFIX = "-m";
    public static final String TASK_PREFIX = "-t";
    public static final String BY_PREFIX = "-by";

    private static final Pattern BASIC_COMMAND_FORMAT =
            Pattern.compile("(?<commandWord>\\S+)" + "((?<parameters>.*)?)");

    protected static final Pattern PARAMETERS_FORMAT =
            Pattern.compile("((?<digit>\\s+\\d+)?)" + "((?<commandFlag>.*-\\S+)?)"
                    +"(?<taskName>\\S+)((?<by>.*" + BY_PREFIX + ")?)((?<dueDate>.*)?)");

    //(?<identifier>(?:\s+\w\S*)*)+ -m+ (?<moduleCode>(?:\\s+" + "(?:\\s+\\w\\S*)+)?)(?<invalid>.*)


    /**
     * Parses the input string read by the <b>UI</b> and converts the string into a specific <b>Command</b>, which is
     * to be executed by the <b>Nuke</b> program.
     * <p></p>
     * <b>Note</b>: The user input has to start with a certain keyword (i.e. <i>command word</i>), otherwise an
     * <i>Invalid Command Exception</i> will be thrown.
     *
     * @param input The user input read by the <b>UI</b>
     * @return The <b>corresponding</b> command to be executed
     * @see Command
     */
    public Command parseCommand(String input) {
        if (input.isBlank()) {
            return new IncorrectCommand(MESSAGE_EMPTY_INPUT);
        }

        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(input.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(MESSAGE_INVALID_COMMAND_FORMAT);
        }

        try {
            // Checks if the matched groups are null
            String commandWord = isMatcherNull(matcher.group(COMMAND_WORD_GROUP))
                    ? null : matcher.group(COMMAND_WORD_GROUP).toLowerCase().trim();
            String digit = isMatcherNull(matcher.group(NUMBER_GROUP))
                    ? null : matcher.group(NUMBER_GROUP).trim();
            String commandFlag = isMatcherNull(matcher.group(COMMAND_FLAG_GROUP))
                    ? null : matcher.group(COMMAND_FLAG_GROUP).toLowerCase().trim();
            String parameters = isMatcherNull(matcher.group(PARAMETERS_GROUP))
                    ? null : matcher.group(PARAMETERS_GROUP).trim();

            if (commandWord.equals(ExitCommand.COMMAND_WORD)) {
                return new ExitCommand();
            } else if (commandWord.equals(HelpCommand.COMMAND_WORD)) {
                return new HelpCommand();
            } else {
                switch (commandWord) {
                case EditCommand.COMMAND_WORD:
                    return EditCommandParser.getEditCommand(parameters);
                case AddCommand.COMMAND_WORD:
                    return AddCommandParser.getAddCommand(parameters);
                case DeleteCommand.COMMAND_WORD:
                    return DeleteCommandParser.getDeleteCommand(parameters);
                case DoneCommand.COMMAND_WORD:
                    return new DoneCommandParser.DoneCommand(parameters);
                case ListCommand.COMMAND_WORD:
                    return ListCommandParser.getListCommand(parameters); //command flag is the -t or -m
                default:
                    return new HelpCommand();
                }
            }
        } catch (InvalidParameterException | NumberFormatException e) {
            return new IncorrectCommand(MESSAGE_INVALID_PARAMETERS);
        } catch (NullPointerException e) {
            return new IncorrectCommand(MESSAGE_INVALID_COMMAND_WORD);
        }
    }

    protected static boolean isMatcherNull(String matcherTest) {
        return (matcherTest == null);
    }

    /**
     * Checks the input given by the user is empty.
     *
     * @param attributes
     *  The input from the user
     * @return
     *  <code>TRUE</code> if the input is empty, or <code>FALSE</code> otherwise
     */
    protected static boolean isEmptyParse(String... attributes) {
        for (String attribute : attributes) {
            if (!attribute.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
