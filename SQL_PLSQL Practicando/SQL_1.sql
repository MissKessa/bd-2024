1) Get all the data about the cars stored in the entity (table) CARS.
SELECT * FROM CARS;

2) Get all the data about the cars in the CARS table with ‘gtd’ model.
SELECT * FROM CARS WHERE model='gtd';

3) Insert a new car into the CARS entity.
INSERT INTO Cars (codecar, namec, model) VALUES (200,'aa','bbb');

4) Delete an existing car from the CARS entity.
DELETE FROM Cars WHERE codecar=200;

5) Update some data about a given car in the CARS entity.
UPDATE Cars SET codecar=400 WHERE codecar=200;
DELETE FROM Cars WHERE codecar=400;