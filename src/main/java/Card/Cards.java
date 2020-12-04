package Card;

import Card.Interfaces.Restore;
import Card.Interfaces.Vulnerable;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.HashMap;


public class Cards {
    public static abstract class card implements Serializable {
        private String name;
        private int mana;
        private int initialMana;
        private String heroClass;
        private String rarity;
        private String type;
        private int price;
        private String description;
        protected JSONObject abilities = new JSONObject();
        protected HashMap<String, Boolean> activatedAbilities = new HashMap<>();
        private boolean owner = true;
        private int randomField = 0;

        /*
         ADD ANY NEW VARIABLE TO THE JSON OBJECT AS WELL
         */
        card(String name, int mana, String heroClass, String rarity, String type, String description) {
            this.name = name;
            setMana(mana);
            setRarity(rarity);
            setType(type);
            setHeroClass(heroClass);
            this.description = description;
            setPrice();
            this.initialMana = mana;
        }

        public void setRandomField(int number) {
            randomField = number;
        }

        public void setMana(int mana) {
            mana = Math.max(mana, 0);
            this.mana = mana;
        }

        public void setInitialMana(int mana) {
            mana = Math.max(mana, 0);
            this.initialMana = mana;
            this.mana = mana;
        }

        private void setRarity(String rarity) {
            assert rarity.equals("Common") || rarity.equals("Rare") || rarity.equals("Epic")
                    || rarity.equals("Legendary");
            this.rarity = rarity;
        }

        private void setType(String type) {
            assert type.equals("Minion") || type.equals("Spell") || type.equals("Weapon") || type.equals("Quest");
            this.type = type;
        }

        private void setHeroClass(String heroClass) {
            assert heroClass.equals("Mage") || heroClass.equals("Rogue") || heroClass.equals("Warlock")
                    || heroClass.equals("Neutral");
            this.heroClass = heroClass;
        }

        private void setPrice() {
            switch (rarity) {
                case "Common":
                    price = 10;
                    break;
                case "Rare":
                    price = 20;
                    break;
                case "Epic":
                    price = 30;
                    break;
                case "Legendary":
                    price = 40;
                    break;
            }
        }

        public String getName() {
            return name;
        }

        public int getMana() {
            return mana;
        }

        public String getHeroClass() {
            return heroClass;
        }

        public String getRarity() {
            return rarity;
        }

        public String getType() {
            return type;
        }

        public String getDescription() {
            return description;
        }

        public int getPrice() {
            return price;
        }

        public void resetChanges() {
            mana = initialMana;
        }

        public boolean getOwner() {
            return owner;
        }

        public void setOwner(boolean owner) {
            this.owner = owner;
        }

        public JSONObject getCardJsonObject() {
            JSONObject cardObject = new JSONObject();
            cardObject.put("name", name);
            cardObject.put("mana", mana);
            cardObject.put("heroClass", heroClass);
            cardObject.put("rarity", rarity);
            cardObject.put("type", type);
            cardObject.put("description", description);
            cardObject.put("abilities", abilities);
            return cardObject;
        }

        public JSONObject getAbilities() {
            return abilities;
        }

        public void addToAbilities(String key, JSONObject newAbility) {
            abilities.put(key, newAbility);
        }

        public void addToAbilities(String key, boolean trueFalse) {
            abilities.put(key, trueFalse);
        }

        public void resetActivatedAbilities() {
            for (Object o : abilities.keySet()) {
                String s = (String) o;
                activatedAbilities.put(s, false);
            }
        }

        public abstract String superPrint();

        @Override
        public String toString() {
            return "name=" + name + "\n" +
                    "mana=" + mana + "\n" +
                    "heroClass=" + heroClass + "\n" +
                    "rarity=" + rarity + "\n" +
                    "type=" + type + "\n" +
                    "price=" + price + "\n" +
                    "description=" + description + "\n";
        }

