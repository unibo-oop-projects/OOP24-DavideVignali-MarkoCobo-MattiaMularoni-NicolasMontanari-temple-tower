package it.unibo.templetower.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CombatView {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(CombatView::new);
    }

    public CombatView() {
        JFrame frame = new JFrame("Combat Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        frame.setLayout(new BorderLayout());

        JPanel backgroundPanel = new JPanel() {
            private Image backgroundImage;

            {
                try {
                    backgroundImage = new ImageIcon("C:\\Users\\Nicolas\\Desktop\\UNIVERSITA\\ProgettoOOP\\temple-tower\\src\\main\\resources\\Images\\combat_room.jpg").getImage();
                } catch (Exception e) {
                    System.err.println("Error loading background image: " + e.getMessage());
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                    g.setColor(Color.WHITE);
                    g.drawString("Background image not found.", 10, 20);
                }
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JPanel characterPanel = new JPanel(null);
        characterPanel.setOpaque(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        backgroundPanel.add(characterPanel, gbc);

        JLabel playerIcon = new JLabel();
        JLabel enemyIcon = new JLabel();

        ImageIcon playerImage = new ImageIcon("C:\\Users\\Nicolas\\Desktop\\UNIVERSITA\\ProgettoOOP\\temple-tower\\src\\main\\resources\\Images\\player.png");
        ImageIcon enemyImage = new ImageIcon("C:\\Users\\Nicolas\\Desktop\\UNIVERSITA\\ProgettoOOP\\temple-tower\\src\\main\\resources\\Images\\enemy.png");

        characterPanel.add(playerIcon);
        characterPanel.add(enemyIcon);

        characterPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                // Calcolare le nuove dimensioni e posizioni ogni volta che il pannello viene ridimensionato
                int panelWidth = characterPanel.getWidth();
                int panelHeight = characterPanel.getHeight();
                int newIconSize = Math.min(panelWidth, panelHeight) / 4;

                playerIcon.setIcon(new ImageIcon(playerImage.getImage().getScaledInstance(newIconSize, newIconSize, Image.SCALE_SMOOTH)));
                enemyIcon.setIcon(new ImageIcon(enemyImage.getImage().getScaledInstance(newIconSize, newIconSize, Image.SCALE_SMOOTH)));

                int playerX = panelWidth / 4 - newIconSize / 2;
                int enemyX = 3 * panelWidth / 4 - newIconSize / 2;
                int y = panelHeight / 2 - newIconSize / 2;

                playerIcon.setBounds(playerX, y, newIconSize, newIconSize);
                enemyIcon.setBounds(enemyX, y, newIconSize, newIconSize);
            }
        });

        JPanel healthBarPanel = new JPanel();
        healthBarPanel.setLayout(new GridLayout(1, 2, 20, 0));
        healthBarPanel.setOpaque(false);

        JProgressBar playerHealthBar = new JProgressBar(0, 100);
        playerHealthBar.setValue(100);
        playerHealthBar.setStringPainted(true);

        JProgressBar enemyHealthBar = new JProgressBar(0, 100);
        enemyHealthBar.setValue(100);
        enemyHealthBar.setStringPainted(true);

        healthBarPanel.add(playerHealthBar);
        healthBarPanel.add(enemyHealthBar);

        JButton attackButton = new JButton("Attack!");

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(healthBarPanel, BorderLayout.CENTER);
        bottomPanel.add(attackButton, BorderLayout.SOUTH);

        backgroundPanel.setPreferredSize(new Dimension(800, 500));

        frame.add(backgroundPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        attackButton.addActionListener(new ActionListener() {
            int playerHealth = 100;
            int enemyHealth = 100;

            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    int playerStartX = playerIcon.getX();
                    int enemyX = enemyIcon.getX();
                    int targetX = enemyX - playerIcon.getWidth() / 2; // Sovrapporre il player leggermente all'enemy

                    for (int i = playerStartX; i <= targetX; i += 10) {
                        playerIcon.setLocation(i, playerIcon.getY());
                        try {
                            Thread.sleep(10); // Ridurre il tempo di pausa per velocizzare il movimento
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }

                    enemyHealth -= 15;
                    if (enemyHealth <= 0) {
                        enemyHealth = 0;
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame, "Enemy defeated!"));
                        attackButton.setEnabled(false);
                    }
                    enemyHealthBar.setValue(enemyHealth);

                    for (int i = targetX; i >= playerStartX; i -= 10) {
                        playerIcon.setLocation(i, playerIcon.getY());
                        try {
                            Thread.sleep(10); // Ridurre il tempo di pausa per velocizzare il movimento
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }

                    playerHealth -= 10;
                    if (playerHealth <= 0) {
                        playerHealth = 0;
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame, "Player defeated!"));
                        attackButton.setEnabled(false);
                    }
                    playerHealthBar.setValue(playerHealth);
                }).start();
            }
        });

        frame.setVisible(true);
    }
}













