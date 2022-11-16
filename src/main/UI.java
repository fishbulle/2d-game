package main;

import entity.Entity;
import object.Heart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class UI {

     GamePanel gp;
     Graphics2D g2;
     Font dogicaPixel, dogicaPixelBold;
     BufferedImage heartFull, heartHalf, heartEmpty;
     public boolean messageOn;
     public String message;
     int messageCounter = 0;
     public boolean gameOver;
     public String currentDialogue;
     public int commandNum = 0;
     public int titleScreenState = 0; // 0: the first screen, 1: the second screen etc

     public UI(GamePanel gp) {
         this.gp = gp;

         try {
             InputStream inputStream = getClass().getClassLoader().getResourceAsStream("fonts/dogicapixel.ttf");
             dogicaPixel = Font.createFont(Font.TRUETYPE_FONT, inputStream);

             inputStream = getClass().getClassLoader().getResourceAsStream("fonts/dogicapixelbold.ttf");
             dogicaPixelBold = Font.createFont(Font.TRUETYPE_FONT, inputStream);
         } catch (FontFormatException | IOException e) {
             e.printStackTrace();
         }

         // CREATE HUD OBJECT
         Entity heart = new Heart(gp);
         heartFull = heart.image;
         heartHalf = heart.image2;
         heartEmpty = heart.image3;
     }

     public void showMessage(String text) {
         message = text;
         messageOn = true;
     }

     public void draw(Graphics2D g2) {

         this.g2 = g2;
         g2.setFont(dogicaPixel);
         g2.setColor(Color.WHITE);

         // TITLE STATE
         if (gp.gameState == gp.titleState) {
             drawTitleScreen();
         }
         // PLAY STATE
         if (gp.gameState == gp.playState) {
             drawPlayerLife();
         }
         // PAUSE STATE
         if (gp.gameState == gp.pauseState) {
             drawPlayerLife();
             drawPauseScreen();
         }
         // DIALOGUE STATE
         if (gp.gameState == gp.dialogueState) {
             drawPlayerLife();
             drawDialogueScreen();
         }
     }

     public void drawPlayerLife() {
         int x = gp.tileSize / 2;
         int y = gp.tileSize / 2;
         int i = 0;
         // 2 lives = 1 full heart

         // DRAW MAX LIFE
         while (i < gp.player.maxLife / 2) {
             g2.drawImage(heartEmpty, x, y, null);
             i++;
             x += gp.tileSize;
         }

         // RESET VALUES
         x = gp.tileSize / 2;
         y = gp.tileSize / 2;
         i = 0;

         // DRAW CURRENT LIFE
         while (i < gp.player.life) {
             g2.drawImage(heartHalf, x, y, null);
             i++;
             if (i < gp.player.life) {
                 g2.drawImage(heartFull, x, y, null);
             }
             i++;
             x += gp.tileSize;
         }

     }

    public void drawTitleScreen() {
        Random random = new Random();
        int A = random.nextInt(255);
        int B = random.nextInt(255);
        int C = random.nextInt(255);

        if (titleScreenState == 0) {

            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

            // GAME TITLE
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50F));
            String text = "Enchanted Forest";
            int x = getXForCenteredText(text);
            int y = gp.tileSize * 3;

            // SHADOW
            g2.setColor(Color.white);
            g2.drawString(text, x, y);
            // MAIN TEXT
            g2.setColor(new Color(A, B, C));
            g2.drawString(text, x + 3, y + 3);

            // PLAYER IMAGE
            x = gp.screenWidth / 2 - (gp.tileSize * 2) / 2;
            y += gp.tileSize * 2;
            g2.drawImage(gp.player.downStill, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

            // MENU
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25F));
            g2.setColor(Color.white);

            text = "NEW GAME";
            x = getXForCenteredText(text);
            y += gp.tileSize * 4;
            g2.drawString(text, x, y);
            if (commandNum == 0) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25F));
            text = "LOAD GAME";
            x = getXForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 1) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25F));
            text = "QUIT GAME";
            x = getXForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 2) {
                g2.drawString(">", x - gp.tileSize, y);
            }
        } else if (titleScreenState == 1) {
            // CLASS SELECTION SCREEN
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25F));
            String text = "Select your class!";
            int x = getXForCenteredText(text);
            int y = gp.tileSize * 3;
            g2.drawString(text, x, y);

            text = "Mage";
            x = getXForCenteredText(text);
            y += gp.tileSize * 3;
            g2.drawString(text, x, y);

            if (commandNum == 0) {
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "Fighter";
            x = getXForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);

            if (commandNum == 1) {
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "Hunter";
            x = getXForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);

            if (commandNum == 2) {
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "Back";
            x = getXForCenteredText(text);
            y += gp.tileSize * 2;
            g2.drawString(text, x, y);

            if (commandNum == 3) {
                g2.drawString(">", x-gp.tileSize, y);
            }
        }
    }

     public void drawPauseScreen() {
         String text = "GAME PAUSED";
         int x = gp.screenWidth / 4;
         int y = gp.screenHeight / 2;
         g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 50F));
         g2.drawString(text, x, y);
     }

     public void drawDialogueScreen() {
         // Dialogue window
         int x = gp.tileSize * 2;
         int y = gp.tileSize / 2;
         int width = gp.screenWidth - (gp.tileSize * 4);
         int height = gp.tileSize * 4;
         drawWindow(x, y, width, height);

         g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 14F));
         x += gp.tileSize;
         y += gp.tileSize;

         for (String line : currentDialogue.split("\n")) {
             g2.drawString(line, x, y);
             y += 40;
         }
     }

     public void drawWindow(int x, int y, int width, int height) {
         Color c = new Color(0, 0, 0, 210); // last number is alpha value (adjusts opacity)
         g2.setColor(c);
         g2.fillRoundRect(x, y, width, height, 35, 35);

         c = new Color(255, 255, 255);
         g2.setColor(c);
         g2.setStroke(new BasicStroke(5));
         g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
     }

     public int getXForCenteredText(String text) {
         int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
         return (gp.screenWidth / 2) - (length / 2);
     }
}
