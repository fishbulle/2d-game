package main;

import entity.Entity;
import object.Heart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class UI {

     GamePanel gp;
     Graphics2D g2;
     Font dogicaPixel, dogicaPixelBold;
     BufferedImage heartFull, heartHalf, heartEmpty;
     public boolean messageOn;
     ArrayList<String> message = new ArrayList<>();
     ArrayList<Integer> messageCounter = new ArrayList<>();
     public boolean gameOver;
     public String currentDialogue;
     public int commandNum = 0;
     public int titleScreenState = 0; // 0: the first screen, 1: the second screen etc
     public int slotCol = 0;
     public int slotRow = 0;

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

     public void addMessage(String text) {
         message.add(text);
         messageCounter.add(0);
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
             drawMessage();
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
         // CHARACTER STATE
         if (gp.gameState == gp.characterState) {
             drawCharacterScreen();
             drawInventory();
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

     public void drawMessage() {
         int messageX = gp.tileSize;
         int messageY = gp.tileSize * 4;
         g2.setFont(g2.getFont().deriveFont(20F));

         for (int i = 0; i < message.size(); i++) {
             if (message.get(i) != null) {
                 g2.setColor(Color.BLACK);
                 g2.drawString(message.get(i), messageX + 2, messageY + 2);
                 g2.setColor(Color.WHITE);
                 g2.drawString(message.get(i), messageX, messageY);
                 int counter = messageCounter.get(i) + 1;  // messageCounter++
                 messageCounter.set(i, counter);           // set the counter to the array
                 messageY += 50;

                 if (messageCounter.get(i) > 180) {  // 3 seconds
                     message.remove(i);
                     messageCounter.remove(i);
                 }
             }
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

     public void drawCharacterScreen() {
         // Create a frame
         final int frameX = gp.tileSize;
         final int frameY = gp.tileSize;
         final int frameWidth = gp.tileSize * 6;
         final int frameHeight = gp.tileSize * 12;

         drawWindow(frameX, frameY, frameWidth, frameHeight);

         // Text
         g2.setColor(Color.WHITE);
         g2.setFont(g2.getFont().deriveFont(20F));
         int textX = frameX + 20;
         int textY = frameY + gp.tileSize;
         final int lineHeight = 35;

         // Names
         g2.drawString("Level", textX, textY);
         textY += lineHeight;
         g2.drawString("Life", textX, textY);
         textY += lineHeight;
         g2.drawString("Strength", textX, textY);
         textY += lineHeight;
         g2.drawString("Dexterity", textX, textY);
         textY += lineHeight;
         g2.drawString("Attack", textX, textY);
         textY += lineHeight;
         g2.drawString("Defense", textX, textY);
         textY += lineHeight;
         g2.drawString("Speed", textX, textY);
         textY += lineHeight;
         g2.drawString("Exp", textX, textY);
         textY += lineHeight;
         g2.drawString("Next level", textX, textY);
         textY += lineHeight;
         g2.drawString("Coin", textX, textY);
         textY += lineHeight + 100;
         g2.drawString("Weapon", textX, textY);
         textY += lineHeight + 15;
         g2.drawString("Shield", textX, textY);
         textY += lineHeight;

         // Values
         int tailX = (frameX + frameWidth) - 25;
         textY = frameY + gp.tileSize;
         String value;

         value = String.valueOf(gp.player.level);
         textX = getXForAlignToRightText(value, tailX);
         g2.drawString(value, textX, textY);
         textY += lineHeight;

         value = gp.player.life + "/" + gp.player.maxLife;
         textX = getXForAlignToRightText(value, tailX);
         g2.drawString(value, textX, textY);
         textY += lineHeight;

         value = String.valueOf(gp.player.strength);
         textX = getXForAlignToRightText(value, tailX);
         g2.drawString(value, textX, textY);
         textY += lineHeight;

         value = String.valueOf(gp.player.dexterity);
         textX = getXForAlignToRightText(value, tailX);
         g2.drawString(value, textX, textY);
         textY += lineHeight;

         value = String.valueOf(gp.player.attack);
         textX = getXForAlignToRightText(value, tailX);
         g2.drawString(value, textX, textY);
         textY += lineHeight;

         value = String.valueOf(gp.player.defense);
         textX = getXForAlignToRightText(value, tailX);
         g2.drawString(value, textX, textY);
         textY += lineHeight;

         value = String.valueOf(gp.player.speed);
         textX = getXForAlignToRightText(value, tailX);
         g2.drawString(value, textX, textY);
         textY += lineHeight;

         value = String.valueOf(gp.player.exp);
         textX = getXForAlignToRightText(value, tailX);
         g2.drawString(value, textX, textY);
         textY += lineHeight;

         value = String.valueOf(gp.player.nextLevelExp);
         textX = getXForAlignToRightText(value, tailX);
         g2.drawString(value, textX, textY);
         textY += lineHeight;

         value = String.valueOf(gp.player.coin);
         textX = getXForAlignToRightText(value, tailX);
         g2.drawString(value, textX, textY);
         textY += lineHeight;

         g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY + 67, null);
         textY += gp.tileSize;
         g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY + 67, null);
     }

     public void drawInventory() {
         // Frame
         int frameX = gp.tileSize * 9;
         int frameY = gp.tileSize;
         int frameWidth = gp.tileSize * 8;
         int frameHeight = gp.tileSize * 5;
         drawWindow(frameX, frameY, frameWidth, frameHeight);

         // Slot
         final int slotXStart = frameX + 20;
         final int slotYStart = frameY + 20;
         int slotX = slotXStart;
         int slotY = slotYStart;
         int slotSize = gp.tileSize + 2;

         // Draw inventory items
         for (int i = 0; i < gp.player.inventory.size(); i++) {
             // Equip cursor
             if (gp.player.inventory.get(i) == gp.player.currentWeapon ||
                 gp.player.inventory.get(i) == gp.player.currentShield ||
                 gp.player.inventory.get(i) == gp.player.currentBoots) {
                 g2.setColor(new Color(240, 190, 90));
                 g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
             }

             g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);
             slotX += slotSize;

             if (i == 6 || i == 13 || i == 20) {
                 slotX = slotXStart;
                 slotY += slotSize;
             }
         }

         // Cursor
         int cursorX = slotXStart + (slotSize * slotCol);
         int cursorY = slotYStart + (slotSize * slotRow);
         int cursorWidth = gp.tileSize;
         int cursorHeight = gp.tileSize;
         g2.setColor(Color.WHITE);
         g2.setStroke(new BasicStroke(2));
         g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

         // Item description frame
         int dFrameY = (frameY + frameHeight) + 20;
         int dFrameHeight = gp.tileSize * 4;

         // Description text
         int textX = frameX + 20;
         int textY = dFrameY + gp.tileSize;
         g2.setFont(g2.getFont().deriveFont(18F));

         int itemIndex = getItemIndex();

         if (itemIndex < gp.player.inventory.size()) {
             drawWindow(frameX, dFrameY, frameWidth, dFrameHeight);
             for (String line: gp.player.inventory.get(itemIndex).description.split("\n")) {
                 g2.drawString(line, textX, textY);
                 textY += 32;
             }
         }
     }

     public int getItemIndex() {
         return slotCol + (slotRow * 7);
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

    public int getXForAlignToRightText(String text, int tailX) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return tailX - length;
    }
}
