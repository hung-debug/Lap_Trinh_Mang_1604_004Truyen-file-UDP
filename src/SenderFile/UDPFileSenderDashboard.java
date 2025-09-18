package SenderFile;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class UDPFileSenderDashboard extends JFrame {
    private JTextField ipField, portField;
    private JTextArea statusArea;
    private JButton connectButton, chooseButton, sendButton;
    private JLabel statusDot;
    private File selectedFile;
    private DatagramSocket socket;
    private InetAddress receiverAddress;
    private int receiverPort;

    public UDPFileSenderDashboard() {
        setTitle("Sender Dashboard (UDP)");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel cấu hình
        JPanel configPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        configPanel.add(new JLabel("Địa chỉ IP của Receiver:"));
        ipField = new JTextField("");
        configPanel.add(ipField);

        configPanel.add(new JLabel("Cổng Receiver:"));
        portField = new JTextField("");
        configPanel.add(portField);

        // Chấm trạng thái
        configPanel.add(new JLabel("Trạng thái kết nối:"));
        statusDot = new JLabel("●");
        statusDot.setForeground(Color.RED);
        statusDot.setFont(new Font("Arial", Font.BOLD, 20));
        configPanel.add(statusDot);

        // Các nút bấm
        JPanel buttonPanel = new JPanel();
        connectButton = createStyledButton("Kết nối", new Color(0, 153, 0));
        chooseButton = createStyledButton("Chọn tệp", new Color(0, 102, 204));
        sendButton = createStyledButton("Gửi tệp", new Color(204, 102, 0));
        buttonPanel.add(connectButton);
        buttonPanel.add(chooseButton);
        buttonPanel.add(sendButton);

        // Khu vực log
        statusArea = new JTextArea();
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(statusArea);
        scrollPane.setPreferredSize(new Dimension(580, 250)); // ✅ log to, dễ đọc

        // Layout
        add(configPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Sự kiện nút
        connectButton.addActionListener(e -> connectToReceiver());
        chooseButton.addActionListener(e -> chooseFile());
        sendButton.addActionListener(e -> sendFile());
    }

    // Tạo button có style + hover
    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        // Hiệu ứng hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor.brighter());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });
        return button;
    }

    // Kết nối tới Receiver
    private void connectToReceiver() {
        try {
            receiverAddress = InetAddress.getByName(ipField.getText().trim());
            receiverPort = Integer.parseInt(portField.getText().trim());
            socket = new DatagramSocket();

            // Gửi CONNECT
            String connectMsg = "CONNECT";
            DatagramPacket connectPacket = new DatagramPacket(
                    connectMsg.getBytes(),
                    connectMsg.length(),
                    receiverAddress,
                    receiverPort
            );
            socket.send(connectPacket);
            statusArea.append("🔄 Đang kết nối tới Receiver...\n");

            // Lắng nghe CONNECT_ACK hoặc DISCONNECT
            new Thread(() -> {
                try {
                    byte[] buffer = new byte[1024];
                    DatagramPacket resp = new DatagramPacket(buffer, buffer.length);
                    socket.receive(resp);
                    String msg = new String(resp.getData(), 0, resp.getLength());
                    if (msg.equals("CONNECT_ACK")) {
                        SwingUtilities.invokeLater(() -> statusDot.setForeground(Color.GREEN));
                        statusArea.append("✅ Kết nối thành công với Receiver!\n");
                    } else if (msg.equals("DISCONNECT")) {
                        SwingUtilities.invokeLater(() -> statusDot.setForeground(Color.RED));
                        statusArea.append("🔴 Receiver đã ngắt kết nối.\n");
                    }
                } catch (Exception ex) {
                    statusArea.append("❌ Lỗi kết nối: " + ex.getMessage() + "\n");
                }
            }).start();

        } catch (Exception e) {
            statusArea.append("❌ Lỗi khi kết nối: " + e.getMessage() + "\n");
        }
    }

    // Chọn file
    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            long fileSize = selectedFile.length();
            statusArea.append("📂 Đã chọn tệp: " + selectedFile.getAbsolutePath() +
                    " (" + fileSize + " bytes)\n");
        }
    }

    // Gửi file
    private void sendFile() {
        if (socket == null || receiverAddress == null) {
            JOptionPane.showMessageDialog(this, "⚠️ Vui lòng kết nối với Receiver trước!");
            return;
        }
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "⚠️ Vui lòng chọn tệp trước khi gửi!");
            return;
        }

        new Thread(() -> {
            try {
                // Gửi tên file
                String fileNameMsg = "FILENAME:" + selectedFile.getName();
                DatagramPacket fileNamePacket = new DatagramPacket(
                        fileNameMsg.getBytes(),
                        fileNameMsg.length(),
                        receiverAddress,
                        receiverPort
                );
                socket.send(fileNamePacket);
                statusArea.append("📤 Đã gửi tên tệp: " + selectedFile.getName() + "\n");

                // Gửi nội dung file
                FileInputStream fis = new FileInputStream(selectedFile);
                byte[] buffer = new byte[60000];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    DatagramPacket packet = new DatagramPacket(buffer, bytesRead, receiverAddress, receiverPort);
                    socket.send(packet);
                }
                fis.close();

                // Gửi EOF
                String eofMsg = "EOF";
                DatagramPacket eofPacket = new DatagramPacket(
                        eofMsg.getBytes(),
                        eofMsg.length(),
                        receiverAddress,
                        receiverPort
                );
                socket.send(eofPacket);

                statusArea.append("✅ Gửi tệp thành công!\n");
            } catch (Exception e) {
                statusArea.append("❌ Lỗi khi gửi tệp: " + e.getMessage() + "\n");
                statusDot.setForeground(Color.RED);
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UDPFileSenderDashboard().setVisible(true));
    }
}
