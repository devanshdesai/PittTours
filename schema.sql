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
    CONSTRAINT Airline_Unique_01 UNIQUE (Airline_Name),   -- Assumption: No two airlines have the same name
    CONSTRAINT Airline_Unique_02 UNIQUE (Airline_Abbreviation)    -- Assumption: No two airlines have the same abbreviation
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
    CONSTRAINT Flight_Check_01 CHECK (Departure_City <> Arrival_City),
    CONSTRAINT Flight_Check_02 CHECK (Departure_Time BETWEEN '0000' AND '2359'),
    CONSTRAINT Flight_Check_03 CHECK (Arrival_Time BETWEEN '0000' AND '2359')
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
    CONSTRAINT Customer_Check_02 CHECK (Frequent_Miles in ('00001','00002','00003','00004','00005','00006','00007','00008','00009','00010', null))
);

CREATE TABLE Reservation (
    Reservation_Number varchar(5),
    CID varchar(9),
    Cost int,
    Credit_Card_Num varchar(16),
    Reservation_Date date,
    Ticketed varchar(1),
    Start_City varchar(3),
    End_City varchar(3),
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


-- Triggers
CREATE OR REPLACE TRIGGER adjustTicket
    AFTER UPDATE OF High_Price, Low_Price ON Price
    FOR EACH ROW
    BEGIN
        IF UPDATING('Low_Price') THEN
            UPDATE Reservation SET
            Cost = :NEW.Low_Price
            WHERE Start_City = :NEW.Departure_City AND End_City = :NEW.Arrival_City;
        ELSE
            UPDATE Reservation SET
            Cost = :NEW.High_Price
            WHERE Start_City = :NEW.Departure_City AND End_City = :NEW.Arrival_City;
        END IF;
        EXCEPTION WHEN NO_DATA_FOUND THEN
            DBMS_OUTPUT.PUT_LINE('No data found');
END;
/

CREATE OR REPLACE TRIGGER planeUpgrade
    AFTER INSERT ON Reservation_Detail
    FOR EACH ROW
    DECLARE
        ownID varchar(5);
        smallPlaneType char(4);
        smallPlaneCapacity int;
        seats int;
    BEGIN
        SELECT COUNT(*) INTO seats FROM Reservation WHERE Reservation_Number = :NEW.Reservation_Number;
        SELECT Plane_Type, Airline_ID INTO smallPlaneType, ownID FROM Flight WHERE Flight_Number = :NEW.Flight_Number;
        SELECT Plane_Capacity INTO smallPlaneCapacity FROM Plane WHERE Plane_Type = smallPlaneType AND Owner_ID = ownID;
        FOR Planes in (SELECT Plane_Type, Plane_Capacity FROM Plane WHERE Owner_ID = ownID) LOOP
			IF Planes.Plane_Capacity > smallPlaneCapacity THEN
				UPDATE Flight SET
					Plane_Type = Planes.Plane_Type
					WHERE Flight_Number = :NEW.Flight_Number;
			END IF;
		END LOOP;
    EXCEPTION WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('No data found');
END;
/

CREATE OR REPLACE TRIGGER cancelReservation
	AFTER UPDATE ON System_Date
	FOR EACH ROW
	DECLARE
		ownID varchar(5);
		biggerPlaneType char(4);
		biggerPlaneCapacity int;
		isTicketed varchar(1);
		dateOfReservation date;
		seats int;
	BEGIN
		FOR reservationRow in (SELECT reservation_Number, Leg, Flight_Number FROM Reservation_Detail) LOOP
			SELECT Reservation_Date, Ticketed into dateOfReservation, isTicketed FROM Reservation WHERE Reservation_Number = reservationRow.Reservation_Number;

			IF (((dateOfReservation - :NEW.C_Date) * 24) <= 12 AND isTicketed = 'N') THEN
				DELETE FROM Reservation_Detail WHERE Leg = reservationRow.leg AND Reservation_Number = reservationRow.Reservation_Number;
				DELETE FROM Reservation WHERE Reservation_Number = reservationRow.Reservation_Number;
			ELSE
                SELECT Plane_Type, Airline_ID into biggerPlaneType, ownID FROM Flight WHERE Flight_Number = reservationRow.Flight_Number;
				SELECT COUNT(*) INTO seats FROM Reservation_Detail WHERE Flight_Number = reservationRow.Flight_Number;
				SELECT Plane_Capacity into biggerPlaneCapacity FROM Plane WHERE Owner_ID = ownID AND Plane_Type = biggerPlaneType;

				FOR planeRow in (SELECT Plane_Type, Plane_Capacity FROM Plane WHERE Owner_ID = ownID) LOOP
					IF planeRow.Plane_Capacity < biggerPlaneCapacity AND planeRow.Plane_Capacity >= seats THEN
						UPDATE Flight SET Plane_Type = planeRow.Plane_Type WHERE Flight_Number = reservationRow.Flight_Number;
					END IF;
				END LOOP;
			END IF;
		END LOOP;
	EXCEPTION WHEN NO_DATA_FOUND THEN
		DBMS_OUTPUT.PUT_LINE('No data found');
END;
/

CREATE OR REPLACE FUNCTION capacity(FN IN varchar2)
    RETURN number is
    lim number;
BEGIN
    SELECT Plane.Plane_Capacity INTO lim FROM Plane
    JOIN Flight ON Plane.Plane_Type = Flight.Plane_Type
    WHERE Flight.flight_number = FN;
    RETURN (lim);
END;
/

CREATE OR REPLACE FUNCTION reserved(FN IN varchar2, DT IN date)
    RETURN number is
    ct number;
BEGIN
    SELECT COUNT(*) INTO ct FROM Reservation_Detail
    WHERE flight_number = FN AND flight_date = DT;
    RETURN (ct);
END;
/
