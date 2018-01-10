# Thunderslash
2D sidescroller game using Java and custom 2D-engine.

![thunderslashv01a](https://user-images.githubusercontent.com/7894317/34798766-fc588904-f665-11e7-8cd2-302b44f3e180.png)

## Introduction
Thunderslash is a 2D action sidescroller game, that is mainly developed as a programming excersise. 
The aim is to create a slower paced sidescrolling action game (e.g. Castlevania), which main features are 
listed below.

## Background 
Initial: ~~The name of the game comes from an initial idea, in which the
player can call lightning strikes (main attack) from sky in front of him, but leaving a small gap between the hit area and player.
This means that the player can't damage enemies that are next to him. Therefore the player has another action: push. 
With this action the player can push enemies next to him further away and then use main attack to damage them.
This game mechanic is inspired by F2P MMORPG Dofus' Iop class spells: Intimidation and Strengthstorm.~~

11.01.2018: This is probably going to change since the minimum range lightning strike mechanic wasn't fun. 
Perhaps the main character could throw ranged, horizontal lightning hammers or something similar.  

## Features
The game uses custom iteratively developed 2D-engine and this project presents multiple new features to it.
The new features are so far (11.01.2018):
* tile based sprite system
* engine creates automatically hitboxes for different sized sprites
* Physics: simple physics (e.g. gravity)
* collision detection with static environment
* camera: smooth follow and optimized rendering
* rendering queues to separate z-levels
* image file reader: encode pixels with data
* tinting and highlighting sprites

## TODO
The **major** features are:
* Actor collisions with physics objects
* implementing support for fore- and background sprites 
* diverse enemies: hopping slimes, walking skeletons, charging tanks, ranged enemies etc.
* tweaking gameplay feeling: screenshake, precise animations, tight controls, persistent objects etc.
* actions correspond the animation frame (e.g. jump and attack only on a certain frame of the animation)
* ~~GUI-elements: images, buttons, strings~~
* ~~main game mechanics: attacking and pushing~~
* ~~enemies: Basic enemy with basic AI~~
* ~~physics support for actors (can be pushed)~~
* ~~physics support for other gameobjects~~
* ~~enemy collision with player~~
* ~~enemy spawnpoint support in level editor~~

minor features:
* tileset support (changeable tilesets between levels)
* graphics (pixel art, 32x32 sprites): multiple tilesets, background, parallax effect
* pickupable items: coins, health, powerups etc.
* particle effect support: emitters/particles
* controller support
* ~~mouse and keyboard support~~
* ~~rebindable keys support~~
* ~~level hazard support (traps, spikes etc.)~~
* ~~animation support~~
* ~~sprite animations for the player: walk, fall, attack, cast, idle~~
* ~~GUI: hp/score~~
* ~~usable items: crystal~~

low priority features:
* friendly NPCs
* text dialogs: conversations + help signs
* resizeable window: currently 720p to ease the development
* ~~simple mainmenu~~

## Contribution
Heikki Heiskanen - design & implementation
