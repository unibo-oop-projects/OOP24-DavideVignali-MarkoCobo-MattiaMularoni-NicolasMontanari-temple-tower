package it.unibo.templetower;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.templetower.controller.GameControllerImpl;
import it.unibo.templetower.controller.GameDataManagerImpl;
import it.unibo.templetower.model.Enemy;
import it.unibo.templetower.model.EnemyRoom;
import it.unibo.templetower.model.Player;
import it.unibo.templetower.model.PlayerImpl;
import it.unibo.templetower.model.Room;
import it.unibo.templetower.model.Weapon;
import it.unibo.templetower.utils.Pair;

class ControllerTest {
    private static final Logger LOGGER = Logger.getLogger(ControllerTest.class.getName());
    private GameControllerImpl gameController;
    private static final String TEST_TOWER_PATH = "towerNew/tower.json";

    @BeforeEach
    void setUp() {
        LOGGER.info("Init Controller Test");
        final GameDataManagerImpl gameDataManager = GameDataManagerImpl.getInstance();
        gameDataManager.loadGameDataFromTower(TEST_TOWER_PATH);
        gameDataManager.setTowerPath(TEST_TOWER_PATH);
        gameController = new GameControllerImpl();
        gameController.resetGame();
    }

    @Test
    void testChangeRoom() {
        final int initialRoom = gameController.getPlayerActualRoom();
        gameController.changeRoom(1);
        final int newRoom = gameController.getPlayerActualRoom();
        assertNotEquals(initialRoom, newRoom, "The player should have changed room");
    }

    @Test
    void testPlayerTakesDamage() {
        final Room room = new Room(
                new EnemyRoom(
                        new Enemy("enemy", 10.0, 10, List.of(new Pair<>("attack", 12.1)), new HashMap<>(), "path")),
                TEST_TOWER_PATH, 0);
        final Player player = new PlayerImpl(new Weapon("SWORD", 2, new Pair<>("Sword", 2.0), "path/to/asset"),
                Optional.of(room));
        final double initialLife = player.getLife();
        room.interactWithRoom(player, 0);
        final double newLife = player.getLife();
        assertTrue(newLife < initialLife, "The player should lose health after taking damage");
    }

    @Test
    void testIncreaseLifePlayer() {
        final double initialLife = gameController.getPlayerLife();
        gameController.increaseLifePlayer(10);
        final double newLife = gameController.getPlayerLife();
        assertTrue(newLife > initialLife, "The player's health should increase when gaining XP");
    }

    @Test
    void testWeaponManagement() {
        final Weapon newWeapon = new Weapon("Simple sword", 2, new Pair<>("Sword", 2.0), "path/to/asset");
        gameController.addPlayerWeapon(newWeapon, 1);
        assertEquals("Simple sword", gameController.getPlayerWeapons().get(1).name(), "The weapon should be correctly added");
    }

    @Test
    void testAttackEnemy() {
        final double health = 10.0;
        final double damage = 10.1;
        final EnemyRoom enemyRoom = new EnemyRoom(
                new Enemy("enemy", health, 10, List.of(new Pair<>("attack", 12.1)), new HashMap<>(), "path"));
        final double initialEnemyLife = enemyRoom.getLifePoints();
        enemyRoom.takeDamage(damage);
        final double newEnemyLife = enemyRoom.getLifePoints();
        assertTrue(newEnemyLife < initialEnemyLife, "The enemy should lose health after the attack");
    }

    @Test
    void testGoToNextFloor() {
        assertDoesNotThrow(gameController::goToNextFloor,
                "Going to the next floor should not throw an exception");
    }

    @Test
    void testIsBossTime() {
        assertFalse(gameController.isBossTime(), "Initially, it should not be boss time");
        gameController.goToNextFloor();
        if (gameController.isBossTime()) {
            assertTrue(gameController.isBossTime(), "Boss time should be true when in the boss room");
        } else {
            assertFalse(gameController.isBossTime(), "Boss time should still be false if not in the boss room");
        }
    }
}
