package me.spigotgoose.weightedrandomness;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class WeightedRandom <T> {

    private HashMap<T, Float> optionsToChooseFrom = new HashMap<>();

    private final List<Float> upperBounds = new ArrayList<>();

    private float total = 0;

    private void calculateTotal() {
        total = 0;
        for(Map.Entry<T, Float> option : optionsToChooseFrom.entrySet()) {
            total += option.getValue();
        }
    }

    private void writeBounds() {
        upperBounds.clear();
        float current = 0;
        for(Map.Entry<T, Float> option : optionsToChooseFrom.entrySet()) {
            current += option.getValue();
            upperBounds.add(current);
        }
    }

    private float randomlyGenerateFloat() {
        return (float) ThreadLocalRandom.current().nextDouble(0, total);
    }

    public WeightedRandom() {
        unsafeChoose();
    }

    /**
     * Chooses objects based on their assigned weights, the bigger the weight is, the more likely it is to be chosen.
     * Chances of an object to be chosen is its weight divided by the total weight of all the options.
     */
    public WeightedRandom(HashMap<T, Float> newOptionsMap) {
        optionsToChooseFrom = newOptionsMap;
        calculateTotal();
        writeBounds();
        unsafeChoose();
    }

    /**
     * Get the map of options the WeightedRandom object chooses from.
     * @return HashMap of options and assigned weights
     */
    public HashMap<T, Float> getOptionsToChooseFrom() {
        return optionsToChooseFrom;
    }

    /**
     * Get the total weight of all the options.
     * @return Total weight of all options as a float
     */
    public float getTotal() {
        return total;
    }

    /**
     * Replace the options and assigned weights map with another HashMap.
     * @param newOptions The new map of options and assigned weights
     */
    public void update(HashMap<T, Float> newOptions) {
        optionsToChooseFrom = newOptions;
        calculateTotal();
        writeBounds();
    }

    /**
     * Randomly chooses an option from the map based on the assigned weights and returns its index.
     * @return Index of chosen option
     */
    public int chooseOptionIndex() {
        float chosenNumber = randomlyGenerateFloat();
        int idx = -1;
        for(Float bound : upperBounds) {
            idx++;
            if(chosenNumber <= bound) {
                break;
            }
        }
        return idx;
    }


    @SuppressWarnings("unchecked")
    private T unsafeChoose() {
        int idx = chooseOptionIndex();
        try {
            return (T) optionsToChooseFrom.keySet().toArray()[idx];
        } catch(Exception exp) {
            return null;
        }
    }


    /**
     * Randomly choose an option from the map based on the assigned weights.
     * @return The chosen object
     */
    public T choose() {
        int idx = chooseOptionIndex();
        int cidx = 0;
        for(Map.Entry<T, Float> entry : optionsToChooseFrom.entrySet()) {
            if(cidx == idx) return entry.getKey();
            cidx++;
        }
        return null;
    }


}
