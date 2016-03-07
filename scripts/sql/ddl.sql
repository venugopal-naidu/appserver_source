drop table if exists doctor;
create table doctor
(
    id int unsigned not null auto_increment,
    name varchar(165),
    gender varchar(165),
    website varchar(210),
    phone varchar(210),
    fax varchar(210),
    email varchar(210),
    degree1 varchar(240),
    univ1 varchar(240),
    degree2 varchar(210),
    univ2 varchar(210),
    degree3 varchar(234),
    univ3 varchar(201),
    degree4 varchar(297),
    univ4 varchar(267),
    degree5 varchar(261),
    univ5 varchar(264),
    experience varchar(234),
    awards varchar(234),
    language varchar(198),
    velkare_verified tinyint default 0,
    created_by varchar(231),
    creation_date varchar(261),
    last_updated_by varchar(234),
    last_update_date varchar(234),
    version bigint not null,
    primary key (id)
);


drop table if exists doctor_hospital;

create table doctor_hospital
(
    service_provider_id int,
    doctor_id int unsigned not null,
    hospital_id int unsigned not null,
    service_hours_from varchar(300),
    service_hours_to varchar(300),
    velkare_verified varchar(300),
    created_by decimal(12,0),
    creation_date varchar(300),
    last_updated_by decimal(12,0),
    last_update_date varchar(300),
    version bigint not null
);

drop table if exists doctor_speciality;
create table doctor_speciality
(
    doctor_id int unsigned not null,
    speciality_id int unsigned not null,
    velkare_verified tinyint default 0
);

drop table if exists hospital;
create table hospital
(
    id int unsigned primary key not null,
    hos_geocode varchar(300),
    name varchar(300) not null,
    address1 varchar(360),
    address2 varchar(450),
    address3 varchar(450),
    address4 varchar(450),
    city varchar(450),
    district varchar(450),
    state varchar(450),
    country varchar(360),
    postal_code varchar(360),
    website varchar(360),
    phone varchar(450),
    fax varchar(450),
    email varchar(360),
    specialists varchar(360),
    velkare_verified varchar(390),
    created_by varchar(360),
    creation_date varchar(450),
    last_updated_by varchar(420),
    last_update_date varchar(360),
    version bigint not null
);

drop table if exists speciality;
create table speciality
(
    id int unsigned primary key not null auto_increment,
    name varchar(100),
    velkare_verified varchar(100),
    version bigint not null
);

