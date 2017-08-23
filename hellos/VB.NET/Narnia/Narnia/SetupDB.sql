create database Narnia;
use Narnia;

create table Characters (
	Name varchar(255),
	TotalAppearances int,
	Role varchar(31),
);

insert into Characters values ('Aslan', '7', 'Major');
insert into Characters values ('Digory Kirke', '3', 'Major');
insert into Characters values ('Lucy Pevensie', '5', 'Major');
insert into Characters values ('Reepicheep the Mouse', '3', 'Minor');

create procedure SelectAll as
select * from Characters;

create procedure SelectByAppearances @appNr int as
select Name from Characters where TotalAppearances >= @appNr;


select @@SERVERNAME;
select @@SERVICENAME;
select @@VERSION;
select @@IDENTITY;
select @@OPTIONS;
select CURRENT_USER;
select @@ERROR;