        public HashMap<String, Boolean> getActivatedAbilities() {
            return activatedAbilities;
        }
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class minion extends card implements Vulnerable, Restore {
        private int health;
        private int initialHealth; //used for health restoration
        private int initialAttackPower;
        private int attackPower;
        private int numberOfAttacksUsed = 1;
        private int numberOfAttacksEachTurn = 1;
        private boolean canBeAttacked = true;
        private JSONObject actionBasedAbilities = new JSONObject();
        private boolean canAttackHero = true;

        {
            actionBasedAbilities.put("uponDamage", null);
            actionBasedAbilities.put("uponDeath", null);
            actionBasedAbilities.put("uponDraw", null);
            actionBasedAbilities.put("turnStart", null);
            actionBasedAbilities.put("turnEnd", null);

            abilities.put("battlecry", null);
            abilities.put("deathRattle", null);
            abilities.put("discover", null);
            abilities.put("divineShield", null);
            abilities.put("taunt", null);
            abilities.put("rush", null);
            abilities.put("charge", null);
            abilities.put("lifeSteal", null);
            abilities.put("poisonous", null);
            abilities.put("overkill", null);
            abilities.put("twinspell", null);
            abilities.put("reborn", null);
            abilities.put("outcast", null);
            abilities.put("dormant", null);
            abilities.put("inspire", null);
            abilities.put("windfury", null);
            abilities.put("megaWindfury", null);
            abilities.put("restore", null);
            abilities.put("echo", null);
            abilities.put("stealth", null);

            resetActivatedAbilities();
        }

        /*
         ADD ANY NEW VARIABLE TO THE JSON OBJECT AS WELL
         */
        minion(String name, int mana, int health, int attackPower, String heroClass, String rarity, String description) {
            super(name, mana, heroClass, rarity, "Minion", description);
            setHealth(health);
            setAttackPower(attackPower);
            initialHealth = health;
            this.initialAttackPower = attackPower;
        }

        public void setHealth(int health) {
            this.health = health;
        }

        public void setAttackPower(int attackPower) {
            this.attackPower = attackPower;
        }

        public int getHealth() {
            return health;
        }

        public int getAttackPower() {
            return attackPower;
        }

        @Override
        public JSONObject getCardJsonObject() {
            JSONObject cardObject = super.getCardJsonObject();
            cardObject.put("health", health);
            cardObject.put("attackPower", attackPower);
            return cardObject;
        }

        public String superPrint() {
            return super.toString();
        }

        @Override
        public String toString() {
            StringBuilder outputString = new StringBuilder("mana=" + getMana() + "\n" +
                    "health=" + health + "'\n" +
                    "attackPower=" + attackPower + "'\n");
            for (Object o : abilities.keySet()) {
                String s = (String) o;
                if (abilities.get(s) != null) outputString.append(s + "\n");
            }
            return outputString.toString();
        }

        public void resetChanges() {
            super.resetChanges();
            health = initialHealth;
            attackPower = initialAttackPower;
            numberOfAttacksUsed = numberOfAttacksEachTurn;
        }

        public void activateWindfury() {
            numberOfAttacksEachTurn = 2;
        }

        public boolean canAttackThisTurn() {
            return numberOfAttacksUsed < numberOfAttacksEachTurn;
        }

        public void setCanAttackThisTurn(boolean canAttackThisTurn) {
            if (canAttackThisTurn) {
                numberOfAttacksUsed = 0;
            } else {
                numberOfAttacksUsed++;
            }
        }

        public boolean canBeAttacked() {
            return canBeAttacked;
        }

        public boolean hasLifesteal() {
            if (abilities.get("lifeSteal") == null) return false;
            return (Boolean) abilities.get("lifeSteal");
        }

        public void setHasLifesteal() {
            abilities.put("lifeSteal", true);
        }

        public boolean isPoisonous() {
            if (abilities.get("poisonous") == null) return false;
            return (Boolean) abilities.get("poisonous");
        }

        public void setPoisonous() {
            abilities.put("poisonous", true);
        }

        public boolean hasUponDamage() {
            if (actionBasedAbilities.get("uponDamage") == null) return false;
            return true;
        }

        public void setCanBeAttacked(boolean canBeAttacked) {
            this.canBeAttacked = canBeAttacked;
        }

        public void setInitialHealth(int amount) {
            initialHealth = amount;
            health = amount;
        }

        public void setInitialAttackPower(int amount) {
            initialAttackPower = amount;
            attackPower = amount;
        }

        public Cards.minion cloneThis() {
            Cards.minion newMinion =
                    new Cards.minion(getName(), getMana(), getHealth(), getAttackPower(), getHeroClass(), getRarity(),
                            getDescription());
            newMinion.abilities = (JSONObject) this.abilities.clone();
            newMinion.actionBasedAbilities = (JSONObject) this.actionBasedAbilities.clone();
            return newMinion;
        }

        public void addToSpecificActions(String keyValue, JSONObject actions) {
            /*
            The json object must include json objects describing each ability or action.
             */

            actionBasedAbilities.put(keyValue, actions);
        }

        public JSONObject getActionBasedAbilities() {
            return actionBasedAbilities;
        }

        public boolean canAttackHero() {
            return canAttackHero;
        }

        public void setCanAttackHero(boolean canAttackHero) {
            this.canAttackHero = canAttackHero;
        }

        public boolean hasTaunt() {
            if (abilities.get("taunt") == null) return false;
            return (Boolean) abilities.get("taunt");
        }

        @Override
        public void takeDamage(int damageAmount) {
            if (abilities.get("divineShield") != null) {
                abilities.put("divineShield", null);
                return;
            }
            health -= damageAmount;
            if (abilities.get("reborn") != null) {
                if (health <= 0) health = 1;
                abilities.put("reborn", null);
            }
        }

        @Override
        public void restoreHP(int amount) {
            if (health > initialHealth) return;
            health += amount;
            if (health > initialHealth) health = initialHealth;
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static class spell extends card {
        {
            abilities.put("deal", null);
            abilities.put("draw", null);
            abilities.put("discover", null);
            abilities.put("restore", null);
            abilities.put("give", null);
            abilities.put("reinstate", null);
            abilities.put("summon", null);

            resetActivatedAbilities();
        }

        spell(String name, int mana, String heroClass, String rarity, String description) {
            super(name, mana, heroClass, rarity, "Spell", description);
        }

        spell(String name, int mana, String heroClass, String rarity, String type, String description) {
            super(name, mana, heroClass, rarity, type, description);
        }


        @Override
        public JSONObject getCardJsonObject() {
            JSONObject cardObject = super.getCardJsonObject();
            return cardObject;
        }

        @Override
        public String superPrint() {
            return super.toString();
        }

        @Override
        public String toString() {
            return getName() + "\n" +
                    "mana " + getMana();
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class quest extends spell {
        private JSONObject questDescription;

        public quest(String name, int mana, String heroClass, String rarity, String description) {
            super(name, mana, heroClass, rarity, "Quest", description);
            questDescription = new JSONObject();
        }

        public void addQuestDescription(JSONObject quest) {
            questDescription.put("quest", quest);
        }

        public void addQuestReward(JSONObject reward) {
            questDescription.put("reward", reward);
        }

        public JSONObject getQuestDescription() {
            return questDescription;
        }

        @Override
        public JSONObject getCardJsonObject() {
            JSONObject cardObject = super.getCardJsonObject();
            return cardObject;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class weapon extends card {
        private int durability, attackPower;
        private JSONObject actionBasedAbilities = new JSONObject();

        {
            actionBasedAbilities.put("turnStart", null);
            actionBasedAbilities.put("turnEnd", null);

            abilities.put("summon", "false");
            abilities.put("battlecry", "false");

            resetActivatedAbilities();
        }

        weapon(String name, int mana, int durability, int attackPower, String heroClass, String rarity, String description) {
            super(name, mana, heroClass, rarity, "Weapon", description);
            setAttackPower(attackPower);
            setDurability(durability);
        }

        public void setDurability(int durability) {
            this.durability = durability;
        }

        public int getDurability() {
            return durability;
        }

        public int getAttackPower() {
            return attackPower;
        }

        public void setAttackPower(int attackPower) {
            this.attackPower = attackPower;
        }

        public JSONObject getActionBasedAbilities() {
            return actionBasedAbilities;
        }

        public void addToActionBasedAbilities(String key, JSONObject value) {
            actionBasedAbilities.put(key, value);
        }

        @Override
        public JSONObject getCardJsonObject() {
            JSONObject cardObject = super.getCardJsonObject();
            cardObject.put("durability", durability);
            cardObject.put("attackPower", attackPower);
            return cardObject;
        }

        @Override
        public String superPrint() {
            return super.toString();
        }

        @Override
        public String toString() {
            return getName() + "\n" +
                    "mana " + getMana() + "\n" +
                    "attack " + attackPower + "\n" +
                    "durability" + durability;
        }
    }


}
