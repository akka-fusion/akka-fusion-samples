create user devuser nosuperuser encrypted password 'Devpass.2019';
create database sample owner devuser template=template1;
create database sample_scheduler owner devuser template=template1;
