1. Tuples from the CARMAKERS relation having ‘barcelona’ in the city attribute.
SELECT * FROM Carmakers a WHERE a.citycm='barcelona';

2. Tuples from the CUSTOMERS relation for ‘madrid’ customers with ‘garcia’ surname. The same for customers having either one or the other condition.
SELECT * FROM Customers WHERE city='madrid' AND surname='garcia';

3. Get a relation having the values of the surname and city attributes from the CUSTOMERS relation.
SELECT surname, city FROM Customers;

4. Get a relation showing the surnames of the CUSTOMERS from ‘madrid’.
SELECT surname FROM Customers WHERE city='madrid';

5. Names of car makers having ‘gtd’ models.
SELECT namecm FROM Carmakers cm, CmCars x WHERE cm.cifcm=x.cifcm AND x.codecar IN 
(SELECT c.codecar FROM Cars c WHERE c.model='gtd');

6. Names of car makers that have sold red cars.
SELECT namecm FROM Carmakers cm, CmCars x, Sales s WHERE cm.cifcm=x.cifcm AND x.codecar=s.codecar AND s.color='red';

7. Names of cars having the same models as the car named ‘cordoba’.
SELECT namec FROM Cars c WHERE c.model = (SELECT model FROM Cars c2 WHERE c2.namec='cordoba');

8. Names of cars NOT having a ‘gtd’ model.
SELECT DISTINCT namec FROM Cars c WHERE namec NOT IN (SELECT namec FROM Cars c2 WHERE c2.model='gtd');

9. Pairs of values: CIFC from the CARMAKERS relation, and DNI from the CUSTOMERS relation belonging to the same city. The same for the ones not belonging to the same city.
SELECT cifcm,dni FROM Carmakers cm, Customers c WHERE cm.citycm=c.city;
SELECT cifcm,dni FROM Carmakers cm, Customers c WHERE cm.citycm!=c.city;

10. Codecar values for the cars stocked in any dealer in ‘barcelona’.
SELECT codecar FROM Distribution dis, Dealers d WHERE dis.cifd=d.cifd AND d.cityd='barcelona';

11. Codecar values for cars bought by a ‘madrid’ customer in a ‘madrid’ dealer.
SELECT s.codecar FROM Sales s, Customers c, Dealers d WHERE s.dni=c.dni AND s.cifd=d.cifd AND c.city='madrid' AND d.cityd='madrid';

12. Codecar values for cars sold in a dealer in the same city as the buying customer.
SELECT s.codecar FROM Sales s, Customers c, Dealers d WHERE s.dni=c.dni AND d.cifd=s.cifd AND d.cityd=c.city;

13. Pairs of CARMAKERS (names) from the same city.
SELECT c.namecm, d.namecm FROM Carmakers c, Carmakers d WHERE c.citycm=d.citycm AND c.cifcm!=d.cifcm;

14. DNI of the customers that have bought a car in a ‘madrid’ dealer.
SELECT DISTINCT c.dni FROM Customers c, Sales s, Dealers d WHERE s.dni=c.dni AND s.cifd=d.cifd AND d.cityd='madrid';

15. Colors of cars sold by the ‘acar’ dealer.
SELECT color FROM Sales s, Dealers d WHERE s.cifd=d.cifd AND d.named='acar';

16. Codecar of the cars sold by any ‘madrid’ dealer.
SELECT DISTINCT codecar FROM Sales s, Dealers d WHERE s.cifd=d.cifd AND d.cityd='madrid';

17. Names of customers that have bought any car in the ‘dcar’ dealer.
SELECT name FROM Customers c WHERE c.dni IN (SELECT s.dni FROM Sales s, Dealers d WHERE s.cifd=d.cifd AND d.named='dcar' );

18. Name and surname of the customers that have bought a ‘gti’ model with ‘white’ color.
SELECT name, surname FROM Customers c, Sales s, Cars c WHERE s.dni=c.dni AND c.codecar=s.codecar AND c.model='gti' AND s.color='white';

19. Name and surname of customers that have bought a car in a ‘madrid’ dealer that has ‘gti’ cars.
SELECT name, surname FROM Customers c WHERE dni IN (SELECT dni FROM Sales s, Dealers d WHERE s.cifd=d.cifd AND d.cityd='madrid' AND s.cifd IN 
(SELECT cifd FROM Distribution dis, Cars c WHERE dis.codecar=c.codecar AND c.model='gti'));

