package it.unibo.templetower;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.templetower.controller.GameControllerImpl;
import it.unibo.templetower.model.EnemyRoom;
import it.unibo.templetower.model.PlayerImpl;
import it.unibo.templetower.model.Enemy;
import it.unibo.templetower.model.Room;
import it.unibo.templetower.model.Weapon;
import it.unibo.templetower.utils.Pair;

class GameControllerImplTest {

    private GameControllerImpl gameController;
    private EnemyRoom enemyRoom;
    private PlayerImpl player;
    private Enemy enemy;
    private Weapon startWeapon;

    @BeforeEach
    void setUp() {
        enemy = new Enemy("Goblin", 50.0, 1, List.of(new Pair<>("Punch", 5.0)), Map.of("Physical", 1.0),
                "tower/tower.json");

        enemyRoom = new EnemyRoom(enemy);

        startWeapon = new Weapon("Sword", 1, new Pair<>("Slash", 10.0), "tower/tower.json");
        Room enemyRoomWrapper = new Room(enemyRoom, "Enemy Room", 1); // Wrappa EnemyRoom in una Room
        player = new PlayerImpl(startWeapon, Optional.of(enemyRoomWrapper)); // Ora passa una Room valida

        gameController = new GameControllerImpl();

        gameController.enterRoom();
    }

    @Test
    void testAttackPlayer() {
        double initialPlayerLife = player.getLife();
        assertTrue(initialPlayerLife > 0, "La vita iniziale del giocatore dovrebbe essere maggiore di zero");

        enemyRoom.interact(player, 0); // Attacco del nemico
        double playerLifeAfterAttack = player.getLife();
        assertTrue(playerLifeAfterAttack < initialPlayerLife,
                "La vita del giocatore dovrebbe diminuire dopo l'attacco");
    }

    @Test
    void testAttackEnemy() {
        double initialEnemyLife = enemyRoom.getLifePoints();
        assertTrue(initialEnemyLife > 0, "La vita iniziale del nemico dovrebbe essere maggiore di zero");

        enemyRoom.interact(player, 1); // Attacco del giocatore
        double enemyLifeAfterAttack = enemyRoom.getLifePoints();
        assertTrue(enemyLifeAfterAttack < initialEnemyLife, "La vita del nemico dovrebbe diminuire dopo l'attacco");
    }

    @Test
    void testPlayerAttackWithMultiplier() {
        double initialEnemyLife = enemyRoom.getLifePoints();
        assertTrue(initialEnemyLife > 0, "La vita iniziale del nemico dovrebbe essere maggiore di zero");

        // Calcola il danno atteso basato sul moltiplicatore
        double expectedDamage = startWeapon.attack().getY() * enemyRoom.calculateMulti(startWeapon.attack().getX());

        player.attack(enemyRoom); // Attacco del giocatore
        double enemyLifeAfterAttack = enemyRoom.getLifePoints();

        assertEquals(initialEnemyLife - expectedDamage, enemyLifeAfterAttack, 0.01,
                "La vita del nemico dovrebbe diminuire correttamente dopo l'attacco con moltiplicatore");
    }
}
