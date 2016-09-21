package com.stefankopieczek.metallurj;

import java.util.Random;

public final class Optimizer {

    public static final float DEFAULT_COOLING_RATE = 0.9f;
    public static final int DEFAULT_MAX_ITERATIONS = 1000;
    public static final int ITERATIONS_PER_TEMPERATURE = 1000;

    private static final Random random = new Random();

    public static <T> T optimize(T initial, CostFunction<T> f, Mutator<T> mutator, float coolingRate, int maxIterations) {
        float temperature = 1.0f;
        T currentState = initial;

        for (int i = 0; i < maxIterations; i++) {
            for (int j = 0; j < ITERATIONS_PER_TEMPERATURE; j++)
                currentState = optimizeStep(currentState, f, mutator, temperature);
            temperature *= coolingRate;
        }

        return currentState;
    }

    public static <T> T optimize(T initial, CostFunction<T> f, Mutator<T> mutator, int maxIterations) {
        return optimize(initial, f, mutator, DEFAULT_COOLING_RATE, maxIterations);
    }

    public static <T> T optimize(T initial, CostFunction<T> f, Mutator<T> mutator, float coolingRate) {
        return optimize(initial, f, mutator, coolingRate, DEFAULT_MAX_ITERATIONS);
    }

    public static <T> T optimize(T initial, CostFunction<T> f, Mutator<T> mutator) {
        return optimize(initial, f, mutator, DEFAULT_COOLING_RATE, DEFAULT_MAX_ITERATIONS);
    }

    private static <T> T optimizeStep(T initial, CostFunction<T> f, Mutator<T> mutator, float temperature) {
        T newState = mutator.getRandomNeighbour(initial);

        if (shouldAccept(newState, initial, f, temperature)) {
            return newState;
        } else {
            return initial;
        }
    }

    private static <T> boolean shouldAccept(T newState, T oldState, CostFunction<T> f, float temperature) {
        Integer newCost = f.apply(newState);
        Integer oldCost = f.apply(oldState);

        if (newCost < oldCost) {
            return true;
        } else {
            double acceptanceProbability = Math.exp((oldCost - newCost) / temperature);
            return random.nextDouble() < acceptanceProbability;
        }
    }
}
