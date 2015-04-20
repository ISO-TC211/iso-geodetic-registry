
ALTER TABLE ci_address_deliverypoint_aud
	DROP CONSTRAINT ci_address_deliverypoint_aud_pkey;

ALTER TABLE ci_address_electronicmailaddress_aud
	DROP CONSTRAINT ci_address_electronicmailaddress_aud_pkey;

ALTER TABLE compoundcrs_singlecrs_aud
	DROP CONSTRAINT compoundcrs_singlecrs_aud_pkey;

ALTER TABLE authorizationtable
	DROP CONSTRAINT fk_limrcebsa641sxwrxghrke49p;

ALTER TABLE delegation
	DROP CONSTRAINT fk_nlm03i9ausudedoigp70tdyof;

ALTER TABLE organization
	DROP CONSTRAINT fk_3qylclpufr4t9g86uwr2mkiy4;

ALTER TABLE organization_aud
	DROP CONSTRAINT fk_c81qse8rg80ollx6jwqlm1vcs;

ALTER TABLE proposaldiscussion
	DROP CONSTRAINT fk_hbwhra9amjnmhjwqo5a4kwfug;

ALTER TABLE registryuser
	DROP CONSTRAINT fk_i2amwuqbqr34ksskewtfd2r21;

ALTER TABLE registryuser_aud
	DROP CONSTRAINT fk_lhj8vb4hpomrdjg53k8hbj64h;

DROP TABLE actor;

DROP TABLE actor_aud;

CREATE TABLE proposalchangerequest (
	uuid uuid NOT NULL,
	editedbysubmitter boolean NOT NULL,
	originalrevision integer NOT NULL,
	reviewed boolean NOT NULL,
	editor_uuid uuid,
	proposal_uuid uuid
);

CREATE TABLE proposalchangerequest_aud (
	uuid uuid NOT NULL,
	rev integer NOT NULL,
	revtype smallint,
	editedbysubmitter boolean,
	originalrevision integer,
	reviewed boolean,
	editor_uuid uuid,
	proposal_uuid uuid
);

CREATE TABLE re_subregisterdescription (
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
	specificationlineage_uuid uuid,
	specificationsource_uuid uuid,
	operatinglanguage_uuid uuid,
	subregistermanager_uuid uuid,
	uniformresourceidentifier_uuid uuid
);

CREATE TABLE re_subregisterdescription_aud (
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
	specificationlineage_uuid uuid,
	specificationsource_uuid uuid,
	operatinglanguage_uuid uuid,
	subregistermanager_uuid uuid,
	uniformresourceidentifier_uuid uuid
);

CREATE TABLE re_subregisterdescription_itemclasses (
	itemclassid uuid NOT NULL,
	subregisterdescriptionid uuid NOT NULL
);

CREATE TABLE re_subregisterdescription_itemclasses_aud (
	rev integer NOT NULL,
	itemclassid uuid NOT NULL,
	subregisterdescriptionid uuid NOT NULL,
	revtype smallint
);

ALTER TABLE ci_address
	ALTER COLUMN administrativearea TYPE text /* TYPE change - table: ci_address original: character varying(2000) new: text */,
	ALTER COLUMN city TYPE text /* TYPE change - table: ci_address original: character varying(2000) new: text */,
	ALTER COLUMN country TYPE text /* TYPE change - table: ci_address original: character varying(2000) new: text */,
	ALTER COLUMN postalcode TYPE text /* TYPE change - table: ci_address original: character varying(2000) new: text */;

ALTER TABLE ci_address_aud
	ALTER COLUMN administrativearea TYPE text /* TYPE change - table: ci_address_aud original: character varying(2000) new: text */,
	ALTER COLUMN city TYPE text /* TYPE change - table: ci_address_aud original: character varying(2000) new: text */,
	ALTER COLUMN country TYPE text /* TYPE change - table: ci_address_aud original: character varying(2000) new: text */,
	ALTER COLUMN postalcode TYPE text /* TYPE change - table: ci_address_aud original: character varying(2000) new: text */;

ALTER TABLE ci_address_deliverypoint
	DROP COLUMN address_deliverypoint,
	ADD COLUMN deliverypoint character varying(255);

