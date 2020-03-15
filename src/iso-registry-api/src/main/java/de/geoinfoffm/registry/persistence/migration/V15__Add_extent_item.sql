CREATE TABLE IF NOT EXISTS extentitem (
    uuid uuid NOT NULL,
    dateaccepted date,
    dateamended date,
    definition text,
    description text,
    itemidentifier numeric(19,2) NOT NULL,
    name text NOT NULL,
    status character varying(255),
    itemclass_uuid uuid,
    register_uuid uuid,
    specificationsource_uuid uuid,
    extent_uuid uuid,
    CONSTRAINT extentitem_pkey PRIMARY KEY (uuid),
    CONSTRAINT fk_2qymbvgp3kssogq6xetcisru5 FOREIGN KEY (register_uuid)
        REFERENCES re_register (uuid),
    CONSTRAINT fk_6ha597ro4q5n8nee3o1d7qmi4 FOREIGN KEY (itemclass_uuid)
        REFERENCES re_itemclass (uuid),
    CONSTRAINT fk_f4l5vjmlltl6arek8bhqmlfmj FOREIGN KEY (specificationsource_uuid)
        REFERENCES re_reference (uuid),
    CONSTRAINT fk_fnp9hbspy6veiarnst0bbb9qq FOREIGN KEY (extent_uuid)
        REFERENCES ex_extent (uuid)
);

CREATE TABLE IF NOT EXISTS extentitem_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    dateaccepted date,
    dateamended date,
    definition text,
    description text,
    itemidentifier numeric(19,2),
    name text,
    status character varying(255),
    itemclass_uuid uuid,
    register_uuid uuid,
    specificationsource_uuid uuid,
    extent_uuid uuid,
    CONSTRAINT extentitem_aud_pkey PRIMARY KEY (uuid, rev),
    CONSTRAINT fk_kvdko1j2dfoy0mgvbu9dmfetj FOREIGN KEY (rev)
        REFERENCES public.revinfo (rev)
);
