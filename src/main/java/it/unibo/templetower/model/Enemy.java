package it.unibo.templetower.model;

import java.util.List;

public record Enemy(List<Attack> attacks, String spritePath) {}