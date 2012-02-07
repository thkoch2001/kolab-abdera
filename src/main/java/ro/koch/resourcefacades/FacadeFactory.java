package ro.koch.resourcefacades;

public interface FacadeFactory<T> {
    T build(DataHandler dataHandler);

    Iterable<Class<?>> getDependencies();
}
