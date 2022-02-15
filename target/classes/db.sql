CREATE TYPE chatType AS ENUM ('PRIVATE','GROUP','ROOM');

CREATE table Chats(
                      id SERIAL primary key not null ,
                      owner varchar(100) not null,
                      chat_type chatType not null,
                      name varchar(100),
                      users_count integer
);

CREATE table Users(
                      username varchar(255) primary key not null,
                      password varchar(255) not null
);

CREATE table Users_chats(
                            username varchar(255),
                            chat_id integer,
                            has_ban bool,
                            primary key (username,chat_id),
                            constraint fk_username FOREIGN KEY (username) REFERENCES
                                Users(username),
                            constraint fk_chat_id FOREIGN KEY (chat_id) REFERENCES
                                Chats(id)
);

CREATE table Messages(
                         id serial not null primary key,
                         text varchar(255),
                         sender_name varchar(255) not null,
                         chat_id integer not null,
                         constraint fk_sendername FOREIGN KEY(sender_name) REFERENCES
                             Users(username),
                         constraint fk_chat_id FOREIGN KEY (chat_id) REFERENCES
                             Chats(id)
);