package app.utils;

import org.slf4j.LoggerFactory;

public class Log {
    /**
     * General
     */
    public static final String PROFILING = "Profiling";
    public static final String CAPACITY = "Capacity:";
    public static final String RUNTIME = "Runtime:";
    public static final String SPEED = "Speed:";

    /**
     * Debug and info
     */
    public static final String START_RUNNING_CODE = "Run the client process...";
    public static final String END_RUNNING_CODE = "The client process was finished!";
    public static final String START_READING_PROCESS = "There is creating a new process, received from the user...";
    public static final String END_READING_PROCESS = "The resulting data were successfully got!";
    public static final String VALID_OS = "The selected operating system is available for the test. Redirecting to the menu...";
    public static final String PROCESS_INFO_START = "The progress info dialog was started. Processing...";
    public static final String PROCESS_INFO_END = "The progress info dialog was finished. Closing...";
    public static final String PROPERTY_FILE_READ = "The .properties file was successfully read!";
    public static final String PROPERTY_FILE_UPDATE = "The .properties file was successfully updated!";
    public static final String PREPARATION_FOR_WRITING_IN_THE_FILE = "Preparation to write to a file...";
    public static final String WRITING_IN_THE_FILE = "Writing to the file....";
    public static final String WRITING_IN_THE_FILE_SUCCESS = "Writing was successfully finished!";
    public static final String START_INIT_PROPERTIES = "Start of the initialization properties...";
    public static final String START_SAVING_TO_DATABASE = "Start saving to the database...";
    public static final String END_SAVING_TO_DATABASE = "Data was successfully written to the database!";
    public static final String END_INIT_PROPERTIES = "Initialization the properties was finished.";
    public static final String DATA_INIT_FOR_DISPLAY = "The resulting data are being initialized for displaying...";
    public static final String DATA_DISPLAY_SUCCESS = "The resulting data were successfully displayed on the screen.";
    public static final String FILE_DATA_DISPLAY_SUCCESS = "Data was successfully written to the file!";
    public static final String CREATING_DIR_FOR_WRITING = "Creating a directory for writing the resulting data...";
    public static final String RETURNING_TO_THE_MENU_SUCCESS = "Returning to the menu was finished successfully!";
    public static final String CLOSING_APP = "It is closing the application...";

    /**
     * Errors and warnings
     */
    public static final String WRONG_OS = "This operating system is not for you. Select your operating system.";
    public static final String LOADING_PROGRESS_IMAGE_ERROR = "Error when loading the image for ProgressDialog.";
    public static final String PATH_TO_PROGRAM_INCORRECT = "Cannot run user's program. Please, check that path to program is correct and your command has the correct syntax.";
    public static final String TIP = "\nTip: Don't specify any commands for start a new process at the path to your program. " +
            "(For example: cmd, start, etc.)\nThe correct examples(WINDOWS):\nC:/../task.exe\njava -classpath C:/../folder main.Main";
    public static final String EMPTY_PATH = "Path to your program wasn't specified. Please, specify it at your .properties file. " +
            "For example(WINDOWS): program_path=C:/../task.exe.\nAlso, if your program needs additional parameters for start (for example, the compiler), " +
            "you can create some executable file (bat, cmd, com or sh), write at it your command for start and specify path to this file at your .properties file.\nFor example: program_path=C:/../test.bat\n" +
            "Note: the profiling program can test only ONE process.";
    public static final String HIBERNATE_ERROR = "Error occurred when hibernate trying to save data to the database.";
    public static final String CREATING_SESSION_FACTORY_ERROR = "Failed to create sessionFactory instance.";
    public static final String SESSION_FACTORY_IS_NULL = "sessionFactory instance is null";
    public static final String URL_TO_DATABASE_IS_NULL = "Specify url for the connecting.";
    public static final String DRIVER_CLASS_NOT_FOUND_ERROR = "Driver class was not found for your DBMS.";
    public static final String PROPERTY_READ_ERROR = "Error occurred when reading the property file.";
    public static final String SMALL_PROGRAM_ERROR = "Your program is too small to profile.";
    public static final String CANCELLING_PROPERTY_FILE = "Pressed 'cancel' when trying to open .properties file.";
    public static final String A_PROPERTY_FILE_WAS_NOT_SELECTED = "The .properties file was not selected.";
    public static final String FILE_DATA_DISPLAY_ERROR = "Error occurred when writing in the file.";
    public static final String DATA_DISPLAY_ERROR = "Error!One of the characteristics or everyone weren't got when processing";
    public static final String CREATING_FILE_ERROR = "Error when creating the file";
    public static final String WRITING_IN_FILE_ERROR = "Errors when writing to file";
    public static final String SETTING_BACKGROUND_ERROR = "Failed to set the background image";
    public static final String SETTING_BUTTON_IMAGE_ERROR = "Failed to set the button's image";
    public static final String NO_OPTION_WHEN_UPDATE_THE_PROPERTY_FILE = "The .properties file did not update because user chose 'no'.";
    public static final String NO_OPTION_WHEN_RETURNING_TO_THE_MENU = "Returning to the menu was not because user chose 'no'.";
    public static final String THE_FILE_IS_NOT_PROPERTIES = "The selected file is not .properties file.";

