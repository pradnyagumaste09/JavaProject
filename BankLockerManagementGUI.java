import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class BankLocker {
    private boolean isOccupied;
    private String lockerOwner;

    public BankLocker() {
        this.isOccupied = false;
        this.lockerOwner = "";
    }

    public String rentLocker(String ownerName) {
        if (!isOccupied) {
            isOccupied = true;
            lockerOwner = ownerName;
            return "Locker rented successfully to " + ownerName;
        } else {
            return "Locker is already rented.";
        }
    }

    public String returnLocker() {
        if (isOccupied) {
            isOccupied = false;
            return "Locker returned successfully.";
        } else {
            return "This locker was not rented.";
        }
    }

    public String displayLockerInfo() {
        if (isOccupied) {
            return "Locker is rented by: " + lockerOwner;
        } else {
            return "Locker is available.";
        }
    }
}

public class BankLockerManagementGUI {
    private static JTextArea displayArea;
    private static JTextField nameField;
    private static BankLocker locker = new BankLocker();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bank Locker Management System");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        displayArea = new JTextArea(10, 30);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        frame.add(scrollPane);

        JLabel nameLabel = new JLabel("Enter your name: ");
        frame.add(nameLabel);

        nameField = new JTextField(20);
        frame.add(nameField);

        JButton rentButton = new JButton("Rent Locker");
        rentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                displayArea.append(locker.rentLocker(name) + "\n");
            }
        });
        frame.add(rentButton);

        JButton returnButton = new JButton("Return Locker");
        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayArea.append(locker.returnLocker() + "\n");
            }
        });
        frame.add(returnButton);

        JButton statusButton = new JButton("Check Locker Status");
        statusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayArea.append(locker.displayLockerInfo() + "\n");
            }
        });
        frame.add(statusButton);

        frame.setVisible(true);
    }
}

