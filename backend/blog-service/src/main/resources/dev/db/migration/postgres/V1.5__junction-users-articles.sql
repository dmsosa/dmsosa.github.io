CREATE TABLE IF NOT EXISTS favorites (
    user_id INTEGER NOT NULL REFERENCES users(id),
    article_id INTEGER NOT NULL REFERENCES articles(id),
    marked_at DATE,
    CONSTRAINT favorites_pk PRIMARY KEY (user_id, article_id)
);