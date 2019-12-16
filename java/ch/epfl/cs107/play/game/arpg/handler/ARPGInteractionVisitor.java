package ch.epfl.cs107.play.game.arpg.handler;

import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.arpg.ARPGBehavior.ARPGCell;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.Bombs;
import ch.epfl.cs107.play.game.arpg.actor.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.collectables.ARPGCollectableAreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.collectables.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Coin;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Heart;
import ch.epfl.cs107.play.game.arpg.actor.mobs.ARPGMobs;
import ch.epfl.cs107.play.game.arpg.actor.mobs.FireSpell;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;

public interface ARPGInteractionVisitor extends RPGInteractionVisitor {
	default void interactWith(ARPGCell cell){
        // by default the interaction is empty
    }
	
	default void interactWith(ARPGPlayer player){
        // by default the interaction is empty
    }

	default void interactWith(Bombs bomb) {
        // by default the interaction is empty
	}
	
	default void interactWith(Coin coin){
        // by default the interaction is empty
    }
	
	default void interactWith(Heart heart){
        // by default the interaction is empty
    }
	
	default void interactWith(CastleKey key) {
		
	}
	
	default void interactWith(ARPGMobs mob) {
		
	}

	default void interactWith(FireSpell spell) {
		
	}

	default void interactWith(CastleDoor door) {
		
	}
}
