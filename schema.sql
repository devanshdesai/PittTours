-- Devansh Desai & Kevin Zhang
-- CS 1555 - Pitt Tours Term Project
-- 11/06/2016

-- Remove all tables if they currently exist in the database
drop table Airline cascade constraints;
drop table Flight cascade constraints;
drop table Plane cascade constraints;
drop table Price cascade constraints;
drop table Customer cascade constraints;
drop table Reservation cascade constraints;
drop table Reservation_Detail cascade constraints;
drop table System_Date cascade constraints;

-- Start the creation of the database tables
create table Airline (
    Airline_ID varchar(5),
    Airline_Name varchar(50),
    Airline_Abbreviation varchar(10),
    Year_Founded int,
    constraint Airline_PK primary key (Airline_ID),
    constraint Airline_Unique_01 unique (Airline_Name),   -- Assumption: No two airlines have the same name
    constraint Airline_Unique_02 unique (Airline_Abbreviation)    -- Assumption: No two airlines have the same abbreviation
);

create table Plane (
    Plane_Type char(4),
    Manufacture varchar(10),
    Plane_Capacity int,
    Last_Service date,
    Year int,
    Owner_ID varchar(5),
    constraint Plane_PK primary key (Plane_Type, Owner_ID),
    constraint Plane_FK_01 foreign key (Owner_ID) references Airline(Airline_ID)
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
    constraint Flight_FK_01 foreign key (Plane_Type, Airline_ID) references Plane(Plane_Type, Owner_ID),
    constraint Flight_FK_02 foreign key (Airline_ID) references Airline(Airline_ID),
    constraint Departure_Arrival_Cities check (Departure_City <> Arrival_City),
    constraint Flight_Check_01 check (Departure_Time >= 0000 and Departure_Time <= 2359 and Arrival_Time >= 0000 and Arrival_Time <= 2359)
);

create table Price (
    Departure_City varchar(3),
    Arrival_City varchar(3),
    Airline_ID varchar(5),
    High_Price int,
    Low_Price int,
    constraint Price_PK primary key (Departure_City, Arrival_City),
    constraint Price_FK_01 foreign key (Airline_ID) references Airline(Airline_ID)
);

create table Customer (
    CID varchar(9),
    Salutation varchar(3),
    First_Name varchar(30),
    Last_Name varchar(30),
    Credit_Card_Num varchar(16),
    Credit_Card_Expire date,
    Street varchar(30),
    City varchar(30),
    State varchar(2),
    Phone varchar(10),
    Email varchar(30),
    Frequent_Miles varchar(5),
    constraint Customer_PK primary key (CID),
    constraint Customer_Check_01 check (Salutation in ('Mr', 'Mrs', 'Ms'))
    constraint Frequent_Miles_Check check (Frequent_Miles in (SELECT Airline_ID FROM Airline))
);

create table Reservation (
    Reservation_Number varchar(5),
    CID varchar(9),
    Cost int,
    Credit_Card_Num varchar(16),
    Reservation_Date date,
    Ticketed varchar(1),
    constraint Reservation_PK primary key (Reservation_Number),
    constraint Reservation_FK_01 foreign key (CID) references Customer(CID)
);

create table Reservation_Detail (
    Reservation_Number varchar(5),
    Flight_Number varchar(3),
    Flight_Date date,
    Leg int,
    constraint Reservation_Detail_PK primary key (Reservation_Number, Leg),
    constraint Reservation_Detail_FK_01 foreign key (Reservation_Number) references Reservation(Reservation_Number),
    constraint Reservation_Detail_FK_02 foreign key (Flight_Number) references Flight(Flight_Number)
);

create table System_Date (
    C_Date date,
    constraint Date_PK primary key (C_Date)
);

commit;

