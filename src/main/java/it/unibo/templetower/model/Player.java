package it.unibo.templetower.model;

public interface Player {
    /**
     * @param enemy
     */
    void attack(EnemyRoom enemy);

    /**
     * @param weapon
     */
    void changeWeapon(Weapon weapon);

    /**
     * @param damage
     */
    void takeDamage(int damage);

    /**
     * 
     */
    void chooseMove();

    /**
     * 
     */
    void changeRoom(Room room);

    /**
     * @return
     */
    int getHealth();

    /**
     * @return
     */
    int getExperience();

    /**
     * 
     */
    void increaseExperience(int xp);

    /**
     * 
     */
    void changeFloor();

    /**
     * 
     */
    int getActualRoom();
}
