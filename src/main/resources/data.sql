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

insert into company (id, name) values (-1, 'comp01'),
                                  (-2, 'comp02'),
                                  (-3, 'comp03'),
                                  (-4, 'comp04'),
                                  (-5, 'comp05'),
                                  (-6, 'comp06'),
                                  (-7, 'comp07'),
                                  (-8, 'comp08'),
                                  (-9, 'comp09'),
                                  (-10, 'comp10'),
                                  (-11, 'comp11'),
                                  (-12, 'comp12'),
                                  (-13, 'comp13'),
                                  (-14, 'comp14');

insert into usr (id, name, password, role, company_id) VALUES (-1, 'test01','test01', 'USER',(select c.id from company c where c.name = 'comp01')),
                                                    (-2, 'test02','test02', 'USER',(select c.id from company c where c.name = 'comp02')),
                                                    (-3, 'test03','test03', 'USER',(select c.id from company c where c.name = 'comp03')),
                                                    (-4, 'test04','test04', 'USER',(select c.id from company c where c.name = 'comp04')),
                                                    (-5, 'test05','test05', 'USER',(select c.id from company c where c.name = 'comp05')),
                                                    (-6, 'test06','test06', 'USER',(select c.id from company c where c.name = 'comp06')),
                                                    (-7, 'test07','test07', 'USER',(select c.id from company c where c.name = 'comp07')),
                                                    (-8, 'test08','test08', 'USER',(select c.id from company c where c.name = 'comp08')),
                                                    (-9, 'test09','test09', 'USER',(select c.id from company c where c.name = 'comp09')),
                                                    (-10, 'test10','test10', 'USER',(select c.id from company c where c.name = 'comp10')),
                                                    (-11, 'test11','test11', 'USER',(select c.id from company c where c.name = 'comp11')),
                                                    (-12, 'test12','test12', 'USER',(select c.id from company c where c.name = 'comp12')),
                                                    (-13, 'test13','test13', 'USER',(select c.id from company c where c.name = 'comp13')),
                                                    (-14, 'test14','test14', 'USER',(select c.id from company c where c.name = 'comp14'));
