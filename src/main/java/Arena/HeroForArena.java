package Arena;

import Card.Cards;
import Card.Interfaces.Restore;
import Card.Interfaces.Vulnerable;
import Hero.Hero;
import Hero.Powers;

public class HeroForArena implements Vulnerable, Restore {
    private int health;
    private final int maxHealth;
    private int armour;
    private String name;
    private Powers.heroPowers heroPower;
    private Powers.Weapon weapon;
    private boolean owner;
    private boolean canBeAttacked = true;
    private String passiveName;

    public HeroForArena(Hero hero, boolean firstPlayer, String passiveName) {
        this.passiveName = passiveName;
        maxHealth = health = hero.getMaxHealth();
        armour = 0;
        name = hero.getName();
        heroPower = hero.getHeroPower();
        weapon = new Powers.Weapon();
        owner = firstPlayer;
    }

    public int reduceArmour(int amount){
        if (armour > 0){
            if (amount < armour) {
                armour -= amount;
                return 0;
            } else {
                armour = 0;
                return amount - armour;
            }
        }
        return amount;
    }
    public void restoreHealth(int amount){
        giveHealth(amount);
        if (health > maxHealth){
            health = maxHealth;
        }
    }
    public void giveHealth(int amount){
        health += amount;
    }
    public void equipArmour(int amount){
        armour += amount;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "health=" + health + "\n" +
                "maxHealth=" + maxHealth + "\n" +
                "armour=" + armour + "\n" +
                "name='" + name + "'\n" +
                "weapon=" + weapon + "\n" +
                "passive: " + passiveName;
    }

    public Powers.heroPowers getHeroPower() {
        return heroPower;
    }

    public void setCanBeAttacked(boolean canBeAttacked) {
        this.canBeAttacked = canBeAttacked;
    }

    @Override
    public void restoreHP(int amount) {
        giveHealth(amount);
        if (health > maxHealth) health = maxHealth;
    }

    @Override
    public void takeDamage(int damageAmount) {
        if (!canBeAttacked) return;
        health -= reduceArmour(damageAmount);
    }

    public int getHealth() {
        return health;
    }

    public boolean weaponIsEquipped(){
        if (weapon == null) return false;
        return weapon.isEquipped();
    }

    public void useWeapon(){
        if (weaponIsEquipped()) weapon.useWeapon();
    }

    public void equipWeapon(Cards.weapon weapon){
        this.weapon.setWeapon(weapon);
    }

    public void refreshWeaponAttack(){
        if (weaponIsEquipped()) {
            weapon.setCanAttackThisTurn(true);
        }
    }

    public boolean getOwner() {
        return owner;
    }

    public int getAttackAmount(){
        int amount;
        if (!weaponIsEquipped()) amount = 0;
        else {
            amount = weapon.getWeapon().getAttackPower();
        }
        return amount;
    }
}

