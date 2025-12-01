package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    /**
     * Reads an integer from the console, showing a custom question.
     * The method repeats until a valid whole number is entered.
     *
     * @param question the prompt shown to the user
     * @return the integer entered by the user
     */
    public static int readInteger(String question) {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader buffer = new BufferedReader(input);
        int num;
        String line;
        while (true) {
            try {
                System.out.print(question);
                line = buffer.readLine();
                num = Integer.parseInt(line);
                return num;

            } catch (IOException ioe) {
                System.out.println(" ERROR: Unable to read.");

            } catch (NumberFormatException nfe) {
                System.out.println(" ERROR: Must be a whole number.");
            }
        }
    }

    /**
     * Reads a valid date from the console by requesting day, month, and year separately.
     * Repeats the process until a valid date is entered.
     *
     * @param question the question displayed before requesting the date components
     * @return a LocalDate representing the entered date
     */
    public static LocalDate readDate(String question) {

        while (true) {
            try {
                System.out.println(question);
                int day = readInteger("   Day: ");
                int month = readInteger("   Month: ");
                int year = readInteger("   Year: ");
                return LocalDate.of(year, month, day);

            } catch (DateTimeException e) {
                System.out.println(" ERROR: Introduce a valid date.");
            }

        }
    }

    /**
     * Reads a string from the console, showing a custom prompt.
     * Repeats the process if an I/O error occurs.
     *
     * @param question the prompt shown to the user
     * @return the string entered by the user
     */
    public static String readString(String question) {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader buffer = new BufferedReader(input);
        String line;
        while (true) {
            try {
                System.out.print(question);
                line = buffer.readLine();
                return line;

            } catch (IOException ioe) {
                System.out.println(" ERROR: Unable to read.");
            }
        }
    }

    /**
     * Validates whether a given email follows the expected format.
     * Displays feedback messages to the user.
     *
     * @param email the email to validate
     * @return true if the email is valid, false otherwise
     */
    public static boolean checkEmail(String email) {
        Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");
        Matcher mather = pattern.matcher(email);
        System.out.println("-> Checking email... ");
        if (mather.find()) {
            System.out.println("-> Email valid! ");
            return true;
        } else {
            System.out.println("-> Please follow the email format: example@example.com");
            return false;
        }
    }
}