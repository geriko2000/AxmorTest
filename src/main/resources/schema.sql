create table if not exists Users
(
    id       int          not null auto_increment,
    username varchar(255) not null,
    password varchar(255) not null,
    primary key (id)
);

create table if not exists Issues
(
    id          int           not null auto_increment,
    name        varchar(255)  not null,
    author_id   int           not null,
    description varchar(1000) not null,
    start_date  datetime      not null,
    status      varchar(10)   not null,
    primary key (id),
    foreign key (author_id) references Users
);

create table if not exists Comments
(
    id        int           not null auto_increment,
    author_id int           not null,
    text      varchar(1000) not null,
    issue_id  int           not null,
    date      datetime      not null,
    issue_status    varchar(10)   not null,
    primary key (id),
    foreign key (author_id) references Users,
    foreign key (issue_id) references Issues
);