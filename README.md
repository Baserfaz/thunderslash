# Thunderslash
2D sidescroller game using Java and custom 2D engine.

## Introduction
This is a 2D action sidescroller project, that is mainly developed as a programming excersice. 
The aim is to create a slower paced sidescrolling action game (e.g. Castlevania). 

## Background 
The name of the game comes from an initial idea, in which the
player can call lightning strikes (main attack) from sky in front of him, but leaving a small gap between the hit area and player.
This means that the player can't damage enemies that are next to him. Therefore the player has another action: push. 
With this action the player can push enemies next to him further away and then use main attack to damage them.
This game mechanic is inspired by F2P MMORPG Dofus' Iop class spells: Intimidation and Strengthstorm.

## Features
The game uses custom 2D engine and this project presents multiple new features. 
These features are so far (27.12.2017):
* 9-slice sprite support (chooses correct sprite by using neighbor data)
* Physics: simple gravity/physics implementation (allow jumping/falling)
* collision detection: similar to AABB but with points (multiple points for left/right/top/bottom)
* block types: jump through platforms and solids (water is not working as intended, so it's been left out for now)
* camera: follow gameobject (mainly the player) and smooth movement
* levels: engine creates levels from .png files using color coded pixels

## TODO
There is a lot to do here, but the *major* features are:
* main game mechanics: attacking and pushing
* enemies: static enemies for now -> no AI
* physics support for actors (can be pushed)
* enemy collision with player
* enemy spawnpoint support in level editor

minor features:
* tileset support (changeable tilesets between levels)
* graphics (pixel art, 32x32 sprites): multiple tilesets, background, parallax effect
* level hazard support (traps, spikes etc.)
* animation support
* sprite animations for the player: walk, jump, fall, attack, push
* GUI: hp/score
* pickupable items: health
* particle effect support

low priority features:
* mainmenu
* story.. (perhaps never going to happen)
* friendly NPCs

## Images
29.12.2017 - New graphics
![ts1](https://user-images.githubusercontent.com/7894317/34445227-0e7f92b0-ecdb-11e7-8709-7f915d8f9c9c.png)

## Contribution
Heikki Heiskanen - design & implementation
