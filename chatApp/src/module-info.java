module chatApp {
    requires javax.servlet.api;
    exports chatApp.domain;
    exports chatApp.domain.chat;
    exports chatApp.domain.exceptions;
    exports chatApp.services.chat;
    exports chatApp.services.persistence.implementation;
    exports chatApp.services.persistence.interfaces;
    exports chatApp.services;
    exports chatApp.strategies;
}