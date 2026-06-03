INSERT INTO employee (id, name, role, email, monthly_salary, is_guest) 
VALUES ('ACAD-1000', 'Dr. Alan Turing', 'Professor', 'turing@smartacademic.edu', 120000, false)
ON CONFLICT (id) DO NOTHING;

INSERT INTO employee (id, name, role, email, monthly_salary, is_guest) 
VALUES ('ACAD-1001', 'Prof. Marie Curie', 'Associate Professor', 'curie@smartacademic.edu', 110000, false)
ON CONFLICT (id) DO NOTHING;

INSERT INTO employee (id, name, role, email, monthly_salary, is_guest) 
VALUES ('ACAD-1002', 'Dr. Albert Einstein', 'Department Head', 'einstein@smartacademic.edu', 130000, false)
ON CONFLICT (id) DO NOTHING;
