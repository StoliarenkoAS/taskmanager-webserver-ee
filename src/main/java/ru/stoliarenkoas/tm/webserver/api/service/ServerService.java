package ru.stoliarenkoas.tm.webserver.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.comparator.ComparatorType;
import ru.stoliarenkoas.tm.webserver.entity.Session;

public interface ServerService {

    @NotNull
    String showAbout() throws Exception;

    @NotNull
    Boolean shutdown(@Nullable Session session) throws Exception;

    @NotNull
    String showHelp(@Nullable Session session) throws Exception;

    @NotNull
    Boolean setSortMethod(@Nullable Session session, @Nullable ComparatorType comparatorType) throws Exception;

    @NotNull
    Boolean dataClearBinary(@Nullable Session session) throws Exception;

    @NotNull
    Boolean dataSaveBinary(@Nullable Session session) throws Exception;

    @NotNull
    Boolean dataLoadBinary(@Nullable Session session) throws Exception;

    @NotNull
    Boolean dataClearJaxbXml(@Nullable Session session) throws Exception;

    @NotNull
    Boolean dataSaveJaxbXml(@Nullable Session session) throws Exception;

    @NotNull
    Boolean dataLoadJaxbXml(@Nullable Session session) throws Exception;

    @NotNull
    Boolean dataClearJaxbJson(@Nullable Session session) throws Exception;

    @NotNull
    Boolean dataSaveJaxbJson(@Nullable Session session) throws Exception;

    @NotNull
    Boolean dataLoadJaxbJson(@Nullable Session session) throws Exception;

    @NotNull
    Boolean dataClearFasterXml(@Nullable Session session) throws Exception;

    @NotNull
    Boolean dataSaveFasterXml(@Nullable Session session) throws Exception;

    @NotNull
    Boolean dataLoadFasterXml(@Nullable Session session) throws Exception;

    @NotNull
    Boolean dataClearFasterJson(@Nullable Session session) throws Exception;

    @NotNull
    Boolean dataSaveFasterJson(@Nullable Session session) throws Exception;

    @NotNull
    Boolean dataLoadFasterJson(@Nullable Session session) throws Exception;

}