ALTER TABLE ci_address_deliverypoint_aud
	DROP COLUMN setordinal,
	DROP COLUMN address_deliverypoint,
	ADD COLUMN deliverypoint character varying(255) NOT NULL,
	ALTER COLUMN revtype DROP NOT NULL;

ALTER TABLE ci_address_electronicmailaddress
	DROP COLUMN address_email,
	ADD COLUMN electronicmailaddress character varying(255);

ALTER TABLE ci_address_electronicmailaddress_aud
	DROP COLUMN setordinal,
	DROP COLUMN address_email,
	ADD COLUMN electronicmailaddress character varying(255) NOT NULL,
	ALTER COLUMN revtype DROP NOT NULL;

ALTER TABLE ci_citation_date
	DROP COLUMN datetype_type,
	ADD COLUMN qname character varying(255);

ALTER TABLE ci_citation_date_aud
	ADD COLUMN datetype_codelist character varying(2000),
	ADD COLUMN datetype_codelistvalue character varying(2000),
	ADD COLUMN datetype_codespace character varying(2000),
	ADD COLUMN qname character varying(255),
	ADD COLUMN datetype_value character varying(2000);

ALTER TABLE ci_onlineresource
	DROP COLUMN type,
	ADD COLUMN qname character varying(255);

ALTER TABLE ci_onlineresource_aud
	ADD COLUMN codelist text,
	ADD COLUMN codelistvalue text,
	ADD COLUMN codespace text,
	ADD COLUMN qname character varying(255),
	ADD COLUMN "value" text;

ALTER TABLE ci_responsibleparty
	DROP COLUMN codelist,
	DROP COLUMN codelistvalue,
	DROP COLUMN type,
	ADD COLUMN role_codelist character varying(2000),
	ADD COLUMN role_codelistvalue character varying(2000),
	ADD COLUMN role_qname character varying(2000);

ALTER TABLE ci_responsibleparty_aud
	ADD COLUMN role_codelist character varying(2000),
	ADD COLUMN role_codelistvalue character varying(2000),
	ADD COLUMN codespace text,
	ADD COLUMN role_qname character varying(2000),
	ADD COLUMN "role" character varying(2000);

ALTER TABLE dq_absoluteexternalpositionalaccuracy
	DROP COLUMN type,
	ADD COLUMN qname character varying(255);

ALTER TABLE dq_griddeddatapositionalaccuracy
	DROP COLUMN type,
	ADD COLUMN qname character varying(255);

ALTER TABLE dq_relativeinternalpositionalaccuracy
	DROP COLUMN type,
	ADD COLUMN qname character varying(255);

ALTER TABLE organization
	ADD COLUMN shortname text NOT NULL DEFAULT '',
	ADD COLUMN address_uuid uuid;

ALTER TABLE organization_aud
	ADD COLUMN revtype smallint,
	ADD COLUMN shortname text,
	ADD COLUMN address_uuid uuid;

ALTER TABLE proposal
	ADD COLUMN title text;

ALTER TABLE proposal_aud
	ADD COLUMN title text;

ALTER TABLE proposalgroup
	DROP COLUMN name;

ALTER TABLE proposalgroup_aud
	DROP COLUMN name;

ALTER TABLE re_locale
	DROP COLUMN type,
	ADD COLUMN qname character varying(255);

ALTER TABLE re_locale_aud
	ADD COLUMN codelist text,
	ADD COLUMN codelistvalue text,
	ADD COLUMN codespace text,
	ADD COLUMN qname character varying(255),
	ADD COLUMN "value" text;

ALTER TABLE registryuser_aud
	ADD COLUMN revtype smallint;

ALTER TABLE ci_address_deliverypoint_aud
	ADD CONSTRAINT ci_address_deliverypoint_aud_pkey PRIMARY KEY (rev, ci_address_uuid, deliverypoint);

ALTER TABLE ci_address_electronicmailaddress_aud
	ADD CONSTRAINT ci_address_electronicmailaddress_aud_pkey PRIMARY KEY (rev, ci_address_uuid, electronicmailaddress);

ALTER TABLE compoundcrs_singlecrs
	ADD CONSTRAINT compoundcrs_singlecrs_pkey PRIMARY KEY (compoundcrs_uuid, index);