    /**
     * Dialog's headers
     */
    public static final String ERROR = "Error";
    public static final String INFORMATION = "Information";
    public static final String QUESTION = "Question";
    public static final String PROCESSING_GUI = "Processing...";

    /**
     * Dialog's message
     */
    public static final String NOT_WINDOWS_OS = "Your operation system is not Windows.";
    public static final String NOT_LINUX_OS = "Your operation system is not Linux.";
    public static final String NOT_MAC_OS = "Your operation system is not Mac.";
    public static final String PROPERTIES_IS_NULL = "Properties file was not specified.";
    public static final String WRONG_DATABASE_URL = "Your url doesn't match the selected database.";
    public static final String WRITING_DATABASE_ERROR = "Error occurred when writing to the database.";
    public static final String WRITING_DATABASE_SUCCESS = "Data was successfully written to the database!";
    public static final String CLIENT_PROCESS_ERROR = "Error occurred when running your program.";
    public static final String EMPTY_PATH_MESSAGE = "Path to your program wasn't specified.";
    public static final String CONFIRMATION_OF_RETURNING_TO_MENU = "Do you want to return to the menu?";
    public static final String CONFIRMATION_OF_UPDATE_PROPERTY_FILE = "Do you want to update the property file?";

    /**
     * Text
     */
    public static final String WELCOME_GUI = "Welcome to the program profiling!";
    public static final String CHOICE_OS = "Select your operating system";
    public static final String MENU_GUI = "Menu";
    public static final String RESULTS = "The results of your program:";

    /**
     * Buttons
     */
    public static final String OPEN_PROPERTY_FILE = "Open file";
    public static final String START_TEST = "Start test";
    public static final String SAVE_TO_FILE = "Save to file";
    public static final String SAVE_TO_DB = "Save to DB";
    public static final String UPDATE_FILE = "Update file";
    public static final String REPEAT_TEST = "Repeat test";
    public static final String EXIT = "Exit";

    /**
     * Tooltips for buttons
     */
    public static final String START_TEST_BUTTON_MESSAGE = "Start test the user's program";
    public static final String OPEN_PROPERTY_BUTTON_MESSAGE = "Set .properties file with the attributes before to start the test";
    public static final String SAVE_FILE_BUTTON_MESSAGE = "Save the results of the user's program to a .txt file";
    public static final String SAVE_DB_BUTTON_MESSAGE = "Save the results of the user's program to a database";
    public static final String UPDATE_FILE_BUTTON_MESSAGE = "Update the selected .properties file";
    public static final String REPEAT_TEST_BUTTON_MESSAGE = "Return on the menu where you can repeat the test.";
    public static final String EXIT_BUTTON_MESSAGE = "Exit from the application";

    /**
     * Console
     */
    public static final String ENTER = "Enter: ";
    public static final String WRONG_ENTER = "Invalid input! Try again.";
    public static final String WELCOME_CONSOLE = "WELCOME TO THE PROGRAM PROFILING!";
    public static final String MENU_CONSOLE = "M E N U";
    public static final String MENU_CHOICE = "Choose one of the next options (using 1-2):";
    public static final String RESULT_PANEL_CHOICE = "Choose one of the next options (using 0-4):";
    public static final String MENU_WRONG_PATH_TO_PROPERTY_FILE = "Wrong path. Try again or press 'enter'.";
    public static final String MENU_PATH_TO_PROPERTY_FILE = "Enter path to your .properties file: ";
    public static final String DATABASE_INPUT = "Choose the type of database (using 1-5) or exit (using 0):";
    public static final String SAVE_TO_DATABASE = "Save to database";
    public static final String TYPE_DATABASE_IS_NOT_SELECTED = "The type of database was not selected. Returning back...";
    public static final String UPDATE_PROPERTY_FILE = "Update the .properties file";
    public static final String PROCESSING_CONSOLE = "P r o c e s s i n g... <=";
    public static final String PROCESSING_WAS_FINISHED = "The profiling was successfully finished!";
    public static final String BACK = "<--";

    private Log() {
    }

    public static org.slf4j.Logger createLog(Class clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
