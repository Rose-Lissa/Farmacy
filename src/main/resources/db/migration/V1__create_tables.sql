create table medicine_types
(
    id     bigint primary key,
    "name" varchar not null
);

create table medicine
(
    id                       bigserial primary key,
    "name"                   varchar not null,
    price                    numeric not null,
    is_manufacturing         boolean not null,
    description              text,
    manufacturing_technology text,
    medicine_type_id         bigint references medicine_types
);

create table users
(
    id         bigserial primary key,
    username   varchar not null unique,
    "password" varchar not null
);

create table worker
(
    id         bigserial primary key references users,
    "name"     varchar not null,
    surname    varchar not null,
    patronymic varchar not null,
    passport   varchar not null
);

create table consumer
(
    id         bigserial primary key references users,
    "name"     varchar not null,
    surname    varchar not null,
    patronymic varchar not null,
    address    varchar not null,
    phone      varchar not null
);

create table "admin"
(
    id bigserial primary key references users
);

create table purchase_order
(
    id      bigserial primary key,
    stage   varchar not null,
    user_id bigint references users
);

create table medicine_item
(
    id                bigserial primary key,
    serial_number     varchar not null unique,
    purchase_order_id bigint references purchase_order,
    medicine_id       bigint references medicine,
    sales             boolean not null
);


create table manufacturing_order
(
    id                bigserial primary key,
    purchase_order_id bigint references purchase_order,
    medicine_id       bigint references medicine,
    number            int not null check ( number > 0 )
);

create table supplier
(
    id     bigserial primary key,
    "name" varchar not null
);


create table external_order
(
    id                bigserial primary key,
    purchase_order_id bigint references purchase_order,
    medicine_id       bigint references medicine,
    supplier_id       bigint references supplier,
    number            int not null check ( number > 0 )
);


create table shopping_cart_medicine
(
    id                bigserial primary key,
    purchase_order_id bigint references purchase_order,
    medicine_id       bigint references medicine,
    number            int not null check ( number > 0 )
);