ALTER TABLE compoundcrs_singlecrs_aud
	ADD CONSTRAINT compoundcrs_singlecrs_aud_pkey PRIMARY KEY (rev, compoundcrs_uuid, componentreferencesystem_uuid, index);

ALTER TABLE proposalchangerequest
	ADD CONSTRAINT proposalchangerequest_pkey PRIMARY KEY (uuid);

ALTER TABLE proposalchangerequest_aud
	ADD CONSTRAINT proposalchangerequest_aud_pkey PRIMARY KEY (uuid, rev);

ALTER TABLE re_subregisterdescription
	ADD CONSTRAINT re_subregisterdescription_pkey PRIMARY KEY (uuid);

ALTER TABLE re_subregisterdescription_aud
	ADD CONSTRAINT re_subregisterdescription_aud_pkey PRIMARY KEY (uuid, rev);

ALTER TABLE re_subregisterdescription_itemclasses_aud
	ADD CONSTRAINT re_subregisterdescription_itemclasses_aud_pkey PRIMARY KEY (rev, itemclassid, subregisterdescriptionid);

ALTER TABLE organization
	ADD CONSTRAINT fk_2ugcb0t5gqdf9mvyvifbecy6e FOREIGN KEY (address_uuid) REFERENCES ci_address(uuid);

ALTER TABLE organization_aud
	ADD CONSTRAINT fk_4u2xs1rrms5cs50wy5yp1hkaf FOREIGN KEY (rev) REFERENCES revinfo(rev);

ALTER TABLE proposalchangerequest
	ADD CONSTRAINT fk_lpfeg35v81tfeuelv4fo1h018 FOREIGN KEY (proposal_uuid) REFERENCES proposal(uuid);

ALTER TABLE proposalchangerequest_aud
	ADD CONSTRAINT fk_ct1as72e63t1grb5ea2m5pb4b FOREIGN KEY (rev) REFERENCES revinfo(rev);

ALTER TABLE re_subregisterdescription
	ADD CONSTRAINT fk_393bsfj9f53p6os9lref5bix0 FOREIGN KEY (operatinglanguage_uuid) REFERENCES re_locale(uuid);

ALTER TABLE re_subregisterdescription
	ADD CONSTRAINT fk_andjxa6w3142iydp72sh86osl FOREIGN KEY (subregistermanager_uuid) REFERENCES re_registermanager(uuid);

ALTER TABLE re_subregisterdescription
	ADD CONSTRAINT fk_cmaxnxk1shgff3d5ilys8nsqi FOREIGN KEY (uniformresourceidentifier_uuid) REFERENCES ci_onlineresource(uuid);

ALTER TABLE re_subregisterdescription
	ADD CONSTRAINT fk_g2yvyd2xb9fxvfv1afkgit2eh FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);

ALTER TABLE re_subregisterdescription
	ADD CONSTRAINT fk_k24htfy054lj6embf072l768g FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);

ALTER TABLE re_subregisterdescription
	ADD CONSTRAINT fk_mse96xybta0oi7nyumqdsllao FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);

ALTER TABLE re_subregisterdescription
	ADD CONSTRAINT fk_r23siluyan9utn5uxw5xse1ey FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);

ALTER TABLE re_subregisterdescription_aud
	ADD CONSTRAINT fk_5unlce0h964y5qdq0bqsn85wd FOREIGN KEY (rev) REFERENCES revinfo(rev);

ALTER TABLE re_subregisterdescription_itemclasses
	ADD CONSTRAINT fk_7qnaohrdxep0a9d0c8x0kccqa FOREIGN KEY (subregisterdescriptionid) REFERENCES re_itemclass(uuid);

ALTER TABLE re_subregisterdescription_itemclasses
	ADD CONSTRAINT fk_m8eiv4qey71iajayov9q9avch FOREIGN KEY (itemclassid) REFERENCES re_subregisterdescription(uuid);

ALTER TABLE re_subregisterdescription_itemclasses_aud
	ADD CONSTRAINT fk_jghj31w6ngxpod2ntvltrp97k FOREIGN KEY (rev) REFERENCES revinfo(rev);

ALTER TABLE registryuser_aud
	ADD CONSTRAINT fk_iaym7as5jgd6s2ts6bmd8b0qe FOREIGN KEY (rev) REFERENCES revinfo(rev);
