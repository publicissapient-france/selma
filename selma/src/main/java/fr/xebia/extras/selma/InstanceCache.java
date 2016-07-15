package fr.xebia.extras.selma;

public interface InstanceCache {
    public <IN, OUT> OUT get(IN in);

    public <IN, OUT> void put(IN in, OUT out);
}
