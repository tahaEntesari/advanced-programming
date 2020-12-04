package Hero;

import Card.Cards;

import java.io.Serializable;

public class Powers {
    public static class heroPowers implements Serializable {
        String name;
        int mana;
        private int numberOfUsagesLeftThisTurn;
        private int maxNumberOfUsagesEachTurn;
        private String useStatus;
        String description;
        public heroPowers(String name, int mana, String description) {
            this.name = name;
            this.mana = mana;
            this.description = description;
            numberOfUsagesLeftThisTurn = maxNumberOfUsagesEachTurn = 1;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getMana() {
            return mana;
        }

        public void setMana(int mana) {
            this.mana = mana;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUseStatus() {
            return useStatus;
        }

        public void setUseStatus(String useStatus) {
            this.useStatus = useStatus;
        }

        public void setNumberOfUsagesLeftThisTurn(int numberOfUsagesLeftThisTurn) {
            this.numberOfUsagesLeftThisTurn = numberOfUsagesLeftThisTurn;
        }

        public void setMaxNumberOfUsagesEachTurn(int maxNumberOfUsagesEachTurn) {
            this.maxNumberOfUsagesEachTurn = maxNumberOfUsagesEachTurn;
            numberOfUsagesLeftThisTurn = maxNumberOfUsagesEachTurn;
        }

        public int getMaxNumberOfUsagesEachTurn() {
            return maxNumberOfUsagesEachTurn;
        }

        public void usedHeroPower(){
            numberOfUsagesLeftThisTurn--;
        }

        public int getNumberOfUsagesLeftThisTurn() {
            return numberOfUsagesLeftThisTurn;
        }

        @Override
        public String toString() {
            return "heroPowers{\n" +
                    "name='" + name + "'\n" +
                    ", mana=" + mana + "\n" +
                    ", number of usages left this turn= " + numberOfUsagesLeftThisTurn + "\n"+
                    ", description='" + description + "'\n" +
                    '}';
        }
    }
    public static class Weapon{
        private boolean equipped = false;
        private boolean canAttackThisTurn = false;
        private Cards.weapon weapon = null;

        public void useWeapon(){
            canAttackThisTurn = false;
            weapon.setDurability(weapon.getDurability() - 1);
            if (weapon.getDurability() <= 0) {
                wornOutWeapon();
            }
        }

        public void wornOutWeapon(){
            equipped = false;
            canAttackThisTurn = false;
            weapon = null;
        }

        public boolean isEquipped() {
            return equipped;
        }

        public void setEquipped(boolean equipped) {
            this.equipped = equipped;
        }

        public Cards.weapon getWeapon() {
            return weapon;
        }

        public void setWeapon(Cards.weapon weapon) {
            this.weapon = weapon;
            if (weapon != null) {
                canAttackThisTurn = true;
                equipped = true;
            }
        }


        public boolean canAttackThisTurn() {
            return canAttackThisTurn;
        }

        public void setCanAttackThisTurn(boolean canAttackThisTurn) {
            this.canAttackThisTurn = canAttackThisTurn;
        }

        @Override
        public String toString() {
            String result;
            if (weapon == null) result = "";
            else {
                result = weapon.getName()
                        + "\n Durability" + weapon.getDurability()
                        + "\n Attack" + weapon.getAttackPower();
            }
            return result;
        }
    }

}
