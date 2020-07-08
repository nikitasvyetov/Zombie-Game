package com.geeselightning.zepr;
//      *Class for Assessment 4*

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class NPC extends Character {

    public NPC(Vector2 npcSpawn, World world){
        super(world);

        set(new Sprite(new Texture("npc.png")));

        health = Constant.NPCHEALTH;
        speed = Constant.NPCSPEED;
        maxhealth = Constant.NPCHEALTH;

        body.setFixedRotation(true);
        body.setLinearDamping(50f);
        setCharacterPosition(npcSpawn);

    }

    @Override
    public void update(float delta) {
        //move according to velocity
        super.update(delta);

         //npc wander randomly
        this.steeringBehavior = SteeringPresets.getWander(this);
        this.currentMode = SteeringState.WANDER;
            // update direction to face direction of travel
        direction = -(this.vectorToAngle(this.getLinearVelocity()));

    }

}
