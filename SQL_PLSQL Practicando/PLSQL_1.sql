2. Code a stored procedure that prints “Hello World”.
CREATE OR REPLACE PROCEDURE PRUEBA AS 
BEGIN
  dbms_output.put_line('Hello world');
END PRUEBA;

3. Code a stored procedure that takes a name as a parameter, and then prints “Hello” + name.
CREATE OR REPLACE PROCEDURE PRUEBA (p VARCHAR) AS 
BEGIN
  dbms_output.put_line('Hello '||p);
END PRUEBA;

4. Code a stored procedure that shows the maximum value for the stockage attribute in table distribution.
CREATE OR REPLACE PROCEDURE PRUEBA AS 

max_stockage Distribution.stockage%TYPE;
BEGIN
    SELECT MAX(stockage) INTO max_stockage FROM Distribution;
    dbms_output.put_line(max_stockage);
END PRUEBA;

5. Code a stored procedure that, given a specific dealer, shows the total number of cars stocked.
CREATE OR REPLACE PROCEDURE PRUEBA (d Dealers.cifd%TYPE) AS 

d_stockage Distribution.stockage%TYPE;
BEGIN
    SELECT SUM(stockage) INTO d_stockage FROM Distribution WHERE cifd=d;
    dbms_output.put_line(d_stockage);
END PRUEBA;

6. Create a table with these attributes: total number of sales, total number of cars, total number of car makers, total number of customers, total number of dealers. Create a stored procedure
that queries the database to compute each of those values, stores them in variables, and then inserts them into the table. Check that the insertion has been performed correctly.
CREATE OR REPLACE PROCEDURE PRUEBA AS 

t_sales NUMBER;
t_cars NUMBER;
t_cms NUMBER;
t_cus NUMBER;
t_deal NUMBER;
BEGIN
    DELETE FROM plsql1_6;
    SELECT COUNT(*) INTO t_sales FROM Sales;
    SELECT COUNT(*) INTO t_cars FROM Cars;
    SELECT COUNT(*) INTO t_cms FROM carmakers;
    SELECT COUNT(*) INTO t_cus FROM customers;
    SELECT COUNT(*) INTO t_deal FROM Dealers;
    
    INSERT INTO plsql1_6 (totalsales,totalcars, totalcarmakers, totalcustomers, totaldealers) VALUES (t_sales, t_cars, t_cms, t_cus, t_deal);
    
END PRUEBA;

7. Create a new table CustomersLog using dni as primary key. Develop a stored procedure that copies the contents from the Customers table into that table. Run the procedure twice.
CREATE OR REPLACE PROCEDURE PRUEBA AS 

BEGIN
    FOR i IN (SELECT dni FROM Customers) LOOP
        INSERT INTO plsql1_7 VALUES(i.dni);
    END LOOP;
    
END PRUEBA;

8. Develop a stored procedure that prints the objects created by the user.
CREATE OR REPLACE PROCEDURE PRUEBA AS 

BEGIN
    FOR i IN (SELECT object_name FROM user_objects /*or select TABLE_NAME FROM TABS*/) LOOP
        dbms_output.put_line(i.object_name);
    END LOOP;
    
END PRUEBA;

9. Create a new table Purchases with the following information: dni, name, surname, number of cars bought (per customer). Develop a PL/SQL procedure that uses a read cursor to insert the information into the new table.
CREATE OR REPLACE PROCEDURE PRUEBA AS 
carsbought NUMBER;
BEGIN
    DELETE PLSQL1_9;
    FOR i IN (SELECT * FROM Customers) LOOP
        SELECT COUNT(*) INTO carsbought FROM Sales WHERE i.dni=dni;
        INSERT INTO PLSQL1_9 (dni, name, surname, carsbought) VALUES(i.dni, i.name, i.surname, carsbought);
    END LOOP;
    COMMIT;
END PRUEBA;

10. Develop a stored procedure that takes a dealer id, and then returns the number of sales that were made in that dealer. Develop a function with the same functionality.
create or replace PROCEDURE PL1_EX10 (dealer Dealers.cifd%TYPE, sales OUT NUMBER) AS 

BEGIN
  SELECT COUNT(*) INTO sales FROM Sales WHERE cifd=dealer;
  
END PL1_EX10;

CREATE OR REPLACE FUNCTION PRUEBAF (d Dealers.cifd%TYPE) RETURN NUMBER AS 
sales NUMBER;
BEGIN
  SELECT COUNT(*) INTO sales FROM Sales s WHERE d=s.cifd;
  RETURN sales;
END PRUEBAF;

