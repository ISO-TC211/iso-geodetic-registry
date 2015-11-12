ALTER TABLE ci_citation_presentationform
	ADD COLUMN codelist text,
	ADD COLUMN codelistvalue text,
	ADD COLUMN codespace text,
	ADD COLUMN qname character varying(255),
	ADD COLUMN value text;

ALTER TABLE ci_citation_presentationform_aud
	ADD COLUMN codelist text,
	ADD COLUMN codelistvalue text,
	ADD COLUMN codespace text,
	ADD COLUMN qname character varying(255),
	ADD COLUMN value text,
	ADD COLUMN setordinal integer;

CREATE TABLE passwordresetrequest
(
  uuid uuid NOT NULL,
  requesttimestamp timestamp without time zone,
  token character varying(255),
  user_uuid uuid,
  CONSTRAINT passwordresetrequest_pkey PRIMARY KEY (uuid),
  CONSTRAINT fk_4c62271ue3ds8n140f6xtj79b FOREIGN KEY (user_uuid)
      REFERENCES registryuser (uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE proposal RENAME COLUMN group_uuid TO parent_uuid;
ALTER TABLE proposal_aud RENAME COLUMN group_uuid TO parent_uuid;

CREATE TABLE proposalrelatedrole
(
  uuid uuid NOT NULL,
  name text,
  proposal_uuid uuid NOT NULL,
  CONSTRAINT proposalrelatedrole_pkey PRIMARY KEY (uuid),
  CONSTRAINT fk_tiqdj2pdqoy91pjgq9tv6ty4f FOREIGN KEY (proposal_uuid)
      REFERENCES proposal (uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_mqb77t9f0u1u34jqfcp1hkcwl UNIQUE (name)
);

CREATE TABLE proposalrelatedrole_aud
(
  uuid uuid NOT NULL,
  rev integer NOT NULL,
  revtype smallint,
  name text,
  proposal_uuid uuid,
  CONSTRAINT proposalrelatedrole_aud_pkey PRIMARY KEY (uuid, rev),
  CONSTRAINT fk_7y13wtf7lh93o2ciju227gs1 FOREIGN KEY (rev)
      REFERENCES revinfo (rev) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE re_alternativeexpression_fieldofapplication
	ADD COLUMN name character varying(2000),
	ADD COLUMN description character varying(2000);

ALTER TABLE re_alternativeexpression_fieldofapplication_aud
	ADD COLUMN name character varying(2000),
	ADD COLUMN description character varying(2000);
	
CREATE TABLE re_alternativename
(
  uuid uuid NOT NULL,
  name text NOT NULL,
  locale_uuid uuid,
  CONSTRAINT re_alternativename_pkey PRIMARY KEY (uuid),
  CONSTRAINT fk_e87efmecc8vcs9ectx9qhlun7 FOREIGN KEY (locale_uuid)
      REFERENCES re_locale (uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE re_alternativename_aud
(
  uuid uuid NOT NULL,
  rev integer NOT NULL,
  revtype smallint,
  name text,
  locale_uuid uuid,
  CONSTRAINT re_alternativename_aud_pkey PRIMARY KEY (uuid, rev),
  CONSTRAINT fk_j42kbw3fwd384s70enl2a94v7 FOREIGN KEY (rev)
      REFERENCES revinfo (rev) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

DROP TABLE re_itemclass_alternativenames;
CREATE TABLE re_itemclass_alternativenames
(
  itemclassid uuid NOT NULL,
  alternativenameid uuid NOT NULL,
  CONSTRAINT re_itemclass_alternativenames_pkey PRIMARY KEY (itemclassid, alternativenameid),
  CONSTRAINT fk_67gio9q8eymq0cxvkmk85y7bq FOREIGN KEY (alternativenameid)
      REFERENCES re_alternativename (uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_qnseiaer3knhi12dl4pms70oh FOREIGN KEY (itemclassid)
      REFERENCES re_itemclass (uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_67gio9q8eymq0cxvkmk85y7bq UNIQUE (alternativenameid)
);

DROP TABLE re_itemclass_alternativenames_aud;
CREATE TABLE re_itemclass_alternativenames_aud
(
  rev integer NOT NULL,
  itemclassid uuid NOT NULL,
  alternativenameid uuid NOT NULL,
  revtype smallint,
  CONSTRAINT re_itemclass_alternativenames_aud_pkey PRIMARY KEY (rev, itemclassid, alternativenameid),
  CONSTRAINT fk_2thkwpt5g2ccu00iweg1wsy14 FOREIGN KEY (rev)
      REFERENCES revinfo (rev) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE re_locale
  DROP COLUMN characterencoding,
  DROP COLUMN codelist,
  DROP COLUMN codelistvalue,
  DROP COLUMN codespace,
  DROP COLUMN value,
  DROP COLUMN qname,
  ADD COLUMN characterencoding_codelist character varying(255),
  ADD COLUMN characterencoding_codelistvalue character varying(255),
  ADD COLUMN characterencoding_codespace character varying(255),
  ADD COLUMN characterencoding_qname character varying(255),
  ADD COLUMN characterencoding_value character varying(255),
  ADD COLUMN language_codelist character varying(255),
  ADD COLUMN language_codelistvalue character varying(255),
  ADD COLUMN language_codespace character varying(255),
  ADD COLUMN language_qname character varying(255),
  ADD COLUMN language_value character varying(255);
  
ALTER TABLE re_locale_aud
  DROP COLUMN characterencoding,
  DROP COLUMN codelist,
  DROP COLUMN codelistvalue,
  DROP COLUMN codespace,
  DROP COLUMN value,
  DROP COLUMN qname,
  ADD COLUMN characterencoding_codelist character varying(255),
  ADD COLUMN characterencoding_codelistvalue character varying(255),
  ADD COLUMN characterencoding_codespace character varying(255),
  ADD COLUMN characterencoding_qname character varying(255),
  ADD COLUMN characterencoding_value character varying(255),
  ADD COLUMN language_codelist character varying(255),
  ADD COLUMN language_codelistvalue character varying(255),
  ADD COLUMN language_codespace character varying(255),
  ADD COLUMN language_qname character varying(255),
  ADD COLUMN language_value character varying(255);

ALTER TABLE re_reference
  DROP COLUMN similarity,
  ADD COLUMN similarity_codelist character varying(255),
  ADD COLUMN similarity_codelistvalue character varying(255),
  ADD COLUMN similarity_codespace character varying(255),
  ADD COLUMN similarity_qname character varying(255),
  ADD COLUMN similarity_value character varying(255);
    
ALTER TABLE re_reference_aud
  DROP COLUMN similarity,
  ADD COLUMN similarity_codelist character varying(255),
  ADD COLUMN similarity_codelistvalue character varying(255),
  ADD COLUMN similarity_codespace character varying(255),
  ADD COLUMN similarity_qname character varying(255),
  ADD COLUMN similarity_value character varying(255);

CREATE TABLE re_registeritem_re_reference
(
  re_registeritem_uuid uuid NOT NULL,
  specificationlineage_uuid uuid NOT NULL,
  CONSTRAINT re_registeritem_re_reference_pkey PRIMARY KEY (re_registeritem_uuid, specificationlineage_uuid),
  CONSTRAINT fk_d03snraxqj9p7mcamhuowx8w9 FOREIGN KEY (specificationlineage_uuid)
      REFERENCES re_reference (uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_d03snraxqj9p7mcamhuowx8w9 UNIQUE (specificationlineage_uuid)
);

CREATE TABLE re_registeritem_re_reference_aud
(
  rev integer NOT NULL,
  re_registeritem_uuid uuid NOT NULL,
  specificationlineage_uuid uuid NOT NULL,
  revtype smallint,
  CONSTRAINT re_registeritem_re_reference_aud_pkey PRIMARY KEY (rev, re_registeritem_uuid, specificationlineage_uuid),
  CONSTRAINT fk_teu59wcgvnue21w1oky8hllvu FOREIGN KEY (rev)
      REFERENCES revinfo (rev) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE simpleproposal ADD COLUMN itemclassname text;
ALTER TABLE simpleproposal_aud ADD COLUMN itemclassname text;
ALTER TABLE simpleproposal ADD COLUMN targetregister_uuid uuid;
ALTER TABLE simpleproposal_aud ADD COLUMN targetregister_uuid uuid;

