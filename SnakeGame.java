/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplication6;

/**
 *
 * @author smon
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final int UNIT_SIZE = 20;
    private static final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private final LinkedList<Point> snake = new LinkedList<>();
    private int appleX, appleY;
    private int snakeLength = 3;
    private int direction = KeyEvent.VK_RIGHT;
    private boolean running = false;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        snake.clear();
        snake.add(new Point(0, 0));
        generateApple();
        running = true;
        Timer timer = new Timer(100, this);
        timer.start();
    }

    public void generateApple() {
        Random random = new Random();
        appleX = random.nextInt((WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
            checkApple();
            repaint();
        }
    }

    public void move() {
        int headX = snake.getFirst().x;
        int headY = snake.getFirst().y;
        int newHeadX = headX;
        int newHeadY = headY;

        switch (direction) {
            case KeyEvent.VK_UP:
                newHeadY = headY - UNIT_SIZE;
                break;
            case KeyEvent.VK_DOWN:
                newHeadY = headY + UNIT_SIZE;
                break;
            case KeyEvent.VK_LEFT:
                newHeadX = headX - UNIT_SIZE;
                break;
            case KeyEvent.VK_RIGHT:
                newHeadX = headX + UNIT_SIZE;
                break;
        }

        snake.addFirst(new Point(newHeadX, newHeadY));

        if (snake.size() > snakeLength) {
            snake.removeLast();
        }
    }

    public void checkCollision() {
        int headX = snake.getFirst().x;
        int headY = snake.getFirst().y;

        // Check wall collision
        if (headX < 0 || headY < 0 || headX >= WIDTH || headY >= HEIGHT) {
            running = false;
        }

        // Check self collision
        for (Point point : snake.subList(1, snake.size())) {
            if (point.equals(snake.getFirst())) {
                running = false;
                break;
            }
        }
    }

    public void checkApple() {
        if (snake.getFirst().x == appleX && snake.getFirst().y == appleY) {
            snakeLength++;
            generateApple();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.red);
            g.fillRect(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            g.setColor(Color.green);
            for (Point point : snake) {
                g.fillRect(point.x, point.y, UNIT_SIZE, UNIT_SIZE);
            }
        } else {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if ((key == KeyEvent.VK_LEFT) && (direction != KeyEvent.VK_RIGHT)) {
                direction = KeyEvent.VK_LEFT;
            }
            if ((key == KeyEvent.VK_RIGHT) && (direction != KeyEvent.VK_LEFT)) {
                direction = KeyEvent.VK_RIGHT;
            }
            if ((key == KeyEvent.VK_UP) && (direction != KeyEvent.VK_DOWN)) {
                direction = KeyEvent.VK_UP;
            }
            if ((key == KeyEvent.VK_DOWN) && (direction != KeyEvent.VK_UP)) {
                direction = KeyEvent.VK_DOWN;
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
