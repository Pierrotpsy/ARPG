package ch.epfl.cs107.play.game.rpg.handler;

import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.collectables.ARPGCollectableAreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.immobile.Grass;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Sign;


public interface RPGInteractionVisitor extends AreaInteractionVisitor {

    /// Add Interaction method with all non Abstract Interactable

    /**
     * Simulate and interaction between RPG Interactor and a Door
     * @param door (Door), not null
     */
    default void interactWith(Door door){
        // by default the interaction is empty
    }
    
    /**
     * Simulate and interaction between RPG Interactor and a Grass
     * @param grass (Grass), not null
     */
    default void interactWith(Grass grass){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a Sign
     * @param sign (Sign), not null
     */
    default void interactWith(Sign sign){
        // by default the interaction is empty
    }


	default void interactWith(CollectableAreaEntity object) {
        // by default the interaction is empty
	}
}
