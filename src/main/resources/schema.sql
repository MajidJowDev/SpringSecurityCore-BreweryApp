/*By default spring looks for a file called "schema.sql" to create any database table at startup*/
/*This table should be exactly like this according to Spring Docs*/
create table persistent_logins (username varchar(64) not null,
                                series varchar(64) primary key,
                                token varchar(64) not null,
                                last_used timestamp not null);