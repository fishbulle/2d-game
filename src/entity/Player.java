package entity;

import main.GamePanel;
import main.KeyHandler;
import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity {

    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    public boolean attackCanceled;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 28;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);
        // SOLID AREA
        solidArea = new Rectangle(14, 32, 20, 16);  // adjust for managing sprite collision
        solidAreaDefaultX = 14;
        solidAreaDefaultY = 32;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImg();
        setItem();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 23;   // player's starting position on the world map
        worldY = gp.tileSize * 21;
        direction = "down";

        // PLAYER STATUS
        maxLife = 6;
        life = maxLife;
        level = 1;
        strength = 1;  // The greater strength, the greater damage
        dexterity = 1; // The more dexterity, the less damage received
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        currentWeapon = new BasicSword(gp);
        currentShield = new BasicShield(gp);
        currentBoots = new LeatherBoots(gp);
        attack = getAttack();   // The total attack value is decided by strength and weapon
        defense = getDefense(); // The total defense value is decided by dexterity and shield
        speed = getSpeed();
    }

    public void setItem() {
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(currentBoots);
    }
    public int getAttack() {
        attackArea = currentWeapon.attackArea;
        return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense() {
        return defense = dexterity * currentShield.defenseValue;
    }

    public int getSpeed() {
        return speed = currentBoots.speedValue;
    }

    public void getPlayerImage() {

        up1 = setUp("player/sprite.up1", gp.tileSize, gp.tileSize);
        up2 = setUp("player/sprite.up2", gp.tileSize, gp.tileSize);
        upStill = setUp("player/sprite.upstill", gp.tileSize, gp.tileSize);
        down1 = setUp("player/sprite.down1", gp.tileSize, gp.tileSize);
        down2 = setUp("player/sprite.down2", gp.tileSize, gp.tileSize);
        downStill = setUp("player/sprite.downstill", gp.tileSize, gp.tileSize);
        left1 = setUp("player/sprite.left1", gp.tileSize, gp.tileSize);
        left2 = setUp("player/sprite.left2", gp.tileSize, gp.tileSize);
        leftStill = setUp("player/sprite.leftstill", gp.tileSize, gp.tileSize);
        right1 = setUp("player/sprite.right1", gp.tileSize, gp.tileSize);
        right2 = setUp("player/sprite.right2", gp.tileSize, gp.tileSize);
        rightStill = setUp("player/sprite.rightstill", gp.tileSize, gp.tileSize);
    }

    public void getPlayerAttackImg() {

        if (currentWeapon.type == type_sword) {
            attackUp1 = setUp("player/attackup1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setUp("player/attackup2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setUp("player/attackdown1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setUp("player/attackdown2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setUp("player/attackleft1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setUp("player/attackleft2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setUp("player/attackright1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setUp("player/attackright2", gp.tileSize * 2, gp.tileSize);
        }

        if (currentWeapon.type == type_axe) {
            attackUp1 = setUp("player/axeup1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setUp("player/axeup2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setUp("player/axedown1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setUp("player/axedown2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setUp("player/axeleft1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setUp("player/axeleft2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setUp("player/axeright1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setUp("player/axeright2", gp.tileSize * 2, gp.tileSize);
        }
    }

    public void update() {

        if (attacking) {

            attacking();

        } else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed) {

            if (keyH.upPressed) {
                direction = "up";
            }
            if (keyH.downPressed) {
                direction = "down";
            }
            if (keyH.leftPressed) {
                direction = "left";
            }
            if (keyH.rightPressed) {
                direction = "right";
            }

            // CHECK FOR COLLISION WITH SOLID TILE
            collisionOn = false;
            gp.collisionCheck.checkTile(this); // "this" means this player class as entity

            // CHECK FOR COLLISION WITH OBJECT
            int objectIndex = gp.collisionCheck.checkObject(this, true);
            pickUpObject(objectIndex);

            // CHECK NPC COLLISION
            int npcIndex = gp.collisionCheck.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // CHECK MONSTER COLLISION
            int monsterIndex = gp.collisionCheck.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            // CHECK EVENT COLLISION
            gp.eHandler.checkEvent();

            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (!collisionOn && !keyH.enterPressed) {
                switch (direction) {
                    case "up" -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "left" -> worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }

            if (keyH.enterPressed && !attackCanceled) {
                gp.playSE(7);
                attacking = true;
                spriteCounter = 0;
            }

            attackCanceled = false;
            gp.keyH.enterPressed = false;

            spriteCounter++;
            if (spriteCounter > 12) {
                if (spriteNum == 1) {
                    spriteNum = 2;      // skipping 3 because that's the "resting" image
                }
                else if (spriteNum == 2) {
                    spriteNum = 1;      // skipping 3 because that's the "resting" image
                }
                else if (spriteNum == 3) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
        else {
            spriteNum = 3;  // resting image
        }

        // Needs to be outside of if statement! ^
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    public void attacking() {
        spriteCounter++;
        if (spriteCounter <= 5) {
            spriteNum = 1;
        }

        if (spriteCounter > 5 && spriteCounter <= 25) {
            spriteNum = 2;

            // Save the current worldX, worldY, solidArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // Adjust player's worldX/Y for the attackArea
            switch (direction) {
                case "up" -> worldY -= attackArea.height;
                case "down" -> worldY += attackArea.height;
                case "left" -> worldX -= attackArea.width;
                case "right" -> worldX += attackArea.width;
            }
            // Attack area becomes solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            // Check monster collision with the updated worldX, worldY and solidArea
            int monsterIndex = gp.collisionCheck.checkEntity(this, gp.monster);
            damageMonster(monsterIndex);

            // After checking collision, restore the original data
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }

        if (spriteCounter > 25) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void pickUpObject(int i) {
        if (i != 999) {
            String text;

            if (inventory.size() != maxInventorySize) {
                inventory.add(gp.obj[i]);
                gp.playSE(1);
                text = "You found a " + gp.obj[i].name + "!";
            }
            else {
                text = "Inventory full.";
            }
            gp.ui.addMessage(text);
            gp.obj[i] = null;
        }
    }

    public void interactNPC(int i) {
        if (gp.keyH.enterPressed) {
            if (i != 999) {
                attackCanceled = true;
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
        }
    }

    public void contactMonster(int i) {
        if (i != 999) {
            if (!invincible) {
                gp.playSE(6);
                int damage = gp.monster[i].attack - defense;
                if (damage < 0) {
                    damage = 0;
                }
                life -= damage;
                invincible = true;
            }
        }
    }

    public void damageMonster(int i) {
        if (i != 999) {
            if (!gp.monster[i].invincible) {
                gp.playSE(5);
                int damage = attack - gp.monster[i].defense;
                if (damage < 0) {
                    damage = 0;
                }
                gp.monster[i].life -= damage;
                gp.ui.addMessage(damage + " damage!");
                gp.monster[i].invincible = true;
                gp.monster[i].damageReaction();

                if (gp.monster[i].life <= 0) {
                    gp.monster[i].dying = true;
                    gp.ui.addMessage(gp.monster[i].name + " is dead!");
                    gp.ui.addMessage(gp.monster[i].exp + " EXP");
                    exp += gp.monster[i].exp;
                    checkLevelUp();
                }
            }
        }
    }

    public void checkLevelUp() {
        if (exp >= nextLevelExp) {
            level++;
            nextLevelExp = nextLevelExp * 2;
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();

            gp.playSE(8);
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "Level up! New level: " + level;
        }
    }

    public void selectItem() {
        int itemIndex = gp.ui.getItemIndex();

        if (itemIndex < inventory.size()) {
            Entity selectedItem = inventory.get(itemIndex);

            if (selectedItem.type == type_sword || selectedItem.type == type_axe) {
                currentWeapon = selectedItem;
                attack = getAttack();
                getPlayerAttackImg();
            }

            if (selectedItem.type == type_shield) {
                currentShield = selectedItem;
                defense = getDefense();
            }

            if (selectedItem.type == type_consumable) {
                selectedItem.use(this);
                inventory.remove(itemIndex);
            }
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch (direction) {
            case "up" -> {
                if (!attacking) {
                    if (spriteNum == 1) {
                        image = up1;
                    }
                    if (spriteNum == 2) {
                        image = up2;
                    }
                    if (spriteNum == 3) {
                        image = upStill;
                    }
                }
                if (attacking) {
                    tempScreenY = screenY - gp.tileSize;
                    if (spriteNum == 1)
                        image = attackUp1;
                    if (spriteNum == 2) {
                        image = attackUp2;
                    }
                }
            }
            case "down" -> {
                if (!attacking) {
                    if (spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2) {
                        image = down2;
                    }
                    if (spriteNum == 3) {
                        image = downStill;
                    }
                }
                if (attacking) {
                    if (spriteNum == 1)
                        image = attackDown1;
                    if (spriteNum == 2) {
                        image = attackDown2;
                    }
                }
            }
            case "left" -> {
                if (!attacking) {
                    if (spriteNum == 1) {
                        image = left1;
                    }
                    if (spriteNum == 2) {
                        image = left2;
                    }
                    if (spriteNum == 3) {
                        image = leftStill;
                    }
                }
                if (attacking) {
                    tempScreenX = screenX - gp.tileSize;
                    if (spriteNum == 1)
                        image = attackLeft1;
                    if (spriteNum == 2) {
                        image = attackLeft2;
                    }
                }
            }
            case "right" -> {
                if (!attacking) {
                    if (spriteNum == 1) {
                        image = right1;
                    }
                    if (spriteNum == 2) {
                        image = right2;
                    }
                    if (spriteNum == 3) {
                        image = rightStill;
                    }
                }
                if (attacking) {
                    if (spriteNum == 1)
                        image = attackRight1;
                    if (spriteNum == 2) {
                        image = attackRight2;
                    }
                }
            }
        }

        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f)); // sets opacity level
        }

        g2.drawImage(image, tempScreenX, tempScreenY,null);
        // Reset alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

    }

}
