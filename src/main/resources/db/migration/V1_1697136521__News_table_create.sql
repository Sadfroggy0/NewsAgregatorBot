CREATE TABLE IF NOT EXISTS news
(
    id bigserial PRIMARY KEY ,
    title character varying COLLATE pg_catalog."default",
    link character varying COLLATE pg_catalog."default" NOT NULL,
    topic character varying COLLATE pg_catalog."default",
    pub_date timestamp without time zone,
    author character varying COLLATE pg_catalog."default",
    resource_id bigint,
    CONSTRAINT resources_fk FOREIGN KEY (resource_id)
        REFERENCES resources (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)