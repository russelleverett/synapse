package byobgyn.com.synapse.utils;

public interface IRequestEncoder {
    String encode(Object value);
    String decode(Object value);
}