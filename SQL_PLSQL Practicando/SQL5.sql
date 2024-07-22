1. Last_name, job, salary, and commission_pct of those employees that DO earn commissions. Results should be ordered by job ascendingly and by salary descendingly.
SELECT last_name, job_id, salary, commission_pct FROM Employees WHERE commission_pct IS NOT NULL ORDER BY job_id ASC, salary DESC;

2. Employees with last_name beginning with J, K, L or M.
SELECT last_name FROM Employees WHERE last_name LIKE 'J%' OR last_name LIKE 'K%' OR last_name LIKE 'L%' OR last_name LIKE 'M%';

3. Show last_name, employee_id of employees, together with last_name, employee_id of their managers.
SELECT e.last_name, e.employee_id , m.last_name, m.employee_id FROM Employees e, Employees m WHERE e.manager_id=m.employee_id;

4. Last_name and hire_date for employees hired after employee ‘Davies’.
SELECT last_name, hire_date FROM Employees WHERE hire_date > (SELECT hire_date FROM Employees WHERE last_name='Davies');

5. Last_name and hire_date of employees hired before their managers. Show the last_name and hire_date of their managers as well.
SELECT e.last_name, e.hire_date, m.last_name, m.hire_date FROM Employees e, Employees m WHERE e.manager_id=m.employee_id AND e.hire_date < m.hire_date;

6. Department id, and minimum salary of departments with the greatest average salary.
SELECT department_id, MIN(salary) FROM employees GROUP BY department_id HAVING AVG(salary) >= ALL (SELECT AVG(salary) FROM employees GROUP BY department_id);

7. Department id, name, and location for departments where there are no ‘SA_REP’ (job_id) employees working.
SELECT department_id, department_name, location_id FROM Departments d WHERE department_id NOT IN 
(SELECT department_id FROM Employees e WHERE job_id='SA_REP');

8. Department id, name, and number of employees working at it for departments having: 
a) less than 3 employees 
SELECT d.department_id, d.department_name, COUNT(*) FROM Departments d, Employees e WHERE d.department_id=e.department_id GROUP BY d.department_id, d.department_name HAVING COUNT(*)<3; 

b) the maximum number of employees
SELECT d.department_id, d.department_name, COUNT(*) FROM Departments d, Employees e WHERE d.department_id=e.department_id GROUP BY d.department_id, d.department_name HAVING COUNT(*)>=ALL
(SELECT COUNT(*) FROM Departments d, Employees e WHERE d.department_id=e.department_id GROUP BY d.department_id, d.department_name);

c) the minimum number of employees.
SELECT d.department_id, d.department_name, COUNT(*) FROM Departments d, Employees e WHERE d.department_id=e.department_id GROUP BY d.department_id, d.department_name HAVING COUNT(*)<=ALL
(SELECT COUNT(*) FROM Departments d, Employees e WHERE d.department_id=e.department_id GROUP BY d.department_id, d.department_name);

9. Job title (job_id) that was fulfilled (hire_date) in the first half of 1990, and also in the first half of 1991.
SELECT e1.job_id FROM Employees e1, Employees e2 WHERE e1.job_id=e2.job_id 
AND EXTRACT(YEAR FROM(e1.hire_date))=1990 AND EXTRACT(MONTH FROM(e1.hire_date))<=6 AND EXTRACT(YEAR FROM(e2.hire_date))=1991 AND EXTRACT(MONTH FROM(e2.hire_date))<=6;

10. Show the top 3 employees that earn the most from the employees table. Try the same again but with the two who earn the least.
SELECT last_name,salary FROM Employees e ORDER BY salary DESC FETCH FIRST 3 ROWS ONLY;
SELECT last_name,salary FROM Employees e ORDER BY salary ASC FETCH FIRST 2 ROWS ONLY;

11. Employee_id and surname of employees that work in the state of California.
SELECT employee_id, last_name FROM Employees e, Departments d, Locations l WHERE e.department_id=d.department_id AND d.location_id=l.location_id AND l.state_province='California';

12. Show the employee with the least salary difference compared to his boss. Show the employee_id and salary of both the employee and the boss and the salary difference.
SELECT e.employee_id,e.salary,m.employee_id,m.salary,m.salary-e.salary diferencia FROM employees e,employees m WHERE e.manager_id=m.employee_id AND e.manager_id IS NOT NULL 
ORDER BY diferencia FETCH FIRST 1 ROWS WITH TIES

13. List employees whose salary is not in the appropriate range for their job (job_id). Show, in addition to the employee_id, its salary and the minimum and maximum salaries of the job.
14. List the employee code, their name (first_name) and how many employees they are the manager of, for all employees who are managers of someone else.
15. Show the employee code and name (first_name) of the employee who is the manager of more employees.
16. The salary of employees working in a job whose minimum wage (min_salary) is 4,200 must be increased by 15%.