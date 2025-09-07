package nixchats;

import nixchats.ui.TextUi;

/**
 * CLI launcher for NixChats - provides command line interface.
 */
public class NixChatsCliLauncher {
    public static void main(String[] args) {
        System.out.println(TextUi.GREETING);
        try {
            NixChatsCli.chat();
        } catch (Exception e) {
            // Last resort: prevent crash on truly unexpected issues.
            System.out.println("An unexpected error occurred. Please try again.");
        } finally {
            System.out.println(TextUi.EXIT);
        }
    }
}
