DROP TABLE IF EXISTS users, items, bookings, comments;

CREATE table IF NOT EXISTS users(
    user_id int generated by default as identity primary key,
    email varchar NOT NULL,
    name varchar NOT NULL
);
CREATE unique index if not exists user_email_uindex ON users (email);

CREATE table IF NOT EXISTS items(
    item_id int generated by default as identity primary key,
    name varchar(50) NOT NULL,
    description varchar NOT NULL,
    is_available boolean,
    owner_id int REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE table IF NOT EXISTS bookings(
    booking_id int generated by default as identity primary key,
    start_date timestamp without time zone,
    end_date timestamp without time zone,
    item_id int REFERENCES items (item_id) ON DELETE CASCADE,
    booker_id int REFERENCES users (user_id) ON DELETE CASCADE,
    status varchar NOT NULL
);

CREATE table IF NOT EXISTS comments(
    comment_id int generated by default as identity primary key,
    text varchar NOT NULL,
    created timestamp without time zone,
    item_id int REFERENCES items (item_id) ON DELETE CASCADE,
    author_id int REFERENCES users (user_id) ON DELETE CASCADE
)