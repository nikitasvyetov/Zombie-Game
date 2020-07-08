package com.geeselightning.zepr.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.geeselightning.zepr.Constant;
import com.geeselightning.zepr.Level;
import com.geeselightning.zepr.Player;

public class PowerUpCure extends PowerUp {

    // Class added for assessment 4

    public PowerUpCure(Level currentLevel, Player player){
        super(new Texture("cure.png"), currentLevel, player, 0, "Cure PowerUp collected");
    }

    @Override
    public void activate(){
        super.activate();
        player.cure();
        currentLevel.cureZombies(player.getCenter(), Constant.CURERANGE);
    }
}

