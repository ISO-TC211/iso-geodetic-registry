CREATE TABLE IF NOT EXISTS coordinateoperationitem_domains (
    coordinateoperationitem_uuid uuid NOT NULL,
    domainofvalidity_uuid uuid,
    scope text,
    CONSTRAINT fk_9ggbxh4vgnuu8sg8c7rqohss5 FOREIGN KEY (domainofvalidity_uuid)
        REFERENCES extentitem (uuid)
);

CREATE TABLE IF NOT EXISTS coordinateoperationitem_domains_aud
(
    rev integer NOT NULL,
    revtype smallint NOT NULL,
    coordinateoperationitem_uuid uuid NOT NULL,
    setordinal integer NOT NULL,
    domainofvalidity_uuid uuid,
    scope text,
    CONSTRAINT coordinateoperationitem_domains_aud_pkey PRIMARY KEY (rev, revtype, coordinateoperationitem_uuid, setordinal),
    CONSTRAINT fk_63nh15x95747o1tc82nf985tc FOREIGN KEY (rev)
        REFERENCES revinfo (rev)
);
