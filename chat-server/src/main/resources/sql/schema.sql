
create table if not exists user_info
(
    id         bigserial
        constraint user_info_pk
            primary key,
    name       varchar(1024) not null,
    email      varchar(1024) not null,
    gender     varchar,
    age        integer,
    is_deleted boolean      default false,
    updated_at timestamp(3) default CURRENT_TIMESTAMP(3),
    created_at timestamp(3) default CURRENT_TIMESTAMP(3)
);

create index if not exists user_info_name_index
    on user_info (name);

create table if not exists user_address
(
    id         bigserial
        constraint user_address_pk
            primary key,
    user_id    bigint
        constraint user_address_user_info_id_fk
            references user_info,
    label      varchar(1024),
    address    varchar,
    updated_at timestamp(3) default CURRENT_TIMESTAMP(3),
    created_at timestamp(3) default CURRENT_TIMESTAMP(3)
);

create index if not exists user_address_user_id_index
    on user_address (user_id);


create table if not exists user_phone_number
(
    id           bigserial
        constraint user_phone_number_pk
            primary key,
    user_id      bigint
        constraint user_phone_number_user_info_id_fk
            references user_info,
    label        varchar(1024),
    country_code varchar(2),
    phone_number varchar(15),
    is_verified  boolean      default false                not null,
    is_deleted   boolean      default false                not null,
    updated_at   timestamp(3) default CURRENT_TIMESTAMP(3) not null,
    created_at   timestamp(3) default CURRENT_TIMESTAMP(3) not null
);

create index if not exists user_phone_number_user_id_index
    on user_phone_number (user_id);

