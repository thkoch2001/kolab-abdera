package ro.koch.resourcefacades.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.ws.rs.core.MediaType;

import ro.koch.resourcefacades.FacadeFactory;

import com.google.common.base.Objects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

public class FacadeRegistry implements ro.koch.resourcefacades.FacadeRegistry {
    ImmutableMultimap<MediaTypeClassPair, FacadeFactory<?>> registry;

    public FacadeRegistry(Multimap<MediaTypeClassPair, FacadeFactory<?>> registry) {
        this.registry = ImmutableListMultimap.copyOf(registry);
    }

    @Override
    public Iterable<FacadeFactory<?>> getFacadeFactories(MediaType mediaType, Class<?> clazz) {
        return registry.get(new MediaTypeClassPair(mediaType, clazz));
    }

    public static class Builder {
        Multimap<MediaTypeClassPair, FacadeFactory<?>> registry = ArrayListMultimap.create();

        <T> Builder put(MediaType mediaType, Class<T> clazz, FacadeFactory<? extends T> factory) {
            registry.put(new MediaTypeClassPair(mediaType, clazz), factory);
            return this;
        }

        FacadeRegistry build() {
            return new FacadeRegistry(registry);
        }
    }

    private static class MediaTypeClassPair {
        public final MediaType mediaType;
        public final Class<?> clazz;

        public MediaTypeClassPair(MediaType mediaType, Class<?> clazz) {
            super();
            this.mediaType = checkNotNull(mediaType);
            this.clazz = checkNotNull(clazz);
        }

        @Override public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            MediaTypeClassPair other = (MediaTypeClassPair) o;
            return Objects.equal(mediaType, other.mediaType)
                && Objects.equal(clazz, other.clazz);
        }

        @Override public int hashCode() {
            return Objects.hashCode(mediaType, clazz);
        }
    }
}