20. Name and surname of customers that have bought (at least) a ‘white’ and a ‘red’ car.
SELECT name, surname FROM Customers c WHERE dni IN 
(SELECT dni FROM Sales s WHERE s.color='white') AND dni IN
(SELECT dni FROM Sales s WHERE s.color='red');

21. DNI of customers that have only bought cars at the dealer with CIFD ‘1’.
SELECT DISTINCT dni FROM Sales WHERE cifd='1' AND dni NOT IN (SELECT dni FROM Sales WHERE cifd!='1');

22. Names of the customers that have NOT bought ‘red’ cars at ‘madrid’ dealers.
SELECT c.name FROM Customers c WHERE c.dni NOT IN (SELECT dni FROM Sales s, Dealers d WHERE s.color='red' AND s.cifd=d.cifd AND d.cityd='madrid');

23. For each dealer (cifd), show the total amount of cars stocked.
SELECT cifd, SUM(stockage) FROM Distribution d GROUP BY cifd;

24. Show the cifd of dealers with an average stockage of more than 10 units (show that average as well)
SELECT cifd, AVG(stockage) FROM Distribution d HAVING AVG(stockage)>10 GROUP BY cifd;

25. CIFD of dealers with a stock between 10 and 18 units inclusive.
SELECT cifd, SUM(stockage) FROM Distribution d HAVING SUM(stockage) BETWEEN 10 AND 18 GROUP BY cifd;

26. Total amount of car makers. Total amount of cities with car makers.
SELECT COUNT(*) FROM Carmakers;
SELECT COUNT(DISTINCT citycm) FROM Carmakers;

27. Name and surname of customers that have bought a car in a ‘madrid’ dealer, and have a namestarting with j.
SELECT name, surname FROM Customers c, Sales s, Dealers d WHERE c.dni=s.dni AND s.cifd=d.cifd AND d.cityd='madrid' AND c.name LIKE 'j%';

28. List customers ordered by name (ascending).
SELECT * FROM CUSTOMERS ORDER BY name ASC;

29. List customers that have bought a car in the same dealer as customer with dni ‘2’ (excluding the customer with dni ‘2’). Same with dni ‘1’.
SELECT DISTINCT name FROM Customers c, Sales s WHERE s.dni=c.dni AND s.cifd IN (SELECT cifd FROM Sales s WHERE s.dni='2') AND s.dni!='2';
SELECT DISTINCT name FROM Customers c, Sales s WHERE s.dni=c.dni AND s.cifd IN (SELECT cifd FROM Sales s WHERE s.dni='1') AND s.dni!='1';

30. Return a list with the dealers which have a total number of car units in stock greater than the global unit average of all dealers together.
SELECT  distribution.cifd,named, cityd
FROM  distribution,dealers
WHERE distribution.cifd = dealers.cifd
GROUP BY distribution.cifd,named,cityd
HAVING SUM(stockage)> (SELECT  AVG(total) FROM (SELECT SUM(stockage)total FROM  distribution GROUP BY cifd));

31. Dealer having the best average stockage of all dealers; that is, dealer having an average stockage greater than the average stockage of each one of the remaining dealers.
SELECT cifd, AVG(stockage) final_avg FROM distribution GROUP BY cifd
HAVING AVG(stockage) >= ALL (SELECT AVG (stockage) avg_stockage FROM distribution GROUP by cifd);

32. List the two customers that have bought more cars in total, ordered by the number of cars bought. List the sales of cars ordered by color. We want to remove the first one and obtain the next two allowing for ties
(and without allowing them);
SELECT s.dni, COUNT(dni) as total FROM Sales s GROUP BY dni ORDER BY total DESC FETCH FIRST 2 ROWS ONLY;
SELECT * FROM Sales s ORDER BY color;

33. Create a view from query 34. Using such view, list for each of the customers who have bought more cars in total, the code of the cars bought, the cifd of the dealers where they were bought, and the color.
CREATE VIEW SQL2_34 AS 
SELECT s.dni, COUNT(dni) as total FROM Sales s GROUP BY dni ORDER BY total DESC FETCH FIRST 2 ROWS ONLY; 

SELECT * FROM SQL2_34;
SELECT x.dni, codecar, cifd, color FROM SQL2_34 x, Sales s WHERE s.dni=x.dni;
