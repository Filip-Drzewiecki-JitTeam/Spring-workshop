
insert into ADDRESS (id, city, street, postal)
values (1, 'Gdansk', 'Luzycka 8c/11', '80200');
insert into ADDRESS (id, city, street, postal)
values (2, 'Kartuzy', 'Rynkowa 7', '80100');

insert into COMPANY (id, name)
values (1, 'Reversed');

insert into EMPLOYEE (id, name, salary, position, address_id, company_id)
values (1, 'Bob', 10000, 'CEO', 1, 1);
insert into EMPLOYEE (id, name, salary, position, address_id)
values (2, 'William', 1000, 'ACCOUNTANT', 2);
