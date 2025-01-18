import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServerGUI {
    private static JTextArea messageArea;
    private static JTextField messageField;
    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) {
        // Create and display the GUI on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });

        // Run the server logic in a separate thread
        new Thread(ChatServerGUI::startServer).start();
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Chat Server");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        messageField = new JTextField();
        messageField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessageToAll(messageField.getText());
                messageField.setText("");
            }
        });
        frame.add(messageField, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static void sendMessageToAll(String message) {
        messageArea.append("Server: " + message + "\n");
        for (PrintWriter writer : clientWriters) {
            writer.println("Server: " + message);
        }
    }

    private static void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> {
                messageArea.append("Error: " + e.getMessage() + "\n");
            });
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                String message;
                while ((message = in.readLine()) != null) {
                    final String receivedMessage = message;
                    SwingUtilities.invokeLater(() -> {
                        messageArea.append("Client: " + receivedMessage + "\n");
                    });

                    for (PrintWriter writer : clientWriters) {
                        writer.println("Client: " + message);
                    }
                }
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> {
                    messageArea.append("Error in client handler: " + e.getMessage() + "\n");
                });
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> {
                        messageArea.append("Error closing socket: " + e.getMessage() + "\n");
                    });
                }
                synchronized (clientWriters) {
                    clientWriters.remove(out);
                }
            }
        }
    }
}
