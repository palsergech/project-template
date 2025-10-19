create table user_profile(
    id          uuid primary key,
    email       varchar(255),
    name        varchar(255),
    version     int not null
);