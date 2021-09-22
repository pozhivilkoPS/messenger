create table user
(
	id int auto_increment,
	login varchar(30) not null,
	password varchar(30) not null,

	constraint user_pk primary key (id)
);

create unique index user_login_uindex
	on user (login);

create unique index user_password_uindex
	on user (password);
