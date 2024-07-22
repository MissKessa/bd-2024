1. Retrieve a relation with two columns, one for the titles and one for the expected revenue for each title. The revenue is calculated as expected sales times price of the title.
SELECT title, expected_sales*price as revenue FROM Titles;

2. Show the titles with expected sales between 200 and 5000 units (copies).
SELECT title, expected_sales as copies FROM Titles WHERE expected_sales BETWEEN 200 AND 5000;

3. Show the name, surname and telephone number for all the authors ordered first by name in ascending order and then by surname in descending order.
SELECT au_name, au_surname, au_telephone FROM Authors ORDER BY au_name ASC, au_surname DESC;

4. Show the name and surname of the authors that do not have telephone number (i.e. it is null).
SELECT au_name, au_surname FROM Authors WHERE au_telephone IS NULL;

5. Show the name, surname and telephone number of all the authors, denoting “no telephone” for those that do not have telephone number. Express the query using the function NVL.
SELECT au_name, au_surname, NVL(au_telephone,'no telephone') FROM authors;

6. Show the title id, title and expected sales for titles with type database (BD) or programming (PROG). Order them descendingly by price. Express the query in two different ways.
SELECT title_id, title, expected_sales FROM Titles WHERE type='BD' OR type='PROG' ORDER BY price DESC;

7. Show the authors with a telephone number starting with ‘456’.
SELECT * FROM Authors WHERE au_telephone LIKE '456%';

8. Show the average price for titles in the relation TITLES. Show average price for titles with BD type. Express the query in two different ways.
SELECT AVG(price) FROM Titles;
SELECT AVG(price) FROM Titles WHERE type='BD';

9. Show the number of titles of each publisher. Number of titles belonging to each type, per publisher.
SELECT pub_id, COUNT(*) FROM Titles GROUP BY pub_id ORDER BY pub_id;
SELECT pub_id, type, COUNT(*) FROM Titles GROUP BY pub_id,type ORDER BY pub_id;

10. Show the number of copies in stock for each type of title. Ignore null values.
SELECT type, COUNT(*) FROM Titles GROUP BY type;

11. Show the average price for each type of title with a publishing date later than year 2000.
SELECT type, AVG(price) FROM (SELECT * FROM Titles WHERE EXTRACT(YEAR FROM d_publishing) >=2000) GROUP BY type;

12. Show the number of copies in stock for each type of title, but only if it is greater than 1.
SELECT type, COUNT(*) as copies FROM Titles HAVING COUNT(*)>1 GROUP BY type;

13. Show the average price for each type of title, but only if it is greater than 35.
SELECT type, AVG(price) FROM Titles HAVING AVG(price)>35 GROUP BY type;

14. Show the average price for the titles of each publisher, but only if its identifier is greater than 2 and the average price is greater than 60. The result should be ordered ascendingly by publisher id.
SELECT pub_id, AVG(price) FROM Titles t HAVING AVG(price)>60 AND pub_id>2 GROUP BY pub_id ORDER BY pub_id ASC;

15. Show the name, surname and editor_order for the title with ‘1’ as identifier.
SELECT ed_name,  ed_surname, editor_order FROM Editors e, TitlesEditors te WHERE e.ed_id=te.ed_id AND te.title_id  = '1';

16. Show the names of the editors and publishers that are in the same city.
SELECT ed_name, pub_name, ed_city FROM Publishers p, Editors e WHERE pub_city=ed_city;

17. Show the titles of BD type books, with the names of the authors and author_order.
SELECT title,author_order,au_id FROM Titles t, TitlesAuthors ta WHERE ta.title_id=t.title_id AND t.type='BD';

18. Show the name and surname of editors with the name of his chief editor.
SELECT e1.ed_name, e1.ed_surname, e2.ed_name FROM Editors e1, Editors e2 WHERE e1.ed_chief=e2.ed_id;

19. Show the data from authors (au_id, au_name and au_surname) that have the same surname.
SELECT a1.au_id, a1.au_name, a1.au_surname, a2.au_id, a2.au_name FROM Authors a1, Authors a2 WHERE a1.au_surname=a2.au_surname AND a1.au_id!=a2.au_id;

20. Show the names of publishers that publish PROG titles. Express the query in two different ways.
SELECT pub_name FROM Publishers p, Titles t WHERE p.pub_id=t.pub_id AND t.type='PROG';

21. Show the title and price of books having the same price as the cheapest book. Same with the most expensive one.
SELECT title, price FROM Titles ORDER BY price ASC FETCH FIRST 1 ROWS WITH TIES;
SELECT title, price FROM Titles ORDER BY price DESC FETCH FIRST 1 ROWS WITH TIES;

22. Show the name and city of authors that live in the same city as ‘Abraham Silberschatz’.
SELECT a.au_name,a.au_city FROM Authors a WHERE a.au_city=(SELECT b.au_city FROM Authors b WHERE b.au_name='Abraham' AND b.au_surname='Silberschatz');

23. Show the name and surname of authors that are both individual authors and co-authors.
SELECT au_name, au_surname FROM Authors a, TitlesAuthors ta, TitlesAuthors tb WHERE a.au_id=ta.au_id AND a.au_id=tb.au_id AND ta.percentage_participation=1 AND tb.percentage_participation<1;

24. Show the types of books that are in common for more than one publisher. Express the query in two different ways.
SELECT DISTINCT t1.type FROM Titles t1, Titles t2 WHERE t1.pub_id!=t2.pub_id AND t1.type=t2.type;

25. Show the types of books with a maximum price at least a 10% more expensive than the average price of that type. And for 20%.
SELECT type FROM Titles HAVING MAX(price)>=AVG(price)*1.1 GROUP BY type;
SELECT type FROM Titles HAVING MAX(price)>=AVG(price)*1.2 GROUP BY type;;


26. Show the books that have a greater pre-publishing than the greatest pre-publishing of ‘Prentice Hall’ publisher.
SELECT title_id, title FROM Titles t WHERE pre_publishing> (SELECT MAX(pre_publishing) FROM Titles d, Publishers p WHERE d.pub_id=p.pub_id AND pub_name LIKE 'Prentice%');

27. Show the titles of the books published by a publisher established in a city starting with ‘B’.
SELECT title FROM Titles t, Publishers p WHERE t.pub_id=p.pub_id AND p.pub_city LIKE 'B%';

28. Show the names of publishers that do not publish BD type books. Express the query in two different ways.
SELECT pub_name FROM Publishers p WHERE p.pub_id NOT IN (SELECT t.pub_id FROM Titles t WHERE t.type='BD');
