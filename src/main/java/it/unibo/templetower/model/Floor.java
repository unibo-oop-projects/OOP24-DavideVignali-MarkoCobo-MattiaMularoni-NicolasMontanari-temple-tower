package it.unibo.templetower.model;

import java.util.List;

public record Floor(String floorName, String spritePath, List<Enemy> enemies, List<Weapon> weapons) {}
