package fr.xebia.extras.selma;

public interface InstanceCache {
    <IN, OUT> OUT get(IN in);

    <IN, OUT> void put(IN in, OUT out);

    void push();
    void pop();
}
