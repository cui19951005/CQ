package com.cq.sdk.test;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by admin on 2016/10/21.
 */
public class Window extends JFrame {
    private Game game;
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        this.game.draw(g);
    }
    public Window() throws HeadlessException {
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()){
                    case 37://左
                        Window.this.game.operation(0);
                        break;
                    case 38://上
                        Window.this.game.operation(2);
                        break;
                    case 39://右
                        Window.this.game.operation(1);
                        break;
                    case 40://下
                        Window.this.game.operation(3);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
