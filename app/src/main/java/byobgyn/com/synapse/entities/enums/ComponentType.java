package byobgyn.com.synapse.entities.enums;

public enum ComponentType implements IConstant {
    CPU(1),
    Memory(2),
    Storage(3),
    Bandwidth(5);

    int value = 1;
    ComponentType(int value) {
        this.value = value;
    }
    public int getValue() { return value; }
}
