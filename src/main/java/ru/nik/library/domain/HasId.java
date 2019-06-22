package ru.nik.library.domain;

public interface HasId {

    Integer getId();

    void setId(Integer id);

    default boolean isNew() {
        return getId() == null;
    }
}
