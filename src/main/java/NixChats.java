import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NixChats {
    public static void main(String[] args) {
        Greetings.greet();
        Scanner sc = new Scanner(System.in);
        List<String> list = new ArrayList<>();
        while (true) {
            System.out.print("You: ");
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("bye")) {
                Greetings.exit();
                break;
            } else if (input.equalsIgnoreCase("list")) {
                Greetings.divider();
                for (int i = 0; i < list.size(); i++) {
                    System.out.println(i+1 + ": " + list.get(i));
                }
                Greetings.divider();
            } else {
                list.add(input);
                Greetings.divider();
                System.out.println("added: " + input);
                Greetings.divider();
            }
        }
        sc.close();
    }


}

