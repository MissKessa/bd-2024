1. Names of customers with both accounts and loans at Perryridge branch. Express the query in several different ways (using EXISTS, using IN, using join tables and using INTERSECT).
SELECT ah.cus_name FROM AccountHolder ah, Account a, Borrower b, Loan l WHERE 
ah.acc_number=a.acc_number AND b.lo_number=l.lo_number AND ah.cus_name=b.cus_name AND
a.br_name=l.br_name AND a.br_name='Perryridge';

2. Names of customers with an account but not a loan at Perryridge branch. Express the query in several different ways (using EXISTS/NOT EXISTS, using IN/NOT IN or using MINUS/EXCEPT).
SELECT ah.cus_name FROM AccountHolder ah, Account a WHERE 
ah.acc_number=a.acc_number AND a.br_name='Perryridge' AND ah.cus_name NOT IN 
(SELECT b.cus_name FROM Borrower b, Loan l  WHERE 
b.lo_number=l.lo_number AND l.br_name='Perryridge');

3. Names of customers with accounts at a branch where Hayes has an account.
SELECT DISTINCT ah.cus_name FROM AccountHolder ah, Account a WHERE 
ah.acc_number=a.acc_number AND a.br_name IN (SELECT a2.br_name FROM AccountHolder ah2, Account a2 WHERE
ah2.acc_number=a2.acc_number AND ah2.cus_name='Hayes');

4. Names of branches whose banking assets are greater than the assets of SOME branch in Brooklyn.
SELECT b.br_name FROM Branch b WHERE b.asset > SOME (SELECT asset FROM Branch WHERE br_city='Brooklyn');

5. Names of branches whose assets are greater than the assets of EVERY branch in Brooklyn.
SELECT b.br_name FROM Branch b WHERE b.asset > ALL (SELECT asset FROM Branch WHERE br_city='Brooklyn');

6. Names of customers at Perryridge branch, ordered by name.
SELECT c.cus_name FROM Customer c, AccountHolder ah, Account a
WHERE (ah.acc_number= a.acc_number AND a.br_name='Perryridge' AND c.cus_name=ah.cus_name) UNION
(SELECT c.cus_name FROM  Customer c,  Borrower b, Loan l  WHERE b.lo_number= l.lo_number AND l.br_name='Perryridge' AND c.cus_name=b.cus_name) ORDER BY cus_name;

7. Name of branch having largest average balance.
SELECT br_name FROM Account a GROUP BY br_name HAVING AVG(a.balance) >= ALL (SELECT AVG(balance) FROM Account GROUP BY br_name);

8. Average balance of all customers in Harrison having at least 2 accounts.
SELECT AVG(balance) FROM Account a, AccountHolder ac, Customer c WHERE c.cus_name=ac.cus_name AND a.acc_number=ac.acc_number
AND c.cus_city='Harrison' AND (SELECT COUNT(*) FROM AccountHolder ac2 WHERE c.cus_name=ac2.cus_name)>=2 GROUP BY c.cus_name;


EXTRA
(Based on query 6) Create a stored procedure that displays the names of customers at a branch (sorted alphabetically). The branch name is received as a parameter. Additionally, show the loans and/or accounts that each
customer has at that branch. First, display any loans (if they exist), followed by all accounts (if they exist). The information should be presented as follows:

Customer: customer_name1
--- >Loan: lo_number amount
--- >Loan: lo_number amount
...
--- >Account: acc_number balance
--- >Account: acc_number balance
Customer: customer_name2
--- >Loan: lo_number amount
--- >Loan: lo_number amount
...
--- >Account: acc_number balance
--- >Account: acc_number balance

create or replace PROCEDURE PRUEBA(br Branch.br_name%TYPE) AS 

BEGIN
    FOR i IN (SELECT c.cus_name FROM Customer c, AccountHolder ah, Account a
WHERE (ah.acc_number= a.acc_number AND a.br_name=br AND c.cus_name=ah.cus_name) UNION
(SELECT c.cus_name FROM  Customer c,  Borrower b, Loan l  WHERE b.lo_number= l.lo_number AND l.br_name=br AND c.cus_name=b.cus_name) ORDER BY cus_name) LOOP
        dbms_output.put_line('Customer: ' || i.cus_name );
        
        FOR j IN (SELECT l.* FROM Loan l, Borrower b WHERE b.cus_name=i.cus_name AND b.lo_number=l.lo_number AND l.br_name=br)LOOP
            dbms_output.put_line('--- >Loan: ' || j.lo_number || ' ' || j.amount);
        END LOOP;
        
         FOR j IN (SELECT a.* FROM Account a, AccountHolder h WHERE h.cus_name=i.cus_name AND h.acc_number=a.acc_number AND a.br_name=br)LOOP
            dbms_output.put_line('--- >Account: ' || j.acc_number || ' ' || j.balance);
        END LOOP;
    END LOOP;
END PRUEBA;