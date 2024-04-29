CREATE TABLE IF NOT EXISTS user_role
(
    id int NOT NULL AUTO_INCREMENT,
    role_name varchar(50) NOT NULL,
    application_user_id int NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (application_user_id, role_name),
    FOREIGN KEY (application_user_id) REFERENCES application_user (id)
);