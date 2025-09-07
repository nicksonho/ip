import nixchats.NixChats;

public class TestGUI {
    public static void main(String[] args) {
        NixChats nixchats = new NixChats();
        
        // Test adding a task
        String response1 = nixchats.getResponse("todo read book");
        System.out.println("Response: " + response1);
        System.out.println("Command type: " + nixchats.getCommandType());
        
        // Test listing tasks
        String response2 = nixchats.getResponse("list");
        System.out.println("\nResponse: " + response2);
        System.out.println("Command type: " + nixchats.getCommandType());
        
        // Test finding tasks
        String response3 = nixchats.getResponse("find book");
        System.out.println("\nResponse: " + response3);
        System.out.println("Command type: " + nixchats.getCommandType());
    }
}
