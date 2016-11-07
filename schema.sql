-- Devansh Desai & Kevin Zhang
-- CS 1555 - Pitt Tours Term Project
-- 11/06/2016


drop table Airline cascade constraints;
drop table Flight cascade constraints;
drop table Plane cascade constraints;
drop table Price cascade constraints;
drop table Customer cascade constraints;
drop table Reservation cascade constraints;

create table Airline (
    Airline_ID varchar(5),
    Airline_Name varchar(50),
    Airline_Abbreviation varchar(10),
    Year_Founded int,
    constraint Airline_PK primary key (Airline_ID)
);

create table Flight (
    Flight_Number varchar(3),
    Airline_ID varchar(5),
    Plane_Type char(4),
    Departure_City varchar(3),
    Arrival_City varchar(3),
    Departure_Time varchar(4),
    Arrival_Time varchar(4),
    Weekly_Schedule varchar(7),
    constraint Flight_PK primary key (Flight_Number),
    constraint Flight_Plane_Type_FK foreign key (Plane_Type) references Plane(Plane_Type),
    constraint Flight_Airline_ID_FK foreign key (Airline_ID) references Airline(Airline_ID)
);

create table Plane (
    Plane_Type char(4),
    Manufacture varchar(10),
    Plane_Capacity int,
    Last_Service date,
    Year int,
    Owner_ID varchar(5),
    constraint Plane_PK primary key (Plane_Type),
    constraint Plane_Owner_ID_FK foreign key (Owner_ID) references Airline(Airline_ID)
);

create table Price (
    Departure_City varchar(3),
    Arrival_City varchar(3),
    Airline_ID varchar(5),
    High_Price int,
    Low_Price int,
    constraint Price_PK primary key (Departure_City, Arrival_City),
    constraint Price_Airline_ID_FK foreign key (Airline_ID) references Airline(Airline_ID)
);

create table Customer (
    CID varchar(9),
    Salutation varchar(3),
    First_Name varchar(30),
    Last_Name varcahr(30),
    Credit_Card_Num varchar(16),
    Credit_Card_Expire date,
    Street varchar(30),
    City varchar(30),
    State varchar(2),
    Phone varchar(10),
    Email varchar(30),
    Frequent_Miles varchar(5),
    constraint Customer_PK primary key (CID)
);
