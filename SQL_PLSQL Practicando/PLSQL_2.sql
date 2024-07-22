1. Add a new attribute DataCaps to the PURCHASES table that stores the name and surname of customers in uppercase. Develop a trigger that keeps the value of that attribute updated.
CREATE OR REPLACE TRIGGER PRUEBA 
BEFORE UPDATE OF NAME,SURNAME OR INSERT ON PLSQL1_9 
FOR EACH ROW
BEGIN
  :new.datacaps:=UPPER(:new.name || ' ' || :new.surname);
END;

2. Create a trigger on table SALES (in own schema). Each time a sale is recorded, the trigger should increment in 1 the number of cars (quantity) in table PURCHASES.
ONLY THE INSERT WAS NEEDED :(

create or replace TRIGGER PRUEBA2 
AFTER INSERT OR UPDATE OF dni ON SALES 
FOR EACH ROW
DECLARE
bought NUMBER;
name Customers.name%TYPE;
surname Customers.surname%TYPE;
old_bought NUMBER;

BEGIN
    SELECT COUNT(*) INTO bought FROM Sales WHERE dni=:new.dni;
    IF INSERTING THEN
        IF bought=0 THEN
            SELECT name, surname INTO name, surname FROM Customers WHERE dni=:new.dni;
            INSERT INTO plsql1_9 (dni, name, surname, carsbought) VALUES (:new.dni, name, surname, 1);
        ELSE
             UPDATE plsql1_9 SET carsbought=carsbought+1 WHERE dni=:new.dni;
        END IF;
       
    ELSE
        IF bought=0 THEN
            SELECT name, surname INTO name, surname FROM Customers WHERE dni=:new.dni;
            INSERT INTO plsql1_9 (dni, name, surname, carsbought) VALUES (:new.dni, name, surname, 1);
        ELSE
             UPDATE plsql1_9 SET carsbought=carsbought+1 WHERE dni=:new.dni;
        END IF;
        
        UPDATE plsql1_9 SET carsbought=carsbought-1 WHERE dni=:old.dni;
        SELECT COUNT(*) INTO old_bought FROM plsql1_9 WHERE dni=:old.dni;
        IF old_bought=0 THEN
            DELETE FROM plsql1_9 WHERE dni=:old.dni;
        END IF;
    END IF;
END;

3. Expand the above trigger so that when a customer deletes a sale, the number of cars in table PURCHASESS is updated correspondingly.
create or replace TRIGGER PRUEBA2 
AFTER INSERT OR DELETE ON SALES 
FOR EACH ROW
DECLARE
bought NUMBER;
name Customers.name%TYPE;
surname Customers.surname%TYPE;

BEGIN
    IF INSERTING THEN
        SELECT COUNT(*) INTO bought FROM plsql1_9 WHERE dni=:new.dni;
        IF bought=0 THEN
            SELECT name, surname INTO name, surname FROM Customers WHERE dni=:new.dni;
            INSERT INTO plsql1_9 (dni, name, surname, carsbought) VALUES (:new.dni, name, surname, 1);
        ELSE
             UPDATE plsql1_9 SET carsbought=carsbought+1 WHERE dni=:new.dni;
        END IF;
       
    ELSE
        SELECT COUNT(*) INTO bought FROM plsql1_9 WHERE dni=:old.dni;
        IF bought=1 THEN
            DELETE FROM plsql1_9 WHERE dni=:old.dni;
        ELSE
             UPDATE plsql1_9 SET carsbought=carsbought-1 WHERE dni=:old.dni;
        END IF;
    
    END IF;
END;

4. Create a table CUSTOMERS_LOG with the following attributes: DniPrev, NamePrev, SurnamePrev, CityPrev, DniCur, NameCur, SurnameCur, CityCur, DateTime. Develop a trigger that manages this table logging 
every update done on the CUSTOMERS table. DateTime stores the date and time in which the update was performed.
create or replace TRIGGER PRUEBA2 
AFTER UPDATE OR INSERT OR DELETE ON CUSTOMERS 
FOR EACH ROW 

BEGIN
    INSERT INTO PLSQL2_4  (DniPrev, NamePrev, SurnamePrev, CityPrev, DniCur, NameCur, SurnameCur, CityCur, DateTime) VALUES 
    (:old.dni, :old.name, :old.surname, :old.city, :new.dni, :new.name, :new.surname,:new.city, SYSDATE);
END;

5. Develop a trigger that decrements the number of cars stocked in a dealer whenever a car is sold, but only in the case that the number of cars stocked in the dealer is greater than 1.
create or replace TRIGGER PRUEBA2 
AFTER INSERT ON SALES 
FOR EACH ROW 
DECLARE
stock Distribution.stockage%TYPE;
BEGIN
    SELECT stockage INTO stock FROM Distribution WHERE :new.codecar=codecar AND :new.cifd=cifd;
    IF stock > 1 THEN
        UPDATE Distribution SET stockage=stockage-1 WHERE :new.codecar=codecar AND :new.cifd=cifd;
    ELSE 
        raise_application_error(-20001, 'There are no cars in stockage');
    END IF;
END;

6. Develop a trigger so that table PURCHASES can be deleted only by the owner of the schema between 11:00 and 13:00.
create or replace TRIGGER PL2_TRIGGER6 
BEFORE DELETE ON PURCHASES 
FOR EACH ROW

DECLARE
    current_user_name VARCHAR2(20);
    table_owner VARCHAR2(20);
    current_hour NUMBER;
BEGIN
    current_user_name:=SYS_CONTEXT('USERENV', 'CURRENT_USER');
    SELECT table_owner INTO table_owner FROM user_triggers WHERE trigger_name='PL2_TRIGGER6';
    current_hour:=TO_NUMBER(TO_CHAR(SYSDATE,'HH24'));

    IF current_user_name <> table_owner THEN
        raise_application_error(-20002, 'You dont own this table, you cannot delete it');
    END IF;
    
    IF current_hour <11 OR current_hour>=13 THEN
        raise_application_error(-20003, 'You can only delete between 11 and 13');
    END IF;
END;

7. Develop a trigger that stores in a PURCHASES_LOG table the operation performed (insert, delete, update), the date of the operation, and the user initiating the operation.
AFTER DELETE OR INSERT OR UPDATE ON PURCHASES 
FOR EACH ROW
DECLARE
    operation_name VARCHAR2(20);
    current_user_name VARCHAR2(20);
BEGIN
    IF INSERTING THEN
        operation_name:='INSERT';
    ELSIF DELETING THEN
        operation_name:='DELETE';
    ELSIF UPDATING THEN
        operation_name:='UPDATE';
    END IF;
    current_user_name:=SYS_CONTEXT('USERENV', 'CURRENT_USER');
    INSERT INTO purchases_log VALUES(operation_name, current_user_name, SYSDATE); 
END;

8. Yellow color has been discontinued, so yellow cars cannot be sold anymore.
create or replace TRIGGER PRUEBA2 
BEFORE INSERT OR UPDATE OF color ON SALES 
FOR EACH ROW 

BEGIN
    IF :new.color='yellow' THEN
        raise_application_error(-2004, 'Yello cannot be sold');
    END IF;
END;

9. No dealer can stock more than 40 cars.
create or replace TRIGGER PRUEBA2 
BEFORE INSERT OR UPDATE ON Distribution 
FOR EACH ROW 

DECLARE
stock Distribution.stockage%TYPE;

BEGIN
    SELECT COALESCE(SUM(stockage),0) INTO stock FROM Distribution WHERE :new.cifd=cifd; /*Returns 0 when the sum is NULL*/
    stock:=stock+:new.stockage;
    IF stock>40 THEN
        raise_application_error(-2005, 'Cannot have more than 40 cars');
    END IF;
END;

10. Dealer 1 closed its doors yesterday. Therefore, this dealer cannot sell from now on.
create or replace TRIGGER PRUEBA2 
BEFORE INSERT OR UPDATE OF cifd ON SALES 
FOR EACH ROW 

BEGIN
    IF :new.cifd='1' THEN
        raise_application_error(-2006, 'Dealer 1 cannot sale');
    END IF;
END;

11. We have detected a possible toxic substance in the paint used to paint red cars. Fortunately, no cars were sold with that toxic before, but from now on, we want to log information about the
cars that are sold. The log should record the cif and name of the dealer, the dni and name of the customer, the code, name and model of the car, and the date and time of the sale.
create or replace TRIGGER PRUEBA2 
AFTER INSERT OR UPDATE OF color ON SALES 
FOR EACH ROW

DECLARE
named Dealers.named%TYPE;
name Customers.name%TYPE;
namecar Cars.namec%TYPE;
model Cars.model%TYPE;

BEGIN
    IF INSERTING THEN
        SELECT named INTO named FROM Dealers WHERE (cifd=:new.cifd);
        SELECT name INTO name FROM Customers WHERE (dni=:new.dni);
        SELECT namec, model INTO namecar, model FROM Cars WHERE :new.codecar=codecar;
        
        INSERT INTO PLSQL2_11 (cifd, named, dni, name, codecar, namecar, modelcar, datesale) VALUES (:new.cifd,named,:new.dni,name, :new.codecar, namecar, model, SYSDATE);
    ELSIF (:old.color='red' AND :new.color!='red') THEN
        DELETE FROM PLSQL2_11 WHERE :old.codecar=codecar AND :old.dni=dni;
    END IF;
END;

12. Circulation of gray cars has been banned by the government. Therefore, gray cars that are sold must be painted in another color: white if the car model is �gtd�, and black if not.
create or replace TRIGGER PRUEBA2 
BEFORE INSERT OR UPDATE on color ON SALES 
FOR EACH ROW WHEN color='gray'
DECLARE
model Cars.model%TYPE;
    
BEGIN
   SELECT model INTO model FROM Cars WHERE :new.codecar=codecar;
    If(model='gtd') THEN
        :new.color='white';
    ELSE 
        :new.color='black';
    END IF;

    EXCEPTION WHEN NO_DATA_FOUND THEN
    raise_application_error(-20010, 'The car doesnt exist'); /*Maybe the codecar is not in the cars table*/
END;