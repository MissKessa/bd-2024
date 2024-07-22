2. Calculate Total Sales: Create a procedure that calculates the total sales for each publisher based on the expected_sales from the Titles table. The procedure should take pub_id as input and display the total sales 
for that publisher.
create or replace PROCEDURE PRUEBA(pub Publishers.pub_id%TYPE) AS 
total NUMBER;

BEGIN
    FOR i IN  (SELECT expected_sales FROM Titles WHERE pub_id=pub)LOOP
        total:=total+i.expected_sales;
    END LOOP;
    dbms_output.put_line(total);
    
    
    ---or SELECT COALESCE(SUM(expected_sales),0) INTO total_sales FROM Titles t WHERE t.pub_id=pub_id;
END PRUEBA;

3. Get Publisher Names List: Create a function that returns a comma-separated string of publisher names for which a specific editor (ed_id) works. The function should take ed_id as input and return the publisher names 
as a text string.
create or replace FUNCTION PRUEBAF (ed Editors.ed_id%TYPE) RETURN VARCHAR AS 
string VARCHAR(100);
BEGIN
    string:='';
    FOR i IN (SELECT pub_name FROM Publishers p, Titles t, TitlesEditors te WHERE p.pub_id=t.pub_id AND t.title_id=te.title_id AND te.ed_id=ed)LOOP
        string:=string|| i.pub_name||',';
    END LOOP;
    RETURN string;
END PRUEBAF;

4. Update Title Price: Create a procedure that updates the price of a title based on its title_id. The procedure should take title_id and the new price as input parameters and update the price column in the Titles table.
create or replace PROCEDURE PRUEBA(titleId Titles.title_id%TYPE, newPrice Titles.price%TYPE) AS 

BEGIN
    UPDATE Titles SET price=newPrice WHERE title_id=titleId;
    EXCEPTION WHEN NO_DATA_FOUND THEN
        raise_application_error(-2000, 'No such title id');
    COMMIT;
END PRUEBA;

5. Maintain Author Title Count: Create a trigger that automatically updates the total number of titles for an author. This value will be stored in the new title_count field in the authors table. Additionally, since there
is already existing data, create a procedure that updates the title_count value based on the data already present.
create or replace PROCEDURE PRUEBA AS 

BEGIN
    FOR i IN (SELECT au_id FROM TitlesAuthors) LOOP
        UPDATE PLSQL3_5 SET au_Count=(SELECT COUNT(*) FROM Titles Authors WHERE au_id=i.au_id) WHERE au_id=i.au_id;
    END LOOP;
END PRUEBA;

create or replace TRIGGER PRUEBA5
AFTER INSERT OR UPDATE OF au_id OR DELETE ON TitlesAuthors 
FOR EACH ROW

DECLARE
counter NUMBER;
BEGIN
    IF INSERTING OR UPDATING THEN
        SELECT COALESCE(au_count,0) INTO counter FROM PLSQL3_5 WHERE :new.au_id=au_id;
        UPDATE PLSQL3_5 SET au_count=au_count+1 WHERE :new.au_id=au_id;
    END IF;
    IF DELETING OR UPDATING THEN
        SELECT COALESCE(au_count,0) INTO counter FROM PLSQL3_5 WHERE :old.au_id=au_id;
        IF counter>0 THEN
            UPDATE PLSQL3_5 SET au_count=au_count-1 WHERE :old.au_id=au_id;
        END IF;
    END IF;
    COMMIT;
END;

6. Insert New Author: Create a procedure that inserts a new author into the Authors table. The procedure should use the author’s name, surname, phone_number, address, and city as input parameters.

7. Validate Title Data: Create a trigger that from now on verifies that the d_publishing of titles is greater than January 1, 2024, and that the price is a positive number. It should generate an error if any validation fails.
create or replace TRIGGER VALIDATE_NEW_TITLE
BEFORE INSERT OR UPDATE of d_publishing, price ON titles
FOR EACH ROW
BEGIN

  -- Validate publishing date
  IF :NEW.d_publishing < TO_DATE('01/01/2024', 'DD/MM/YYYY') THEN
    RAISE_APPLICATION_ERROR(-20001, 'Invalid publishing date. It must be after 01/01/2024.');
  END IF;

  -- Validate price
  IF :NEW.price <= 0 THEN
    RAISE_APPLICATION_ERROR(-20002, 'Invalid price. It must be a positive number.');
  END IF;
