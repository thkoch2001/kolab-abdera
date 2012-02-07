package ro.koch.resourcefacades;

import javax.ws.rs.core.MediaType;

public interface DataHandler {
    <T> T getFacade(Class<T> clazz); // delegate call to FacadeRegistry
    boolean hasFacade(Class<?> clazz); // delegate call to FacadeRegistry

    MediaType getMediaType();
}
