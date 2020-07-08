package com.geeselightning.zepr.tests;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.geeselightning.zepr.Constant;
import com.geeselightning.zepr.Player;
import com.geeselightning.zepr.Zombie;
import com.geeselightning.zepr.screens.LoadingScreen;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class PlayerTest {

    @Test
    // Test 2.1
    public void playerPositionResetsWhenRespawned() {
        World world = new World(new Vector2(0, 0), true);
        Player player = new Player(new Texture("player01.png"), new Vector2(0, 0), world);
        Vector2 originalPosition = new Vector2(player.getX(), player.getY());
        player.setCharacterPosition(new Vector2(10, 10));
        player.respawn(new Vector2(0, 0));
        assertEquals("Position should reset when the player is respawned.", originalPosition, new Vector2(player.getX(), player.getY()));
        player.dispose();
        world.dispose();
    }

    @Test
    // Test 2.2
    public void playerDoesNoDamageToZombieWhenAtMaxRange() {
        World world = new World(new Vector2(0, 0), true);
        Player player = new Player(new Texture("player01.png"), new Vector2(0, 0), world);

        Zombie zombie = new Zombie(new Vector2(0, Constant.PLAYERRANGE), world, Zombie.Type.ZOMBIE1);
        double originalHealth = zombie.getHealth();
        player.attack(zombie, 0);

        assertEquals("Zombie on the edge of range should not take damage when the player attacks.",
                zombie.getHealth(), originalHealth, 0.1);
        player.dispose();
        world.dispose();
    }

    @Test
    // Test 2.3
    public void playerDoesDamageToZombieWhenInRange() {
        World world = new World(new Vector2(0, 0), true);
        Player player = new Player(new Texture("player01.png"), new Vector2(0, 0), world);

        //Load sounds played during attack
        LoadingScreen.loadSounds();

        Zombie zombie = new Zombie(new Vector2(0, Constant.PLAYERRANGE-1), world, Zombie.Type.ZOMBIE1);
        double originalHealth = zombie.getHealth();
        player.setAttacking(true);
        player.attack(zombie, 0);

        assertNotEquals("Zombie within range should take damage when the player attacks.",
                zombie.getHealth(), originalHealth, 0.1);
        player.dispose();
        world.dispose();
    }

    @Test
    //Test 2.4
    public void playerDoesNoDamageToZombieOutOfRange() {
        World world = new World(new Vector2(0, 0), true);
        Player player = new Player(new Texture("player01.png"), new Vector2(0, 0), world);

        Zombie zombie = new Zombie(new Vector2(player.getCenter().x, player.getCenter().y +100), world, Zombie.Type.ZOMBIE1);
        double originalHealth = zombie.getHealth();
        player.attack(zombie, 0);

        assertEquals("Zombie outside of range should not take damage when the player attacks.",
                zombie.getHealth(), originalHealth, 0.1);
        player.dispose();
        world.dispose();
    }

    @Test
    // Test 2.5
    public void playerTypesNerdySportyHaveDifferentHealth() {
        World world = new World(new Vector2(0, 0), true);

        Player player = new Player(new Texture("player01.png"), new Vector2(0, 0), world);
        Player.setType(Player.PlayerType.NERDY);
        player.refreshAttributes();
        player.respawn(Constant.ORIGIN);
        double nerdyHealth = player.getHealth();

        Player.setType(Player.PlayerType.SPORTY);
        player.refreshAttributes();
        player.respawn(Constant.ORIGIN);
        assertNotEquals("Sporty and nerdy students should have a different number of hit points.",
                nerdyHealth, player.getHealth());
        player.dispose();
        world.dispose();
    }

    @Test
    // Test 2.6
    public void playerTypesNerdySportyHaveDifferentSpeed() {
        World world = new World(new Vector2(0, 0), true);
        Player player = new Player(new Texture("player01.png"), new Vector2(0, 0), world);
        Player.setType(Player.PlayerType.NERDY);
        player.respawn(Constant.ORIGIN);
        double nerdySpeed = player.getSpeed();
        Player.setType(Player.PlayerType.SPORTY);
        player.respawn(Constant.ORIGIN);
        assertNotEquals("Sporty and nerdy students should have a different speed.",
                nerdySpeed, player.getSpeed());
        player.dispose();
        world.dispose();
    }

    // All tests below added for assessment 4

    @Test
    // Test 2.7
    public void playerTypesArtsySportyHaveDifferentSpeed(){
        World world = new World(new Vector2(0, 0), true);
        Player player = new Player(new Texture("player01.png"), new Vector2(0, 0), world);
        Player.setType(Player.PlayerType.ARTSY);
        player.respawn(Constant.ORIGIN);
        double artsySpeed = player.getSpeed();

        Player.setType(Player.PlayerType.SPORTY);
        player.respawn(Constant.ORIGIN);
        assertNotEquals("Artsy and nerdy students should have a different speed.",
                artsySpeed, player.getSpeed());
        player.dispose();
        world.dispose();
    }

    @Test
    // Test 2.8
    public void playerTypesArtsySportyHaveDifferentHealth(){
        World world = new World(new Vector2(0, 0), true);
        Player player = new Player(new Texture("player01.png"), new Vector2(0, 0), world);
        Player.setType(Player.PlayerType.ARTSY);
        player.respawn(Constant.ORIGIN);
        double artsySpeed = player.getHealth();

        Player.setType(Player.PlayerType.SPORTY);
        player.respawn(Constant.ORIGIN);
        assertNotEquals("Artsy and nerdy students should have a different speed.",
                artsySpeed, player.getHealth());
        player.dispose();
        world.dispose();
    }

    @Test
    // Test 2.9
    public void zombieModeEnabledWhenPlayerInfected() {
        World world = new World(new Vector2(0, 0), true);
        Player player = new Player(new Texture("player01.png"), new Vector2(0, 0), world);
        player.infect();
        assertEquals("zombieMode should be true after the player is infected.", true, player.getZombieMode());
    }

    @Test
    // Test 2.10
    public void zombieModeDisabledWhenPlayerCured() {
        World world = new World(new Vector2(0, 0), true);
        Player player = new Player(new Texture("player01.png"), new Vector2(0, 0), world);
        player.cure();
        assertEquals("zombieMode should be true after the player is infected.", false, player.getZombieMode());
    }

}