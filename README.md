# Thunderslash
2D sidescroller game using Java and custom 2D engine.

![bouncingenemy](https://user-images.githubusercontent.com/7894317/34470350-3dfd2bf6-ef38-11e7-9399-1498ac29ce11.png)

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
* Actor collisions with physics objects
* implementing randomized & hand put foreground, background support for better graphics. 
* fixing bugs and optimizing algorithms (mainly caching sprites etc.)
* more diverse enemies: hopping slimes, walking skeletons, charging tanks, ranged enemies etc.
* tweaking gameplay feeling
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
* player jump animation + current animations are placeholders
* pickupable items: coins, health etc.
* particle effect support
* controller support
* ~~mouse and keyboard support~~
* ~~rebindable keys support~~
* ~~level hazard support (traps, spikes etc.)~~
* ~~animation support~~
* ~~sprite animations for the player: walk, fall, attack, cast, idle~~
* ~~GUI: hp/score~~
* ~~usable items: crystal~~

low priority features:
* story.. (perhaps never going to happen)
* friendly NPCs
* text dialogs: conversations + help signs
* ~~simple mainmenu~~

## Contribution
Heikki Heiskanen - design & implementation
