package nixchats;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import nixchats.data.TaskList;
import nixchats.exception.InputException;
import nixchats.exception.NixChatsException;
import nixchats.parser.Parser;
import nixchats.storage.Storage;
import nixchats.ui.TextUi;

/**
 * CLI interface for NixChats.
 */
public class NixChatsCli {
    
    /**
     * Starts the chatbot CLI.
     * @throws NixChatsException if the file cannot be read.
     * @throws IOException if the file cannot be written.
     */
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
                String command = Parser.getCommand(line);
                switch (command) {
                case "bye":
                    break;
                case "list":
                    printWithDivider(() -> {
                        System.out.println("Here are the tasks in your list:");
                        list.printTasks();
                    });
                    continue;
                case "find":
                    printWithDivider(() -> {
                        String keyword = Parser.getKeyword(line);
                        if (keyword.isEmpty()) {
                            System.out.println("Please provide a keyword to search for, e.g., \"find book\".");
                        } else {
                            list.findTasks(keyword);
                        }
                    });
                    continue;
                case "mark":
                    printWithDivider(() -> {
                        int idx = Parser.parseTaskIndex(line, list.size());
                        list.getTask(idx).markAsDone();
                    });
                    continue;
                case "unmark":
                    printWithDivider(() -> {
                        int idx = Parser.parseTaskIndex(line, list.size());
                        list.getTask(idx).unmarkAsNotDone();
                    });
                    continue;
                case "delete":
                    printWithDivider(() -> {
                        int idx = Parser.parseTaskIndex(line, list.size());
                        list.deleteTask(idx);
                    });
                    continue;
                default:
                    printWithDivider(() -> {
                        try {
                            list.addTask(line);
                            System.out.println("Got it, I have added: " + line);
                        } catch (InputException e) {
                            System.out.println(e.getMessage());
                        }
                    });
                    continue;
                }
                break;
            } catch (IllegalArgumentException e) {
                // User input error (missing arg, bad number, out of range, etc.)
                printWithDivider(() -> System.out.println(e.getMessage()));
            } catch (Exception e) {
                // Last-resort guard so the loop doesn't die on unexpected issues
                printWithDivider(() -> System.out.println("An unexpected error occurred. Please try again."));
            }
        }
        storage.save(list);
        sc.close();
    }

    /**
     * Executes the given action and wraps the output with dividers.
     */
    private static void printWithDivider(Runnable action) {
        System.out.println(TextUi.DIVIDER);
        action.run();
        System.out.println(TextUi.DIVIDER);
    }
}
