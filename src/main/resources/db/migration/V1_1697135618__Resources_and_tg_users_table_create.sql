CREATE TABLE IF NOT EXISTS resources
(
    id serial PRIMARY KEY,
    name character varying COLLATE pg_catalog."default" NOT NULL,
    url character varying COLLATE pg_catalog."default",
    CONSTRAINT resources_name_unique_key UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS tg_user
(
    id bigserial PRIMARY KEY,
    telegram_user_id bigint NOT NULL,
    user_name character varying COLLATE pg_catalog."default" NOT NULL,
    chat_id bigint NOT NULL
);

CREATE TABLE IF NOT EXISTS user_resource
(
    user_id     integer NOT NULL,
    resource_id integer NOT NULL,
    CONSTRAINT user_resource_pkey PRIMARY KEY (user_id, resource_id),
    CONSTRAINT resource_fk FOREIGN KEY (resource_id)
        REFERENCES resources (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT user_fk FOREIGN KEY (user_id)
        REFERENCES tg_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)