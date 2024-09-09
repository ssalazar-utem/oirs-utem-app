BEGIN TRANSACTION;


DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
    pk bigserial NOT NULL,
    email varchar(255) NOT NULL,
    role smallint NOT NULL DEFAULT '0',
    active boolean NOT NULL DEFAULT true,
    created timestamptz NOT NULL DEFAULT NOW(),
    updated timestamptz NOT NULL DEFAULT NOW(),
    PRIMARY KEY (pk)
);
CREATE UNIQUE INDEX usr_email_uid ON users(UPPER(email));


DROP TABLE IF EXISTS access CASCADE;
CREATE TABLE access (
    pk bigserial NOT NULL,
    email varchar(255) NOT NULL,
    ip varchar(255) NOT NULL,
    user_agent text NOT NULL,
    request_uri text NOT NULL,
    created timestamptz NOT NULL DEFAULT NOW(),
    updated timestamptz NOT NULL DEFAULT NOW(),
    PRIMARY KEY (pk)
);
CREATE INDEX acs_email_idx ON access(UPPER(email));


DROP TABLE IF EXISTS categories CASCADE;
CREATE TABLE categories (
    pk bigserial NOT NULL,
    token varchar(255) NOT NULL,
    name varchar(255) NOT NULL,
    description text,
    created timestamptz NOT NULL DEFAULT NOW(),
    updated timestamptz NOT NULL DEFAULT NOW(),
    PRIMARY KEY (pk)
);
CREATE UNIQUE INDEX cat_tkn_uidx ON categories(token);


DROP TABLE IF EXISTS tickets CASCADE;
CREATE TABLE tickets (
    pk bigserial NOT NULL,
    token varchar(255) NOT NULL,
    user_fk bigint NOT NULL,
    category_fk bigint NOT NULL,
    icso_type smallint NOT NULL DEFAULT '0',
    icso_status smallint NOT NULL DEFAULT '0',
    subject varchar(255) NOT NULL,
    message text NOT NULL,
    response text,
    created timestamptz NOT NULL DEFAULT NOW(),
    updated timestamptz NOT NULL DEFAULT NOW(),
    FOREIGN KEY (user_fk) REFERENCES users(pk) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (category_fk) REFERENCES categories(pk) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (pk)
);
CREATE UNIQUE INDEX tck_tkn_uidx ON tickets(token);
CREATE INDEX tck_usr_idx ON tickets(user_fk);
CREATE INDEX tck_cat_idx ON tickets(category_fk);



DROP TABLE IF EXISTS history CASCADE;
CREATE TABLE history (
    pk bigserial NOT NULL,
    token varchar(255) NOT NULL,
    status varchar(255) NOT NULL,
    user_email varchar(255) NOT NULL,
    subject varchar(255) NOT NULL,
    message text NOT NULL,
    staff_email varchar(255),
    response text,
    created timestamptz NOT NULL DEFAULT NOW(),
    updated timestamptz NOT NULL DEFAULT NOW(),
    PRIMARY KEY (pk)
);



DROP TABLE IF EXISTS attachments CASCADE;
CREATE TABLE attachments (
    pk bigserial NOT NULL,
    token varchar(255) NOT NULL,
    ticket_fk bigint NOT NULL,
    mime varchar(255) NOT NULL,
    content bytea NOT NULL,
    created timestamptz NOT NULL DEFAULT NOW(),
    updated timestamptz NOT NULL DEFAULT NOW(),
    FOREIGN KEY (ticket_fk) REFERENCES tickets(pk) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (pk)
);
CREATE UNIQUE INDEX att_tkn_uidx ON attachments(token);

COMMIT;
