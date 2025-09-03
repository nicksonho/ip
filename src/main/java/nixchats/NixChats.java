package nixchats;

import nixchats.data.TaskList;
import nixchats.storage.Storage;
import nixchats.exception.NixChatsException;
import nixchats.ui.TextUi;
import nixchats.exception.InputException;
import nixchats.parser.Parser;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;



public class NixChats {
    public static void main(String[] args) {
        System.out.println(TextUi.GREETING);
        try {
            chat();
        } catch (Exception e) {
            // Last resort: prevent crash on truly unexpected issues.
            System.out.println("An unexpected error occurred. Please try again.");
        } finally {
            System.out.println(TextUi.EXIT);
        }
    }

    public static void chat() throws NixChatsException, IOException {
        Scanner sc = new Scanner(System.in);
        Path filePath = Paths.get("data", "NixChatHistory.txt");
        Storage storage = new Storage(filePath);
        TaskList list = storage.load();

        if (list.isEmpty()) {
            System.out.println("Congrats, you have completed all your tasks!");
        } else {
            System.out.println("Here are your current tasks:");
            list.printTasks();
        }

        while (true) {
            System.out.print("You: ");
            String input = sc.nextLine();
            String line = input == null ? "" : input.trim();
            try {
                if (line.equalsIgnoreCase("bye")) {
                    break;
                } else if (line.equalsIgnoreCase("list")) {
                    System.out.println(TextUi.DIVIDER);
                    System.out.println("Here are the tasks in your list:");
                    list.printTasks();
                    System.out.println(TextUi.DIVIDER);
                } else if (line.startsWith("mark")) {
                    System.out.println(TextUi.DIVIDER);
                    int idx = Parser.parseTaskIndex(line, list.size());
                    list.getTask(idx).markAsDone();
                    System.out.println(TextUi.DIVIDER);
                } else if (line.startsWith("unmark")) {
                    System.out.println(TextUi.DIVIDER);
                    int idx = Parser.parseTaskIndex(line, list.size());
                    list.getTask(idx).unmarkAsNotDone();
                    System.out.println(TextUi.DIVIDER);
                } else if (line.startsWith("delete")) {
                    System.out.println(TextUi.DIVIDER);
                    int idx = Parser.parseTaskIndex(line, list.size());
                    list.deleteTask(idx);
                    System.out.println(TextUi.DIVIDER);
                } else {
                    System.out.println(TextUi.DIVIDER);
                    list.addTask(line);
                    System.out.println("Got it, I have added: " + line);
                    System.out.println(TextUi.DIVIDER);
                }
            } catch (InputException e) {
                System.out.println(e.getMessage());
                System.out.println(TextUi.DIVIDER);
            } catch (IllegalArgumentException e) {
                // User input error (missing arg, bad number, out of range, etc.)
                System.out.println(e.getMessage());
                System.out.println(TextUi.DIVIDER);
            } catch (Exception e) {
                // Last-resort guard so the loop doesn't die on unexpected issues
                System.out.println("An unexpected error occurred. Please try again.");
                System.out.println(TextUi.DIVIDER);
            }
        }
        storage.save(list);
        sc.close();
    }
}

