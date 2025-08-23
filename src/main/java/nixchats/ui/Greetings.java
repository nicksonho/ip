package nixchats.ui;

public class Greetings {

    public static void divider() {
        System.out.println("____________________________________________________________");
    }

    public static void greet() {
        divider();
        String message = """
                Hello! I'm NixChats, a chatbot that can help you track your tasks!
                What can I do for you?

                Usage:
                  list
                    - Show all tasks.
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
                  bye
                    - Exit the application.
                """;
        System.out.println(message);
        divider();
    }


    public static void exit() {
        divider();
        System.out.println("Bye. Hope to see you again soon!");
        divider();
    }
}