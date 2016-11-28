-- Devansh Desai & Kevin Zhang
-- CS 1555 - Pitt Tours Term Project
-- 11/28/2016

-- Remove all tables if they currently exist in the database
DROP TABLE Airline CASCADE CONSTRAINTS;
DROP TABLE Flight CASCADE CONSTRAINTS;
DROP TABLE Plane CASCADE CONSTRAINTS;
DROP TABLE Price CASCADE CONSTRAINTS;
DROP TABLE Customer CASCADE CONSTRAINTS;
DROP TABLE Reservation CASCADE CONSTRAINTS;
DROP TABLE Reservation_Detail CASCADE CONSTRAINTS;
DROP TABLE System_Date CASCADE CONSTRAINTS;

-- Start the creation of the database tables
CREATE TABLE Airline (
    Airline_ID varchar(5) NOT NULL,
    Airline_Name varchar(50) NOT NULL,
    Airline_Abbreviation varchar(10),
    Year_Founded int,
    CONSTRAINT Airline_PK PRIMARY KEY (Airline_ID),
    CONSTRAINT Airline_Unique_01 unique (Airline_Name),   -- Assumption: No two airlines have the same name
    CONSTRAINT Airline_Unique_02 unique (Airline_Abbreviation)    -- Assumption: No two airlines have the same abbreviation
);

CREATE TABLE Plane (
    Plane_Type char(4) NOT NULL,
    Manufacture varchar(10),
    Plane_Capacity int,
    Last_Service date,
    Year int,
    Owner_ID varchar(5) NOT NULL,
    CONSTRAINT Plane_PK PRIMARY KEY (Plane_Type, Owner_ID),
    CONSTRAINT Plane_FK_01 FOREIGN KEY (Owner_ID) REFERENCES Airline(Airline_ID)
);

CREATE TABLE Flight (
    Flight_Number varchar(3) NOT NULL,
    Airline_ID varchar(5) NOT NULL,
    Plane_Type char(4) NOT NULL,
    Departure_City varchar(3),
    Arrival_City varchar(3),
    Departure_Time varchar(4),
    Arrival_Time varchar(4),
    Weekly_Schedule varchar(7),
    CONSTRAINT Flight_PK PRIMARY KEY (Flight_Number),
    CONSTRAINT Flight_FK_01 FOREIGN KEY (Plane_Type, Airline_ID) REFERENCES Plane(Plane_Type, Owner_ID),
    CONSTRAINT Flight_FK_02 FOREIGN KEY (Airline_ID) REFERENCES Airline(Airline_ID),
    CONSTRAINT Departure_Arrival_Cities CHECK (Departure_City <> Arrival_City),
    CONSTRAINT Flight_Check_01 CHECK (Departure_Time BETWEEN '0000' AND '2359'),
    CONSTRAINT Flight_Check_02 CHECK (Arrival_Time BETWEEN '0000' AND '2359')
);

CREATE TABLE Price (
    Departure_City varchar(3) NOT NULL,
    Arrival_City varchar(3) NOT NULL,
    Airline_ID varchar(5) NOT NULL,
    High_Price int,
    Low_Price int,
    CONSTRAINT Price_PK PRIMARY KEY (Departure_City, Arrival_City, Airline_ID),
    CONSTRAINT Price_FK_01 FOREIGN KEY (Airline_ID) REFERENCES Airline(Airline_ID)
);

CREATE TABLE Customer (
    CID varchar(9) NOT NULL,
    Salutation varchar(3) NOT NULL,
    First_Name varchar(30) NOT NULL,
    Last_Name varchar(30) NOT NULL,
    Credit_Card_Num varchar(16),
    Credit_Card_Expire date,
    Street varchar(30),
    City varchar(30),
    State varchar(2),
    Phone varchar(10),
    Email varchar(30),
    Frequent_Miles varchar(5),
    CONSTRAINT Customer_PK PRIMARY KEY (CID),
    CONSTRAINT Customer_Check_01 CHECK (Salutation in ('Mr', 'Mrs', 'Ms')),
    CONSTRAINT Frequent_Miles_Check CHECK (Frequent_Miles in ('001','002','003','004','005','006','007','008','009','010', null))
);

CREATE TABLE Reservation (
    Reservation_Number varchar(5),
    CID varchar(9),
    Cost int,
    Credit_Card_Num varchar(16),
    Reservation_Date date,
    Ticketed varchar(1),
    Departure_City varchar(3),
    Arrival_City varchar(3),
    CONSTRAINT Reservation_PK PRIMARY KEY (Reservation_Number),
    CONSTRAINT Reservation_FK_01 FOREIGN KEY (CID) REFERENCES Customer(CID)
);

CREATE TABLE Reservation_Detail (
    Reservation_Number varchar(5),
    Flight_Number varchar(3),
    Flight_Date date,
    Leg int,
    CONSTRAINT Reservation_Detail_PK PRIMARY KEY (Reservation_Number, Leg),
    CONSTRAINT Reservation_Detail_FK_01 FOREIGN KEY (Reservation_Number) REFERENCES Reservation(Reservation_Number),
    CONSTRAINT Reservation_Detail_FK_02 FOREIGN KEY (Flight_Number) REFERENCES Flight(Flight_Number)
);

CREATE TABLE System_Date (
    C_Date date NOT NULL,
    CONSTRAINT Date_PK PRIMARY KEY (C_Date)
);

commit;

--Triggers

CREATE OR REPLACE TRIGGER adjustTicket
AFTER UPDATE OF high_price, low_price ON PRICE
FOR EACH ROW
DECLARE
    reservationNumber varchar(5);
    startCity varchar(3);
    endCity varchar(3);
BEGIN
    IF UPDATING('low_price') THEN
        UPDATE RESERVATION SET
        cost = :NEW.low_price
        WHERE Start_City = :NEW.departure_city AND End_City = :NEW.arrival_city;
    ELSE
        UPDATE RESERVATION SET
        cost = :NEW.high_price
        WHERE Start_City = :NEW.departure_city AND End_City = :NEW.arrival_city;
    END IF;
    EXCEPTION WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('No data found');
END;
/
