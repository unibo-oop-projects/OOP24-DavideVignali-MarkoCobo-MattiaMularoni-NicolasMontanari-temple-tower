package it.unibo.templetower.model;

/**
 * Record representing an attack type in the game.
 * Contains information about the attack identifier and the path to its effect resources.
 * 
 * @param attackId   the unique identifier for the attack
 * @param effectPath the file path to the attack's effect resources
 */
public record AttackType(String attackId, String effectPath) { }
