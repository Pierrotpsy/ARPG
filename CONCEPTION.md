# ARPG

les éventuelles modifications personnelles que vous avez apportées à l'architecture proposée en les justifiant;
les classes/interfaces ajoutées et comment elles s'insèrent dans l'architecture;
le comportement que vous attribuez à chacun des composants introduits (si le composant n'est pas demandé ou s'il l'est mais que son comportement est une petite variante de celui suggéré dans l'énoncé).

##Modifications :
####The following modifications were made for simplicity and ease of use :
- Added new methods in DiscreteCoordinates to get neighbours in a certain radius, a certain number of coordinates in front of a coordinate, and all the coordinates in an arena. 
- Added new methods in Animation to create a single animation and not 4 of them.
- Added new methods in RPGSprites to extract sprites without having 4 versions of a character for each direction, simply works by entering if your sprites are vertical or horizontal.

##Added classes and interfaces :
 - CollectableAreaEntity
 - FlyableEntity
 - ARPG
 - ARPGBehavior
 - ARPGPlayerGUI
 - Test
 - ARPGInventory
 - ARPGItem
 - ARPGPlayer
 - ARPGCollectableAreaEntity
 - Bomb
 - Bow
 - CastleKey
 - Coin
 - Heart
 - Staff
 - Bombs
 - Bridge
 - CastleDoor
 - CaveDoor
 - Grass
 - Rock
 - ARPGMobs
 - Bomber
 - DarkLord
 - FireSpell
 - FlameSkull
 - LogMonster
 - Projectile
 - Arrow
 - MagicWaterProjectile
 - ARPGArea
 - Castle
 - CastleRoad
 - Cave
 - Farm
 - Road
 - Spawnable
 - Temple
 - TempleRoad
 - Village
 - ARPGInteractionVisitor
 - Inventory
 - InventoryItem


##Extensions :
- HP Bar instead of hearts for player. The mobs also have hp bars.
- Some Areas have been made spawnable, which makes random monsters appear at a certain rate in those areas.
-  An inventory has been added, which can be accessed by pressing Q.
- Rocks have been added, they can only be destroyed by a bomb.
- A bridge has been added, linking the Road to the TempleRoad.
- Three zones have been added to the game, the TempleRoad, the Temple and the Cave.
- Unique collectable objects have been added in the temple (staff) and in the cave(bow and arrow).
- The Cave is accessed via a CaveDoor, which can only be opened by detonating a bomb near it.
- A new type of mob, the Bomber, has been added. It is immune to bomb explosions, and spawns bombs randomly, these bombs explode quicker than the player's.

 