11. Develop a PL/SQL function that takes a city and then returns the number of customers in that city. Develop a stored procedure with the same functionality.
CREATE OR REPLACE FUNCTION PRUEBAF (ci Customers.city%TYPE) RETURN NUMBER AS 
cust NUMBER;
BEGIN
  SELECT COUNT(*) INTO cust FROM Customers c WHERE c.city=ci;
  RETURN cust;
END PRUEBAF;

12. Develop a stored procedure called ListCarsByCustomer. The procedure should generate a report with the cars that have been bought by each customer with this template:
- Customer: name1 surname1 numcars1 numdeal1
---> Car: codecar1 namec1 model1 color1
---> Car: codecar2 namec2 model2 color2
---> . . .
- Customer: name2 surname2 numcars2 numdeal2
---> Car: codecar1 namec1 model1 color1
---> Car: codecar2 namec2 model2 color2
---> . . .
- . . .
Customers that have bought no cars should not be shown in the report.
create or replace PROCEDURE PRUEBA AS 

name Customers.name%TYPE;
surname Customers.name%TYPE;
numcars NUMBER;
numDeal NUMBER;

nameCar Cars.namec%TYPE;
model Cars.model%TYPE;
color Sales.color%TYPE;

BEGIN
    FOR i IN (SELECT dni FROM Sales) LOOP
        SELECT name, surname INTO name,surname FROM Customers WHERE i.dni=dni;
        SELECT COUNT(codecar)INTO numcars FROM Sales WHERE i.dni=dni;
        SELECT COUNT(DISTINCT cifd) INTO numDeal FROM Sales WHERE i.dni=dni;
        dbms_output.put_line('- Customer: '||name || ' ' || surname || ' ' || numcars || ' ' || numDeal);
        
        BEGIN
        FOR j IN (SELECT s.codecar FROM Sales s WHERE s.dni=i.dni) LOOP
            SELECT namec, model INTO nameCar, model FROM Cars WHERE j.codecar=codecar;
            SELECT color INTO color FROM Sales WHERE j.codecar=codecar AND i.dni=dni;
            dbms_output.put_line('---> Car: '|| j.codecar || ' ' || nameCar || ' ' || model || ' ' || color);
        END LOOP;
        END;
        
    END LOOP;
END PRUEBA;

13. Develop a stored procedure ListCarsOneCustomer that does the same as the previous one, but just for one customer. The dni of that customer should be passed as an in parameter of the procedure.
create or replace PROCEDURE PRUEBA(givenDni Customers.dni%TYPE) AS 

name Customers.name%TYPE;
surname Customers.name%TYPE;
numcars NUMBER;
numDeal NUMBER;

nameCar Cars.namec%TYPE;
model Cars.model%TYPE;
color Sales.color%TYPE;

BEGIN
   
        SELECT name, surname INTO name,surname FROM Customers WHERE givenDni=dni;
        SELECT COUNT(codecar)INTO numcars FROM Sales WHERE givenDni=dni;
        SELECT COUNT(DISTINCT cifd) INTO numDeal FROM Sales WHERE givenDni=dni;
        dbms_output.put_line('- Customer: '||name || ' ' || surname || ' ' || numcars || ' ' || numDeal);
        
       
        FOR j IN (SELECT s.codecar FROM Sales s WHERE s.dni=givenDni) LOOP
            SELECT namec, model INTO nameCar, model FROM Cars WHERE j.codecar=codecar;
            SELECT color INTO color FROM Sales WHERE j.codecar=codecar AND givenDni=dni;
            dbms_output.put_line('---> Car: '|| j.codecar || ' ' || nameCar || ' ' || model || ' ' || color);
        END LOOP;
        
        
END PRUEBA;

14. Create a labPL1 package including all procedures and functions defined before.
create or replace PACKAGE PL1 AS 
    PROCEDURE PL1_EX2;
    PROCEDURE PL1_EX3 (NAME IN VARCHAR2);
    PROCEDURE PL1_EX4;
    PROCEDURE PL1_EX5 (dealer Dealers.cifd%TYPE);
    PROCEDURE PL1_EX6;
    PROCEDURE PL1_EX7;
    PROCEDURE PL1_EX7_1;
    PROCEDURE PL1_EX8;
    PROCEDURE PL1_EX9;
    PROCEDURE PL1_EX10 (dealer Dealers.cifd%TYPE, sales OUT NUMBER);
    FUNCTION PL1_EX10_FUNCTION (dealer Dealers.cifd%TYPE) RETURN NUMBER;
    FUNCTION PL1_EX11 (givenCity Customers.city%TYPE) RETURN NUMBER;
    PROCEDURE PL1_EX12;
    PROCEDURE PL1_EX13 (dni_cus IN Customers.dni%TYPE);
END PL1;