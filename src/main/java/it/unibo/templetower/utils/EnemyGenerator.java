package it.unibo.templetower.utils;

import java.util.List;
import java.util.Random;
import it.unibo.templetower.model.Enemy;

/**
 * Utility class for generating enemies based on a level budget.
 */
public class EnemyGenerator {

    /**
     * Picks an enemy from the provided list whose level is closest to a random target between 1 and budget.
     *
     * @param enemyList list of available enemies
     * @param budget the current budget (>=1)
     * @param random an instance of Random
     * @return the chosen Enemy
     */
    public static Enemy pickEnemyByBudget(List<Enemy> enemyList, int budget, Random random) {
        int target = random.nextInt(budget) + 1; // random value in [1, budget]
        Enemy best = enemyList.get(0);
        int bestDiff = Math.abs(best.level() - target);
        for (Enemy e : enemyList) {
            int diff = Math.abs(e.level() - target);
            if (diff < bestDiff) {
                best = e;
                bestDiff = diff;
            }
        }
        return best;
    }
}
