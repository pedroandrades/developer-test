create table hungry_professional(
	id integer not null,
	name varchar(100) not null,
	password varchar(100) not null,
	nickname varchar(50) not null,
	vote_date DATE,
	primary key (id)
);

create table restaurant(
    id integer not null,
    name varchar(100) not null,
    primary key (id)
);

create table vote(
    id integer not null,
    vote_date date not null,
    restaurant integer not null,
    votes integer default 0,
    primary key (id),
    foreign key (restaurant) references restaurant(id)
)
