package ro.koch.resourcefacades;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;

public interface Input<T> {
    T get();

    public static interface InputStreamFacade extends Input<InputStream>{}
    public static interface StringFacade extends Input<String>{}
    public static interface ByteArrayFacade extends Input<byte[]>{}

    public static class Trivial<T> implements Input<T> {
        private final T input;
        public Trivial(T input) { this.input = checkNotNull(input); }
        @Override public T get() { return input; }
    }

    // It's not so trivial after all, if the InputStream is accessed multiple times...
    public static class TrivialInputStream extends Trivial<InputStream> implements InputStreamFacade {
        public TrivialInputStream(InputStream input) { super(input); }
    }

    public static class TrivialString extends Trivial<String> implements StringFacade {
        public TrivialString(String input) { super(input); }
    }

    public static class TrivialByteArray extends Trivial<byte[]> implements ByteArrayFacade {
        public TrivialByteArray(byte[] input) { super(input); }
    }
}
