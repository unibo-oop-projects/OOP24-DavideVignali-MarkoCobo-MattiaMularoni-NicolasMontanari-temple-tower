package it.unibo.templetower.model;

import it.unibo.templetower.utils.Pair;

public record Weapon(String name, Integer level, Pair<String,Double> attack, String spritePath) {}
