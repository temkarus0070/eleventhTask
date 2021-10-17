CREATE TYPE chatType AS ENUM ('PRIVATE','GROUP','ROOM');

CREATE table Chats(
    id integer primary key not null ,
    owner varchar(100) not null,
    chat_type chatType not null,
    name varchar(100),
    users_count integer
);

CREATE table Users(
    name varchar(255) primary key not null,
    password varchar(255) not null
);

CREATE table Users_chats(
    username varchar(255),
    chat_id integer,
    has_ban bool,
    primary key (username,chat_id),
    constraint fk_username FOREIGN KEY (username) REFERENCES
                        Users(name),
    constraint fk_chat_id FOREIGN KEY (chat_id) REFERENCES
                        Chats(id)
);
