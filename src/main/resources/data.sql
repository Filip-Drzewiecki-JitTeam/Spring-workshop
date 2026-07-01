
insert into ADDRESS (id, city, street, postal) values (1,  'Gdansk',   'Luzycka 8c/11',   '80200');
insert into ADDRESS (id, city, street, postal) values (2,  'Kartuzy',  'Rynkowa 7',        '80100');
insert into ADDRESS (id, city, street, postal) values (3,  'Warsaw',   'Marszalkowska 12', '00001');
insert into ADDRESS (id, city, street, postal) values (4,  'Krakow',   'Florianska 3',     '31001');
insert into ADDRESS (id, city, street, postal) values (5,  'Wroclaw',  'Swidnicka 44',     '50001');
insert into ADDRESS (id, city, street, postal) values (6,  'Poznan',   'Polwiejska 22',    '61001');
insert into ADDRESS (id, city, street, postal) values (7,  'Gdynia',   'Swietojanska 10',  '81001');
insert into ADDRESS (id, city, street, postal) values (8,  'Lodz',     'Piotrkowska 5',    '90001');
insert into ADDRESS (id, city, street, postal) values (9,  'Lublin',   'Krakowskie 8',     '20001');
insert into ADDRESS (id, city, street, postal) values (10, 'Katowice', 'Stawowa 15',       '40001');
insert into ADDRESS (id, city, street, postal) values (11, 'Szczecin', 'Bogurodzicy 2',    '70001');
insert into ADDRESS (id, city, street, postal) values (12, 'Bydgoszcz','Dluga 9',          '85001');

insert into COMPANY (id, name) values (1, 'Reversed');
insert into COMPANY (id, name) values (2, 'Tera');

insert into EMPLOYEE (id, name, surname, personal_id, salary, annual_income, position, address_id, company_id)
values (1,  'Bob',      'Smith',      'PL001', 10000, 120000, 'CEO',        1,  1);
insert into EMPLOYEE (id, name, surname, personal_id, salary, annual_income, position, address_id, company_id)
values (2,  'William',  'Jones',      'PL002',  1000,  12000, 'ACCOUNTANT', 2,  2);
insert into EMPLOYEE (id, name, surname, personal_id, salary, annual_income, position, address_id, company_id)
values (3,  'Anna',     'Kowalski',   'PL003',  5500,  66000, 'MANAGER',    3,  1);
insert into EMPLOYEE (id, name, surname, personal_id, salary, annual_income, position, address_id, company_id)
values (4,  'Marek',    'Nowak',      'PL004',  4200,  50400, 'ADVISOR',    4,  2);
insert into EMPLOYEE (id, name, surname, personal_id, salary, annual_income, position, address_id, company_id)
values (5,  'Karolina', 'Wisniewska', 'PL005',  6700,  80400, 'MANAGER',    5,  1);
insert into EMPLOYEE (id, name, surname, personal_id, salary, annual_income, position, address_id, company_id)
values (6,  'Piotr',    'Wojcik',     'PL006',  3200,  38400, 'ACCOUNTANT', 6,  2);
insert into EMPLOYEE (id, name, surname, personal_id, salary, annual_income, position, address_id, company_id)
values (7,  'Ewa',      'Kaminska',   'PL007',  7200,  86400, 'ADVISOR',    7,  1);
insert into EMPLOYEE (id, name, surname, personal_id, salary, annual_income, position, address_id, company_id)
values (8,  'Tomasz',   'Lewandowski','PL008',  3800,  45600, 'ACCOUNTANT', 8,  2);
insert into EMPLOYEE (id, name, surname, personal_id, salary, annual_income, position, address_id, company_id)
values (9,  'Monika',   'Dabrowska',  'PL009',  2500,  30000, 'ADVISOR',    9,  1);
insert into EMPLOYEE (id, name, surname, personal_id, salary, annual_income, position, address_id, company_id)
values (10, 'Lukasz',   'Szymanski',  'PL010',  9500, 114000, 'CEO',        10, 2);
insert into EMPLOYEE (id, name, surname, personal_id, salary, annual_income, position, address_id, company_id)
values (11, 'Natalia',  'Zielinska',  'PL011',  4500,  54000, 'MANAGER',    11, 1);
insert into EMPLOYEE (id, name, surname, personal_id, salary, annual_income, position, address_id, company_id)
values (12, 'Rafal',    'Krawczyk',   'PL012',  1800,  21600, 'ACCOUNTANT', 12, 2);

-- Reset sequences so new inserts don't collide with seed data IDs
ALTER TABLE ADDRESS  ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE COMPANY  ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE EMPLOYEE ALTER COLUMN ID RESTART WITH 100;

