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
The game uses custom 2D engine and this project presents multiple new features. These featrues are so far:
* 9-slicing sprite support (chooses correct sprite by using neighbor data)
* Physics: simple gravity/physics implementation (allow jumping/falling)
* collision detection: similar to AABB but with points
* block types: jump through platforms and solid (water not working properly)
* camera: follow gameobject and smooth movement
* levels: creates levels from .png files (color coded pixel data).

## Images
27.12.2017 - Early screencapture of the game: solid blocks, platforms and player. Using simplified graphics.
![t](https://user-images.githubusercontent.com/7894317/34393185-5721752a-eb59-11e7-85de-7406b7c64cef.png)

## Contribution
Heikki Heiskanen - design & implementation
