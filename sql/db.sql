create database OurFit;
Use OurFit;

alter database OurFit default character set utf8;

Create table Member (
Id int auto_increment primary key,
Email varchar(255) not null unique,
Password varchar(255),
Nickname varchar(255) not null unique,
Gender varchar(6) not null,
Height float,
Weight float,
Squat float,
BenchPress float,
Deadlift float,
OverheadPress float
);

alter table Member convert to character set utf8;

create table Category (
name varchar(255) primary key
);

insert into Category (name) values('Diet'), ('Strength'), ('Bodybuilding');

create table Exercise_routine (
Id int auto_increment primary key,
Category_name varchar(255) not null,
Routinename varchar(255) not null,
Imgpath Text not null,
Level int not null,
Fewtime int not null,
period int not null,
foreign key (Category_name) references Category(name)
);

alter table Exercise_routine convert to character set utf8;