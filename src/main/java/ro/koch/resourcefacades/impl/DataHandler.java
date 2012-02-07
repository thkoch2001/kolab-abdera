package ro.koch.resourcefacades.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;

import ro.koch.resourcefacades.FacadeFactory;
import ro.koch.resourcefacades.FacadeRegistry;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

public class DataHandler implements ro.koch.resourcefacades.DataHandler {
    private final FacadeRegistry facadeRegistry;
    private final MediaType mediaType;
    private final MutableClassToInstanceMap<Object> facades = MutableClassToInstanceMap.create();

    public DataHandler(FacadeRegistry facadeRegistry, MediaType mediaType, ClassToInstanceMap<Object> inputFacades) {
        super();
        this.facadeRegistry = checkNotNull(facadeRegistry);
        this.mediaType = checkNotNull(mediaType);
        for(Entry<Class<?>, ?> entry: inputFacades.entrySet()) {
            facades.put(entry.getKey(), checkNotNull(entry.getValue()));
        }
        checkArgument(!facades.isEmpty(),"No inputFacades provided!");
    }

    public DataHandler(FacadeRegistry facadeRegistry, MediaType mediaType, Class<Object> clazz, Object facade) {
        this(facadeRegistry, mediaType, ImmutableClassToInstanceMap.builder().put(clazz, facade).build());
    }

    @Override public <T> T getFacade(Class<T> clazz) {
        if(!facades.containsKey(clazz)) {
            FacadeFactory<?> factory = resolveFacadeFactory(clazz);
            // check factory type
            // check for null
            facades.put(clazz, factory.build(this));
        }
        return facades.getInstance(clazz);
    }

    private FacadeFactory<?> resolveFacadeFactory(Class<?> clazz) {
        for(FacadeFactory<?> factory : facadeRegistry.getFacadeFactories(mediaType, clazz)) {
            if(checkDependencies(factory)) return factory;
        }
        return null;
    }

    private boolean checkDependencies(FacadeFactory<?> factory) {
        for(Class<?> dependency : factory.getDependencies()) {
            if(facades.containsKey(dependency)) continue;
            if(!hasFacade(dependency)) return false;
        }
        return true;
    }

    @Override public boolean hasFacade(Class<?> clazz) {
        if(facades.containsKey(clazz)) return true;
        return null != resolveFacadeFactory(clazz);
    }

    @Override public MediaType getMediaType() {
        return mediaType;
    }

}