END;

8. Calculate Title Antiquity: Create a function that calculates the antiquity of a title based on its d_publishing date. The function should take title_id as input and return the title’s antiquity in years.
create or replace FUNCTION CalculateTitleAge(p_title_id IN titles.title_id%TYPE) RETURN NUMBER IS
  v_d_publishing titles.d_publishing%TYPE;
  v_age NUMBER;

BEGIN
  -- Obtener la fecha de publicación del libro
  SELECT d_publishing INTO v_d_publishing
  FROM titles
  WHERE title_id = p_title_id;

  -- Calcular la antigüedad del libro
  v_age := TRUNC(MONTHS_BETWEEN(SYSDATE, v_d_publishing) / 12);

  -- Retornar la antigüedad
  RETURN v_age;

EXCEPTION
  WHEN NO_DATA_FOUND THEN
    RAISE_APPLICATION_ERROR(-20003, 'Title with ID ' || p_title_id || ' not found.');
  
END;

9. Ensure Price Uniqueness for Titles: Create a trigger that prevents duplicate prices for titles in the Titles table from now on. The trigger should generate an error if the new price already exists in the table.

10. Check Author Existence: Create a function that checks if an author already exists in the database based on their name and surname. The function should take the author’s name and surname as input and return a boolean value 
indicating whether the author exists.

11. Calculate Royalties (Author Rights): Create a function that calculates the total royalties for an author based on the title’s price, expected_sales, and the author’s percentage_participation from the TitlesAuthors table. 
The function should use the au_id as input and return the total royalties for the corresponding author.

12. Log Title Updates: Create a trigger that records any updates on the price or title in the Titles table. The trigger should insert a row into the LOG_TITLES table containing the title_id, old and new values of the updated 
columns, the date/time of the update, and the name of the user who performed the operation.

13. Build Stored Procedure ListTitlesByPublisher: Create a stored procedure that generates a listing displaying titles from a publisher along with their authors in the following format:
-Publisher: publisherName1 totalTitles1
--->Title: titleName1 totalAuthors1
------>Author: authorSurname1 authorName1
------>Author: authorSurname2 authorName2
--->Title: titleName2 totalAuthors2
------>Author: authorSurname1 authorName1
------>Author: authorSurname2 authorName2
...
-Publisher: publisherName2 totalTitles2
--->...
-...

create or replace PROCEDURE PRUEBA AS 
totalTitles NUMBER;
totalAuthors NUMBER;
BEGIN
    FOR i IN (SELECT * FROM Publishers) LOOP
        SELECT COUNT(*) INTO totalTitles FROM Titles WHERE pub_id=i.pub_id;
        dbms_output.put_line('-Publisher: '|| i.pub_name || ' ' || totalTitles);
        
        FOR j IN (SELECT * FROM Titles WHERE pub_id=i.pub_id) LOOP 
            SELECT COUNT(*) INTO totalAuthors FROM TitlesAuthors WHERE j.title_id=title_id;
            dbms_output.put_line('--->Title: '|| j.title || ' ' || totalAuthors);
            
            FOR k IN (SELECT a.* FROM Authors a, TitlesAuthors t WHERE t.au_id=a.au_id AND j.title_id=t.title_id) LOOP
                dbms_output.put_line('------>Author: '||k.au_surname || ' ' || k.au_name);
            END LOOP;
        END LOOP;
    END LOOP;
END PRUEBA;


14. Create a stored procedure named ListTitlesByPriceRange based on the previous procedure. This procedure should display only the titles within a specified price range. Include the publication price in the Title 
information shown. The minimum and maximum prices will be two input parameters. 
-Publisher: publisherName1 totalTitles1
--->Title: titleName1 priceSale totalAuthors1
--->Author: authorSurname1 authorName1
--->Author: authorSurname2 authorName2
--->Title: titleName2 priceSale totalAuthors2
--->Author: authorSurname1 authorName1
--->Author: authorSurname2 authorName2
-Publisher: publisherName2 totalTitles2
--->...
-...