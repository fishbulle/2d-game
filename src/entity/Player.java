package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/sprite.up1.png"));
            up2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/sprite.up2.png"));
            upStill = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/sprite.upstill.png"));
            down1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/sprite.down1.png"));
            down2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/sprite.down2.png"));
            downStill = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/sprite.downstill.png"));
            left1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/sprite.left1.png"));
            left2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/sprite.left2.png"));
            leftStill = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/sprite.leftstill.png"));
            right1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/sprite.right1.png"));
            right2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/sprite.right2.png"));
            rightStill = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/sprite.rightstill.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update () {

        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

            if (keyH.upPressed) {
                direction = "up";
                y -= speed;
            }
            if (keyH.downPressed) {
                direction = "down";
                y += speed;
            }
            if (keyH.leftPressed) {
                direction = "left";
                x -= speed;
            }
            if (keyH.rightPressed) {
                direction = "right";
                x += speed;
            }

            spriteCounter++;
            if (spriteCounter > 12) {
                if (spriteNum == 1) {
                    spriteNum = 3;
                }
                else if (spriteNum == 2) {
                    spriteNum = 3;
                }
                else if (spriteNum == 3) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
        else {
            spriteNum = 2;
        }
    }

    public void draw(Graphics g2) {
//        g2.setColor(Color.WHITE);
//        g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        BufferedImage image = null;

        switch (direction) {
            case "up" -> {
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = upStill;
                }
                if (spriteNum == 3) {
                    image = up2;
                }
            }
            case "down" -> {
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = downStill;
                }
                if (spriteNum == 3) {
                    image = down2;
                }
            }
            case "left" -> {
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = leftStill;
                }
                if (spriteNum == 3) {
                    image = left2;
                }
            }
            case "right" -> {
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = rightStill;
                }
                if (spriteNum == 3) {
                    image = right2;
                }
            }
        }

        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);

    }

}