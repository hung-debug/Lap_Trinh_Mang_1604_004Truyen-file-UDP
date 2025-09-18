package SenderFile;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPFileReceiverDashboard extends JFrame {
    private JTextField portField;
    private JTextArea statusArea;
    private JButton startButton, stopButton;
    private boolean running = false;
    private DatagramSocket socket;
    private Thread receiverThread;
    private JLabel statusDot; // chấm xanh/đỏ báo trạng thái

    private InetAddress senderAddress; // lưu Sender
    private int senderPort;

    public UDPFileReceiverDashboard() {
        setTitle("UDP File Receiver Dashboard");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel cấu hình
        JPanel configPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        configPanel.add(new JLabel("Receiver Port:"));
        portField = new JTextField("");
        configPanel.add(portField);

        String localIP = "Không xác định";
        try {
            localIP = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        configPanel.add(new JLabel("Receiver IP (máy này):"));
        configPanel.add(new JLabel(localIP));

        // Trạng thái chấm xanh/đỏ
        configPanel.add(new JLabel("Trạng thái kết nối:"));
        statusDot = new JLabel("●");
        statusDot.setForeground(Color.RED); // mặc định là đỏ (chưa kết nối)
        statusDot.setFont(new Font("Arial", Font.BOLD, 20));
        configPanel.add(statusDot);

        add(configPanel, BorderLayout.NORTH);

        // Khung log (to hơn)
        statusArea = new JTextArea();
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(statusArea);
        scrollPane.setPreferredSize(new Dimension(580, 300));
        add(scrollPane, BorderLayout.CENTER);

        // Nút bấm
        JPanel buttonPanel = new JPanel();
        startButton = createStyledButton("Start Receiver", new Color(0, 153, 0));
        stopButton = createStyledButton("Stop Receiver", new Color(204, 0, 0));
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        startButton.addActionListener(e -> startReceiver());
        stopButton.addActionListener(e -> stopReceiver());
    }

    // Hàm tạo JButton có hover
    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        // Hover effect
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

    private void startReceiver() {
        if (running) return;
        running = true;
        int port = Integer.parseInt(portField.getText());
        try {
            socket = new DatagramSocket(port);
            statusArea.append("Receiver đang chạy trên cổng: " + port + "\n");

            receiverThread = new Thread(() -> {
                try {
                    byte[] buffer = new byte[65535];
                    DatagramPacket packet;
                    FileOutputStream fos = null;
                    long receivedSize = 0;
                    String currentFileName = null;

                    while (running) {
                        packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);

                        String msg = new String(packet.getData(), 0, packet.getLength());

                        // ✅ Xử lý CONNECT
                        if (msg.equals("CONNECT")) {
                            senderAddress = packet.getAddress();
                            senderPort = packet.getPort();

                            String ack = "CONNECT_ACK";
                            byte[] ackData = ack.getBytes();
                            DatagramPacket ackPacket = new DatagramPacket(
                                    ackData, ackData.length,
                                    senderAddress, senderPort
                            );
                            socket.send(ackPacket);

                            statusArea.append("🔗 Sender connected: " + senderAddress + ":" + senderPort + "\n");
                            SwingUtilities.invokeLater(() -> statusDot.setForeground(Color.GREEN));
                            continue;
                        }

                        // ✅ Nhận file
                        if (msg.startsWith("FILENAME:")) {
                            currentFileName = msg.substring(9);
                            JFileChooser fileChooser = new JFileChooser();
                            fileChooser.setSelectedFile(new File(currentFileName));
                            int option = fileChooser.showSaveDialog(this);
                            if (option == JFileChooser.APPROVE_OPTION) {
                                fos = new FileOutputStream(fileChooser.getSelectedFile());
                                receivedSize = 0;
                                statusArea.append("📂 Nhận file: " + fileChooser.getSelectedFile() + "\n");
                            }
                        } else if (msg.equals("EOF")) {
                            if (fos != null) fos.close();

                            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
                            statusArea.append("📥 Nhận file hoàn tất.\n");
                            statusArea.append("   ➤ Tên file: " + currentFileName + "\n");
                            statusArea.append("   ➤ Dung lượng: " + receivedSize + " bytes\n");
                            statusArea.append("   ➤ Thời gian: " + date + "\n");
                        } else {
                            if (fos != null) {
                                fos.write(packet.getData(), 0, packet.getLength());
                                receivedSize += packet.getLength();
                            }
                        }
                    }
                } catch (Exception ex) {
                    statusArea.append("Lỗi nhận file: " + ex.getMessage() + "\n");
                }
            });
            receiverThread.start();
        } catch (Exception e) {
            statusArea.append("Không thể mở cổng: " + e.getMessage() + "\n");
        }
    }

    private void stopReceiver() {
        running = false;

        try {
            // gửi DISCONNECT về đúng Sender đã lưu
            if (senderAddress != null && senderPort > 0 && socket != null && !socket.isClosed()) {
                String disconnectMsg = "DISCONNECT";
                byte[] data = disconnectMsg.getBytes();
                DatagramPacket packet = new DatagramPacket(
                        data, data.length,
                        senderAddress, senderPort
                );
                socket.send(packet);
                statusArea.append("📴 Gửi DISCONNECT tới Sender: " + senderAddress + ":" + senderPort + "\n");
            }
        } catch (Exception ex) {
            statusArea.append("Lỗi khi gửi DISCONNECT: " + ex.getMessage() + "\n");
        }

        if (socket != null && !socket.isClosed()) socket.close();
        statusDot.setForeground(Color.RED); // khi stop thì chấm đỏ lại
        statusArea.append("Receiver đã dừng.\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UDPFileReceiverDashboard().setVisible(true));
    }
}
