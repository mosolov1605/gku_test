drop table if exists usr;
drop table if exists company;
drop table if exists document;

create table company (
    id int auto_increment primary key,
    name varchar2(255) not null
);

create table usr (
    id int auto_increment primary key,
    name varchar2(255) not null,
    password varchar2(255) not null,
    role varchar2(255) not null,
    company_id int, foreign key (company_id) references company(id)
);

create table document (
    id int auto_increment primary key ,
    name varchar2(255) not null,
    txt text,
    date_create timestamp not null ,
    date_close timestamp,
    date_delete timestamp,
    company_id int not null,
    counter_company_id int not null,
    confirm_company boolean default false,
    confirm_counter_company boolean default false,
    foreign key (company_id) references company(id),
    foreign key (counter_company_id) references company(id),
    constraint sess_comp_check check (company_id <> counter_company_id)
);

insert into company (name) values ('comp01'),
                                  ('comp02'),
                                  ('comp03'),
                                  ('comp04'),
                                  ('comp05'),
                                  ('comp06'),
                                  ('comp07'),
                                  ('comp08'),
                                  ('comp09'),
                                  ('comp10'),
                                  ('comp11'),
                                  ('comp12'),
                                  ('comp13'),
                                  ('comp14');

insert into usr (name, password, role, company_id) VALUES ('test01','test01', 'USER',(select c.id from company c where c.name = 'comp01')),
                                                    ('test02','test02', 'USER',(select c.id from company c where c.name = 'comp02')),
                                                    ('test03','test03', 'USER',(select c.id from company c where c.name = 'comp03')),
                                                    ('test04','test04', 'USER',(select c.id from company c where c.name = 'comp04')),
                                                    ('test05','test05', 'USER',(select c.id from company c where c.name = 'comp05')),
                                                    ('test06','test06', 'USER',(select c.id from company c where c.name = 'comp06')),
                                                    ('test07','test07', 'USER',(select c.id from company c where c.name = 'comp07')),
                                                    ('test08','test08', 'USER',(select c.id from company c where c.name = 'comp08')),
                                                    ('test09','test09', 'USER',(select c.id from company c where c.name = 'comp09')),
                                                    ('test10','test10', 'USER',(select c.id from company c where c.name = 'comp10')),
                                                    ('test11','test11', 'USER',(select c.id from company c where c.name = 'comp11')),
                                                    ('test12','test12', 'USER',(select c.id from company c where c.name = 'comp12')),
                                                    ('test13','test13', 'USER',(select c.id from company c where c.name = 'comp13')),
                                                    ('test14','test14', 'USER',(select c.id from company c where c.name = 'comp14'));
