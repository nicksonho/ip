package nixchats.ui;

public class TextUi {
    public static String DIVIDER = "==========================================================";
    public static String GREETING =
            """
                                    Hello! I'm NixChats, a chatbot that can help you track your tasks!
                                    What can I do for you?

                                    Usage:
                                      list
                                        - Show all tasks.
                                      find <keyword>
                                        - Find tasks containing the keyword.
                                      todo <description>
                                        - Add a to-do task.
                                      deadline <description> /by <when>
                                        - Add a deadline task.
                                      event <description> /from <start> /to <end>
                                        - Add an event task.
                                      mark <task-number>
                                        - Mark a task as done.
                                      unmark <task-number>
                                        - Mark a task as not done.
                                      delete <task-number>
                                        - Delete a task.
                                      bye
                                        - Exit the application.
                    """;
    public static String EXIT = DIVIDER + "\nBye! Hope to see you again soon!\n" + DIVIDER;
}
