package ro.koch.resourcefacades;

import javax.ws.rs.core.MediaType;

public interface FacadeRegistry {
    Iterable<FacadeFactory<?>> getFacadeFactories(MediaType mediaType, Class<? extends Object> clazz);
}
