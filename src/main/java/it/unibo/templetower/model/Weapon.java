package it.unibo.templetower.model;

import it.unibo.templetower.utils.Pair;

/**
 * Record representing a weapon in the game.
 * Contains information about the weapon's name, level, attack, and sprite path.
 * 
 * @param name the name of the weapon
 * @param level the level of the weapon
 * @param attack the attack details of the weapon
 * @param spritePath the file path to the weapon's sprite
 */
public record Weapon(String name, Integer level, Pair<String,Double> attack, String spritePath) {}
