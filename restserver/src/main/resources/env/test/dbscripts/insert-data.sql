INSERT INTO users(username,password,enabled)
VALUES ('user','password', true);
INSERT INTO users(username,password,enabled)
VALUES ('alex','password', true);

INSERT INTO authorities (username, authority)
VALUES ('user', 'ROLE_USER');
INSERT INTO authorities (username, authority)
VALUES ('user', 'ROLE_ADMIN');
INSERT INTO authorities (username, authority)
VALUES ('alex', 'ROLE_ADMIN');