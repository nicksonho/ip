import java.util.Scanner;

public class NixChats {
    public static void main(String[] args) {
        Greetings.greet();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("You: ");
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("bye")) {
                Greetings.exit();
                break;
            }
            System.out.println("____________________________________________________________");
            System.out.println("Echo: " + input);
            System.out.println("____________________________________________________________");
        }
        sc.close();
    }


}

