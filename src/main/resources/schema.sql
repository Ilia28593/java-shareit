CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  varchar(100) NOT NULL,
    email varchar(255) NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);
CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        varchar(100) NOT NULL,
    description varchar(255) NOT NULL,
    available   boolean,
    user_id     BIGINT references users (id),
    CONSTRAINT UQ_OWNER_ITEM_NAME UNIQUE (user_id, name)
);
CREATE TABLE IF NOT EXISTS item_requests
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description VARCHAR(255)                NOT NULL,
    user_id     BIGINT REFERENCES users (id),
    created     TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS comments
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text    VARCHAR2(255)               NOT NULL,
    item_id BIGINT references items (id),
    user_id BIGINT references users (id),
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id    BIGINT references items (id),
    user_id    BIGINT references users (id),
    status     varchar(100)
);
