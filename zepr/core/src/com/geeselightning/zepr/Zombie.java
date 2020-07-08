package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class Zombie extends Character {

    private int hitRange;

    public enum Type {ZOMBIE1, ZOMBIE2, ZOMBIE3, BOSS1, BOSS2}

    private static ArrayList<NPC> NPCs;
    private double smallestDistance;
    private Character closeTarget;

    /**
     * Constructor for the Zombie class
     *
     * @param zombieSpawn the coordinates to spawn the zombie at
     * @param world       the Box2D world to add the zombie to
     * @param type        the type of zombie to spawn
     *                    #changed:  Added assignment of different values for different zombie types.
     *                    Hitrange now scales with sprite size. Box2D body code added.
     */
    public Zombie(Vector2 zombieSpawn, World world, Type type) {
        super(world);

        speed = Constant.ZOMBIESPEED;
        attackDamage = Constant.ZOMBIEDMG;
        maxhealth = Constant.ZOMBIEMAXHP;

        switch (type) {
            case ZOMBIE1:
                speed *= 1;
                attackDamage *= 1;
                maxhealth *= 1;
                set(new Sprite(new Texture("zombie01.png")));
                break;
            case ZOMBIE2:
                speed *= 1.2f;
                attackDamage *= 2;
                maxhealth *= 2;
                set(new Sprite(new Texture("zombie02.png")));
                break;
            case ZOMBIE3:
                speed *= 2;
                attackDamage *= 3;
                maxhealth *= 1;
                set(new Sprite(new Texture("zombie03.png")));
                break;
            case BOSS1:
                speed *= 100;
                attackDamage *= 2;
                maxhealth *= 5;
                set(new Sprite(new Texture("GeeseLightningBoss.png")));
                break;
            case BOSS2:
                speed *= 60;
                attackDamage *= 1;
                maxhealth *= 5;
                set(new Sprite(new Texture("JJBossZombie.png")));
                break;
        }

        health = maxhealth;

        body.setFixedRotation(true);
        body.setLinearDamping(50f);
        setCharacterPosition(zombieSpawn);

        hitRange = (int) (Constant.ZOMBIERANGE * getWidth() / 30 - getWidth() * getHealth() / 1200);
    }

    /**
     * Attack and damage the player if in range and hit counter refreshed
     *
     * @param player instance of Player class to attack
     * @param delta  the time between the start of the previous call and now
     */
    public void attack(Player player, float delta) {
        if (canHitGlobal(player, hitRange) && hitRefresh > Constant.ZOMBIEHITCOOLDOWN) {
            player.takeDamage(attackDamage);
            hitRefresh = 0;
        } else
            hitRefresh += delta;
    }

    public void attackNPC(NPC npc, float delta) {
        if (canHitGlobal(npc, hitRange) && hitRefresh > Constant.ZOMBIEHITCOOLDOWN) {
            npc.takeDamage(attackDamage);
            hitRefresh = 0;
        } else
            hitRefresh += delta;
    }

    //      *Code for Assessment 4*
    public Character changeTargeting() {
        ArrayList<NPC> NPCs = Level.getAliveNPC();
        smallestDistance = findDistance(body.getLocalCenter(), Level.getPlayer().getCenter());
        closeTarget = Level.getPlayer();
        for (int i = 0; i < NPCs.size(); i++) {
            NPC npc = NPCs.get(i);
            if (findDistance(body.getLocalCenter(), npc.getCenter()) < smallestDistance){
                smallestDistance = findDistance(body.getLocalCenter(), npc.getCenter());
                closeTarget = npc;
            }
        }
        return closeTarget;
    }

    //      *Code for Assessment 4*
    public double findDistance(Vector2 obj1, Vector2 obj2) {
        return Math.sqrt(Math.pow((obj2.x - obj1.x), 2) + Math.pow((obj2.y - obj1.y), 2));
    }


    /**
     * Method to update positional and action behavior
     * @param delta the time between the start of the previous call and now
     * #changed:  Code to remove from aliveZombies list when dead now moved to Level
     *            Added LibGDX AI steering behaviour and wandering when player undetected.
     */
    @Override
    public void update(float delta) {
        //move according to velocity
        super.update(delta);

        if (Level.getPlayer().isVisible()) {
            // seek out player using gdx-ai seek functionality
            this.steeringBehavior = SteeringPresets.getSeek(this, changeTargeting());
            this.currentMode = SteeringState.SEEK;
            // update direction to face the player
            direction = getDirectionTo(changeTargeting().getCenter());
        } else { //player cannot be seen, so wander randomly
            this.steeringBehavior = SteeringPresets.getWander(this);
            this.currentMode = SteeringState.WANDER;
            // update direction to face direction of travel
            direction = -(this.vectorToAngle(this.getLinearVelocity()));

        }

    }
}
