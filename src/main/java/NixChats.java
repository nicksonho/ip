import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NixChats {
    public static void main(String[] args) {
        Greetings.greet();
        chat();
    }

    public static void chat() {
        Scanner sc = new Scanner(System.in);
        List<Task> list = new ArrayList<>();
        while (true) {
            System.out.print("You: ");
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("bye")) {
                Greetings.exit();
                break;
            } else if (input.equalsIgnoreCase("list")) {
                Greetings.divider();
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < list.size(); i++) {
                    System.out.println(i + 1 + ". " + list.get(i).toString());
                }
                Greetings.divider();
            } else if (input.startsWith("mark")) {
                Greetings.divider();
                String[] parts = input.split(" ");
                int taskNum = Integer.parseInt(parts[1]) - 1;  // -1 for 0-based index
                Task t = list.get(taskNum);
                t.markAsDone();
                Greetings.divider();
            } else if (input.startsWith("unmark")) {
                Greetings.divider();
                String[] parts = input.split(" ");
                int taskNum = Integer.parseInt(parts[1]) - 1;  // -1 for 0-based index
                Task t = list.get(taskNum);
                t.unmarkAsNotDone();
                Greetings.divider();
            } else {
                Greetings.divider();
                addTask(list, input);
                Greetings.divider();
            }
        }
        sc.close();
    }

    public static void addTask(List<Task> list, String input) {
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        String trimmed = input.trim();
        String lowerInput = trimmed.toLowerCase();

        int firstSpace = trimmed.indexOf(' ');
        if (firstSpace == -1) {
            System.out.println("Invalid input. Please try again.");
        }

        // Handle ToDoTask
        if (lowerInput.startsWith("todo")) {
            System.out.println("Got it. I've added this task:");
            ToDoTask toDoTask = new ToDoTask(trimmed.substring(firstSpace + 1).trim());
            list.add(toDoTask);
            System.out.println(toDoTask.toString());
        }

        // Handle DeadlineTask
        else if (lowerInput.startsWith("deadline ")) {
            System.out.println("Got it. I've added this task:");
            String[] parts = trimmed.split("\\s+/by\\s+", 2);
            if (parts.length == 2) {
                String description = parts[0].substring(9).trim(); // Remove "deadline "
                String byTime = parts[1].trim();
                DeadlineTask deadline = new DeadlineTask(description, byTime);
                list.add(deadline);
                System.out.println(deadline.toString());
            }
        }

        // Handle EventTask
        else if (lowerInput.startsWith("event ")) {
            System.out.println("Got it. I've added this task:");
            // Split by /from first
            String[] fromParts = trimmed.split("\\s+/from\\s+", 2);
            if (fromParts.length == 2) {
                String description = fromParts[0].substring(6).trim(); // Remove "event "

                // Then split the second part by /to
                String[] toParts = fromParts[1].split("\\s+/to\\s+", 2);
                if (toParts.length == 2) {
                    String fromTime = toParts[0].trim();
                    String toTime = toParts[1].trim();
                    EventTask event = new EventTask(description, fromTime, toTime);
                    list.add(event);
                    System.out.println(event.toString());
                }
            }
        }

        else {
            System.out.println("Invalid input. Please try again.");
        }
    }
}

