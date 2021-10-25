module chatApp {
    requires javax.servlet.api;
    requires java.sql;
    exports chatApp.domain;
    exports chatApp.domain.chat;
    exports chatApp.domain.exceptions;
    exports chatApp.services.persistence.implementation;
    exports chatApp.services.persistence.interfaces;
    exports chatApp.services;
    exports chatApp.factories;
    exports chatApp.services.persistence;
}