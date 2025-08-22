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
            Task task = new Task(input);
            if (input.equalsIgnoreCase("bye")) {
                Greetings.exit();
                break;
            } else if (input.equalsIgnoreCase("list")) {
                Greetings.divider();
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
                list.add(task);
                Greetings.divider();
                System.out.println("added: " + input);
                Greetings.divider();
            }
        }
        sc.close();
    }
}

