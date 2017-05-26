--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.5
-- Dumped by pg_dump version 9.3.5
-- Started on 2015-02-10 14:52:05

--
-- TOC entry 170 (class 1259 OID 491521)
-- Name: acl_class; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE acl_class (
    uuid uuid NOT NULL,
    class text
);


--
-- TOC entry 171 (class 1259 OID 491527)
-- Name: acl_class_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE acl_class_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    class text
);


--
-- TOC entry 172 (class 1259 OID 491533)
-- Name: acl_entry; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE acl_entry (
    uuid uuid NOT NULL,
    ace_order integer NOT NULL,
    audit_failure boolean NOT NULL,
    audit_success boolean NOT NULL,
    granting boolean NOT NULL,
    mask integer NOT NULL,
    acl_object_identity uuid,
    sid uuid
);


--
-- TOC entry 173 (class 1259 OID 491536)
-- Name: acl_entry_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE acl_entry_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    ace_order integer,
    audit_failure boolean,
    audit_success boolean,
    granting boolean,
    mask integer,
    acl_object_identity uuid,
    sid uuid
);


--
-- TOC entry 174 (class 1259 OID 491539)
-- Name: acl_object_identity; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE acl_object_identity (
    uuid uuid NOT NULL,
    entries_inheriting boolean NOT NULL,
    object_id_identity uuid NOT NULL,
    object_id_class uuid,
    owner_sid uuid,
    parent_object uuid
);


--
-- TOC entry 175 (class 1259 OID 491542)
-- Name: acl_object_identity_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE acl_object_identity_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    entries_inheriting boolean,
    object_id_identity uuid,
    object_id_class uuid,
    owner_sid uuid,
    parent_object uuid
);


--
-- TOC entry 176 (class 1259 OID 491545)
-- Name: acl_sid; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE acl_sid (
    uuid uuid NOT NULL,
    principal boolean NOT NULL,
    sid character varying(100)
);


--
-- TOC entry 177 (class 1259 OID 491548)
-- Name: acl_sid_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE acl_sid_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    principal boolean,
    sid character varying(100)
);


--
-- TOC entry 178 (class 1259 OID 491551)
-- Name: actor; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE actor (
    uuid uuid NOT NULL
);


--
-- TOC entry 179 (class 1259 OID 491554)
-- Name: actor_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE actor_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint
);


--
-- TOC entry 180 (class 1259 OID 491557)
-- Name: addition; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE addition (
    uuid uuid NOT NULL
);


--
-- TOC entry 181 (class 1259 OID 491560)
-- Name: addition_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE addition_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL
);


--
-- TOC entry 182 (class 1259 OID 491563)
-- Name: alias; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE alias (
    uuid uuid NOT NULL,
    alias text,
    remarks text,
    aliaseditem_uuid uuid,
    namingsystem_uuid uuid
);


--
-- TOC entry 183 (class 1259 OID 491569)
-- Name: alias_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE alias_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    alias text,
    remarks text,
    aliaseditem_uuid uuid,
    namingsystem_uuid uuid
);


--
-- TOC entry 184 (class 1259 OID 491575)
-- Name: appeal; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE appeal (
    uuid uuid NOT NULL,
    appealdate date NOT NULL,
    disposition character varying(255) NOT NULL,
    dispositiondate date,
    impact character varying(255),
    justification character varying(255),
    situation character varying(255),
    appealedproposal_uuid uuid
);


--
-- TOC entry 185 (class 1259 OID 491581)
-- Name: appeal_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE appeal_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    appealdate date,
    disposition character varying(255),
    dispositiondate date,
    impact character varying(255),
    justification character varying(255),
    situation character varying(255),
    appealedproposal_uuid uuid
);


--
-- TOC entry 186 (class 1259 OID 491587)
-- Name: area; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE area (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    area_east_bound_lon double precision,
    area_north_bound_lat double precision,
    area_south_bound_lat double precision,
    area_west_bound_lon double precision,
    iso_a2_code character varying(255),
    iso_a3_code character varying(255),
    iso_n_code integer
);


--
-- TOC entry 187 (class 1259 OID 491593)
-- Name: area_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE area_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    area_east_bound_lon double precision,
    area_north_bound_lat double precision,
    area_south_bound_lat double precision,
    area_west_bound_lon double precision,
    iso_a2_code character varying(255),
    iso_a3_code character varying(255),
    iso_n_code integer
);


--
-- TOC entry 188 (class 1259 OID 491599)
-- Name: authorizationtable; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE authorizationtable (
    uuid uuid NOT NULL,
    actor_uuid uuid NOT NULL,
    role_uuid uuid NOT NULL
);


--
-- TOC entry 189 (class 1259 OID 491602)
-- Name: authorizationtable_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE authorizationtable_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    actor_uuid uuid,
    role_uuid uuid
);


--
-- TOC entry 190 (class 1259 OID 491605)
-- Name: axis; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE axis (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    coord_axis_abbreviation character varying(255),
    coord_axis_orientation character varying(255),
    coord_axis_orientation_codespace character varying(255),
    maximumvalue bytea,
    minimumvalue bytea,
    rangemeaning character varying(255),
    axisunit_uuid uuid
);


--
-- TOC entry 191 (class 1259 OID 491611)
-- Name: axis_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE axis_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    coord_axis_abbreviation character varying(255),
    maximumvalue bytea,
    minimumvalue bytea,
    rangemeaning character varying(255),
    axisunit_uuid uuid
);


--
-- TOC entry 192 (class 1259 OID 491617)
-- Name: cartesiancs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE cartesiancs (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text
);


--
-- TOC entry 193 (class 1259 OID 491623)
-- Name: cartesiancs_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE cartesiancs_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text
);


--
-- TOC entry 194 (class 1259 OID 491629)
-- Name: ci_address; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_address (
    uuid uuid NOT NULL,
    administrativearea character varying(2000),
    city character varying(2000),
    country character varying(2000),
    postalcode character varying(2000)
);


--
-- TOC entry 195 (class 1259 OID 491635)
-- Name: ci_address_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_address_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    administrativearea character varying(2000),
    city character varying(2000),
    country character varying(2000),
    postalcode character varying(2000)
);


--
-- TOC entry 196 (class 1259 OID 491641)
-- Name: ci_address_deliverypoint; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_address_deliverypoint (
    ci_address_uuid uuid NOT NULL,
    address_deliverypoint character varying(2000)
);


--
-- TOC entry 197 (class 1259 OID 491647)
-- Name: ci_address_deliverypoint_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_address_deliverypoint_aud (
    rev integer NOT NULL,
    revtype smallint NOT NULL,
    ci_address_uuid uuid NOT NULL,
    setordinal integer NOT NULL,
    address_deliverypoint character varying(2000)
);


--
-- TOC entry 198 (class 1259 OID 491653)
-- Name: ci_address_electronicmailaddress; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_address_electronicmailaddress (
    ci_address_uuid uuid NOT NULL,
    address_email character varying(2000)
);


--
-- TOC entry 199 (class 1259 OID 491659)
-- Name: ci_address_electronicmailaddress_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_address_electronicmailaddress_aud (
    rev integer NOT NULL,
    revtype smallint NOT NULL,
    ci_address_uuid uuid NOT NULL,
    setordinal integer NOT NULL,
    address_email character varying(2000)
);


--
-- TOC entry 200 (class 1259 OID 491665)
-- Name: ci_citation; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_citation (
    uuid uuid NOT NULL,
    collectivetitle text,
    edition text,
    editiondate date,
    identifier_code character varying(2000),
    isbn character varying(255),
    issn character varying(255),
    othercitationdetails text,
    issueidentification character varying(2000),
    name character varying(2000),
    page character varying(2000),
    title text,
    authority_uuid uuid
);


--
-- TOC entry 201 (class 1259 OID 491671)
-- Name: ci_citation_alternatetitle; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_citation_alternatetitle (
    ci_citation_uuid uuid NOT NULL,
    alternatetitle text
);


--
-- TOC entry 202 (class 1259 OID 491677)
-- Name: ci_citation_alternatetitle_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_citation_alternatetitle_aud (
    rev integer NOT NULL,
    ci_citation_uuid uuid NOT NULL,
    alternatetitle text NOT NULL,
    revtype smallint
);


--
-- TOC entry 203 (class 1259 OID 491683)
-- Name: ci_citation_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_citation_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    collectivetitle text,
    edition text,
    editiondate date,
    identifier_code character varying(2000),
    isbn character varying(255),
    issn character varying(255),
    othercitationdetails text,
    issueidentification character varying(2000),
    name character varying(2000),
    page character varying(2000),
    title text,
    authority_uuid uuid
);


--
-- TOC entry 204 (class 1259 OID 491689)
-- Name: ci_citation_ci_responsibleparty; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_citation_ci_responsibleparty (
    ci_citation_uuid uuid NOT NULL,
    citedresponsibleparty_uuid uuid NOT NULL
);


--
-- TOC entry 205 (class 1259 OID 491692)
-- Name: ci_citation_ci_responsibleparty_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_citation_ci_responsibleparty_aud (
    rev integer NOT NULL,
    ci_citation_uuid uuid NOT NULL,
    citedresponsibleparty_uuid uuid NOT NULL,
    revtype smallint
);


--
-- TOC entry 206 (class 1259 OID 491695)
-- Name: ci_citation_date; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_citation_date (
    ci_citation_uuid uuid NOT NULL,
    date_date date,
    datetype_codelist character varying(2000),
    datetype_codelistvalue character varying(2000),
    datetype_codespace character varying(2000),
    datetype_type bytea,
    datetype_value character varying(2000)
);


--
-- TOC entry 207 (class 1259 OID 491701)
-- Name: ci_citation_date_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_citation_date_aud (
    rev integer NOT NULL,
    revtype smallint NOT NULL,
    ci_citation_uuid uuid NOT NULL,
    setordinal integer NOT NULL,
    date_date date
);


--
-- TOC entry 208 (class 1259 OID 491704)
-- Name: ci_citation_presentationform; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_citation_presentationform (
    ci_citation_uuid uuid NOT NULL,
    presentationform integer
);


--
-- TOC entry 209 (class 1259 OID 491707)
-- Name: ci_citation_presentationform_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_citation_presentationform_aud (
    rev integer NOT NULL,
    ci_citation_uuid uuid NOT NULL,
    presentationform integer NOT NULL,
    revtype smallint
);


--
-- TOC entry 210 (class 1259 OID 491710)
-- Name: ci_contact; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_contact (
    uuid uuid NOT NULL,
    contactinstructions character varying(2000),
    hoursofservice character varying(2000),
    address_uuid uuid,
    onlineresource_uuid uuid,
    phone_uuid uuid
);


--
-- TOC entry 211 (class 1259 OID 491716)
-- Name: ci_contact_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_contact_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    contactinstructions character varying(2000),
    hoursofservice character varying(2000),
    address_uuid uuid,
    onlineresource_uuid uuid,
    phone_uuid uuid
);


--
-- TOC entry 212 (class 1259 OID 491722)
-- Name: ci_onlineresource; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_onlineresource (
    uuid uuid NOT NULL,
    applicationprofile character varying(255),
    description character varying(255),
    codelist text,
    codelistvalue text,
    codespace text,
    type bytea,
    value text,
    linkage character varying(255),
    name character varying(255),
    protocol character varying(255)
);


--
-- TOC entry 213 (class 1259 OID 491728)
-- Name: ci_onlineresource_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_onlineresource_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    applicationprofile character varying(255),
    description character varying(255),
    linkage character varying(255),
    name character varying(255),
    protocol character varying(255)
);


--
-- TOC entry 214 (class 1259 OID 491734)
-- Name: ci_responsibleparty; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_responsibleparty (
    uuid uuid NOT NULL,
    individualname text,
    organisationname text,
    positionname text,
    codelist text,
    codelistvalue text,
    codespace text,
    type bytea,
    role character varying(2000),
    contactinfo_uuid uuid
);


--
-- TOC entry 215 (class 1259 OID 491740)
-- Name: ci_responsibleparty_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_responsibleparty_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    individualname text,
    organisationname text,
    positionname text,
    contactinfo_uuid uuid
);


--
-- TOC entry 216 (class 1259 OID 491746)
-- Name: ci_telephone; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_telephone (
    uuid uuid NOT NULL
);


--
-- TOC entry 217 (class 1259 OID 491749)
-- Name: ci_telephone_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_telephone_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint
);


--
-- TOC entry 218 (class 1259 OID 491752)
-- Name: ci_telephone_facsimile; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_telephone_facsimile (
    ci_telephone_uuid uuid NOT NULL,
    telephone_fax character varying(255)
);


--
-- TOC entry 219 (class 1259 OID 491755)
-- Name: ci_telephone_facsimile_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_telephone_facsimile_aud (
    rev integer NOT NULL,
    revtype smallint NOT NULL,
    ci_telephone_uuid uuid NOT NULL,
    setordinal integer NOT NULL,
    telephone_fax character varying(255)
);


--
-- TOC entry 220 (class 1259 OID 491758)
-- Name: ci_telephone_voice; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_telephone_voice (
    ci_telephone_uuid uuid NOT NULL,
    telephone_voice character varying(255)
);


--
-- TOC entry 221 (class 1259 OID 491761)
-- Name: ci_telephone_voice_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ci_telephone_voice_aud (
    rev integer NOT NULL,
    revtype smallint NOT NULL,
    ci_telephone_uuid uuid NOT NULL,
    setordinal integer NOT NULL,
    telephone_voice character varying(255)
);


--
-- TOC entry 222 (class 1259 OID 491764)
-- Name: clarification; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE clarification (
    uuid uuid NOT NULL
);


--
-- TOC entry 223 (class 1259 OID 491767)
-- Name: clarification_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE clarification_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL
);


--
-- TOC entry 224 (class 1259 OID 491770)
-- Name: compoundcrs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE compoundcrs (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    domainofvalidity_uuid uuid,
    crs_scope text
);


--
-- TOC entry 225 (class 1259 OID 491776)
-- Name: compoundcrs_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE compoundcrs_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    domainofvalidity_uuid uuid,
    crs_scope text
);


--
-- TOC entry 226 (class 1259 OID 491782)
-- Name: compoundcrs_singlecrs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE compoundcrs_singlecrs (
    compoundcrs_uuid uuid NOT NULL,
    componentreferencesystem_uuid uuid NOT NULL,
    index integer NOT NULL
);


--
-- TOC entry 227 (class 1259 OID 491785)
-- Name: compoundcrs_singlecrs_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE compoundcrs_singlecrs_aud (
    rev integer NOT NULL,
    compoundcrs_uuid uuid NOT NULL,
    componentreferencesystem_uuid uuid NOT NULL,
    revtype smallint,
    index integer NOT NULL
);


--
-- TOC entry 228 (class 1259 OID 491788)
-- Name: concatenatedoperationitem; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE concatenatedoperationitem (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    operationversion character varying(255),
    domainofvalidity_uuid uuid,
    sourcecrs_uuid uuid,
    targetcrs_uuid uuid
);


--
-- TOC entry 229 (class 1259 OID 491794)
-- Name: concatenatedoperationitem_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE concatenatedoperationitem_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    operationversion character varying(255),
    domainofvalidity_uuid uuid,
    sourcecrs_uuid uuid,
    targetcrs_uuid uuid
);


--
-- TOC entry 230 (class 1259 OID 491800)
-- Name: concatenatedoperationitem_coordinateoperationitem; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE concatenatedoperationitem_coordinateoperationitem (
    concatenatedoperationitem_uuid uuid NOT NULL,
    coordoperation_uuid uuid NOT NULL
);


--
-- TOC entry 231 (class 1259 OID 491803)
-- Name: concatenatedoperationitem_coordinateoperationitem_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE concatenatedoperationitem_coordinateoperationitem_aud (
    rev integer NOT NULL,
    concatenatedoperationitem_uuid uuid NOT NULL,
    coordoperation_uuid uuid NOT NULL,
    revtype smallint
);


--
-- TOC entry 232 (class 1259 OID 491806)
-- Name: conversionitem; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE conversionitem (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    operationversion character varying(255),
    domainofvalidity_uuid uuid,
    sourcecrs_uuid uuid,
    targetcrs_uuid uuid,
    method_uuid uuid
);


--
-- TOC entry 233 (class 1259 OID 491812)
-- Name: conversionitem_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE conversionitem_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    operationversion character varying(255),
    domainofvalidity_uuid uuid,
    sourcecrs_uuid uuid,
    targetcrs_uuid uuid,
    method_uuid uuid
);


--
-- TOC entry 234 (class 1259 OID 491818)
-- Name: coordinateoperationitem_dq_positionalaccuracy; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE coordinateoperationitem_dq_positionalaccuracy (
    coordinateoperationitem_uuid uuid NOT NULL,
    coordinateoperationaccuracy_uuid uuid NOT NULL
);


--
-- TOC entry 235 (class 1259 OID 491821)
-- Name: coordinateoperationitem_dq_positionalaccuracy_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE coordinateoperationitem_dq_positionalaccuracy_aud (
    rev integer NOT NULL,
    coordinateoperationitem_uuid uuid NOT NULL,
    coordinateoperationaccuracy_uuid uuid NOT NULL,
    revtype smallint
);


--
-- TOC entry 236 (class 1259 OID 491824)
-- Name: coordinateoperationitem_scope; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE coordinateoperationitem_scope (
    coordinateoperationitem_uuid uuid NOT NULL,
    scope character varying(255)
);


--
-- TOC entry 237 (class 1259 OID 491827)
-- Name: coordinateoperationitem_scope_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE coordinateoperationitem_scope_aud (
    rev integer NOT NULL,
    coordinateoperationitem_uuid uuid NOT NULL,
    scope character varying(255) NOT NULL,
    revtype smallint
);


--
-- TOC entry 238 (class 1259 OID 491830)
-- Name: coordinatesystem_axis; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE coordinatesystem_axis (
    coordinatesystem_uuid uuid NOT NULL,
    axes_uuid uuid NOT NULL,
    axis_index integer NOT NULL
);


--
-- TOC entry 239 (class 1259 OID 491833)
-- Name: coordinatesystem_axis_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE coordinatesystem_axis_aud (
    rev integer NOT NULL,
    coordinatesystem_uuid uuid NOT NULL,
    axes_uuid uuid NOT NULL,
    axis_index integer NOT NULL,
    revtype smallint
);


--
-- TOC entry 240 (class 1259 OID 491836)
-- Name: crs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE crs (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    domainofvalidity_uuid uuid,
    crs_scope text
);


--
-- TOC entry 241 (class 1259 OID 491842)
-- Name: crs_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE crs_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    domainofvalidity_uuid uuid,
    crs_scope text
);


--
-- TOC entry 242 (class 1259 OID 491848)
-- Name: delegation; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE delegation (
    uuid uuid NOT NULL,
    actor_uuid uuid NOT NULL,
    role_uuid uuid NOT NULL,
    isapproved boolean NOT NULL,
    delegatingorganization_uuid uuid NOT NULL
);


--
-- TOC entry 243 (class 1259 OID 491851)
-- Name: delegation_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE delegation_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    actor_uuid uuid,
    role_uuid uuid,
    isapproved boolean,
    delegatingorganization_uuid uuid
);


--
-- TOC entry 244 (class 1259 OID 491854)
-- Name: dq_absoluteexternalpositionalaccuracy; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE dq_absoluteexternalpositionalaccuracy (
    uuid uuid NOT NULL,
    evaluationmethoddescription text,
    evaluationmethodtype_codelist character varying(255),
    evaluationmethodtype_codelistvalue character varying(255),
    evaluationmethodtype_codespace character varying(255),
    type bytea,
    evaluationmethodtype character varying(255),
    measuredescription text,
    measureidentification character varying(255),
    nameofmeasure text,
    evaluationprocedure_uuid uuid,
    authority_uuid uuid,
    result_uuid uuid
);


--
-- TOC entry 245 (class 1259 OID 491860)
-- Name: dq_absoluteexternalpositionalaccuracy_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE dq_absoluteexternalpositionalaccuracy_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    evaluationmethoddescription text,
    measuredescription text,
    measureidentification character varying(255),
    nameofmeasure text,
    evaluationprocedure_uuid uuid,
    authority_uuid uuid,
    result_uuid uuid
);


--
-- TOC entry 246 (class 1259 OID 491866)
-- Name: dq_element_datetime; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE dq_element_datetime (
    dq_element_uuid uuid NOT NULL,
    datetime timestamp without time zone
);


--
-- TOC entry 247 (class 1259 OID 491869)
-- Name: dq_element_datetime_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE dq_element_datetime_aud (
    rev integer NOT NULL,
    dq_element_uuid uuid NOT NULL,
    datetime timestamp without time zone NOT NULL,
    revtype smallint
);


--
-- TOC entry 248 (class 1259 OID 491872)
-- Name: dq_griddeddatapositionalaccuracy; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE dq_griddeddatapositionalaccuracy (
    uuid uuid NOT NULL,
    evaluationmethoddescription text,
    evaluationmethodtype_codelist character varying(255),
    evaluationmethodtype_codelistvalue character varying(255),
    evaluationmethodtype_codespace character varying(255),
    type bytea,
    evaluationmethodtype character varying(255),
    measuredescription text,
    measureidentification character varying(255),
    nameofmeasure text,
    evaluationprocedure_uuid uuid,
    authority_uuid uuid,
    result_uuid uuid
);


--
-- TOC entry 249 (class 1259 OID 491878)
-- Name: dq_griddeddatapositionalaccuracy_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE dq_griddeddatapositionalaccuracy_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    evaluationmethoddescription text,
    measuredescription text,
    measureidentification character varying(255),
    nameofmeasure text,
    evaluationprocedure_uuid uuid,
    authority_uuid uuid,
    result_uuid uuid
);


--
-- TOC entry 250 (class 1259 OID 491884)
-- Name: dq_relativeinternalpositionalaccuracy; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE dq_relativeinternalpositionalaccuracy (
    uuid uuid NOT NULL,
    evaluationmethoddescription text,
    evaluationmethodtype_codelist character varying(255),
    evaluationmethodtype_codelistvalue character varying(255),
    evaluationmethodtype_codespace character varying(255),
    type bytea,
    evaluationmethodtype character varying(255),
    measuredescription text,
    measureidentification character varying(255),
    nameofmeasure text,
    evaluationprocedure_uuid uuid,
    authority_uuid uuid,
    result_uuid uuid
);


--
-- TOC entry 251 (class 1259 OID 491890)
-- Name: dq_relativeinternalpositionalaccuracy_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE dq_relativeinternalpositionalaccuracy_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    evaluationmethoddescription text,
    measuredescription text,
    measureidentification character varying(255),
    nameofmeasure text,
    evaluationprocedure_uuid uuid,
    authority_uuid uuid,
    result_uuid uuid
);


--
-- TOC entry 252 (class 1259 OID 491896)
-- Name: ellipsoid; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ellipsoid (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    inverseflattening double precision,
    issphere boolean NOT NULL,
    semimajoraxis double precision,
    semiminoraxis double precision,
    inverseflatteninguom_uuid uuid,
    semimajoraxisuom_uuid uuid,
    semiminoraxisuom_uuid uuid
);


--
-- TOC entry 253 (class 1259 OID 491902)
-- Name: ellipsoid_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ellipsoid_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    inverseflattening double precision,
    issphere boolean,
    semimajoraxis double precision,
    semiminoraxis double precision,
    inverseflatteninguom_uuid uuid,
    semimajoraxisuom_uuid uuid,
    semiminoraxisuom_uuid uuid
);


--
-- TOC entry 254 (class 1259 OID 491908)
-- Name: ellipsoidalcs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ellipsoidalcs (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text
);


--
-- TOC entry 255 (class 1259 OID 491914)
-- Name: ellipsoidalcs_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ellipsoidalcs_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text
);


--
-- TOC entry 256 (class 1259 OID 491920)
-- Name: engineeringcrs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE engineeringcrs (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    domainofvalidity_uuid uuid,
    crs_scope text,
    basecrs_uuid uuid,
    conversion_uuid uuid,
    coordinatesystem_uuid uuid NOT NULL,
    datum_uuid uuid
);


--
-- TOC entry 257 (class 1259 OID 491926)
-- Name: engineeringcrs_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE engineeringcrs_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    domainofvalidity_uuid uuid,
    crs_scope text,
    basecrs_uuid uuid,
    conversion_uuid uuid,
    coordinatesystem_uuid uuid,
    datum_uuid uuid
);


--
-- TOC entry 258 (class 1259 OID 491932)
-- Name: engineeringdatum; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE engineeringdatum (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    origin_description text,
    realization_epoch date,
    datum_scope text,
    domainofvalidity_uuid uuid,
    coordinatereferenceepoch date
);


--
-- TOC entry 259 (class 1259 OID 491938)
-- Name: engineeringdatum_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE engineeringdatum_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    origin_description text,
    realization_epoch date,
    datum_scope text,
    domainofvalidity_uuid uuid,
    coordinatereferenceepoch date
);


--
-- TOC entry 260 (class 1259 OID 491944)
-- Name: epsgisomapping; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE epsgisomapping (
    uuid uuid NOT NULL,
    epsgcode integer,
    isocode integer,
    itemclass character varying(255)
);


--
-- TOC entry 261 (class 1259 OID 491947)
-- Name: ex_extent; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ex_extent (
    uuid uuid NOT NULL,
    description text
);


--
-- TOC entry 262 (class 1259 OID 491953)
-- Name: ex_extent_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ex_extent_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    description text
);


--
-- TOC entry 263 (class 1259 OID 491959)
-- Name: ex_extent_ex_geographicextent; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ex_extent_ex_geographicextent (
    ex_extent_uuid uuid NOT NULL,
    geographicelement_uuid uuid NOT NULL
);


--
-- TOC entry 264 (class 1259 OID 491962)
-- Name: ex_extent_ex_geographicextent_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ex_extent_ex_geographicextent_aud (
    rev integer NOT NULL,
    ex_extent_uuid uuid NOT NULL,
    geographicelement_uuid uuid NOT NULL,
    revtype smallint
);


--
-- TOC entry 265 (class 1259 OID 491965)
-- Name: ex_extent_ex_verticalextent; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ex_extent_ex_verticalextent (
    ex_extent_uuid uuid NOT NULL,
    verticalelement_uuid uuid NOT NULL
);


--
-- TOC entry 266 (class 1259 OID 491968)
-- Name: ex_extent_ex_verticalextent_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ex_extent_ex_verticalextent_aud (
    rev integer NOT NULL,
    ex_extent_uuid uuid NOT NULL,
    verticalelement_uuid uuid NOT NULL,
    revtype smallint
);


--
-- TOC entry 267 (class 1259 OID 491971)
-- Name: ex_geographicboundingbox; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ex_geographicboundingbox (
    uuid uuid NOT NULL,
    extenttypecode boolean,
    eastboundlongitude double precision,
    northboundlatitude double precision,
    southboundlatitude double precision,
    westboundlongitude double precision
);


--
-- TOC entry 268 (class 1259 OID 491974)
-- Name: ex_geographicboundingbox_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ex_geographicboundingbox_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    extenttypecode boolean,
    eastboundlongitude double precision,
    northboundlatitude double precision,
    southboundlatitude double precision,
    westboundlongitude double precision
);


--
-- TOC entry 269 (class 1259 OID 491977)
-- Name: ex_temporalextent; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ex_temporalextent (
    dtype character varying(31) NOT NULL,
    uuid uuid NOT NULL
);


--
-- TOC entry 270 (class 1259 OID 491980)
-- Name: ex_temporalextent_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ex_temporalextent_aud (
    dtype character varying(31) NOT NULL,
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint
);


--
-- TOC entry 271 (class 1259 OID 491983)
-- Name: ex_verticalextent; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ex_verticalextent (
    uuid uuid NOT NULL,
    maximumvalue double precision,
    minimumvalue double precision,
    uomlength bytea
);


--
-- TOC entry 272 (class 1259 OID 491989)
-- Name: ex_verticalextent_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE ex_verticalextent_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    maximumvalue double precision,
    minimumvalue double precision,
    uomlength bytea
);


--
-- TOC entry 273 (class 1259 OID 491995)
-- Name: formula; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE formula (
    uuid uuid NOT NULL,
    formula text,
    formulacitation_uuid uuid
);


--
-- TOC entry 274 (class 1259 OID 492001)
-- Name: formula_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE formula_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    formula text,
    formulacitation_uuid uuid
);


--
-- TOC entry 275 (class 1259 OID 492007)
-- Name: generaloperationparameteritem; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE generaloperationparameteritem (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    minimumoccurs integer,
    group_uuid uuid
);


--
-- TOC entry 276 (class 1259 OID 492013)
-- Name: generaloperationparameteritem_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE generaloperationparameteritem_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    minimumoccurs integer,
    group_uuid uuid
);


--
-- TOC entry 277 (class 1259 OID 492019)
-- Name: generalparametervalue; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE generalparametervalue (
    dtype character varying(31) NOT NULL,
    uuid uuid NOT NULL,
    parametertype integer,
    parameter_uuid uuid,
    group_uuid uuid
);


--
-- TOC entry 278 (class 1259 OID 492022)
-- Name: generalparametervalue_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE generalparametervalue_aud (
    dtype character varying(31) NOT NULL,
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    parameter_uuid uuid,
    group_uuid uuid,
    parametertype integer
);


--
-- TOC entry 279 (class 1259 OID 492025)
-- Name: geodeticcrs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE geodeticcrs (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    domainofvalidity_uuid uuid,
    crs_scope text,
    basecrs_uuid uuid,
    conversion_uuid uuid,
    coordinatesystem_uuid uuid NOT NULL,
    datum_uuid uuid
);


--
-- TOC entry 280 (class 1259 OID 492031)
-- Name: geodeticcrs_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE geodeticcrs_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    domainofvalidity_uuid uuid,
    crs_scope text,
    basecrs_uuid uuid,
    conversion_uuid uuid,
    coordinatesystem_uuid uuid,
    datum_uuid uuid
);


--
-- TOC entry 281 (class 1259 OID 492037)
-- Name: geodeticdatum; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE geodeticdatum (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    origin_description text,
    realization_epoch date,
    datum_scope text,
    domainofvalidity_uuid uuid,
    ellipsoid_uuid uuid,
    primemeridian_uuid uuid,
    coordinatereferenceepoch date
);


--
-- TOC entry 282 (class 1259 OID 492043)
-- Name: geodeticdatum_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE geodeticdatum_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    origin_description text,
    realization_epoch date,
    datum_scope text,
    domainofvalidity_uuid uuid,
    ellipsoid_uuid uuid,
    primemeridian_uuid uuid,
    coordinatereferenceepoch date
);


--
-- TOC entry 283 (class 1259 OID 492049)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 284 (class 1259 OID 492051)
-- Name: identifieditem_aliases; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE identifieditem_aliases (
    identifieditem_uuid uuid NOT NULL,
    aliases character varying(255)
);


--
-- TOC entry 285 (class 1259 OID 492054)
-- Name: identifieditem_aliases_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE identifieditem_aliases_aud (
    rev integer NOT NULL,
    identifieditem_uuid uuid NOT NULL,
    aliases character varying(255) NOT NULL,
    revtype smallint
);


--
-- TOC entry 286 (class 1259 OID 492057)
-- Name: namingsystemitem; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE namingsystemitem (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text
);


--
-- TOC entry 287 (class 1259 OID 492063)
-- Name: namingsystemitem_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE namingsystemitem_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text
);


--
-- TOC entry 288 (class 1259 OID 492069)
-- Name: operationmethoditem; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE operationmethoditem (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    formula text,
    reversible boolean,
    sourcedimensions integer,
    targetdimensions integer,
    formulacitation_uuid uuid
);


--
-- TOC entry 289 (class 1259 OID 492075)
-- Name: operationmethoditem_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE operationmethoditem_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    formula text,
    reversible boolean,
    sourcedimensions integer,
    targetdimensions integer,
    formulacitation_uuid uuid
);


--
-- TOC entry 290 (class 1259 OID 492081)
-- Name: operationmethoditem_generaloperationparameteritem; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE operationmethoditem_generaloperationparameteritem (
    operationmethoditem_uuid uuid NOT NULL,
    parameter_uuid uuid NOT NULL
);


--
-- TOC entry 291 (class 1259 OID 492084)
-- Name: operationmethoditem_generaloperationparameteritem_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE operationmethoditem_generaloperationparameteritem_aud (
    rev integer NOT NULL,
    operationmethoditem_uuid uuid NOT NULL,
    parameter_uuid uuid NOT NULL,
    revtype smallint
);


--
-- TOC entry 292 (class 1259 OID 492087)
-- Name: operationparametergroupitem; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE operationparametergroupitem (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    minimumoccurs integer,
    group_uuid uuid,
    maximumoccurs integer
);


--
-- TOC entry 293 (class 1259 OID 492093)
-- Name: operationparametergroupitem_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE operationparametergroupitem_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    minimumoccurs integer,
    group_uuid uuid,
    maximumoccurs integer
);


--
-- TOC entry 294 (class 1259 OID 492099)
-- Name: operationparameteritem; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE operationparameteritem (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    minimumoccurs integer,
    group_uuid uuid
);


--
-- TOC entry 295 (class 1259 OID 492105)
-- Name: operationparameteritem_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE operationparameteritem_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    minimumoccurs integer,
    group_uuid uuid
);


--
-- TOC entry 296 (class 1259 OID 492111)
-- Name: operationparametervalue_parametervalue; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE operationparametervalue_parametervalue (
    operationparametervalue_uuid uuid NOT NULL,
    uom_uuid uuid,
    value bytea
);


--
-- TOC entry 297 (class 1259 OID 492117)
-- Name: operationparametervalue_parametervalue_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE operationparametervalue_parametervalue_aud (
    rev integer NOT NULL,
    revtype smallint NOT NULL,
    operationparametervalue_uuid uuid NOT NULL,
    value bytea,
    uom_uuid uuid
);


--
-- TOC entry 298 (class 1259 OID 492123)
-- Name: operationparametervalue_parametervaluesimple; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE operationparametervalue_parametervaluesimple (
    operationparametervalue_uuid uuid NOT NULL,
    parametervaluesimple text
);


--
-- TOC entry 299 (class 1259 OID 492129)
-- Name: operationparametervalue_parametervaluesimple_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE operationparametervalue_parametervaluesimple_aud (
    rev integer NOT NULL,
    operationparametervalue_uuid uuid NOT NULL,
    parametervaluesimple text NOT NULL,
    revtype smallint
);


--
-- TOC entry 300 (class 1259 OID 492135)
-- Name: organization; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE organization (
    name text NOT NULL,
    uuid uuid NOT NULL,
    submittingorganization_uuid uuid NOT NULL
);


--
-- TOC entry 301 (class 1259 OID 492141)
-- Name: organization_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE organization_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    name text,
    submittingorganization_uuid uuid
);


--
-- TOC entry 302 (class 1259 OID 492147)
-- Name: organizationrelatedrole; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE organizationrelatedrole (
    uuid uuid NOT NULL,
    name text,
    organization_uuid uuid NOT NULL
);


--
-- TOC entry 303 (class 1259 OID 492153)
-- Name: organizationrelatedrole_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE organizationrelatedrole_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    name text,
    organization_uuid uuid
);


--
-- TOC entry 304 (class 1259 OID 492159)
-- Name: passthroughoperationitem; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE passthroughoperationitem (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    operationversion character varying(255),
    domainofvalidity_uuid uuid,
    sourcecrs_uuid uuid,
    targetcrs_uuid uuid
);


--
-- TOC entry 305 (class 1259 OID 492165)
-- Name: passthroughoperationitem_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE passthroughoperationitem_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    operationversion character varying(255),
    domainofvalidity_uuid uuid,
    sourcecrs_uuid uuid,
    targetcrs_uuid uuid
);


--
-- TOC entry 306 (class 1259 OID 492171)
-- Name: post; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE post (
    uuid uuid NOT NULL,
    author character varying(255),
    subject character varying(255),
    text character varying(255),
    timeedited timestamp without time zone,
    timeposted timestamp without time zone,
    discussion_id uuid,
    parentpost_uuid uuid
);


--
-- TOC entry 307 (class 1259 OID 492177)
-- Name: post_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE post_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    author character varying(255),
    subject character varying(255),
    text character varying(255),
    timeedited timestamp without time zone,
    timeposted timestamp without time zone,
    discussion_id uuid,
    parentpost_uuid uuid
);


--
-- TOC entry 308 (class 1259 OID 492183)
-- Name: primemeridian; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE primemeridian (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    greenwichlongitude double precision,
    greenwichlongitudeuom_uuid uuid
);


--
-- TOC entry 309 (class 1259 OID 492189)
-- Name: primemeridian_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE primemeridian_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    greenwichlongitude double precision,
    greenwichlongitudeuom_uuid uuid
);


--
-- TOC entry 310 (class 1259 OID 492195)
-- Name: projectedcrs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE projectedcrs (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    domainofvalidity_uuid uuid,
    crs_scope text,
    basecrs_uuid uuid,
    conversion_uuid uuid,
    coordinatesystem_uuid uuid NOT NULL,
    datum_uuid uuid
);


--
-- TOC entry 311 (class 1259 OID 492201)
-- Name: projectedcrs_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE projectedcrs_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    domainofvalidity_uuid uuid,
    crs_scope text,
    basecrs_uuid uuid,
    conversion_uuid uuid,
    coordinatesystem_uuid uuid,
    datum_uuid uuid
);


--
-- TOC entry 312 (class 1259 OID 492207)
-- Name: proposal; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE proposal (
    uuid uuid NOT NULL,
    datesubmitted timestamp without time zone,
    isconcluded boolean,
    status character varying(255),
    group_uuid uuid,
    sponsor_uuid uuid
);


--
-- TOC entry 313 (class 1259 OID 492210)
-- Name: proposal_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE proposal_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    datesubmitted timestamp without time zone,
    isconcluded boolean,
    status character varying(255),
    group_uuid uuid,
    sponsor_uuid uuid
);


--
-- TOC entry 314 (class 1259 OID 492213)
-- Name: proposaldiscussion; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE proposaldiscussion (
    uuid uuid NOT NULL,
    discussiontype character varying(255),
    discussedproposal_uuid uuid,
    owner_uuid uuid
);


--
-- TOC entry 315 (class 1259 OID 492216)
-- Name: proposaldiscussion_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE proposaldiscussion_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    discussiontype character varying(255),
    discussedproposal_uuid uuid,
    owner_uuid uuid
);


--
-- TOC entry 316 (class 1259 OID 492219)
-- Name: proposaldiscussion_invitees; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE proposaldiscussion_invitees (
    proposaldiscussion_uuid uuid NOT NULL,
    invitees character varying(255),
    invitees_key character varying(255) NOT NULL
);


--
-- TOC entry 317 (class 1259 OID 492225)
-- Name: proposaldiscussion_invitees_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE proposaldiscussion_invitees_aud (
    rev integer NOT NULL,
    proposaldiscussion_uuid uuid NOT NULL,
    invitees character varying(255) NOT NULL,
    invitees_key character varying(255) NOT NULL,
    revtype smallint
);


--
-- TOC entry 318 (class 1259 OID 492231)
-- Name: proposalgroup; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE proposalgroup (
    name text,
    uuid uuid NOT NULL
);


--
-- TOC entry 319 (class 1259 OID 492237)
-- Name: proposalgroup_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE proposalgroup_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    name text
);


--
-- TOC entry 320 (class 1259 OID 492243)
-- Name: proposalnote; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE proposalnote (
    uuid uuid NOT NULL,
    note text,
    author_uuid uuid,
    proposal_uuid uuid
);


--
-- TOC entry 321 (class 1259 OID 492249)
-- Name: proposalnote_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE proposalnote_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    note text,
    author_uuid uuid,
    proposal_uuid uuid
);


--
-- TOC entry 322 (class 1259 OID 492255)
-- Name: re_additioninformation; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_additioninformation (
    uuid uuid NOT NULL
);


--
-- TOC entry 323 (class 1259 OID 492258)
-- Name: re_additioninformation_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_additioninformation_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL
);


--
-- TOC entry 324 (class 1259 OID 492261)
-- Name: re_alternativeexpression; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_alternativeexpression (
    uuid uuid NOT NULL,
    definition character varying(2000),
    description character varying(2000),
    name character varying(2000),
    locale_uuid uuid
);


--
-- TOC entry 325 (class 1259 OID 492267)
-- Name: re_alternativeexpression_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_alternativeexpression_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    definition character varying(2000),
    description character varying(2000),
    name character varying(2000),
    locale_uuid uuid
);


--
-- TOC entry 326 (class 1259 OID 492273)
-- Name: re_alternativeexpression_fieldofapplication; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_alternativeexpression_fieldofapplication (
    re_alternativeexpression_uuid uuid NOT NULL,
    fieldofapplication character varying(2000)
);


--
-- TOC entry 327 (class 1259 OID 492279)
-- Name: re_alternativeexpression_fieldofapplication_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_alternativeexpression_fieldofapplication_aud (
    rev integer NOT NULL,
    revtype smallint NOT NULL,
    re_alternativeexpression_uuid uuid NOT NULL,
    setordinal integer NOT NULL,
    fieldofapplication character varying(2000)
);


--
-- TOC entry 328 (class 1259 OID 492285)
-- Name: re_amendmentinformation; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_amendmentinformation (
    amendmenttype character varying(255),
    uuid uuid NOT NULL
);


--
-- TOC entry 329 (class 1259 OID 492288)
-- Name: re_amendmentinformation_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_amendmentinformation_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    amendmenttype character varying(255)
);


--
-- TOC entry 330 (class 1259 OID 492291)
-- Name: re_clarificationinformation; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_clarificationinformation (
    proposedchange character varying(2000),
    uuid uuid NOT NULL
);


--
-- TOC entry 331 (class 1259 OID 492297)
-- Name: re_clarificationinformation_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_clarificationinformation_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    proposedchange character varying(2000)
);


--
-- TOC entry 332 (class 1259 OID 492303)
-- Name: re_itemclass; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_itemclass (
    uuid uuid NOT NULL,
    name text,
    technicalstandard_uuid uuid
);


--
-- TOC entry 333 (class 1259 OID 492309)
-- Name: re_itemclass_alternativenames; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_itemclass_alternativenames (
    re_itemclass_uuid uuid NOT NULL,
    alternativename_name character varying(255) NOT NULL
);


--
-- TOC entry 334 (class 1259 OID 492312)
-- Name: re_itemclass_alternativenames_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_itemclass_alternativenames_aud (
    rev integer NOT NULL,
    revtype smallint NOT NULL,
    re_itemclass_uuid uuid NOT NULL,
    setordinal integer NOT NULL,
    alternativename_name character varying(255)
);


--
-- TOC entry 335 (class 1259 OID 492315)
-- Name: re_itemclass_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_itemclass_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    name text,
    technicalstandard_uuid uuid
);


--
-- TOC entry 336 (class 1259 OID 492321)
-- Name: re_locale; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_locale (
    uuid uuid NOT NULL,
    characterencoding integer,
    country text,
    codelist text,
    codelistvalue text,
    codespace text,
    type bytea,
    value text,
    name text,
    citation_uuid uuid
);


--
-- TOC entry 337 (class 1259 OID 492327)
-- Name: re_locale_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_locale_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    characterencoding integer,
    country text,
    name text,
    citation_uuid uuid
);


--
-- TOC entry 338 (class 1259 OID 492333)
-- Name: re_proposalmanagementinformation; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_proposalmanagementinformation (
    uuid uuid NOT NULL,
    controlbodydecisionevent text,
    controlbodynotes text,
    datedisposed date,
    dateproposed date,
    disposition character varying(255),
    justification text NOT NULL,
    registermanagernotes text,
    status character varying(255),
    item_uuid uuid,
    sponsor_uuid uuid
);


--
-- TOC entry 339 (class 1259 OID 492339)
-- Name: re_proposalmanagementinformation_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_proposalmanagementinformation_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    controlbodydecisionevent text,
    controlbodynotes text,
    datedisposed date,
    dateproposed date,
    disposition character varying(255),
    justification text,
    registermanagernotes text,
    status character varying(255),
    item_uuid uuid,
    sponsor_uuid uuid
);


--
-- TOC entry 340 (class 1259 OID 492345)
-- Name: re_reference; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_reference (
    uuid uuid NOT NULL,
    itemidentifieratsource character varying(2000),
    notes character varying(2000),
    referencetext character varying(2000),
    similarity integer,
    text text,
    citation_uuid uuid
);


--
-- TOC entry 341 (class 1259 OID 492351)
-- Name: re_reference_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_reference_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    itemidentifieratsource character varying(2000),
    notes character varying(2000),
    referencetext character varying(2000),
    similarity integer,
    text text,
    citation_uuid uuid
);


--
-- TOC entry 342 (class 1259 OID 492357)
-- Name: re_register; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_register (
    uuid uuid NOT NULL,
    text text,
    contentsummary text,
    dateoflastchange timestamp without time zone,
    name text NOT NULL,
    versiondate date,
    versionnumber text,
    citation_uuid uuid,
    manager_uuid uuid,
    operatinglanguage_uuid uuid,
    owner_uuid uuid,
    uniformresourceidentifier_uuid uuid
);


--
-- TOC entry 343 (class 1259 OID 492363)
-- Name: re_register_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_register_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    text text,
    contentsummary text,
    dateoflastchange timestamp without time zone,
    name text,
    versiondate date,
    versionnumber text,
    citation_uuid uuid,
    manager_uuid uuid,
    operatinglanguage_uuid uuid,
    owner_uuid uuid,
    uniformresourceidentifier_uuid uuid
);


--
-- TOC entry 344 (class 1259 OID 492369)
-- Name: re_register_itemclasses; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_register_itemclasses (
    itemclassid uuid NOT NULL,
    registerid uuid NOT NULL
);


--
-- TOC entry 345 (class 1259 OID 492372)
-- Name: re_register_itemclasses_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_register_itemclasses_aud (
    rev integer NOT NULL,
    registerid uuid NOT NULL,
    itemclassid uuid NOT NULL,
    revtype smallint
);


--
-- TOC entry 346 (class 1259 OID 492375)
-- Name: re_register_re_locale; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_register_re_locale (
    re_register_uuid uuid NOT NULL,
    alternativelanguages_uuid uuid NOT NULL
);


--
-- TOC entry 347 (class 1259 OID 492378)
-- Name: re_register_re_locale_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_register_re_locale_aud (
    rev integer NOT NULL,
    re_register_uuid uuid NOT NULL,
    alternativelanguages_uuid uuid NOT NULL,
    revtype smallint
);


--
-- TOC entry 348 (class 1259 OID 492381)
-- Name: re_register_submitters; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_register_submitters (
    submitterid uuid NOT NULL,
    registerid uuid NOT NULL
);


--
-- TOC entry 349 (class 1259 OID 492384)
-- Name: re_register_submitters_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_register_submitters_aud (
    rev integer NOT NULL,
    registerid uuid NOT NULL,
    submitterid uuid NOT NULL,
    revtype smallint
);


--
-- TOC entry 350 (class 1259 OID 492387)
-- Name: re_registeritem; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_registeritem (
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
    specificationsource_uuid uuid
);


--
-- TOC entry 351 (class 1259 OID 492393)
-- Name: re_registeritem_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_registeritem_aud (
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
    specificationsource_uuid uuid
);


--
-- TOC entry 352 (class 1259 OID 492399)
-- Name: re_registeritem_fieldofapplication; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_registeritem_fieldofapplication (
    re_registeritem_uuid uuid NOT NULL,
    description character varying(2000),
    name character varying(2000)
);


--
-- TOC entry 353 (class 1259 OID 492405)
-- Name: re_registeritem_fieldofapplication_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_registeritem_fieldofapplication_aud (
    rev integer NOT NULL,
    revtype smallint NOT NULL,
    re_registeritem_uuid uuid NOT NULL,
    setordinal integer NOT NULL,
    description character varying(2000),
    name character varying(2000)
);


--
-- TOC entry 354 (class 1259 OID 492411)
-- Name: re_registeritem_re_alternativeexpression; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_registeritem_re_alternativeexpression (
    re_registeritem_uuid uuid NOT NULL,
    alternativeexpressions_uuid uuid NOT NULL
);


--
-- TOC entry 355 (class 1259 OID 492414)
-- Name: re_registeritem_re_alternativeexpression_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_registeritem_re_alternativeexpression_aud (
    rev integer NOT NULL,
    re_registeritem_uuid uuid NOT NULL,
    alternativeexpressions_uuid uuid NOT NULL,
    revtype smallint
);


--
-- TOC entry 356 (class 1259 OID 492417)
-- Name: re_registeritem_successor; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_registeritem_successor (
    successorid uuid NOT NULL,
    predecessorid uuid NOT NULL
);


--
-- TOC entry 357 (class 1259 OID 492420)
-- Name: re_registeritem_successor_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_registeritem_successor_aud (
    rev integer NOT NULL,
    predecessorid uuid NOT NULL,
    successorid uuid NOT NULL,
    revtype smallint
);


--
-- TOC entry 358 (class 1259 OID 492423)
-- Name: re_registermanager; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_registermanager (
    uuid uuid NOT NULL,
    name text,
    contact_uuid uuid
);


--
-- TOC entry 359 (class 1259 OID 492429)
-- Name: re_registermanager_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_registermanager_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    name text,
    contact_uuid uuid
);


--
-- TOC entry 360 (class 1259 OID 492435)
-- Name: re_registerowner; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_registerowner (
    uuid uuid NOT NULL,
    name text,
    contact_uuid uuid
);


--
-- TOC entry 361 (class 1259 OID 492441)
-- Name: re_registerowner_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_registerowner_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    name text,
    contact_uuid uuid
);


--
-- TOC entry 362 (class 1259 OID 492447)
-- Name: re_submittingorganization; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_submittingorganization (
    uuid uuid NOT NULL,
    name text,
    contact_uuid uuid
);


--
-- TOC entry 363 (class 1259 OID 492453)
-- Name: re_submittingorganization_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE re_submittingorganization_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    name text,
    contact_uuid uuid
);


--
-- TOC entry 364 (class 1259 OID 492459)
-- Name: registerrelatedrole; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE registerrelatedrole (
    uuid uuid NOT NULL,
    name text,
    register_uuid uuid NOT NULL
);


--
-- TOC entry 365 (class 1259 OID 492465)
-- Name: registerrelatedrole_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE registerrelatedrole_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    name text,
    register_uuid uuid
);


--
-- TOC entry 366 (class 1259 OID 492471)
-- Name: registryuser; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE registryuser (
    confirmationtoken bytea,
    emailaddress text NOT NULL,
    isactive boolean NOT NULL,
    name text,
    passwordhash character varying(255) NOT NULL,
    preferredlanguage character varying(255) NOT NULL,
    uuid uuid NOT NULL,
    organization_uuid uuid
);


--
-- TOC entry 367 (class 1259 OID 492477)
-- Name: registryuser_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE registryuser_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    confirmationtoken bytea,
    emailaddress text,
    isactive boolean,
    name text,
    passwordhash character varying(255),
    preferredlanguage character varying(255),
    organization_uuid uuid
);


--
-- TOC entry 368 (class 1259 OID 492483)
-- Name: registryusergroup; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE registryusergroup (
    uuid uuid NOT NULL,
    name text
);


--
-- TOC entry 369 (class 1259 OID 492489)
-- Name: registryusergroup_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE registryusergroup_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    name text
);


--
-- TOC entry 370 (class 1259 OID 492495)
-- Name: result; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE result (
    dtype character varying(31) NOT NULL,
    uuid uuid NOT NULL,
    accuracy real,
    errorstatistic text,
    accuracyunit_uuid uuid
);


--
-- TOC entry 371 (class 1259 OID 492501)
-- Name: result_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE result_aud (
    dtype character varying(31) NOT NULL,
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    accuracy real,
    errorstatistic text,
    accuracyunit_uuid uuid
);


--
-- TOC entry 372 (class 1259 OID 492507)
-- Name: retirement; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE retirement (
    uuid uuid NOT NULL
);


--
-- TOC entry 373 (class 1259 OID 492510)
-- Name: retirement_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE retirement_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL
);


--
-- TOC entry 374 (class 1259 OID 492513)
-- Name: revinfo; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE revinfo (
    rev integer NOT NULL,
    revtstmp bigint
);


--
-- TOC entry 375 (class 1259 OID 492516)
-- Name: role; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE role (
    uuid uuid NOT NULL,
    name text
);


--
-- TOC entry 376 (class 1259 OID 492522)
-- Name: role_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE role_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    name text
);


--
-- TOC entry 377 (class 1259 OID 492528)
-- Name: simpleproposal; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE simpleproposal (
    uuid uuid NOT NULL,
    proposalmanagementinformation_uuid uuid NOT NULL
);


--
-- TOC entry 378 (class 1259 OID 492531)
-- Name: simpleproposal_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE simpleproposal_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    proposalmanagementinformation_uuid uuid
);


--
-- TOC entry 379 (class 1259 OID 492534)
-- Name: singleoperationitem; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE singleoperationitem (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    operationversion character varying(255),
    domainofvalidity_uuid uuid,
    sourcecrs_uuid uuid,
    targetcrs_uuid uuid,
    method_uuid uuid
);


--
-- TOC entry 380 (class 1259 OID 492540)
-- Name: singleoperationitem_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE singleoperationitem_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    operationversion character varying(255),
    domainofvalidity_uuid uuid,
    sourcecrs_uuid uuid,
    targetcrs_uuid uuid,
    method_uuid uuid
);


--
-- TOC entry 381 (class 1259 OID 492546)
-- Name: singleoperationitem_generalparametervalue; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE singleoperationitem_generalparametervalue (
    singleoperationitem_uuid uuid NOT NULL,
    parametervalue_uuid uuid NOT NULL
);


--
-- TOC entry 382 (class 1259 OID 492549)
-- Name: singleoperationitem_generalparametervalue_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE singleoperationitem_generalparametervalue_aud (
    rev integer NOT NULL,
    singleoperationitem_uuid uuid NOT NULL,
    parametervalue_uuid uuid NOT NULL,
    revtype smallint
);


--
-- TOC entry 383 (class 1259 OID 492552)
-- Name: sphericalcs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE sphericalcs (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text
);


--
-- TOC entry 384 (class 1259 OID 492558)
-- Name: sphericalcs_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE sphericalcs_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text
);


--
-- TOC entry 385 (class 1259 OID 492564)
-- Name: supersession; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE supersession (
    controlbodynotes text,
    justification text NOT NULL,
    registermanagernotes text,
    uuid uuid NOT NULL,
    targetregister_uuid uuid
);


--
-- TOC entry 386 (class 1259 OID 492570)
-- Name: supersession_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE supersession_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL,
    controlbodynotes text,
    justification text,
    registermanagernotes text,
    targetregister_uuid uuid
);


--
-- TOC entry 387 (class 1259 OID 492576)
-- Name: supersession_supersedingitems; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE supersession_supersedingitems (
    supersessionid uuid NOT NULL,
    supersedingitems_uuid uuid NOT NULL
);


--
-- TOC entry 388 (class 1259 OID 492579)
-- Name: supersession_supersedingitems_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE supersession_supersedingitems_aud (
    rev integer NOT NULL,
    supersessionid uuid NOT NULL,
    supersedingitems_uuid uuid NOT NULL,
    revtype smallint
);


--
-- TOC entry 389 (class 1259 OID 492582)
-- Name: supersessionpart; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE supersessionpart (
    uuid uuid NOT NULL
);


--
-- TOC entry 390 (class 1259 OID 492585)
-- Name: supersessionpart_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE supersessionpart_aud (
    uuid uuid NOT NULL,
    rev integer NOT NULL
);


--
-- TOC entry 391 (class 1259 OID 492588)
-- Name: transformationitem; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE transformationitem (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    operationversion character varying(255),
    domainofvalidity_uuid uuid,
    sourcecrs_uuid uuid,
    targetcrs_uuid uuid,
    method_uuid uuid
);


--
-- TOC entry 392 (class 1259 OID 492594)
-- Name: transformationitem_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE transformationitem_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    operationversion character varying(255),
    domainofvalidity_uuid uuid,
    sourcecrs_uuid uuid,
    targetcrs_uuid uuid,
    method_uuid uuid
);


--
-- TOC entry 393 (class 1259 OID 492600)
-- Name: unitofmeasure; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE unitofmeasure (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    measuretype character varying(255) NOT NULL,
    offsettostandardunit double precision,
    scaletostandardunitdenominator double precision,
    scaletostandardunitnumerator double precision,
    symbol character varying(255),
    standardunit_uuid uuid
);


--
-- TOC entry 394 (class 1259 OID 492606)
-- Name: unitofmeasure_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE unitofmeasure_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    measuretype character varying(255),
    offsettostandardunit double precision,
    scaletostandardunitdenominator double precision,
    scaletostandardunitnumerator double precision,
    symbol character varying(255),
    standardunit_uuid uuid
);


--
-- TOC entry 395 (class 1259 OID 492612)
-- Name: verticalcrs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE verticalcrs (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    domainofvalidity_uuid uuid,
    crs_scope text,
    basecrs_uuid uuid,
    conversion_uuid uuid,
    coordinatesystem_uuid uuid NOT NULL,
    datum_uuid uuid
);


--
-- TOC entry 396 (class 1259 OID 492618)
-- Name: verticalcrs_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE verticalcrs_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    domainofvalidity_uuid uuid,
    crs_scope text,
    basecrs_uuid uuid,
    conversion_uuid uuid,
    coordinatesystem_uuid uuid,
    datum_uuid uuid
);


--
-- TOC entry 397 (class 1259 OID 492624)
-- Name: verticalcs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE verticalcs (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text
);


--
-- TOC entry 398 (class 1259 OID 492630)
-- Name: verticalcs_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE verticalcs_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text
);


--
-- TOC entry 399 (class 1259 OID 492636)
-- Name: verticaldatum; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE verticaldatum (
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
    data_source text,
    identifier integer NOT NULL,
    information_source text,
    remarks text,
    origin_description text,
    realization_epoch date,
    datum_scope text,
    domainofvalidity_uuid uuid,
    coordinatereferenceepoch date
);


--
-- TOC entry 400 (class 1259 OID 492642)
-- Name: verticaldatum_aud; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE verticaldatum_aud (
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
    data_source text,
    identifier integer,
    information_source text,
    remarks text,
    origin_description text,
    realization_epoch date,
    datum_scope text,
    domainofvalidity_uuid uuid,
    coordinatereferenceepoch date
);


--
-- TOC entry 2889 (class 2606 OID 492653)
-- Name: acl_class_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY acl_class_aud
    ADD CONSTRAINT acl_class_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 2885 (class 2606 OID 492655)
-- Name: acl_class_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY acl_class
    ADD CONSTRAINT acl_class_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2895 (class 2606 OID 492657)
-- Name: acl_entry_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY acl_entry_aud
    ADD CONSTRAINT acl_entry_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 2891 (class 2606 OID 492659)
-- Name: acl_entry_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY acl_entry
    ADD CONSTRAINT acl_entry_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2901 (class 2606 OID 492661)
-- Name: acl_object_identity_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY acl_object_identity_aud
    ADD CONSTRAINT acl_object_identity_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 2897 (class 2606 OID 492663)
-- Name: acl_object_identity_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY acl_object_identity
    ADD CONSTRAINT acl_object_identity_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2907 (class 2606 OID 492665)
-- Name: acl_sid_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY acl_sid_aud
    ADD CONSTRAINT acl_sid_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 2903 (class 2606 OID 492667)
-- Name: acl_sid_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY acl_sid
    ADD CONSTRAINT acl_sid_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2911 (class 2606 OID 492669)
-- Name: actor_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY actor_aud
    ADD CONSTRAINT actor_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 2909 (class 2606 OID 492671)
-- Name: actor_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY actor
    ADD CONSTRAINT actor_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2915 (class 2606 OID 492673)
-- Name: addition_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY addition_aud
    ADD CONSTRAINT addition_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 2913 (class 2606 OID 492675)
-- Name: addition_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY addition
    ADD CONSTRAINT addition_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2919 (class 2606 OID 492677)
-- Name: alias_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY alias_aud
    ADD CONSTRAINT alias_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 2917 (class 2606 OID 492679)
-- Name: alias_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY alias
    ADD CONSTRAINT alias_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2923 (class 2606 OID 492681)
-- Name: appeal_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY appeal_aud
    ADD CONSTRAINT appeal_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 2921 (class 2606 OID 492683)
-- Name: appeal_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY appeal
    ADD CONSTRAINT appeal_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2927 (class 2606 OID 492685)
-- Name: area_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY area_aud
    ADD CONSTRAINT area_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 2925 (class 2606 OID 492687)
-- Name: area_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY area
    ADD CONSTRAINT area_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2933 (class 2606 OID 492689)
-- Name: authorizationtable_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY authorizationtable_aud
    ADD CONSTRAINT authorizationtable_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 2929 (class 2606 OID 492691)
-- Name: authorizationtable_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY authorizationtable
    ADD CONSTRAINT authorizationtable_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2937 (class 2606 OID 492693)
-- Name: axis_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY axis_aud
    ADD CONSTRAINT axis_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 2935 (class 2606 OID 492695)
-- Name: axis_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY axis
    ADD CONSTRAINT axis_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2941 (class 2606 OID 492697)
-- Name: cartesiancs_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY cartesiancs_aud
    ADD CONSTRAINT cartesiancs_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 2939 (class 2606 OID 492699)
-- Name: cartesiancs_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY cartesiancs
    ADD CONSTRAINT cartesiancs_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2945 (class 2606 OID 492701)
-- Name: ci_address_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_address_aud
    ADD CONSTRAINT ci_address_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 2947 (class 2606 OID 492703)
-- Name: ci_address_deliverypoint_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_address_deliverypoint_aud
    ADD CONSTRAINT ci_address_deliverypoint_aud_pkey PRIMARY KEY (rev, revtype, ci_address_uuid, setordinal);


--
-- TOC entry 2949 (class 2606 OID 492705)
-- Name: ci_address_electronicmailaddress_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_address_electronicmailaddress_aud
    ADD CONSTRAINT ci_address_electronicmailaddress_aud_pkey PRIMARY KEY (rev, revtype, ci_address_uuid, setordinal);


--
-- TOC entry 2943 (class 2606 OID 492707)
-- Name: ci_address_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_address
    ADD CONSTRAINT ci_address_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2953 (class 2606 OID 492709)
-- Name: ci_citation_alternatetitle_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_citation_alternatetitle_aud
    ADD CONSTRAINT ci_citation_alternatetitle_aud_pkey PRIMARY KEY (rev, ci_citation_uuid, alternatetitle);


--
-- TOC entry 2955 (class 2606 OID 492711)
-- Name: ci_citation_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_citation_aud
    ADD CONSTRAINT ci_citation_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 2961 (class 2606 OID 492713)
-- Name: ci_citation_ci_responsibleparty_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_citation_ci_responsibleparty_aud
    ADD CONSTRAINT ci_citation_ci_responsibleparty_aud_pkey PRIMARY KEY (rev, ci_citation_uuid, citedresponsibleparty_uuid);


--
-- TOC entry 2957 (class 2606 OID 492715)
-- Name: ci_citation_ci_responsibleparty_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_citation_ci_responsibleparty
    ADD CONSTRAINT ci_citation_ci_responsibleparty_pkey PRIMARY KEY (ci_citation_uuid, citedresponsibleparty_uuid);


--
-- TOC entry 2963 (class 2606 OID 492717)
-- Name: ci_citation_date_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_citation_date_aud
    ADD CONSTRAINT ci_citation_date_aud_pkey PRIMARY KEY (rev, revtype, ci_citation_uuid, setordinal);


--
-- TOC entry 2951 (class 2606 OID 492719)
-- Name: ci_citation_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_citation
    ADD CONSTRAINT ci_citation_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2965 (class 2606 OID 492721)
-- Name: ci_citation_presentationform_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_citation_presentationform_aud
    ADD CONSTRAINT ci_citation_presentationform_aud_pkey PRIMARY KEY (rev, ci_citation_uuid, presentationform);


--
-- TOC entry 2969 (class 2606 OID 492723)
-- Name: ci_contact_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_contact_aud
    ADD CONSTRAINT ci_contact_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 2967 (class 2606 OID 492725)
-- Name: ci_contact_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_contact
    ADD CONSTRAINT ci_contact_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2973 (class 2606 OID 492727)
-- Name: ci_onlineresource_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_onlineresource_aud
    ADD CONSTRAINT ci_onlineresource_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 2971 (class 2606 OID 492729)
-- Name: ci_onlineresource_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_onlineresource
    ADD CONSTRAINT ci_onlineresource_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2977 (class 2606 OID 492731)
-- Name: ci_responsibleparty_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_responsibleparty_aud
    ADD CONSTRAINT ci_responsibleparty_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 2975 (class 2606 OID 492733)
-- Name: ci_responsibleparty_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_responsibleparty
    ADD CONSTRAINT ci_responsibleparty_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2981 (class 2606 OID 492735)
-- Name: ci_telephone_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_telephone_aud
    ADD CONSTRAINT ci_telephone_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 2983 (class 2606 OID 492737)
-- Name: ci_telephone_facsimile_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_telephone_facsimile_aud
    ADD CONSTRAINT ci_telephone_facsimile_aud_pkey PRIMARY KEY (rev, revtype, ci_telephone_uuid, setordinal);


--
-- TOC entry 2979 (class 2606 OID 492739)
-- Name: ci_telephone_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_telephone
    ADD CONSTRAINT ci_telephone_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2985 (class 2606 OID 492741)
-- Name: ci_telephone_voice_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_telephone_voice_aud
    ADD CONSTRAINT ci_telephone_voice_aud_pkey PRIMARY KEY (rev, revtype, ci_telephone_uuid, setordinal);


--
-- TOC entry 2989 (class 2606 OID 492743)
-- Name: clarification_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY clarification_aud
    ADD CONSTRAINT clarification_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 2987 (class 2606 OID 492745)
-- Name: clarification_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY clarification
    ADD CONSTRAINT clarification_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2993 (class 2606 OID 492747)
-- Name: compoundcrs_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY compoundcrs_aud
    ADD CONSTRAINT compoundcrs_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 2991 (class 2606 OID 492749)
-- Name: compoundcrs_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY compoundcrs
    ADD CONSTRAINT compoundcrs_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2995 (class 2606 OID 492751)
-- Name: compoundcrs_singlecrs_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY compoundcrs_singlecrs_aud
    ADD CONSTRAINT compoundcrs_singlecrs_aud_pkey PRIMARY KEY (rev, compoundcrs_uuid, componentreferencesystem_uuid);


--
-- TOC entry 2999 (class 2606 OID 492753)
-- Name: concatenatedoperationitem_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY concatenatedoperationitem_aud
    ADD CONSTRAINT concatenatedoperationitem_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3001 (class 2606 OID 492755)
-- Name: concatenatedoperationitem_coordinateoperationitem_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY concatenatedoperationitem_coordinateoperationitem_aud
    ADD CONSTRAINT concatenatedoperationitem_coordinateoperationitem_aud_pkey PRIMARY KEY (rev, concatenatedoperationitem_uuid, coordoperation_uuid);


--
-- TOC entry 2997 (class 2606 OID 492757)
-- Name: concatenatedoperationitem_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY concatenatedoperationitem
    ADD CONSTRAINT concatenatedoperationitem_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3005 (class 2606 OID 492759)
-- Name: conversionitem_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY conversionitem_aud
    ADD CONSTRAINT conversionitem_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3003 (class 2606 OID 492761)
-- Name: conversionitem_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY conversionitem
    ADD CONSTRAINT conversionitem_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3007 (class 2606 OID 492763)
-- Name: coordinateoperationitem_dq_positionalaccuracy_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY coordinateoperationitem_dq_positionalaccuracy_aud
    ADD CONSTRAINT coordinateoperationitem_dq_positionalaccuracy_aud_pkey PRIMARY KEY (rev, coordinateoperationitem_uuid, coordinateoperationaccuracy_uuid);


--
-- TOC entry 3009 (class 2606 OID 492765)
-- Name: coordinateoperationitem_scope_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY coordinateoperationitem_scope_aud
    ADD CONSTRAINT coordinateoperationitem_scope_aud_pkey PRIMARY KEY (rev, coordinateoperationitem_uuid, scope);


--
-- TOC entry 3011 (class 2606 OID 492767)
-- Name: coordinatesystem_axis_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY coordinatesystem_axis_aud
    ADD CONSTRAINT coordinatesystem_axis_aud_pkey PRIMARY KEY (rev, coordinatesystem_uuid, axes_uuid);


--
-- TOC entry 3015 (class 2606 OID 492769)
-- Name: crs_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY crs_aud
    ADD CONSTRAINT crs_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3013 (class 2606 OID 492771)
-- Name: crs_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY crs
    ADD CONSTRAINT crs_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3021 (class 2606 OID 492773)
-- Name: delegation_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY delegation_aud
    ADD CONSTRAINT delegation_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3017 (class 2606 OID 492775)
-- Name: delegation_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY delegation
    ADD CONSTRAINT delegation_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3025 (class 2606 OID 492777)
-- Name: dq_absoluteexternalpositionalaccuracy_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY dq_absoluteexternalpositionalaccuracy_aud
    ADD CONSTRAINT dq_absoluteexternalpositionalaccuracy_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3023 (class 2606 OID 492779)
-- Name: dq_absoluteexternalpositionalaccuracy_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY dq_absoluteexternalpositionalaccuracy
    ADD CONSTRAINT dq_absoluteexternalpositionalaccuracy_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3027 (class 2606 OID 492781)
-- Name: dq_element_datetime_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY dq_element_datetime_aud
    ADD CONSTRAINT dq_element_datetime_aud_pkey PRIMARY KEY (rev, dq_element_uuid, datetime);


--
-- TOC entry 3031 (class 2606 OID 492783)
-- Name: dq_griddeddatapositionalaccuracy_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY dq_griddeddatapositionalaccuracy_aud
    ADD CONSTRAINT dq_griddeddatapositionalaccuracy_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3029 (class 2606 OID 492785)
-- Name: dq_griddeddatapositionalaccuracy_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY dq_griddeddatapositionalaccuracy
    ADD CONSTRAINT dq_griddeddatapositionalaccuracy_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3035 (class 2606 OID 492787)
-- Name: dq_relativeinternalpositionalaccuracy_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY dq_relativeinternalpositionalaccuracy_aud
    ADD CONSTRAINT dq_relativeinternalpositionalaccuracy_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3033 (class 2606 OID 492789)
-- Name: dq_relativeinternalpositionalaccuracy_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY dq_relativeinternalpositionalaccuracy
    ADD CONSTRAINT dq_relativeinternalpositionalaccuracy_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3039 (class 2606 OID 492791)
-- Name: ellipsoid_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ellipsoid_aud
    ADD CONSTRAINT ellipsoid_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3037 (class 2606 OID 492793)
-- Name: ellipsoid_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ellipsoid
    ADD CONSTRAINT ellipsoid_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3043 (class 2606 OID 492795)
-- Name: ellipsoidalcs_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ellipsoidalcs_aud
    ADD CONSTRAINT ellipsoidalcs_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3041 (class 2606 OID 492797)
-- Name: ellipsoidalcs_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ellipsoidalcs
    ADD CONSTRAINT ellipsoidalcs_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3047 (class 2606 OID 492799)
-- Name: engineeringcrs_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY engineeringcrs_aud
    ADD CONSTRAINT engineeringcrs_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3045 (class 2606 OID 492801)
-- Name: engineeringcrs_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY engineeringcrs
    ADD CONSTRAINT engineeringcrs_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3051 (class 2606 OID 492803)
-- Name: engineeringdatum_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY engineeringdatum_aud
    ADD CONSTRAINT engineeringdatum_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3049 (class 2606 OID 492805)
-- Name: engineeringdatum_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY engineeringdatum
    ADD CONSTRAINT engineeringdatum_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3053 (class 2606 OID 492807)
-- Name: epsgisomapping_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY epsgisomapping
    ADD CONSTRAINT epsgisomapping_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3059 (class 2606 OID 492809)
-- Name: ex_extent_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ex_extent_aud
    ADD CONSTRAINT ex_extent_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3061 (class 2606 OID 492811)
-- Name: ex_extent_ex_geographicextent_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ex_extent_ex_geographicextent_aud
    ADD CONSTRAINT ex_extent_ex_geographicextent_aud_pkey PRIMARY KEY (rev, ex_extent_uuid, geographicelement_uuid);


--
-- TOC entry 3063 (class 2606 OID 492813)
-- Name: ex_extent_ex_verticalextent_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ex_extent_ex_verticalextent_aud
    ADD CONSTRAINT ex_extent_ex_verticalextent_aud_pkey PRIMARY KEY (rev, ex_extent_uuid, verticalelement_uuid);


--
-- TOC entry 3057 (class 2606 OID 492815)
-- Name: ex_extent_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ex_extent
    ADD CONSTRAINT ex_extent_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3067 (class 2606 OID 492817)
-- Name: ex_geographicboundingbox_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ex_geographicboundingbox_aud
    ADD CONSTRAINT ex_geographicboundingbox_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3065 (class 2606 OID 492819)
-- Name: ex_geographicboundingbox_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ex_geographicboundingbox
    ADD CONSTRAINT ex_geographicboundingbox_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3071 (class 2606 OID 492821)
-- Name: ex_temporalextent_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ex_temporalextent_aud
    ADD CONSTRAINT ex_temporalextent_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3069 (class 2606 OID 492823)
-- Name: ex_temporalextent_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ex_temporalextent
    ADD CONSTRAINT ex_temporalextent_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3075 (class 2606 OID 492825)
-- Name: ex_verticalextent_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ex_verticalextent_aud
    ADD CONSTRAINT ex_verticalextent_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3073 (class 2606 OID 492827)
-- Name: ex_verticalextent_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ex_verticalextent
    ADD CONSTRAINT ex_verticalextent_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3079 (class 2606 OID 492829)
-- Name: formula_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY formula_aud
    ADD CONSTRAINT formula_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3077 (class 2606 OID 492831)
-- Name: formula_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY formula
    ADD CONSTRAINT formula_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3083 (class 2606 OID 492833)
-- Name: generaloperationparameteritem_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY generaloperationparameteritem_aud
    ADD CONSTRAINT generaloperationparameteritem_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3081 (class 2606 OID 492835)
-- Name: generaloperationparameteritem_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY generaloperationparameteritem
    ADD CONSTRAINT generaloperationparameteritem_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3087 (class 2606 OID 492837)
-- Name: generalparametervalue_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY generalparametervalue_aud
    ADD CONSTRAINT generalparametervalue_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3085 (class 2606 OID 492839)
-- Name: generalparametervalue_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY generalparametervalue
    ADD CONSTRAINT generalparametervalue_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3091 (class 2606 OID 492841)
-- Name: geodeticcrs_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY geodeticcrs_aud
    ADD CONSTRAINT geodeticcrs_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3089 (class 2606 OID 492843)
-- Name: geodeticcrs_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY geodeticcrs
    ADD CONSTRAINT geodeticcrs_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3095 (class 2606 OID 492845)
-- Name: geodeticdatum_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY geodeticdatum_aud
    ADD CONSTRAINT geodeticdatum_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3093 (class 2606 OID 492847)
-- Name: geodeticdatum_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY geodeticdatum
    ADD CONSTRAINT geodeticdatum_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3097 (class 2606 OID 492849)
-- Name: identifieditem_aliases_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY identifieditem_aliases_aud
    ADD CONSTRAINT identifieditem_aliases_aud_pkey PRIMARY KEY (rev, identifieditem_uuid, aliases);


--
-- TOC entry 3101 (class 2606 OID 492851)
-- Name: namingsystemitem_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY namingsystemitem_aud
    ADD CONSTRAINT namingsystemitem_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3099 (class 2606 OID 492853)
-- Name: namingsystemitem_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY namingsystemitem
    ADD CONSTRAINT namingsystemitem_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3105 (class 2606 OID 492855)
-- Name: operationmethoditem_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY operationmethoditem_aud
    ADD CONSTRAINT operationmethoditem_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3107 (class 2606 OID 492857)
-- Name: operationmethoditem_generaloperationparameteritem_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY operationmethoditem_generaloperationparameteritem_aud
    ADD CONSTRAINT operationmethoditem_generaloperationparameteritem_aud_pkey PRIMARY KEY (rev, operationmethoditem_uuid, parameter_uuid);


--
-- TOC entry 3103 (class 2606 OID 492859)
-- Name: operationmethoditem_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY operationmethoditem
    ADD CONSTRAINT operationmethoditem_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3111 (class 2606 OID 492861)
-- Name: operationparametergroupitem_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY operationparametergroupitem_aud
    ADD CONSTRAINT operationparametergroupitem_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3109 (class 2606 OID 492863)
-- Name: operationparametergroupitem_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY operationparametergroupitem
    ADD CONSTRAINT operationparametergroupitem_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3115 (class 2606 OID 492865)
-- Name: operationparameteritem_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY operationparameteritem_aud
    ADD CONSTRAINT operationparameteritem_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3113 (class 2606 OID 492867)
-- Name: operationparameteritem_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY operationparameteritem
    ADD CONSTRAINT operationparameteritem_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3117 (class 2606 OID 492869)
-- Name: operationparametervalue_parametervalue_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY operationparametervalue_parametervalue_aud
    ADD CONSTRAINT operationparametervalue_parametervalue_aud_pkey PRIMARY KEY (rev, revtype, operationparametervalue_uuid);


--
-- TOC entry 3119 (class 2606 OID 492871)
-- Name: operationparametervalue_parametervaluesimple_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY operationparametervalue_parametervaluesimple_aud
    ADD CONSTRAINT operationparametervalue_parametervaluesimple_aud_pkey PRIMARY KEY (rev, operationparametervalue_uuid, parametervaluesimple);


--
-- TOC entry 3125 (class 2606 OID 492873)
-- Name: organization_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY organization_aud
    ADD CONSTRAINT organization_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3121 (class 2606 OID 492875)
-- Name: organization_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY organization
    ADD CONSTRAINT organization_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3131 (class 2606 OID 492877)
-- Name: organizationrelatedrole_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY organizationrelatedrole_aud
    ADD CONSTRAINT organizationrelatedrole_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3127 (class 2606 OID 492879)
-- Name: organizationrelatedrole_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY organizationrelatedrole
    ADD CONSTRAINT organizationrelatedrole_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3135 (class 2606 OID 492881)
-- Name: passthroughoperationitem_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY passthroughoperationitem_aud
    ADD CONSTRAINT passthroughoperationitem_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3133 (class 2606 OID 492883)
-- Name: passthroughoperationitem_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY passthroughoperationitem
    ADD CONSTRAINT passthroughoperationitem_pkey PRIMARY KEY (uuid);


--
-- TOC entry 2931 (class 2606 OID 492885)
-- Name: pk_authorizationtable; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY authorizationtable
    ADD CONSTRAINT pk_authorizationtable UNIQUE (actor_uuid, role_uuid);


--
-- TOC entry 3139 (class 2606 OID 492887)
-- Name: post_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY post_aud
    ADD CONSTRAINT post_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3137 (class 2606 OID 492889)
-- Name: post_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY post
    ADD CONSTRAINT post_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3143 (class 2606 OID 492891)
-- Name: primemeridian_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY primemeridian_aud
    ADD CONSTRAINT primemeridian_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3141 (class 2606 OID 492893)
-- Name: primemeridian_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY primemeridian
    ADD CONSTRAINT primemeridian_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3147 (class 2606 OID 492895)
-- Name: projectedcrs_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY projectedcrs_aud
    ADD CONSTRAINT projectedcrs_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3145 (class 2606 OID 492897)
-- Name: projectedcrs_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY projectedcrs
    ADD CONSTRAINT projectedcrs_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3151 (class 2606 OID 492899)
-- Name: proposal_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY proposal_aud
    ADD CONSTRAINT proposal_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3149 (class 2606 OID 492901)
-- Name: proposal_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY proposal
    ADD CONSTRAINT proposal_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3155 (class 2606 OID 492903)
-- Name: proposaldiscussion_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY proposaldiscussion_aud
    ADD CONSTRAINT proposaldiscussion_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3159 (class 2606 OID 492905)
-- Name: proposaldiscussion_invitees_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY proposaldiscussion_invitees_aud
    ADD CONSTRAINT proposaldiscussion_invitees_aud_pkey PRIMARY KEY (rev, proposaldiscussion_uuid, invitees, invitees_key);


--
-- TOC entry 3157 (class 2606 OID 492907)
-- Name: proposaldiscussion_invitees_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY proposaldiscussion_invitees
    ADD CONSTRAINT proposaldiscussion_invitees_pkey PRIMARY KEY (proposaldiscussion_uuid, invitees_key);


--
-- TOC entry 3153 (class 2606 OID 492909)
-- Name: proposaldiscussion_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY proposaldiscussion
    ADD CONSTRAINT proposaldiscussion_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3163 (class 2606 OID 492911)
-- Name: proposalgroup_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY proposalgroup_aud
    ADD CONSTRAINT proposalgroup_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3161 (class 2606 OID 492913)
-- Name: proposalgroup_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY proposalgroup
    ADD CONSTRAINT proposalgroup_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3167 (class 2606 OID 492915)
-- Name: proposalnote_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY proposalnote_aud
    ADD CONSTRAINT proposalnote_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3165 (class 2606 OID 492917)
-- Name: proposalnote_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY proposalnote
    ADD CONSTRAINT proposalnote_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3171 (class 2606 OID 492919)
-- Name: re_additioninformation_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_additioninformation_aud
    ADD CONSTRAINT re_additioninformation_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3169 (class 2606 OID 492921)
-- Name: re_additioninformation_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_additioninformation
    ADD CONSTRAINT re_additioninformation_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3175 (class 2606 OID 492923)
-- Name: re_alternativeexpression_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_alternativeexpression_aud
    ADD CONSTRAINT re_alternativeexpression_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3177 (class 2606 OID 492925)
-- Name: re_alternativeexpression_fieldofapplication_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_alternativeexpression_fieldofapplication_aud
    ADD CONSTRAINT re_alternativeexpression_fieldofapplication_aud_pkey PRIMARY KEY (rev, revtype, re_alternativeexpression_uuid, setordinal);


--
-- TOC entry 3173 (class 2606 OID 492927)
-- Name: re_alternativeexpression_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_alternativeexpression
    ADD CONSTRAINT re_alternativeexpression_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3181 (class 2606 OID 492929)
-- Name: re_amendmentinformation_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_amendmentinformation_aud
    ADD CONSTRAINT re_amendmentinformation_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3179 (class 2606 OID 492931)
-- Name: re_amendmentinformation_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_amendmentinformation
    ADD CONSTRAINT re_amendmentinformation_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3185 (class 2606 OID 492933)
-- Name: re_clarificationinformation_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_clarificationinformation_aud
    ADD CONSTRAINT re_clarificationinformation_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3183 (class 2606 OID 492935)
-- Name: re_clarificationinformation_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_clarificationinformation
    ADD CONSTRAINT re_clarificationinformation_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3191 (class 2606 OID 492937)
-- Name: re_itemclass_alternativenames_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_itemclass_alternativenames_aud
    ADD CONSTRAINT re_itemclass_alternativenames_aud_pkey PRIMARY KEY (rev, revtype, re_itemclass_uuid, setordinal);


--
-- TOC entry 3189 (class 2606 OID 492939)
-- Name: re_itemclass_alternativenames_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_itemclass_alternativenames
    ADD CONSTRAINT re_itemclass_alternativenames_pkey PRIMARY KEY (re_itemclass_uuid, alternativename_name);


--
-- TOC entry 3193 (class 2606 OID 492941)
-- Name: re_itemclass_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_itemclass_aud
    ADD CONSTRAINT re_itemclass_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3187 (class 2606 OID 492943)
-- Name: re_itemclass_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_itemclass
    ADD CONSTRAINT re_itemclass_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3197 (class 2606 OID 492945)
-- Name: re_locale_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_locale_aud
    ADD CONSTRAINT re_locale_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3195 (class 2606 OID 492947)
-- Name: re_locale_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_locale
    ADD CONSTRAINT re_locale_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3201 (class 2606 OID 492949)
-- Name: re_proposalmanagementinformation_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_proposalmanagementinformation_aud
    ADD CONSTRAINT re_proposalmanagementinformation_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3199 (class 2606 OID 492951)
-- Name: re_proposalmanagementinformation_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_proposalmanagementinformation
    ADD CONSTRAINT re_proposalmanagementinformation_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3205 (class 2606 OID 492953)
-- Name: re_reference_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_reference_aud
    ADD CONSTRAINT re_reference_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3203 (class 2606 OID 492955)
-- Name: re_reference_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_reference
    ADD CONSTRAINT re_reference_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3209 (class 2606 OID 492957)
-- Name: re_register_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_register_aud
    ADD CONSTRAINT re_register_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3213 (class 2606 OID 492959)
-- Name: re_register_itemclasses_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_register_itemclasses_aud
    ADD CONSTRAINT re_register_itemclasses_aud_pkey PRIMARY KEY (rev, registerid, itemclassid);


--
-- TOC entry 3211 (class 2606 OID 492961)
-- Name: re_register_itemclasses_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_register_itemclasses
    ADD CONSTRAINT re_register_itemclasses_pkey PRIMARY KEY (registerid, itemclassid);


--
-- TOC entry 3207 (class 2606 OID 492963)
-- Name: re_register_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_register
    ADD CONSTRAINT re_register_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3219 (class 2606 OID 492965)
-- Name: re_register_re_locale_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_register_re_locale_aud
    ADD CONSTRAINT re_register_re_locale_aud_pkey PRIMARY KEY (rev, re_register_uuid, alternativelanguages_uuid);


--
-- TOC entry 3215 (class 2606 OID 492967)
-- Name: re_register_re_locale_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_register_re_locale
    ADD CONSTRAINT re_register_re_locale_pkey PRIMARY KEY (re_register_uuid, alternativelanguages_uuid);


--
-- TOC entry 3223 (class 2606 OID 492969)
-- Name: re_register_submitters_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_register_submitters_aud
    ADD CONSTRAINT re_register_submitters_aud_pkey PRIMARY KEY (rev, submitterid, registerid);


--
-- TOC entry 3221 (class 2606 OID 492971)
-- Name: re_register_submitters_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_register_submitters
    ADD CONSTRAINT re_register_submitters_pkey PRIMARY KEY (registerid, submitterid);


--
-- TOC entry 3227 (class 2606 OID 492973)
-- Name: re_registeritem_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_registeritem_aud
    ADD CONSTRAINT re_registeritem_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3229 (class 2606 OID 492975)
-- Name: re_registeritem_fieldofapplication_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_registeritem_fieldofapplication_aud
    ADD CONSTRAINT re_registeritem_fieldofapplication_aud_pkey PRIMARY KEY (rev, revtype, re_registeritem_uuid, setordinal);


--
-- TOC entry 3225 (class 2606 OID 492977)
-- Name: re_registeritem_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_registeritem
    ADD CONSTRAINT re_registeritem_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3235 (class 2606 OID 492979)
-- Name: re_registeritem_re_alternativeexpression_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_registeritem_re_alternativeexpression_aud
    ADD CONSTRAINT re_registeritem_re_alternativeexpression_aud_pkey PRIMARY KEY (rev, re_registeritem_uuid, alternativeexpressions_uuid);


--
-- TOC entry 3231 (class 2606 OID 492981)
-- Name: re_registeritem_re_alternativeexpression_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_registeritem_re_alternativeexpression
    ADD CONSTRAINT re_registeritem_re_alternativeexpression_pkey PRIMARY KEY (re_registeritem_uuid, alternativeexpressions_uuid);


--
-- TOC entry 3237 (class 2606 OID 492983)
-- Name: re_registeritem_successor_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_registeritem_successor_aud
    ADD CONSTRAINT re_registeritem_successor_aud_pkey PRIMARY KEY (rev, successorid, predecessorid);


--
-- TOC entry 3241 (class 2606 OID 492985)
-- Name: re_registermanager_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_registermanager_aud
    ADD CONSTRAINT re_registermanager_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3239 (class 2606 OID 492987)
-- Name: re_registermanager_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_registermanager
    ADD CONSTRAINT re_registermanager_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3245 (class 2606 OID 492989)
-- Name: re_registerowner_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_registerowner_aud
    ADD CONSTRAINT re_registerowner_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3243 (class 2606 OID 492991)
-- Name: re_registerowner_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_registerowner
    ADD CONSTRAINT re_registerowner_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3249 (class 2606 OID 492993)
-- Name: re_submittingorganization_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_submittingorganization_aud
    ADD CONSTRAINT re_submittingorganization_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3247 (class 2606 OID 492995)
-- Name: re_submittingorganization_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_submittingorganization
    ADD CONSTRAINT re_submittingorganization_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3255 (class 2606 OID 492997)
-- Name: registerrelatedrole_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY registerrelatedrole_aud
    ADD CONSTRAINT registerrelatedrole_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3251 (class 2606 OID 492999)
-- Name: registerrelatedrole_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY registerrelatedrole
    ADD CONSTRAINT registerrelatedrole_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3259 (class 2606 OID 493001)
-- Name: registryuser_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY registryuser_aud
    ADD CONSTRAINT registryuser_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3257 (class 2606 OID 493003)
-- Name: registryuser_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY registryuser
    ADD CONSTRAINT registryuser_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3265 (class 2606 OID 493005)
-- Name: registryusergroup_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY registryusergroup_aud
    ADD CONSTRAINT registryusergroup_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3261 (class 2606 OID 493007)
-- Name: registryusergroup_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY registryusergroup
    ADD CONSTRAINT registryusergroup_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3269 (class 2606 OID 493009)
-- Name: result_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY result_aud
    ADD CONSTRAINT result_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3267 (class 2606 OID 493011)
-- Name: result_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY result
    ADD CONSTRAINT result_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3273 (class 2606 OID 493013)
-- Name: retirement_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY retirement_aud
    ADD CONSTRAINT retirement_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3271 (class 2606 OID 493015)
-- Name: retirement_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY retirement
    ADD CONSTRAINT retirement_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3275 (class 2606 OID 493017)
-- Name: revinfo_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY revinfo
    ADD CONSTRAINT revinfo_pkey PRIMARY KEY (rev);


--
-- TOC entry 3281 (class 2606 OID 493019)
-- Name: role_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY role_aud
    ADD CONSTRAINT role_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3277 (class 2606 OID 493021)
-- Name: role_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY role
    ADD CONSTRAINT role_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3285 (class 2606 OID 493023)
-- Name: simpleproposal_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY simpleproposal_aud
    ADD CONSTRAINT simpleproposal_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3283 (class 2606 OID 493025)
-- Name: simpleproposal_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY simpleproposal
    ADD CONSTRAINT simpleproposal_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3289 (class 2606 OID 493027)
-- Name: singleoperationitem_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY singleoperationitem_aud
    ADD CONSTRAINT singleoperationitem_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3291 (class 2606 OID 493029)
-- Name: singleoperationitem_generalparametervalue_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY singleoperationitem_generalparametervalue_aud
    ADD CONSTRAINT singleoperationitem_generalparametervalue_aud_pkey PRIMARY KEY (rev, singleoperationitem_uuid, parametervalue_uuid);


--
-- TOC entry 3287 (class 2606 OID 493031)
-- Name: singleoperationitem_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY singleoperationitem
    ADD CONSTRAINT singleoperationitem_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3295 (class 2606 OID 493033)
-- Name: sphericalcs_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY sphericalcs_aud
    ADD CONSTRAINT sphericalcs_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3293 (class 2606 OID 493035)
-- Name: sphericalcs_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY sphericalcs
    ADD CONSTRAINT sphericalcs_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3299 (class 2606 OID 493037)
-- Name: supersession_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY supersession_aud
    ADD CONSTRAINT supersession_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3297 (class 2606 OID 493039)
-- Name: supersession_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY supersession
    ADD CONSTRAINT supersession_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3301 (class 2606 OID 493041)
-- Name: supersession_supersedingitems_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY supersession_supersedingitems_aud
    ADD CONSTRAINT supersession_supersedingitems_aud_pkey PRIMARY KEY (rev, supersessionid, supersedingitems_uuid);


--
-- TOC entry 3305 (class 2606 OID 493043)
-- Name: supersessionpart_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY supersessionpart_aud
    ADD CONSTRAINT supersessionpart_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3303 (class 2606 OID 493045)
-- Name: supersessionpart_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY supersessionpart
    ADD CONSTRAINT supersessionpart_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3309 (class 2606 OID 493047)
-- Name: transformationitem_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY transformationitem_aud
    ADD CONSTRAINT transformationitem_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3307 (class 2606 OID 493049)
-- Name: transformationitem_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY transformationitem
    ADD CONSTRAINT transformationitem_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3019 (class 2606 OID 493051)
-- Name: uk_5ad8yawbym2uolno93wvhymua; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY delegation
    ADD CONSTRAINT uk_5ad8yawbym2uolno93wvhymua UNIQUE (actor_uuid, role_uuid);


--
-- TOC entry 3217 (class 2606 OID 493053)
-- Name: uk_5y1lhiw2mrbyhgfrqmgslwlf6; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_register_re_locale
    ADD CONSTRAINT uk_5y1lhiw2mrbyhgfrqmgslwlf6 UNIQUE (alternativelanguages_uuid);


--
-- TOC entry 3279 (class 2606 OID 493055)
-- Name: uk_7d8a768x6aiuvmsa24hqiharg; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY role
    ADD CONSTRAINT uk_7d8a768x6aiuvmsa24hqiharg UNIQUE (name);


--
-- TOC entry 2959 (class 2606 OID 493057)
-- Name: uk_cj59exysvhvaeyaqgrshs0ax9; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY ci_citation_ci_responsibleparty
    ADD CONSTRAINT uk_cj59exysvhvaeyaqgrshs0ax9 UNIQUE (citedresponsibleparty_uuid);


--
-- TOC entry 3123 (class 2606 OID 493059)
-- Name: uk_cktxhfp9h9p3jptwhtnnfppxd; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY organization
    ADD CONSTRAINT uk_cktxhfp9h9p3jptwhtnnfppxd UNIQUE (submittingorganization_uuid);


--
-- TOC entry 2899 (class 2606 OID 493061)
-- Name: uk_ehrgv7ffpt0jenafv0o1bu5tm; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY acl_object_identity
    ADD CONSTRAINT uk_ehrgv7ffpt0jenafv0o1bu5tm UNIQUE (object_id_class, object_id_identity);


--
-- TOC entry 2893 (class 2606 OID 493063)
-- Name: uk_gh5egfpe4gaqokya6s0567b0l; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY acl_entry
    ADD CONSTRAINT uk_gh5egfpe4gaqokya6s0567b0l UNIQUE (acl_object_identity, ace_order);


--
-- TOC entry 2887 (class 2606 OID 493065)
-- Name: uk_iy7ua5fso3il3u3ymoc4uf35w; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY acl_class
    ADD CONSTRAINT uk_iy7ua5fso3il3u3ymoc4uf35w UNIQUE (class);


--
-- TOC entry 2905 (class 2606 OID 493067)
-- Name: uk_meabypi3cnm8604op6qvd517v; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY acl_sid
    ADD CONSTRAINT uk_meabypi3cnm8604op6qvd517v UNIQUE (sid, principal);


--
-- TOC entry 3263 (class 2606 OID 493069)
-- Name: uk_prs56y60ujwe94sg8umdfyf0b; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY registryusergroup
    ADD CONSTRAINT uk_prs56y60ujwe94sg8umdfyf0b UNIQUE (name);


--
-- TOC entry 3055 (class 2606 OID 493071)
-- Name: uk_rj6338g473ygmddvreal3y6oc; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY epsgisomapping
    ADD CONSTRAINT uk_rj6338g473ygmddvreal3y6oc UNIQUE (isocode);


--
-- TOC entry 3233 (class 2606 OID 493073)
-- Name: uk_s5ikuot891kvxvqmryn2408cw; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY re_registeritem_re_alternativeexpression
    ADD CONSTRAINT uk_s5ikuot891kvxvqmryn2408cw UNIQUE (alternativeexpressions_uuid);


--
-- TOC entry 3129 (class 2606 OID 493075)
-- Name: uk_tetevxldsjcnbnauwwa6wqkut; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY organizationrelatedrole
    ADD CONSTRAINT uk_tetevxldsjcnbnauwwa6wqkut UNIQUE (name);


--
-- TOC entry 3253 (class 2606 OID 493077)
-- Name: uk_vfk4ug4rybb5luo37gjc1x5b; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY registerrelatedrole
    ADD CONSTRAINT uk_vfk4ug4rybb5luo37gjc1x5b UNIQUE (name);


--
-- TOC entry 3313 (class 2606 OID 493079)
-- Name: unitofmeasure_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY unitofmeasure_aud
    ADD CONSTRAINT unitofmeasure_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3311 (class 2606 OID 493081)
-- Name: unitofmeasure_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY unitofmeasure
    ADD CONSTRAINT unitofmeasure_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3317 (class 2606 OID 493083)
-- Name: verticalcrs_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY verticalcrs_aud
    ADD CONSTRAINT verticalcrs_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3315 (class 2606 OID 493085)
-- Name: verticalcrs_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY verticalcrs
    ADD CONSTRAINT verticalcrs_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3321 (class 2606 OID 493087)
-- Name: verticalcs_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY verticalcs_aud
    ADD CONSTRAINT verticalcs_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3319 (class 2606 OID 493089)
-- Name: verticalcs_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY verticalcs
    ADD CONSTRAINT verticalcs_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3325 (class 2606 OID 493091)
-- Name: verticaldatum_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY verticaldatum_aud
    ADD CONSTRAINT verticaldatum_aud_pkey PRIMARY KEY (uuid, rev);


--
-- TOC entry 3323 (class 2606 OID 493093)
-- Name: verticaldatum_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY verticaldatum
    ADD CONSTRAINT verticaldatum_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3664 (class 2606 OID 493094)
-- Name: fk_1cpdgxv1uwquxa7n2nm9q5lvi; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY verticalcrs
    ADD CONSTRAINT fk_1cpdgxv1uwquxa7n2nm9q5lvi FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3390 (class 2606 OID 493099)
-- Name: fk_1f4bv6dg3n87x5xnj73fxmvr7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY compoundcrs
    ADD CONSTRAINT fk_1f4bv6dg3n87x5xnj73fxmvr7 FOREIGN KEY (domainofvalidity_uuid) REFERENCES ex_extent(uuid);


--
-- TOC entry 3447 (class 2606 OID 493104)
-- Name: fk_1fgwthkph57egwgbb1arpe4q9; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ellipsoidalcs
    ADD CONSTRAINT fk_1fgwthkph57egwgbb1arpe4q9 FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3540 (class 2606 OID 493109)
-- Name: fk_1itk52fqo1e5iepykhla7up6i; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY post_aud
    ADD CONSTRAINT fk_1itk52fqo1e5iepykhla7up6i FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3439 (class 2606 OID 493114)
-- Name: fk_1k82cvxmepf0uj9ffn0ykemgl; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ellipsoid
    ADD CONSTRAINT fk_1k82cvxmepf0uj9ffn0ykemgl FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3384 (class 2606 OID 493119)
-- Name: fk_1orluc06jm04r374ut11gmrm0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_telephone_facsimile
    ADD CONSTRAINT fk_1orluc06jm04r374ut11gmrm0 FOREIGN KEY (ci_telephone_uuid) REFERENCES ci_telephone(uuid);


--
-- TOC entry 3670 (class 2606 OID 493124)
-- Name: fk_1pophtkkjvgxhnr2bsr0ygupl; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY verticalcs
    ADD CONSTRAINT fk_1pophtkkjvgxhnr2bsr0ygupl FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3431 (class 2606 OID 493129)
-- Name: fk_1qrsq719o76yrh01723ywa5vy; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dq_griddeddatapositionalaccuracy
    ADD CONSTRAINT fk_1qrsq719o76yrh01723ywa5vy FOREIGN KEY (authority_uuid) REFERENCES ci_citation(uuid);


--
-- TOC entry 3475 (class 2606 OID 493134)
-- Name: fk_1rgubsf19nxij3tjhmc1pik96; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY generaloperationparameteritem
    ADD CONSTRAINT fk_1rgubsf19nxij3tjhmc1pik96 FOREIGN KEY (group_uuid) REFERENCES operationparametergroupitem(uuid);


--
-- TOC entry 3651 (class 2606 OID 493139)
-- Name: fk_1s8sh0n4x4fechw7prutf5hvu; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY transformationitem
    ADD CONSTRAINT fk_1s8sh0n4x4fechw7prutf5hvu FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3630 (class 2606 OID 493144)
-- Name: fk_1xptuu64le4n2v6kt15cpwyvy; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY singleoperationitem
    ADD CONSTRAINT fk_1xptuu64le4n2v6kt15cpwyvy FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3539 (class 2606 OID 493149)
-- Name: fk_20b6xp2q3i5xuk3no0x1h4wgo; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY post
    ADD CONSTRAINT fk_20b6xp2q3i5xuk3no0x1h4wgo FOREIGN KEY (parentpost_uuid) REFERENCES post(uuid);


--
-- TOC entry 3397 (class 2606 OID 493154)
-- Name: fk_2anp7ab91i9xo75oq2t3l6sca; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY compoundcrs_singlecrs_aud
    ADD CONSTRAINT fk_2anp7ab91i9xo75oq2t3l6sca FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3552 (class 2606 OID 493159)
-- Name: fk_2bloq50panoubp4hmigph9jc4; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY projectedcrs_aud
    ADD CONSTRAINT fk_2bloq50panoubp4hmigph9jc4 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3489 (class 2606 OID 493164)
-- Name: fk_2c5n9jgdq4pxwc3h1cj9f4ha3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY geodeticdatum
    ADD CONSTRAINT fk_2c5n9jgdq4pxwc3h1cj9f4ha3 FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3476 (class 2606 OID 493169)
-- Name: fk_2cmclw9uvt0ukx3po459flqvs; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY generaloperationparameteritem
    ADD CONSTRAINT fk_2cmclw9uvt0ukx3po459flqvs FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3440 (class 2606 OID 493174)
-- Name: fk_2dnd30hpmfqgv6hb2q68826vf; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ellipsoid
    ADD CONSTRAINT fk_2dnd30hpmfqgv6hb2q68826vf FOREIGN KEY (semiminoraxisuom_uuid) REFERENCES unitofmeasure(uuid);


--
-- TOC entry 3435 (class 2606 OID 493179)
-- Name: fk_2dvunix8g8jyvmgmcq0dtcjm6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dq_relativeinternalpositionalaccuracy
    ADD CONSTRAINT fk_2dvunix8g8jyvmgmcq0dtcjm6 FOREIGN KEY (evaluationprocedure_uuid) REFERENCES ci_citation(uuid);


--
-- TOC entry 3614 (class 2606 OID 493184)
-- Name: fk_2lhai4vuystyv0gms74nlktm5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_submittingorganization
    ADD CONSTRAINT fk_2lhai4vuystyv0gms74nlktm5 FOREIGN KEY (contact_uuid) REFERENCES ci_responsibleparty(uuid);


--
-- TOC entry 3354 (class 2606 OID 493189)
-- Name: fk_2lvgkpfwnoc5beuhea1a4v98h; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY axis_aud
    ADD CONSTRAINT fk_2lvgkpfwnoc5beuhea1a4v98h FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3469 (class 2606 OID 493194)
-- Name: fk_2u744j86ikvk4i2y8y9gxdyfh; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ex_extent_ex_verticalextent_aud
    ADD CONSTRAINT fk_2u744j86ikvk4i2y8y9gxdyfh FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3511 (class 2606 OID 493199)
-- Name: fk_31y1njt9lb6tmth8cpba8emlp; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationparametergroupitem
    ADD CONSTRAINT fk_31y1njt9lb6tmth8cpba8emlp FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3349 (class 2606 OID 493204)
-- Name: fk_336te6ljauy14t1te6087y2td; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY axis
    ADD CONSTRAINT fk_336te6ljauy14t1te6087y2td FOREIGN KEY (axisunit_uuid) REFERENCES unitofmeasure(uuid);


--
-- TOC entry 3425 (class 2606 OID 493209)
-- Name: fk_39em6pgy4ooqbgrkb6bk9oe9r; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY delegation_aud
    ADD CONSTRAINT fk_39em6pgy4ooqbgrkb6bk9oe9r FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3374 (class 2606 OID 493214)
-- Name: fk_3amdi4x4nmktmos97quia4b7t; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_citation_presentationform
    ADD CONSTRAINT fk_3amdi4x4nmktmos97quia4b7t FOREIGN KEY (ci_citation_uuid) REFERENCES ci_citation(uuid);


--
-- TOC entry 3333 (class 2606 OID 493219)
-- Name: fk_3f8wpmd7ibfsxn45ev18tajh0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_object_identity_aud
    ADD CONSTRAINT fk_3f8wpmd7ibfsxn45ev18tajh0 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3566 (class 2606 OID 493224)
-- Name: fk_3gqqno6bygwky50226y0vtfrr; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_additioninformation
    ADD CONSTRAINT fk_3gqqno6bygwky50226y0vtfrr FOREIGN KEY (uuid) REFERENCES re_proposalmanagementinformation(uuid);


--
-- TOC entry 3575 (class 2606 OID 493229)
-- Name: fk_3h5n0y6th0eeb85g7ampc97l3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_clarificationinformation_aud
    ADD CONSTRAINT fk_3h5n0y6th0eeb85g7ampc97l3 FOREIGN KEY (uuid, rev) REFERENCES re_proposalmanagementinformation_aud(uuid, rev);


--
-- TOC entry 3606 (class 2606 OID 493234)
-- Name: fk_3jmjskdt9o6amnqbd0qw34rn5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_registeritem_fieldofapplication_aud
    ADD CONSTRAINT fk_3jmjskdt9o6amnqbd0qw34rn5 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3586 (class 2606 OID 493239)
-- Name: fk_3kl1j69nwnutdmd2la8bchfy9; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_register
    ADD CONSTRAINT fk_3kl1j69nwnutdmd2la8bchfy9 FOREIGN KEY (uniformresourceidentifier_uuid) REFERENCES ci_onlineresource(uuid);


--
-- TOC entry 3644 (class 2606 OID 493244)
-- Name: fk_3ondt3fp3pniqwp1m4yvhf8ui; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY supersession
    ADD CONSTRAINT fk_3ondt3fp3pniqwp1m4yvhf8ui FOREIGN KEY (targetregister_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3528 (class 2606 OID 493249)
-- Name: fk_3qylclpufr4t9g86uwr2mkiy4; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY organization
    ADD CONSTRAINT fk_3qylclpufr4t9g86uwr2mkiy4 FOREIGN KEY (uuid) REFERENCES actor(uuid);


--
-- TOC entry 3362 (class 2606 OID 493254)
-- Name: fk_3rk02qcwhibvnm78m2wfu74dk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_address_deliverypoint_aud
    ADD CONSTRAINT fk_3rk02qcwhibvnm78m2wfu74dk FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3335 (class 2606 OID 493259)
-- Name: fk_3sf8nla6ekwjgf9mj142ptvme; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY actor_aud
    ADD CONSTRAINT fk_3sf8nla6ekwjgf9mj142ptvme FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3523 (class 2606 OID 493264)
-- Name: fk_3wqwoj7plh3jt4v0lguhn6x94; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationparametervalue_parametervalue
    ADD CONSTRAINT fk_3wqwoj7plh3jt4v0lguhn6x94 FOREIGN KEY (operationparametervalue_uuid) REFERENCES generalparametervalue(uuid);


--
-- TOC entry 3508 (class 2606 OID 493269)
-- Name: fk_40jn2dwy3lb7q3opohwvd9u2d; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationmethoditem_aud
    ADD CONSTRAINT fk_40jn2dwy3lb7q3opohwvd9u2d FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3616 (class 2606 OID 493274)
-- Name: fk_43lqrbtgjdl3grm1gkl0gvvrm; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY registerrelatedrole
    ADD CONSTRAINT fk_43lqrbtgjdl3grm1gkl0gvvrm FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3406 (class 2606 OID 493279)
-- Name: fk_4abps57eftf7qvj7kqy0ga4sf; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY conversionitem
    ADD CONSTRAINT fk_4abps57eftf7qvj7kqy0ga4sf FOREIGN KEY (method_uuid) REFERENCES operationmethoditem(uuid);


--
-- TOC entry 3441 (class 2606 OID 493284)
-- Name: fk_4fu02mv415uxso6scoae2vw00; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ellipsoid
    ADD CONSTRAINT fk_4fu02mv415uxso6scoae2vw00 FOREIGN KEY (inverseflatteninguom_uuid) REFERENCES unitofmeasure(uuid);


--
-- TOC entry 3451 (class 2606 OID 493289)
-- Name: fk_4lyudgausijsrq0uhefj3s5eq; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ellipsoidalcs_aud
    ADD CONSTRAINT fk_4lyudgausijsrq0uhefj3s5eq FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3553 (class 2606 OID 493294)
-- Name: fk_4on5n216wmt470pwa75nh0woy; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY proposal
    ADD CONSTRAINT fk_4on5n216wmt470pwa75nh0woy FOREIGN KEY (group_uuid) REFERENCES proposalgroup(uuid);


--
-- TOC entry 3623 (class 2606 OID 493299)
-- Name: fk_4qc0i8cs2ibc95yqs0s509vpu; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY result_aud
    ADD CONSTRAINT fk_4qc0i8cs2ibc95yqs0s509vpu FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3675 (class 2606 OID 493304)
-- Name: fk_4qk9vsdk9h46mf0b92muw5c8j; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY verticaldatum
    ADD CONSTRAINT fk_4qk9vsdk9h46mf0b92muw5c8j FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3417 (class 2606 OID 493309)
-- Name: fk_4u4es0hfg1wb7by01in4ty9rf; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY crs
    ADD CONSTRAINT fk_4u4es0hfg1wb7by01in4ty9rf FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3482 (class 2606 OID 493314)
-- Name: fk_4vr74583i6tkm5cs9vjl57q07; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY generalparametervalue_aud
    ADD CONSTRAINT fk_4vr74583i6tkm5cs9vjl57q07 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3407 (class 2606 OID 493319)
-- Name: fk_4wrku6mcr6uv0sw8n7836vvhq; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY conversionitem
    ADD CONSTRAINT fk_4wrku6mcr6uv0sw8n7836vvhq FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3436 (class 2606 OID 493324)
-- Name: fk_532ilunwqp3vvc9cdublp4gvb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dq_relativeinternalpositionalaccuracy
    ADD CONSTRAINT fk_532ilunwqp3vvc9cdublp4gvb FOREIGN KEY (authority_uuid) REFERENCES ci_citation(uuid);


--
-- TOC entry 3471 (class 2606 OID 493329)
-- Name: fk_592m3qvoc14lva2i5xadw2pra; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ex_temporalextent_aud
    ADD CONSTRAINT fk_592m3qvoc14lva2i5xadw2pra FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3385 (class 2606 OID 493334)
-- Name: fk_5a3k8yw78881j0lra0g5gd9va; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_telephone_facsimile_aud
    ADD CONSTRAINT fk_5a3k8yw78881j0lra0g5gd9va FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3340 (class 2606 OID 493339)
-- Name: fk_5btssribs7w4yftruyt2bsp9w; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY appeal
    ADD CONSTRAINT fk_5btssribs7w4yftruyt2bsp9w FOREIGN KEY (appealedproposal_uuid) REFERENCES proposal(uuid);


--
-- TOC entry 3676 (class 2606 OID 493344)
-- Name: fk_5egu3m42nd12thb2pwyucopev; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY verticaldatum
    ADD CONSTRAINT fk_5egu3m42nd12thb2pwyucopev FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3652 (class 2606 OID 493349)
-- Name: fk_5glwtoic24dafhev9oifnorvg; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY transformationitem
    ADD CONSTRAINT fk_5glwtoic24dafhev9oifnorvg FOREIGN KEY (method_uuid) REFERENCES operationmethoditem(uuid);


--
-- TOC entry 3408 (class 2606 OID 493354)
-- Name: fk_5i8aq1t8h10jbbusi9782ixo4; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY conversionitem
    ADD CONSTRAINT fk_5i8aq1t8h10jbbusi9782ixo4 FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3595 (class 2606 OID 493359)
-- Name: fk_5o1xt3dxkj0pr7j9ymncku013; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_register_re_locale
    ADD CONSTRAINT fk_5o1xt3dxkj0pr7j9ymncku013 FOREIGN KEY (re_register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3379 (class 2606 OID 493364)
-- Name: fk_5qoapxgg3sgqh76lr3aimuqf8; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_contact_aud
    ADD CONSTRAINT fk_5qoapxgg3sgqh76lr3aimuqf8 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3409 (class 2606 OID 493369)
-- Name: fk_5sfmp6m1jt4631tnybopr9gjc; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY conversionitem
    ADD CONSTRAINT fk_5sfmp6m1jt4631tnybopr9gjc FOREIGN KEY (domainofvalidity_uuid) REFERENCES ex_extent(uuid);


--
-- TOC entry 3348 (class 2606 OID 493374)
-- Name: fk_5v68kspdwr0x7385999hkkixq; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY authorizationtable_aud
    ADD CONSTRAINT fk_5v68kspdwr0x7385999hkkixq FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3366 (class 2606 OID 493379)
-- Name: fk_5xwv9wosvpb37daig65ts9io9; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_citation_alternatetitle
    ADD CONSTRAINT fk_5xwv9wosvpb37daig65ts9io9 FOREIGN KEY (ci_citation_uuid) REFERENCES ci_citation(uuid);


--
-- TOC entry 3596 (class 2606 OID 493384)
-- Name: fk_5y1lhiw2mrbyhgfrqmgslwlf6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_register_re_locale
    ADD CONSTRAINT fk_5y1lhiw2mrbyhgfrqmgslwlf6 FOREIGN KEY (alternativelanguages_uuid) REFERENCES re_locale(uuid);


--
-- TOC entry 3452 (class 2606 OID 493389)
-- Name: fk_5yno3t357per94v3edk873mm7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY engineeringcrs
    ADD CONSTRAINT fk_5yno3t357per94v3edk873mm7 FOREIGN KEY (domainofvalidity_uuid) REFERENCES ex_extent(uuid);


--
-- TOC entry 3631 (class 2606 OID 493394)
-- Name: fk_62e4tpxe2plyyxk4c2xfnhn0h; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY singleoperationitem
    ADD CONSTRAINT fk_62e4tpxe2plyyxk4c2xfnhn0h FOREIGN KEY (domainofvalidity_uuid) REFERENCES ex_extent(uuid);


--
-- TOC entry 3330 (class 2606 OID 493399)
-- Name: fk_6c3ugmk053uy27bk2sred31lf; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_object_identity
    ADD CONSTRAINT fk_6c3ugmk053uy27bk2sred31lf FOREIGN KEY (object_id_class) REFERENCES acl_class(uuid);


--
-- TOC entry 3601 (class 2606 OID 493404)
-- Name: fk_6egtnnrcooe32538lb3ljauku; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_registeritem
    ADD CONSTRAINT fk_6egtnnrcooe32538lb3ljauku FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3522 (class 2606 OID 493409)
-- Name: fk_6jfg0mea6elpbqfwsf9qotaqr; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationparameteritem_aud
    ADD CONSTRAINT fk_6jfg0mea6elpbqfwsf9qotaqr FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3458 (class 2606 OID 493414)
-- Name: fk_6jlwtlm2d9s57418oh5m57el; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY engineeringdatum
    ADD CONSTRAINT fk_6jlwtlm2d9s57418oh5m57el FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3448 (class 2606 OID 493419)
-- Name: fk_6k5jp181u0ebs32tebobi5s3y; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ellipsoidalcs
    ADD CONSTRAINT fk_6k5jp181u0ebs32tebobi5s3y FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3446 (class 2606 OID 493424)
-- Name: fk_6ka74mi2ja77v1ghm4d6xg2a2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ellipsoid_aud
    ADD CONSTRAINT fk_6ka74mi2ja77v1ghm4d6xg2a2 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3331 (class 2606 OID 493429)
-- Name: fk_6oap2k8q5bl33yq3yffrwedhf; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_object_identity
    ADD CONSTRAINT fk_6oap2k8q5bl33yq3yffrwedhf FOREIGN KEY (parent_object) REFERENCES acl_object_identity(uuid);


--
-- TOC entry 3371 (class 2606 OID 493434)
-- Name: fk_6oebp10saklu9vkpohkwmgtnq; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_citation_ci_responsibleparty_aud
    ADD CONSTRAINT fk_6oebp10saklu9vkpohkwmgtnq FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3615 (class 2606 OID 493439)
-- Name: fk_6oogjq1jf2p0ki8prd49n4rfa; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_submittingorganization_aud
    ADD CONSTRAINT fk_6oogjq1jf2p0ki8prd49n4rfa FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3639 (class 2606 OID 493444)
-- Name: fk_6r6yo0uhg2a9vttrl1foou8jw; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sphericalcs
    ADD CONSTRAINT fk_6r6yo0uhg2a9vttrl1foou8jw FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3426 (class 2606 OID 493449)
-- Name: fk_6t75iph5gbda1cl80cgjkqea; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dq_absoluteexternalpositionalaccuracy
    ADD CONSTRAINT fk_6t75iph5gbda1cl80cgjkqea FOREIGN KEY (evaluationprocedure_uuid) REFERENCES ci_citation(uuid);


--
-- TOC entry 3547 (class 2606 OID 493454)
-- Name: fk_6vvda85ol9an7j3qyx6ffe9e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY projectedcrs
    ADD CONSTRAINT fk_6vvda85ol9an7j3qyx6ffe9e FOREIGN KEY (domainofvalidity_uuid) REFERENCES ex_extent(uuid);


--
-- TOC entry 3442 (class 2606 OID 493459)
-- Name: fk_6xw9f44qun3gnyxh0xdau78iw; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ellipsoid
    ADD CONSTRAINT fk_6xw9f44qun3gnyxh0xdau78iw FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3490 (class 2606 OID 493464)
-- Name: fk_74jlt1dcclssfy97qgpxxy4ts; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY geodeticdatum
    ADD CONSTRAINT fk_74jlt1dcclssfy97qgpxxy4ts FOREIGN KEY (domainofvalidity_uuid) REFERENCES ex_extent(uuid);


--
-- TOC entry 3437 (class 2606 OID 493469)
-- Name: fk_75e579q6ko0ro9ma9vef6dwwy; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dq_relativeinternalpositionalaccuracy
    ADD CONSTRAINT fk_75e579q6ko0ro9ma9vef6dwwy FOREIGN KEY (result_uuid) REFERENCES result(uuid);


--
-- TOC entry 3584 (class 2606 OID 493474)
-- Name: fk_7b5m10jn4p0ud3unihohqw6me; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_reference
    ADD CONSTRAINT fk_7b5m10jn4p0ud3unihohqw6me FOREIGN KEY (citation_uuid) REFERENCES ci_citation(uuid);


--
-- TOC entry 3443 (class 2606 OID 493479)
-- Name: fk_7bb6gb4tpmd86o7bl0qt0yoy9; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ellipsoid
    ADD CONSTRAINT fk_7bb6gb4tpmd86o7bl0qt0yoy9 FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3350 (class 2606 OID 493484)
-- Name: fk_7kb13fvk3v15u71qghf0lwu3q; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY axis
    ADD CONSTRAINT fk_7kb13fvk3v15u71qghf0lwu3q FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3388 (class 2606 OID 493489)
-- Name: fk_7nng9665q20j7ax414kf6hg2s; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY clarification
    ADD CONSTRAINT fk_7nng9665q20j7ax414kf6hg2s FOREIGN KEY (uuid) REFERENCES simpleproposal(uuid);


--
-- TOC entry 3561 (class 2606 OID 493494)
-- Name: fk_7tb13ktjq9h3er0j5qghkscnl; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY proposalgroup
    ADD CONSTRAINT fk_7tb13ktjq9h3er0j5qghkscnl FOREIGN KEY (uuid) REFERENCES proposal(uuid);


--
-- TOC entry 3453 (class 2606 OID 493499)
-- Name: fk_7u5l85r2fytoy3ayyktrjvk1q; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY engineeringcrs
    ADD CONSTRAINT fk_7u5l85r2fytoy3ayyktrjvk1q FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3418 (class 2606 OID 493504)
-- Name: fk_7x49lhtcalmv9ehhtcwcbytt6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY crs
    ADD CONSTRAINT fk_7x49lhtcalmv9ehhtcwcbytt6 FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3414 (class 2606 OID 493509)
-- Name: fk_7ycc84cp78d9mo0ggbt5y9nk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY coordinateoperationitem_scope_aud
    ADD CONSTRAINT fk_7ycc84cp78d9mo0ggbt5y9nk FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3498 (class 2606 OID 493514)
-- Name: fk_80d0pi1tsms2uwd63ogeg84s5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY namingsystemitem
    ADD CONSTRAINT fk_80d0pi1tsms2uwd63ogeg84s5 FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3355 (class 2606 OID 493519)
-- Name: fk_839iiarhs7t24sqnoetdg5pxy; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cartesiancs
    ADD CONSTRAINT fk_839iiarhs7t24sqnoetdg5pxy FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3600 (class 2606 OID 493524)
-- Name: fk_89npqryd0rn1u71vt91ktqgnm; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_register_submitters_aud
    ADD CONSTRAINT fk_89npqryd0rn1u71vt91ktqgnm FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3571 (class 2606 OID 493529)
-- Name: fk_8aoy24ui3vd2u2ktd831092ig; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_alternativeexpression_fieldofapplication_aud
    ADD CONSTRAINT fk_8aoy24ui3vd2u2ktd831092ig FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3674 (class 2606 OID 493534)
-- Name: fk_8asagqyth9ak9i7oewxng4hyb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY verticalcs_aud
    ADD CONSTRAINT fk_8asagqyth9ak9i7oewxng4hyb FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3646 (class 2606 OID 493539)
-- Name: fk_8cp4mubyxb0iu01g6q1a7aj92; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY supersession_aud
    ADD CONSTRAINT fk_8cp4mubyxb0iu01g6q1a7aj92 FOREIGN KEY (uuid, rev) REFERENCES proposalgroup_aud(uuid, rev);


--
-- TOC entry 3548 (class 2606 OID 493544)
-- Name: fk_8dn7yngpht8exypn5uc72bh2s; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY projectedcrs
    ADD CONSTRAINT fk_8dn7yngpht8exypn5uc72bh2s FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3368 (class 2606 OID 493549)
-- Name: fk_8j4wi1k15mdhmt2iuaoy9a8tp; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_citation_aud
    ADD CONSTRAINT fk_8j4wi1k15mdhmt2iuaoy9a8tp FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3648 (class 2606 OID 493554)
-- Name: fk_8js1nu21o7lfcs07fe47s40q; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY supersession_supersedingitems_aud
    ADD CONSTRAINT fk_8js1nu21o7lfcs07fe47s40q FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3491 (class 2606 OID 493559)
-- Name: fk_8s542nwst863ped8ena3eij9w; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY geodeticdatum
    ADD CONSTRAINT fk_8s542nwst863ped8ena3eij9w FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3336 (class 2606 OID 493564)
-- Name: fk_8vka5i4rwft3ffvjuwypmldna; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY addition
    ADD CONSTRAINT fk_8vka5i4rwft3ffvjuwypmldna FOREIGN KEY (uuid) REFERENCES simpleproposal(uuid);


--
-- TOC entry 3663 (class 2606 OID 493569)
-- Name: fk_8wo1u8xhs6k9fgvgmxgq5tkbw; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY unitofmeasure_aud
    ADD CONSTRAINT fk_8wo1u8xhs6k9fgvgmxgq5tkbw FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3429 (class 2606 OID 493574)
-- Name: fk_8y8ntfh88v0vafdu3wp4k6gf5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dq_absoluteexternalpositionalaccuracy_aud
    ADD CONSTRAINT fk_8y8ntfh88v0vafdu3wp4k6gf5 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3477 (class 2606 OID 493579)
-- Name: fk_94yqhy8hl4bjhe6pi1e58jfxv; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY generaloperationparameteritem
    ADD CONSTRAINT fk_94yqhy8hl4bjhe6pi1e58jfxv FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3597 (class 2606 OID 493584)
-- Name: fk_96j3c7pp1fr7f6e95j0hfmt7j; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_register_re_locale_aud
    ADD CONSTRAINT fk_96j3c7pp1fr7f6e95j0hfmt7j FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3541 (class 2606 OID 493589)
-- Name: fk_99aqg2rupy6flctp6ix9sgmq5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY primemeridian
    ADD CONSTRAINT fk_99aqg2rupy6flctp6ix9sgmq5 FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3464 (class 2606 OID 493594)
-- Name: fk_99blqnddsbrv9n4k8677s9lnb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ex_extent_aud
    ADD CONSTRAINT fk_99blqnddsbrv9n4k8677s9lnb FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3454 (class 2606 OID 493599)
-- Name: fk_9dqxwju4vffcrvye16amqjic1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY engineeringcrs
    ADD CONSTRAINT fk_9dqxwju4vffcrvye16amqjic1 FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3367 (class 2606 OID 493604)
-- Name: fk_9je3e5xqbbavov6280ch2bsbr; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_citation_alternatetitle_aud
    ADD CONSTRAINT fk_9je3e5xqbbavov6280ch2bsbr FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3410 (class 2606 OID 493609)
-- Name: fk_9l11g7wbl2t43w7hv68429gpf; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY conversionitem
    ADD CONSTRAINT fk_9l11g7wbl2t43w7hv68429gpf FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3483 (class 2606 OID 493614)
-- Name: fk_9oeoxkdp11h6tqyyw730ym8xl; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY geodeticcrs
    ADD CONSTRAINT fk_9oeoxkdp11h6tqyyw730ym8xl FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3465 (class 2606 OID 493619)
-- Name: fk_9pi0vg4ohc41d3xd9reygmuyg; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ex_extent_ex_geographicextent
    ADD CONSTRAINT fk_9pi0vg4ohc41d3xd9reygmuyg FOREIGN KEY (ex_extent_uuid) REFERENCES ex_extent(uuid);


--
-- TOC entry 3376 (class 2606 OID 493624)
-- Name: fk_9quwx2crk25pu8mwakhd2i1vh; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_contact
    ADD CONSTRAINT fk_9quwx2crk25pu8mwakhd2i1vh FOREIGN KEY (phone_uuid) REFERENCES ci_telephone(uuid);


--
-- TOC entry 3444 (class 2606 OID 493629)
-- Name: fk_9tfh9mtbh378gju44n6orij92; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ellipsoid
    ADD CONSTRAINT fk_9tfh9mtbh378gju44n6orij92 FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3533 (class 2606 OID 493634)
-- Name: fk_9ysu13feunvtuljjhb9269jf6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY passthroughoperationitem
    ADD CONSTRAINT fk_9ysu13feunvtuljjhb9269jf6 FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3573 (class 2606 OID 493639)
-- Name: fk_a0y1sursxqannla5569v3u88p; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_amendmentinformation_aud
    ADD CONSTRAINT fk_a0y1sursxqannla5569v3u88p FOREIGN KEY (uuid, rev) REFERENCES re_proposalmanagementinformation_aud(uuid, rev);


--
-- TOC entry 3342 (class 2606 OID 493644)
-- Name: fk_a1typ41kwccygloq2lxannxha; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY area
    ADD CONSTRAINT fk_a1typ41kwccygloq2lxannxha FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3677 (class 2606 OID 493649)
-- Name: fk_a5xb8idjar5lm89dgf4goeihw; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY verticaldatum
    ADD CONSTRAINT fk_a5xb8idjar5lm89dgf4goeihw FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3488 (class 2606 OID 493654)
-- Name: fk_a80ls3qt1a7q2d12kuw3hjjic; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY geodeticcrs_aud
    ADD CONSTRAINT fk_a80ls3qt1a7q2d12kuw3hjjic FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3587 (class 2606 OID 493659)
-- Name: fk_aaycr004gke6og1qsy33k6wh1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_register
    ADD CONSTRAINT fk_aaycr004gke6og1qsy33k6wh1 FOREIGN KEY (operatinglanguage_uuid) REFERENCES re_locale(uuid);


--
-- TOC entry 3369 (class 2606 OID 493664)
-- Name: fk_abei7ag504908kvc3f35nerfq; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_citation_ci_responsibleparty
    ADD CONSTRAINT fk_abei7ag504908kvc3f35nerfq FOREIGN KEY (ci_citation_uuid) REFERENCES ci_citation(uuid);


--
-- TOC entry 3517 (class 2606 OID 493669)
-- Name: fk_aejov834y7pyp29be9ovu5b56; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationparameteritem
    ADD CONSTRAINT fk_aejov834y7pyp29be9ovu5b56 FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3594 (class 2606 OID 493674)
-- Name: fk_akeinuksc0365g5mm8nlh8fw0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_register_itemclasses_aud
    ADD CONSTRAINT fk_akeinuksc0365g5mm8nlh8fw0 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3632 (class 2606 OID 493679)
-- Name: fk_ap19d0rntfxs2f5vce946gksu; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY singleoperationitem
    ADD CONSTRAINT fk_ap19d0rntfxs2f5vce946gksu FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3339 (class 2606 OID 493684)
-- Name: fk_aqta6x397j529sgwawo1jfds6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY alias_aud
    ADD CONSTRAINT fk_aqta6x397j529sgwawo1jfds6 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3518 (class 2606 OID 493689)
-- Name: fk_arnkp8tiddeiqsrnpsrvjaehm; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationparameteritem
    ADD CONSTRAINT fk_arnkp8tiddeiqsrnpsrvjaehm FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3497 (class 2606 OID 493694)
-- Name: fk_awieoh027aogfhauew6u3cxh5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY identifieditem_aliases_aud
    ADD CONSTRAINT fk_awieoh027aogfhauew6u3cxh5 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3558 (class 2606 OID 493699)
-- Name: fk_b1slu7fvqjorljg60fvnx24ka; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY proposaldiscussion_aud
    ADD CONSTRAINT fk_b1slu7fvqjorljg60fvnx24ka FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3578 (class 2606 OID 493704)
-- Name: fk_b4v25i0c4q9j7qgkmnsjhbtbm; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_itemclass_alternativenames_aud
    ADD CONSTRAINT fk_b4v25i0c4q9j7qgkmnsjhbtbm FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3360 (class 2606 OID 493709)
-- Name: fk_b5oevnwmj7tfk1yxey0tteait; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_address_aud
    ADD CONSTRAINT fk_b5oevnwmj7tfk1yxey0tteait FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3588 (class 2606 OID 493714)
-- Name: fk_b6gwauf349db84hc8kcfepv6h; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_register
    ADD CONSTRAINT fk_b6gwauf349db84hc8kcfepv6h FOREIGN KEY (citation_uuid) REFERENCES ci_citation(uuid);


--
-- TOC entry 3398 (class 2606 OID 493719)
-- Name: fk_b8dx7kamot2alce6yiftjiy67; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY concatenatedoperationitem
    ADD CONSTRAINT fk_b8dx7kamot2alce6yiftjiy67 FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3503 (class 2606 OID 493724)
-- Name: fk_baiiise9kv91jxfeinr5iyjv0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationmethoditem
    ADD CONSTRAINT fk_baiiise9kv91jxfeinr5iyjv0 FOREIGN KEY (formulacitation_uuid) REFERENCES ci_citation(uuid);


--
-- TOC entry 3645 (class 2606 OID 493729)
-- Name: fk_bckvktkvmkvijn6i9vlj7djvr; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY supersession
    ADD CONSTRAINT fk_bckvktkvmkvijn6i9vlj7djvr FOREIGN KEY (uuid) REFERENCES proposalgroup(uuid);


--
-- TOC entry 3361 (class 2606 OID 493734)
-- Name: fk_bf31d3skoid2w686ateedoses; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_address_deliverypoint
    ADD CONSTRAINT fk_bf31d3skoid2w686ateedoses FOREIGN KEY (ci_address_uuid) REFERENCES ci_address(uuid);


--
-- TOC entry 3432 (class 2606 OID 493739)
-- Name: fk_bfbdykv60kcuh59gag439b92e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dq_griddeddatapositionalaccuracy
    ADD CONSTRAINT fk_bfbdykv60kcuh59gag439b92e FOREIGN KEY (result_uuid) REFERENCES result(uuid);


--
-- TOC entry 3622 (class 2606 OID 493744)
-- Name: fk_bfw9q1c8clhk50esijkv5vu81; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY result
    ADD CONSTRAINT fk_bfw9q1c8clhk50esijkv5vu81 FOREIGN KEY (accuracyunit_uuid) REFERENCES unitofmeasure(uuid);


--
-- TOC entry 3653 (class 2606 OID 493749)
-- Name: fk_bjlkape5rxifhr156rhxy78fs; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY transformationitem
    ADD CONSTRAINT fk_bjlkape5rxifhr156rhxy78fs FOREIGN KEY (domainofvalidity_uuid) REFERENCES ex_extent(uuid);


--
-- TOC entry 3499 (class 2606 OID 493754)
-- Name: fk_bjnn21us8p4m95qofocp6915x; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY namingsystemitem
    ADD CONSTRAINT fk_bjnn21us8p4m95qofocp6915x FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3351 (class 2606 OID 493759)
-- Name: fk_bp1vimj1bjcwg9bnb0mda4lpi; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY axis
    ADD CONSTRAINT fk_bp1vimj1bjcwg9bnb0mda4lpi FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3445 (class 2606 OID 493764)
-- Name: fk_bucwrmn40tq8lq153rr5ug5c6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ellipsoid
    ADD CONSTRAINT fk_bucwrmn40tq8lq153rr5ug5c6 FOREIGN KEY (semimajoraxisuom_uuid) REFERENCES unitofmeasure(uuid);


--
-- TOC entry 3504 (class 2606 OID 493769)
-- Name: fk_c0e1jnkadw3tbl5tcswn3l0ke; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationmethoditem
    ADD CONSTRAINT fk_c0e1jnkadw3tbl5tcswn3l0ke FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3530 (class 2606 OID 493774)
-- Name: fk_c81qse8rg80ollx6jwqlm1vcs; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY organization_aud
    ADD CONSTRAINT fk_c81qse8rg80ollx6jwqlm1vcs FOREIGN KEY (uuid, rev) REFERENCES actor_aud(uuid, rev);


--
-- TOC entry 3519 (class 2606 OID 493779)
-- Name: fk_caeyvx8p7wc9turkkwxuk6opm; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationparameteritem
    ADD CONSTRAINT fk_caeyvx8p7wc9turkkwxuk6opm FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3565 (class 2606 OID 493784)
-- Name: fk_cc0mac1xtkscdvkvnywppxnhp; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY proposalnote_aud
    ADD CONSTRAINT fk_cc0mac1xtkscdvkvnywppxnhp FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3329 (class 2606 OID 493789)
-- Name: fk_ccdx1x6s82umb760wabj169bb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_entry_aud
    ADD CONSTRAINT fk_ccdx1x6s82umb760wabj169bb FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3654 (class 2606 OID 493794)
-- Name: fk_cdr98mgq0mfc4deny8tym7w5i; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY transformationitem
    ADD CONSTRAINT fk_cdr98mgq0mfc4deny8tym7w5i FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3370 (class 2606 OID 493799)
-- Name: fk_cj59exysvhvaeyaqgrshs0ax9; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_citation_ci_responsibleparty
    ADD CONSTRAINT fk_cj59exysvhvaeyaqgrshs0ax9 FOREIGN KEY (citedresponsibleparty_uuid) REFERENCES ci_responsibleparty(uuid);


--
-- TOC entry 3529 (class 2606 OID 493804)
-- Name: fk_cktxhfp9h9p3jptwhtnnfppxd; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY organization
    ADD CONSTRAINT fk_cktxhfp9h9p3jptwhtnnfppxd FOREIGN KEY (submittingorganization_uuid) REFERENCES re_submittingorganization(uuid);


--
-- TOC entry 3549 (class 2606 OID 493809)
-- Name: fk_clfxp4va8svv5eovernfnkh4t; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY projectedcrs
    ADD CONSTRAINT fk_clfxp4va8svv5eovernfnkh4t FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3516 (class 2606 OID 493814)
-- Name: fk_cm14ti2itw15y99oly9kvko8v; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationparametergroupitem_aud
    ADD CONSTRAINT fk_cm14ti2itw15y99oly9kvko8v FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3510 (class 2606 OID 493819)
-- Name: fk_crnraadpjo98ijh0ppshfiw9x; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationmethoditem_generaloperationparameteritem_aud
    ADD CONSTRAINT fk_crnraadpjo98ijh0ppshfiw9x FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3624 (class 2606 OID 493824)
-- Name: fk_crvt0bf5lcbmp6qbb4xkg3w2y; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY retirement
    ADD CONSTRAINT fk_crvt0bf5lcbmp6qbb4xkg3w2y FOREIGN KEY (uuid) REFERENCES simpleproposal(uuid);


--
-- TOC entry 3399 (class 2606 OID 493829)
-- Name: fk_d14tefv0jfp4fnw7yh5vh6wn5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY concatenatedoperationitem
    ADD CONSTRAINT fk_d14tefv0jfp4fnw7yh5vh6wn5 FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3609 (class 2606 OID 493834)
-- Name: fk_d7ddolkdl449quuytrk7ul5g; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_registeritem_successor_aud
    ADD CONSTRAINT fk_d7ddolkdl449quuytrk7ul5g FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3643 (class 2606 OID 493839)
-- Name: fk_d8pbciuvuxi5u5os1dv2pj0bg; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sphericalcs_aud
    ADD CONSTRAINT fk_d8pbciuvuxi5u5os1dv2pj0bg FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3400 (class 2606 OID 493844)
-- Name: fk_d9bmpqotsc8589s2st5pqeql9; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY concatenatedoperationitem
    ADD CONSTRAINT fk_d9bmpqotsc8589s2st5pqeql9 FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3500 (class 2606 OID 493849)
-- Name: fk_db24phebrc7qaxlmbcp7u19wt; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY namingsystemitem
    ADD CONSTRAINT fk_db24phebrc7qaxlmbcp7u19wt FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3430 (class 2606 OID 493854)
-- Name: fk_dbc0jgvmcyvsjt7h90nbiry5l; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dq_element_datetime_aud
    ADD CONSTRAINT fk_dbc0jgvmcyvsjt7h90nbiry5l FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3678 (class 2606 OID 493859)
-- Name: fk_dk3eism0tkfgta0vnx5ka6is; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY verticaldatum
    ADD CONSTRAINT fk_dk3eism0tkfgta0vnx5ka6is FOREIGN KEY (domainofvalidity_uuid) REFERENCES ex_extent(uuid);


--
-- TOC entry 3403 (class 2606 OID 493864)
-- Name: fk_dkvp8jce3bwngqcfwntjutlw9; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY concatenatedoperationitem_aud
    ADD CONSTRAINT fk_dkvp8jce3bwngqcfwntjutlw9 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3391 (class 2606 OID 493869)
-- Name: fk_dnb445m2gq0mme2jlmd5amla6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY compoundcrs
    ADD CONSTRAINT fk_dnb445m2gq0mme2jlmd5amla6 FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3526 (class 2606 OID 493874)
-- Name: fk_dnvp48vmg7d420e6k75ygbsws; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationparametervalue_parametervaluesimple
    ADD CONSTRAINT fk_dnvp48vmg7d420e6k75ygbsws FOREIGN KEY (operationparametervalue_uuid) REFERENCES generalparametervalue(uuid);


--
-- TOC entry 3472 (class 2606 OID 493879)
-- Name: fk_doin166ddq1rcre7bwyed80sf; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ex_verticalextent_aud
    ADD CONSTRAINT fk_doin166ddq1rcre7bwyed80sf FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3589 (class 2606 OID 493884)
-- Name: fk_dpx480bjh8jkee434ppq5ft8a; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_register
    ADD CONSTRAINT fk_dpx480bjh8jkee434ppq5ft8a FOREIGN KEY (manager_uuid) REFERENCES re_registermanager(uuid);


--
-- TOC entry 3363 (class 2606 OID 493889)
-- Name: fk_dw9aiwjwb9gvt8t0wiccriw01; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_address_electronicmailaddress
    ADD CONSTRAINT fk_dw9aiwjwb9gvt8t0wiccriw01 FOREIGN KEY (ci_address_uuid) REFERENCES ci_address(uuid);


--
-- TOC entry 3337 (class 2606 OID 493894)
-- Name: fk_e453nj6xbwmf0of3dsy4g3a2b; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY addition_aud
    ADD CONSTRAINT fk_e453nj6xbwmf0of3dsy4g3a2b FOREIGN KEY (uuid, rev) REFERENCES simpleproposal_aud(uuid, rev);


--
-- TOC entry 3542 (class 2606 OID 493899)
-- Name: fk_e7v8ib773h3olf5n694126dpl; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY primemeridian
    ADD CONSTRAINT fk_e7v8ib773h3olf5n694126dpl FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3343 (class 2606 OID 493904)
-- Name: fk_ebcnvmfdpol5h4gmxmkiuegbh; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY area
    ADD CONSTRAINT fk_ebcnvmfdpol5h4gmxmkiuegbh FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3505 (class 2606 OID 493909)
-- Name: fk_edfcasu1vm29drvlwobljs0nh; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationmethoditem
    ADD CONSTRAINT fk_edfcasu1vm29drvlwobljs0nh FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3592 (class 2606 OID 493914)
-- Name: fk_eqjxto9nmqe4mkpliasbeviiv; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_register_itemclasses
    ADD CONSTRAINT fk_eqjxto9nmqe4mkpliasbeviiv FOREIGN KEY (itemclassid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3608 (class 2606 OID 493919)
-- Name: fk_esqbc0hd659t6ped1ul1nt7rc; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_registeritem_re_alternativeexpression_aud
    ADD CONSTRAINT fk_esqbc0hd659t6ped1ul1nt7rc FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3492 (class 2606 OID 493924)
-- Name: fk_etbfc75viw787qkqu5t31a2e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY geodeticdatum
    ADD CONSTRAINT fk_etbfc75viw787qkqu5t31a2e FOREIGN KEY (primemeridian_uuid) REFERENCES primemeridian(uuid);


--
-- TOC entry 3555 (class 2606 OID 493929)
-- Name: fk_eu24gebhne29337e49oo5dnib; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY proposal_aud
    ADD CONSTRAINT fk_eu24gebhne29337e49oo5dnib FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3375 (class 2606 OID 493934)
-- Name: fk_exw3k4xgyi6kairj3mtguaxgp; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_citation_presentationform_aud
    ADD CONSTRAINT fk_exw3k4xgyi6kairj3mtguaxgp FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3493 (class 2606 OID 493939)
-- Name: fk_f3n7pyvuowwv1ua1w9hpc2jdl; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY geodeticdatum
    ADD CONSTRAINT fk_f3n7pyvuowwv1ua1w9hpc2jdl FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3669 (class 2606 OID 493944)
-- Name: fk_f3pejgktso6nlta6tvalx9m33; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY verticalcrs_aud
    ADD CONSTRAINT fk_f3pejgktso6nlta6tvalx9m33 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3658 (class 2606 OID 493949)
-- Name: fk_f6a5ysuqa82qx1w2e8muoxh39; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY unitofmeasure
    ADD CONSTRAINT fk_f6a5ysuqa82qx1w2e8muoxh39 FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3629 (class 2606 OID 493954)
-- Name: fk_fa092ro1la89dgoui4i3k55ph; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY simpleproposal_aud
    ADD CONSTRAINT fk_fa092ro1la89dgoui4i3k55ph FOREIGN KEY (uuid, rev) REFERENCES proposal_aud(uuid, rev);


--
-- TOC entry 3459 (class 2606 OID 493959)
-- Name: fk_fes9wuwcf2sv2o5rrencbdetx; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY engineeringdatum
    ADD CONSTRAINT fk_fes9wuwcf2sv2o5rrencbdetx FOREIGN KEY (domainofvalidity_uuid) REFERENCES ex_extent(uuid);


--
-- TOC entry 3625 (class 2606 OID 493964)
-- Name: fk_fh6qjwpm8pmgf3dqngkiyq9i0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY retirement_aud
    ADD CONSTRAINT fk_fh6qjwpm8pmgf3dqngkiyq9i0 FOREIGN KEY (uuid, rev) REFERENCES simpleproposal_aud(uuid, rev);


--
-- TOC entry 3327 (class 2606 OID 493969)
-- Name: fk_fhuoesmjef3mrv0gpja4shvcr; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_entry
    ADD CONSTRAINT fk_fhuoesmjef3mrv0gpja4shvcr FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(uuid);


--
-- TOC entry 3455 (class 2606 OID 493974)
-- Name: fk_fk9esnobo7ggu70d1qdira59i; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY engineeringcrs
    ADD CONSTRAINT fk_fk9esnobo7ggu70d1qdira59i FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3538 (class 2606 OID 493979)
-- Name: fk_fmxqcgayeeh6qooh0bs9904sv; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY passthroughoperationitem_aud
    ADD CONSTRAINT fk_fmxqcgayeeh6qooh0bs9904sv FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3460 (class 2606 OID 493984)
-- Name: fk_fp6ww8gs5jfy6b9p2rtqar6un; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY engineeringdatum
    ADD CONSTRAINT fk_fp6ww8gs5jfy6b9p2rtqar6un FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3344 (class 2606 OID 493989)
-- Name: fk_fv38w36oypk7n1fb8p2e1hqs5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY area
    ADD CONSTRAINT fk_fv38w36oypk7n1fb8p2e1hqs5 FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3520 (class 2606 OID 493994)
-- Name: fk_fwobnowinhusv90if319nucef; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationparameteritem
    ADD CONSTRAINT fk_fwobnowinhusv90if319nucef FOREIGN KEY (group_uuid) REFERENCES operationparametergroupitem(uuid);


--
-- TOC entry 3570 (class 2606 OID 493999)
-- Name: fk_g1o4xnwtms3sb4yiumy73tbrc; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_alternativeexpression_fieldofapplication
    ADD CONSTRAINT fk_g1o4xnwtms3sb4yiumy73tbrc FOREIGN KEY (re_alternativeexpression_uuid) REFERENCES re_alternativeexpression(uuid);


--
-- TOC entry 3640 (class 2606 OID 494004)
-- Name: fk_g4wkxpaw0ul7ex45wkiy2wky; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sphericalcs
    ADD CONSTRAINT fk_g4wkxpaw0ul7ex45wkiy2wky FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3659 (class 2606 OID 494009)
-- Name: fk_g8oqktgpycgdr4kx7qpyc05kh; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY unitofmeasure
    ADD CONSTRAINT fk_g8oqktgpycgdr4kx7qpyc05kh FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3467 (class 2606 OID 494014)
-- Name: fk_gdlpyyjhvfmsac8e7ja29ffcu; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ex_extent_ex_verticalextent
    ADD CONSTRAINT fk_gdlpyyjhvfmsac8e7ja29ffcu FOREIGN KEY (verticalelement_uuid) REFERENCES ex_verticalextent(uuid);


--
-- TOC entry 3401 (class 2606 OID 494019)
-- Name: fk_gejqm8bs3l8ts4fgwueletm9p; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY concatenatedoperationitem
    ADD CONSTRAINT fk_gejqm8bs3l8ts4fgwueletm9p FOREIGN KEY (domainofvalidity_uuid) REFERENCES ex_extent(uuid);


--
-- TOC entry 3527 (class 2606 OID 494024)
-- Name: fk_gmf1g5nes2bvp0l2c91pyvrh2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationparametervalue_parametervaluesimple_aud
    ADD CONSTRAINT fk_gmf1g5nes2bvp0l2c91pyvrh2 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3585 (class 2606 OID 494029)
-- Name: fk_gsvqxmufu59losborfqxmd8f4; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_reference_aud
    ADD CONSTRAINT fk_gsvqxmufu59losborfqxmd8f4 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3613 (class 2606 OID 494034)
-- Name: fk_h1fup0kk64na6scuwehiudmaa; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_registerowner_aud
    ADD CONSTRAINT fk_h1fup0kk64na6scuwehiudmaa FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3567 (class 2606 OID 494039)
-- Name: fk_h3ng4jqxf01vrevmwjpl29ukp; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_additioninformation_aud
    ADD CONSTRAINT fk_h3ng4jqxf01vrevmwjpl29ukp FOREIGN KEY (uuid, rev) REFERENCES re_proposalmanagementinformation_aud(uuid, rev);


--
-- TOC entry 3621 (class 2606 OID 494044)
-- Name: fk_h5sk5308694br1n7vbxlxeiph; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY registryusergroup_aud
    ADD CONSTRAINT fk_h5sk5308694br1n7vbxlxeiph FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3660 (class 2606 OID 494049)
-- Name: fk_h8trv5synhmf8paobvksnvys5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY unitofmeasure
    ADD CONSTRAINT fk_h8trv5synhmf8paobvksnvys5 FOREIGN KEY (standardunit_uuid) REFERENCES unitofmeasure(uuid);


--
-- TOC entry 3423 (class 2606 OID 494054)
-- Name: fk_h92bsgoar99xfmh48xoqaaimt; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY delegation
    ADD CONSTRAINT fk_h92bsgoar99xfmh48xoqaaimt FOREIGN KEY (delegatingorganization_uuid) REFERENCES organization(uuid);


--
-- TOC entry 3556 (class 2606 OID 494059)
-- Name: fk_hbwhra9amjnmhjwqo5a4kwfug; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY proposaldiscussion
    ADD CONSTRAINT fk_hbwhra9amjnmhjwqo5a4kwfug FOREIGN KEY (owner_uuid) REFERENCES actor(uuid);


--
-- TOC entry 3636 (class 2606 OID 494064)
-- Name: fk_hcffkolsswrapx8kgflnjq06e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY singleoperationitem_aud
    ADD CONSTRAINT fk_hcffkolsswrapx8kgflnjq06e FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3359 (class 2606 OID 494069)
-- Name: fk_hdov7ao2dy4mm21frr3i9h6gt; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cartesiancs_aud
    ADD CONSTRAINT fk_hdov7ao2dy4mm21frr3i9h6gt FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3473 (class 2606 OID 494074)
-- Name: fk_hfbmtb7cct48ltm0hdqwrs4vx; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY formula
    ADD CONSTRAINT fk_hfbmtb7cct48ltm0hdqwrs4vx FOREIGN KEY (formulacitation_uuid) REFERENCES ci_citation(uuid);


--
-- TOC entry 3598 (class 2606 OID 494079)
-- Name: fk_hfm16wvm2grvb8eun80omj1sj; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_register_submitters
    ADD CONSTRAINT fk_hfm16wvm2grvb8eun80omj1sj FOREIGN KEY (registerid) REFERENCES re_register(uuid);


--
-- TOC entry 3655 (class 2606 OID 494084)
-- Name: fk_hixwm139c88idfectr0nqf8av; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY transformationitem
    ADD CONSTRAINT fk_hixwm139c88idfectr0nqf8av FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3392 (class 2606 OID 494089)
-- Name: fk_hls9u4361l26ogapa9c03n681; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY compoundcrs
    ADD CONSTRAINT fk_hls9u4361l26ogapa9c03n681 FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3546 (class 2606 OID 494094)
-- Name: fk_hmw0rfx2myjulehgp5fsarrb3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY primemeridian_aud
    ADD CONSTRAINT fk_hmw0rfx2myjulehgp5fsarrb3 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3568 (class 2606 OID 494099)
-- Name: fk_hpcuoyrh40015q9uaq81sdemi; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_alternativeexpression
    ADD CONSTRAINT fk_hpcuoyrh40015q9uaq81sdemi FOREIGN KEY (locale_uuid) REFERENCES re_locale(uuid);


--
-- TOC entry 3395 (class 2606 OID 494104)
-- Name: fk_hq08dc76dtdg92txfelaa97gy; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY compoundcrs_aud
    ADD CONSTRAINT fk_hq08dc76dtdg92txfelaa97gy FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3364 (class 2606 OID 494109)
-- Name: fk_hqcm5230ori6nd457ab2uk9t9; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_address_electronicmailaddress_aud
    ADD CONSTRAINT fk_hqcm5230ori6nd457ab2uk9t9 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3550 (class 2606 OID 494114)
-- Name: fk_hssan25nbuwo6evm1vsjgbnjs; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY projectedcrs
    ADD CONSTRAINT fk_hssan25nbuwo6evm1vsjgbnjs FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3461 (class 2606 OID 494119)
-- Name: fk_i1e9ycnk4cglmb2kwdwrpp2f7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY engineeringdatum
    ADD CONSTRAINT fk_i1e9ycnk4cglmb2kwdwrpp2f7 FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3416 (class 2606 OID 494124)
-- Name: fk_i1i1pcc87u83nq1m8lfibatu3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY coordinatesystem_axis_aud
    ADD CONSTRAINT fk_i1i1pcc87u83nq1m8lfibatu3 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3618 (class 2606 OID 494129)
-- Name: fk_i2amwuqbqr34ksskewtfd2r21; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY registryuser
    ADD CONSTRAINT fk_i2amwuqbqr34ksskewtfd2r21 FOREIGN KEY (uuid) REFERENCES actor(uuid);


--
-- TOC entry 3494 (class 2606 OID 494134)
-- Name: fk_i3disp1qd41wfs4m53c7f2gul; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY geodeticdatum
    ADD CONSTRAINT fk_i3disp1qd41wfs4m53c7f2gul FOREIGN KEY (ellipsoid_uuid) REFERENCES ellipsoid(uuid);


--
-- TOC entry 3501 (class 2606 OID 494139)
-- Name: fk_i43qxs400k6eip1sv3mwyc6dt; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY namingsystemitem
    ADD CONSTRAINT fk_i43qxs400k6eip1sv3mwyc6dt FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3328 (class 2606 OID 494144)
-- Name: fk_i6xyfccd4y3wlwhgwpo4a9rm1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_entry
    ADD CONSTRAINT fk_i6xyfccd4y3wlwhgwpo4a9rm1 FOREIGN KEY (sid) REFERENCES acl_sid(uuid);


--
-- TOC entry 3591 (class 2606 OID 494149)
-- Name: fk_i9qek6vr4srw6tjaemfeuo6rx; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_register_aud
    ADD CONSTRAINT fk_i9qek6vr4srw6tjaemfeuo6rx FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3602 (class 2606 OID 494154)
-- Name: fk_ib3duovmf5qnbl2svfwbc8ajh; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_registeritem
    ADD CONSTRAINT fk_ib3duovmf5qnbl2svfwbc8ajh FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3593 (class 2606 OID 494159)
-- Name: fk_is6x6oac0b0335i09xbykp4d1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_register_itemclasses
    ADD CONSTRAINT fk_is6x6oac0b0335i09xbykp4d1 FOREIGN KEY (registerid) REFERENCES re_register(uuid);


--
-- TOC entry 3404 (class 2606 OID 494164)
-- Name: fk_iwuprlr6libd5ijn2yw6h6i1y; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY concatenatedoperationitem_coordinateoperationitem
    ADD CONSTRAINT fk_iwuprlr6libd5ijn2yw6h6i1y FOREIGN KEY (concatenatedoperationitem_uuid) REFERENCES concatenatedoperationitem(uuid);


--
-- TOC entry 3509 (class 2606 OID 494169)
-- Name: fk_ixm3774apxucco8lw0cch26gp; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationmethoditem_generaloperationparameteritem
    ADD CONSTRAINT fk_ixm3774apxucco8lw0cch26gp FOREIGN KEY (operationmethoditem_uuid) REFERENCES operationmethoditem(uuid);


--
-- TOC entry 3462 (class 2606 OID 494174)
-- Name: fk_j1ett1i9pd2stqag0b2q1e1d2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY engineeringdatum
    ADD CONSTRAINT fk_j1ett1i9pd2stqag0b2q1e1d2 FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3346 (class 2606 OID 494179)
-- Name: fk_j2wqxx25vkb47xhuqkfepl7cu; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY area_aud
    ADD CONSTRAINT fk_j2wqxx25vkb47xhuqkfepl7cu FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3356 (class 2606 OID 494184)
-- Name: fk_j58e605l226j3ewdagt0k46jd; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cartesiancs
    ADD CONSTRAINT fk_j58e605l226j3ewdagt0k46jd FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3357 (class 2606 OID 494189)
-- Name: fk_j5yxgf2565ti2uejvp0d9r0ni; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cartesiancs
    ADD CONSTRAINT fk_j5yxgf2565ti2uejvp0d9r0ni FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3466 (class 2606 OID 494194)
-- Name: fk_j9o7str77fa6vu44ody6j23hw; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ex_extent_ex_geographicextent_aud
    ADD CONSTRAINT fk_j9o7str77fa6vu44ody6j23hw FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3671 (class 2606 OID 494199)
-- Name: fk_jccnul3jj72x9ofc5c5ekc0ln; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY verticalcs
    ADD CONSTRAINT fk_jccnul3jj72x9ofc5c5ekc0ln FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3554 (class 2606 OID 494204)
-- Name: fk_jdbgtdd45h7pbc215syx1gfx2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY proposal
    ADD CONSTRAINT fk_jdbgtdd45h7pbc215syx1gfx2 FOREIGN KEY (sponsor_uuid) REFERENCES re_submittingorganization(uuid);


--
-- TOC entry 3433 (class 2606 OID 494209)
-- Name: fk_jdxe01gtvf4vo6gb1mtxyn39r; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dq_griddeddatapositionalaccuracy
    ADD CONSTRAINT fk_jdxe01gtvf4vo6gb1mtxyn39r FOREIGN KEY (evaluationprocedure_uuid) REFERENCES ci_citation(uuid);


--
-- TOC entry 3603 (class 2606 OID 494214)
-- Name: fk_jep4qkjmid58gxswvtyfqb6db; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_registeritem
    ADD CONSTRAINT fk_jep4qkjmid58gxswvtyfqb6db FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3506 (class 2606 OID 494219)
-- Name: fk_jo5vwka1ycqwwjb4nu3n4nvg; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationmethoditem
    ADD CONSTRAINT fk_jo5vwka1ycqwwjb4nu3n4nvg FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3656 (class 2606 OID 494224)
-- Name: fk_jrokj74h9un8hwt0318qsk3sh; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY transformationitem
    ADD CONSTRAINT fk_jrokj74h9un8hwt0318qsk3sh FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3427 (class 2606 OID 494229)
-- Name: fk_jrt5r8nk9rx1m9shbk51sb4aa; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dq_absoluteexternalpositionalaccuracy
    ADD CONSTRAINT fk_jrt5r8nk9rx1m9shbk51sb4aa FOREIGN KEY (authority_uuid) REFERENCES ci_citation(uuid);


--
-- TOC entry 3422 (class 2606 OID 494234)
-- Name: fk_jtyq4486hu5mfloji81xbemw9; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY crs_aud
    ADD CONSTRAINT fk_jtyq4486hu5mfloji81xbemw9 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3449 (class 2606 OID 494239)
-- Name: fk_juagxtv5tmwqhc445941g9iq4; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ellipsoidalcs
    ADD CONSTRAINT fk_juagxtv5tmwqhc445941g9iq4 FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3590 (class 2606 OID 494244)
-- Name: fk_jvs1cwk4khdml4olyc9lm0jnw; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_register
    ADD CONSTRAINT fk_jvs1cwk4khdml4olyc9lm0jnw FOREIGN KEY (owner_uuid) REFERENCES re_registerowner(uuid);


--
-- TOC entry 3512 (class 2606 OID 494249)
-- Name: fk_k6vckolovbus6tkqs9cm7yrro; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationparametergroupitem
    ADD CONSTRAINT fk_k6vckolovbus6tkqs9cm7yrro FOREIGN KEY (group_uuid) REFERENCES operationparametergroupitem(uuid);


--
-- TOC entry 3456 (class 2606 OID 494254)
-- Name: fk_k8o6757h3yg83ew0o6g1jjtyx; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY engineeringcrs
    ADD CONSTRAINT fk_k8o6757h3yg83ew0o6g1jjtyx FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3470 (class 2606 OID 494259)
-- Name: fk_kc10ru7xejpissqjf9qutkr7d; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ex_geographicboundingbox_aud
    ADD CONSTRAINT fk_kc10ru7xejpissqjf9qutkr7d FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3543 (class 2606 OID 494264)
-- Name: fk_keqg0ae50fqdgq4c217669qaw; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY primemeridian
    ADD CONSTRAINT fk_keqg0ae50fqdgq4c217669qaw FOREIGN KEY (greenwichlongitudeuom_uuid) REFERENCES unitofmeasure(uuid);


--
-- TOC entry 3377 (class 2606 OID 494269)
-- Name: fk_kf9yrteb434wuhn3a54d26kwf; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_contact
    ADD CONSTRAINT fk_kf9yrteb434wuhn3a54d26kwf FOREIGN KEY (onlineresource_uuid) REFERENCES ci_onlineresource(uuid);


--
-- TOC entry 3338 (class 2606 OID 494274)
-- Name: fk_kl5hsnrish3cdy78e8x2q1fg; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY alias
    ADD CONSTRAINT fk_kl5hsnrish3cdy78e8x2q1fg FOREIGN KEY (namingsystem_uuid) REFERENCES namingsystemitem(uuid);


--
-- TOC entry 3572 (class 2606 OID 494279)
-- Name: fk_klk578oq09sjb72jye15ca2ix; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_amendmentinformation
    ADD CONSTRAINT fk_klk578oq09sjb72jye15ca2ix FOREIGN KEY (uuid) REFERENCES re_proposalmanagementinformation(uuid);


--
-- TOC entry 3576 (class 2606 OID 494284)
-- Name: fk_km9wudbk2dfh57ehlxtx2g6xw; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_itemclass
    ADD CONSTRAINT fk_km9wudbk2dfh57ehlxtx2g6xw FOREIGN KEY (technicalstandard_uuid) REFERENCES ci_citation(uuid);


--
-- TOC entry 3557 (class 2606 OID 494289)
-- Name: fk_kygmc0fxxlx70p61fwga4cv35; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY proposaldiscussion
    ADD CONSTRAINT fk_kygmc0fxxlx70p61fwga4cv35 FOREIGN KEY (discussedproposal_uuid) REFERENCES proposal(uuid);


--
-- TOC entry 3617 (class 2606 OID 494294)
-- Name: fk_l00hwm61gshcciho3vhdn0wp9; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY registerrelatedrole_aud
    ADD CONSTRAINT fk_l00hwm61gshcciho3vhdn0wp9 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3672 (class 2606 OID 494299)
-- Name: fk_l062t88bi6owir67wvuev9s73; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY verticalcs
    ADD CONSTRAINT fk_l062t88bi6owir67wvuev9s73 FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3569 (class 2606 OID 494304)
-- Name: fk_l259ecvm68uvdcql29t8pilv0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_alternativeexpression_aud
    ADD CONSTRAINT fk_l259ecvm68uvdcql29t8pilv0 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3438 (class 2606 OID 494309)
-- Name: fk_lblcb40yn1p1n8qfetdr0eu5w; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dq_relativeinternalpositionalaccuracy_aud
    ADD CONSTRAINT fk_lblcb40yn1p1n8qfetdr0eu5w FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3474 (class 2606 OID 494314)
-- Name: fk_lgenbok2lk2tjsetkadv9u0ak; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY formula_aud
    ADD CONSTRAINT fk_lgenbok2lk2tjsetkadv9u0ak FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3641 (class 2606 OID 494319)
-- Name: fk_lhc6241x09od8rqgk9iqpd3lx; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sphericalcs
    ADD CONSTRAINT fk_lhc6241x09od8rqgk9iqpd3lx FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3620 (class 2606 OID 494324)
-- Name: fk_lhj8vb4hpomrdjg53k8hbj64h; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY registryuser_aud
    ADD CONSTRAINT fk_lhj8vb4hpomrdjg53k8hbj64h FOREIGN KEY (uuid, rev) REFERENCES actor_aud(uuid, rev);


--
-- TOC entry 3457 (class 2606 OID 494329)
-- Name: fk_lhqly975yv3re46uw7xl3lqln; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY engineeringcrs_aud
    ADD CONSTRAINT fk_lhqly975yv3re46uw7xl3lqln FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3560 (class 2606 OID 494334)
-- Name: fk_lhte5hvx98vyfyclleqijstfw; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY proposaldiscussion_invitees_aud
    ADD CONSTRAINT fk_lhte5hvx98vyfyclleqijstfw FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3347 (class 2606 OID 494339)
-- Name: fk_limrcebsa641sxwrxghrke49p; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY authorizationtable
    ADD CONSTRAINT fk_limrcebsa641sxwrxghrke49p FOREIGN KEY (actor_uuid) REFERENCES actor(uuid);


--
-- TOC entry 3679 (class 2606 OID 494344)
-- Name: fk_ljdfi3hu4bycx9avvuq4v69; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY verticaldatum
    ADD CONSTRAINT fk_ljdfi3hu4bycx9avvuq4v69 FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3627 (class 2606 OID 494349)
-- Name: fk_lko9riqo7twd4dqjwuk4w53kp; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY simpleproposal
    ADD CONSTRAINT fk_lko9riqo7twd4dqjwuk4w53kp FOREIGN KEY (proposalmanagementinformation_uuid) REFERENCES re_proposalmanagementinformation(uuid);


--
-- TOC entry 3650 (class 2606 OID 494354)
-- Name: fk_lnauf4vf0as3a17srworjylg7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY supersessionpart_aud
    ADD CONSTRAINT fk_lnauf4vf0as3a17srworjylg7 FOREIGN KEY (uuid, rev) REFERENCES simpleproposal_aud(uuid, rev);


--
-- TOC entry 3413 (class 2606 OID 494359)
-- Name: fk_lrjmn53sd79jsd041v4lndcl3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY coordinateoperationitem_dq_positionalaccuracy_aud
    ADD CONSTRAINT fk_lrjmn53sd79jsd041v4lndcl3 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3480 (class 2606 OID 494364)
-- Name: fk_ltl9d16uw0iep8cachcak5209; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY generaloperationparameteritem_aud
    ADD CONSTRAINT fk_ltl9d16uw0iep8cachcak5209 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3583 (class 2606 OID 494369)
-- Name: fk_m3459cj9udctyvs33b2njjsmh; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_proposalmanagementinformation_aud
    ADD CONSTRAINT fk_m3459cj9udctyvs33b2njjsmh FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3559 (class 2606 OID 494374)
-- Name: fk_meu07vuhfpuv8fl6t4i6wofee; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY proposaldiscussion_invitees
    ADD CONSTRAINT fk_meu07vuhfpuv8fl6t4i6wofee FOREIGN KEY (proposaldiscussion_uuid) REFERENCES proposaldiscussion(uuid);


--
-- TOC entry 3381 (class 2606 OID 494379)
-- Name: fk_mgl2mx1d1pfchbwqoacglq9e6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_responsibleparty
    ADD CONSTRAINT fk_mgl2mx1d1pfchbwqoacglq9e6 FOREIGN KEY (contactinfo_uuid) REFERENCES ci_contact(uuid);


--
-- TOC entry 3484 (class 2606 OID 494384)
-- Name: fk_mhe3suihusk87vap1vj8jmdhb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY geodeticcrs
    ADD CONSTRAINT fk_mhe3suihusk87vap1vj8jmdhb FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3341 (class 2606 OID 494389)
-- Name: fk_mi4p52tg5lx3an9fifu1dya1o; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY appeal_aud
    ADD CONSTRAINT fk_mi4p52tg5lx3an9fifu1dya1o FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3481 (class 2606 OID 494394)
-- Name: fk_miknjup26mpvd4168yb8crthg; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY generalparametervalue
    ADD CONSTRAINT fk_miknjup26mpvd4168yb8crthg FOREIGN KEY (group_uuid) REFERENCES operationparametergroupitem(uuid);


--
-- TOC entry 3352 (class 2606 OID 494399)
-- Name: fk_mml34h65pwcra2mirk072b1vm; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY axis
    ADD CONSTRAINT fk_mml34h65pwcra2mirk072b1vm FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3380 (class 2606 OID 494404)
-- Name: fk_mnjhtpej5y5luwe5vn8m4eu2g; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_onlineresource_aud
    ADD CONSTRAINT fk_mnjhtpej5y5luwe5vn8m4eu2g FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3581 (class 2606 OID 494409)
-- Name: fk_mp4qs8urlmd93q138t3vh2u8d; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_locale_aud
    ADD CONSTRAINT fk_mp4qs8urlmd93q138t3vh2u8d FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3638 (class 2606 OID 494414)
-- Name: fk_msol3frrf9bvcabm0ippp0dhe; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY singleoperationitem_generalparametervalue_aud
    ADD CONSTRAINT fk_msol3frrf9bvcabm0ippp0dhe FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3478 (class 2606 OID 494419)
-- Name: fk_mu6kqm56nh4h16rmgwcdvblrd; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY generaloperationparameteritem
    ADD CONSTRAINT fk_mu6kqm56nh4h16rmgwcdvblrd FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3513 (class 2606 OID 494424)
-- Name: fk_muhv8jec1n87ayp2n7n5xehf; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationparametergroupitem
    ADD CONSTRAINT fk_muhv8jec1n87ayp2n7n5xehf FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3633 (class 2606 OID 494429)
-- Name: fk_mwpdwffrm7parffsh12992w57; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY singleoperationitem
    ADD CONSTRAINT fk_mwpdwffrm7parffsh12992w57 FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3534 (class 2606 OID 494434)
-- Name: fk_mxhaymuw5a9go7uuha1y7q3v3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY passthroughoperationitem
    ADD CONSTRAINT fk_mxhaymuw5a9go7uuha1y7q3v3 FOREIGN KEY (domainofvalidity_uuid) REFERENCES ex_extent(uuid);


--
-- TOC entry 3450 (class 2606 OID 494439)
-- Name: fk_mykwfo4v7ijx0294g3q5s9tus; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ellipsoidalcs
    ADD CONSTRAINT fk_mykwfo4v7ijx0294g3q5s9tus FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3372 (class 2606 OID 494444)
-- Name: fk_mylxnld177xwc390imihqyih; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_citation_date
    ADD CONSTRAINT fk_mylxnld177xwc390imihqyih FOREIGN KEY (ci_citation_uuid) REFERENCES ci_citation(uuid);


--
-- TOC entry 3386 (class 2606 OID 494449)
-- Name: fk_n9bhcyhv7vbsplak2q5eh0ck0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_telephone_voice
    ADD CONSTRAINT fk_n9bhcyhv7vbsplak2q5eh0ck0 FOREIGN KEY (ci_telephone_uuid) REFERENCES ci_telephone(uuid);


--
-- TOC entry 3373 (class 2606 OID 494454)
-- Name: fk_natxmvhmf8on52hyoa1gn0icr; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_citation_date_aud
    ADD CONSTRAINT fk_natxmvhmf8on52hyoa1gn0icr FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3514 (class 2606 OID 494459)
-- Name: fk_nj75g2448osbmwpmufxwd17ay; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationparametergroupitem
    ADD CONSTRAINT fk_nj75g2448osbmwpmufxwd17ay FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3535 (class 2606 OID 494464)
-- Name: fk_njwshycs12rp6lg6pgode8oco; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY passthroughoperationitem
    ADD CONSTRAINT fk_njwshycs12rp6lg6pgode8oco FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3424 (class 2606 OID 494469)
-- Name: fk_nlm03i9ausudedoigp70tdyof; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY delegation
    ADD CONSTRAINT fk_nlm03i9ausudedoigp70tdyof FOREIGN KEY (actor_uuid) REFERENCES actor(uuid);


--
-- TOC entry 3396 (class 2606 OID 494474)
-- Name: fk_nlp7nx93udj8gmylsbwv96v2n; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY compoundcrs_singlecrs
    ADD CONSTRAINT fk_nlp7nx93udj8gmylsbwv96v2n FOREIGN KEY (compoundcrs_uuid) REFERENCES compoundcrs(uuid);


--
-- TOC entry 3580 (class 2606 OID 494479)
-- Name: fk_nns45lukvc8sj0tiv6jsiwqf0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_locale
    ADD CONSTRAINT fk_nns45lukvc8sj0tiv6jsiwqf0 FOREIGN KEY (citation_uuid) REFERENCES ci_citation(uuid);


--
-- TOC entry 3415 (class 2606 OID 494484)
-- Name: fk_nq0ivj0wf04e55879mvftm8gv; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY coordinatesystem_axis
    ADD CONSTRAINT fk_nq0ivj0wf04e55879mvftm8gv FOREIGN KEY (axes_uuid) REFERENCES axis(uuid);


--
-- TOC entry 3382 (class 2606 OID 494489)
-- Name: fk_nucsideajthvmlqfxdtitqggl; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_responsibleparty_aud
    ADD CONSTRAINT fk_nucsideajthvmlqfxdtitqggl FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3485 (class 2606 OID 494494)
-- Name: fk_nx6pglsqqq47jma4mciut4gqc; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY geodeticcrs
    ADD CONSTRAINT fk_nx6pglsqqq47jma4mciut4gqc FOREIGN KEY (domainofvalidity_uuid) REFERENCES ex_extent(uuid);


--
-- TOC entry 3332 (class 2606 OID 494499)
-- Name: fk_nxv5we2ion9fwedbkge7syoc3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_object_identity
    ADD CONSTRAINT fk_nxv5we2ion9fwedbkge7syoc3 FOREIGN KEY (owner_sid) REFERENCES acl_sid(uuid);


--
-- TOC entry 3502 (class 2606 OID 494504)
-- Name: fk_o0dd09gtnc0ln6pp1actq40d2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY namingsystemitem_aud
    ADD CONSTRAINT fk_o0dd09gtnc0ln6pp1actq40d2 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3544 (class 2606 OID 494509)
-- Name: fk_o0j9fglew8hngi9hjoy45jxgi; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY primemeridian
    ADD CONSTRAINT fk_o0j9fglew8hngi9hjoy45jxgi FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3628 (class 2606 OID 494514)
-- Name: fk_ob2p61dyjely5o0g3vvm9uoe6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY simpleproposal
    ADD CONSTRAINT fk_ob2p61dyjely5o0g3vvm9uoe6 FOREIGN KEY (uuid) REFERENCES proposal(uuid);


--
-- TOC entry 3334 (class 2606 OID 494519)
-- Name: fk_og3qrad0imul4c9h9pi44083s; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_sid_aud
    ADD CONSTRAINT fk_og3qrad0imul4c9h9pi44083s FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3665 (class 2606 OID 494524)
-- Name: fk_oj7hy4tosntspixju0bd3xfvt; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY verticalcrs
    ADD CONSTRAINT fk_oj7hy4tosntspixju0bd3xfvt FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3673 (class 2606 OID 494529)
-- Name: fk_ojk3i03ygj5ixh9qn17lya9wl; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY verticalcs
    ADD CONSTRAINT fk_ojk3i03ygj5ixh9qn17lya9wl FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3345 (class 2606 OID 494534)
-- Name: fk_ojr9xca0kplhs1al8fcc0xw7l; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY area
    ADD CONSTRAINT fk_ojr9xca0kplhs1al8fcc0xw7l FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3387 (class 2606 OID 494539)
-- Name: fk_om3pwhhnxigl4uvd1vhh7183b; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_telephone_voice_aud
    ADD CONSTRAINT fk_om3pwhhnxigl4uvd1vhh7183b FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3649 (class 2606 OID 494544)
-- Name: fk_oqw3ii0q1xjdfw1v4qpsqik4i; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY supersessionpart
    ADD CONSTRAINT fk_oqw3ii0q1xjdfw1v4qpsqik4i FOREIGN KEY (uuid) REFERENCES simpleproposal(uuid);


--
-- TOC entry 3610 (class 2606 OID 494549)
-- Name: fk_or23buyawsh514fkdy9aj17ba; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_registermanager
    ADD CONSTRAINT fk_or23buyawsh514fkdy9aj17ba FOREIGN KEY (contact_uuid) REFERENCES ci_responsibleparty(uuid);


--
-- TOC entry 3604 (class 2606 OID 494554)
-- Name: fk_otq21eoi6fn6hu8an7y3uajo9; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_registeritem
    ADD CONSTRAINT fk_otq21eoi6fn6hu8an7y3uajo9 FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3428 (class 2606 OID 494559)
-- Name: fk_p01wpeapjy9idrr69fl004gud; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dq_absoluteexternalpositionalaccuracy
    ADD CONSTRAINT fk_p01wpeapjy9idrr69fl004gud FOREIGN KEY (result_uuid) REFERENCES result(uuid);


--
-- TOC entry 3383 (class 2606 OID 494564)
-- Name: fk_p0lyfkfnhbjpyk432x5ri8v60; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_telephone_aud
    ADD CONSTRAINT fk_p0lyfkfnhbjpyk432x5ri8v60 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3402 (class 2606 OID 494569)
-- Name: fk_p2fm6v9d82lpxkot4dnf0k0op; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY concatenatedoperationitem
    ADD CONSTRAINT fk_p2fm6v9d82lpxkot4dnf0k0op FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3563 (class 2606 OID 494574)
-- Name: fk_p2jte3fks6db0otib7t17cpv2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY proposalnote
    ADD CONSTRAINT fk_p2jte3fks6db0otib7t17cpv2 FOREIGN KEY (proposal_uuid) REFERENCES proposal(uuid);


--
-- TOC entry 3412 (class 2606 OID 494579)
-- Name: fk_p5p2stxfio4lu56v126o6suxi; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY conversionitem_aud
    ADD CONSTRAINT fk_p5p2stxfio4lu56v126o6suxi FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3666 (class 2606 OID 494584)
-- Name: fk_pa0myatsjqa39e8wq0ejy648v; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY verticalcrs
    ADD CONSTRAINT fk_pa0myatsjqa39e8wq0ejy648v FOREIGN KEY (domainofvalidity_uuid) REFERENCES ex_extent(uuid);


--
-- TOC entry 3619 (class 2606 OID 494589)
-- Name: fk_pciuxlu8hgj5ii84ia96vfx7k; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY registryuser
    ADD CONSTRAINT fk_pciuxlu8hgj5ii84ia96vfx7k FOREIGN KEY (organization_uuid) REFERENCES organization(uuid);


--
-- TOC entry 3405 (class 2606 OID 494594)
-- Name: fk_pd83ygwucojbcl5jeuekp9t9k; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY concatenatedoperationitem_coordinateoperationitem_aud
    ADD CONSTRAINT fk_pd83ygwucojbcl5jeuekp9t9k FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3642 (class 2606 OID 494599)
-- Name: fk_pfmalaf35s0a6ih3l0anbfd8g; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sphericalcs
    ADD CONSTRAINT fk_pfmalaf35s0a6ih3l0anbfd8g FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3634 (class 2606 OID 494604)
-- Name: fk_pn5nu4od20ency5gwu6vt9kww; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY singleoperationitem
    ADD CONSTRAINT fk_pn5nu4od20ency5gwu6vt9kww FOREIGN KEY (method_uuid) REFERENCES operationmethoditem(uuid);


--
-- TOC entry 3393 (class 2606 OID 494609)
-- Name: fk_pod3fgco2m1eoiwggmx0fi3eb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY compoundcrs
    ADD CONSTRAINT fk_pod3fgco2m1eoiwggmx0fi3eb FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3525 (class 2606 OID 494614)
-- Name: fk_prvxb64sab0ajn8bxmeiqt2pb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationparametervalue_parametervalue_aud
    ADD CONSTRAINT fk_prvxb64sab0ajn8bxmeiqt2pb FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3680 (class 2606 OID 494619)
-- Name: fk_q09jf3jxf5k7kmy4egxowu7q1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY verticaldatum_aud
    ADD CONSTRAINT fk_q09jf3jxf5k7kmy4egxowu7q1 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3486 (class 2606 OID 494624)
-- Name: fk_q5v92oulqvphpf73ayoli8nm3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY geodeticcrs
    ADD CONSTRAINT fk_q5v92oulqvphpf73ayoli8nm3 FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3434 (class 2606 OID 494629)
-- Name: fk_qaa5bp9t4p7abrhsemi7amlsi; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dq_griddeddatapositionalaccuracy_aud
    ADD CONSTRAINT fk_qaa5bp9t4p7abrhsemi7amlsi FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3647 (class 2606 OID 494634)
-- Name: fk_qbqixjov29ohk6lcnayt3jixe; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY supersession_supersedingitems
    ADD CONSTRAINT fk_qbqixjov29ohk6lcnayt3jixe FOREIGN KEY (supersessionid) REFERENCES supersession(uuid);


--
-- TOC entry 3564 (class 2606 OID 494639)
-- Name: fk_qcmo24g6jt48nnad6rfudj6ov; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY proposalnote
    ADD CONSTRAINT fk_qcmo24g6jt48nnad6rfudj6ov FOREIGN KEY (author_uuid) REFERENCES registryuser(uuid);


--
-- TOC entry 3353 (class 2606 OID 494644)
-- Name: fk_qf5ph5rkimjtp4hv7feac2isk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY axis
    ADD CONSTRAINT fk_qf5ph5rkimjtp4hv7feac2isk FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3419 (class 2606 OID 494649)
-- Name: fk_qgc7r8qw1s7rhjjoi4ivqbcej; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY crs
    ADD CONSTRAINT fk_qgc7r8qw1s7rhjjoi4ivqbcej FOREIGN KEY (domainofvalidity_uuid) REFERENCES ex_extent(uuid);


--
-- TOC entry 3657 (class 2606 OID 494654)
-- Name: fk_qq3iow897txhrdymjjqaogixb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY transformationitem_aud
    ADD CONSTRAINT fk_qq3iow897txhrdymjjqaogixb FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3667 (class 2606 OID 494659)
-- Name: fk_qsfblee3xmx88vfk9qbbhffe5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY verticalcrs
    ADD CONSTRAINT fk_qsfblee3xmx88vfk9qbbhffe5 FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3612 (class 2606 OID 494664)
-- Name: fk_qt5tpyt5u32ut4c18jn5fafb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_registerowner
    ADD CONSTRAINT fk_qt5tpyt5u32ut4c18jn5fafb FOREIGN KEY (contact_uuid) REFERENCES ci_responsibleparty(uuid);


--
-- TOC entry 3479 (class 2606 OID 494669)
-- Name: fk_qtsrph9rtb6sm8pigw84mlwsq; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY generaloperationparameteritem
    ADD CONSTRAINT fk_qtsrph9rtb6sm8pigw84mlwsq FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3394 (class 2606 OID 494674)
-- Name: fk_quoc77pnmjtppdd1ytdxn1ll8; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY compoundcrs
    ADD CONSTRAINT fk_quoc77pnmjtppdd1ytdxn1ll8 FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3605 (class 2606 OID 494679)
-- Name: fk_qvmrximcvu28lnml62t467stf; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_registeritem_aud
    ADD CONSTRAINT fk_qvmrximcvu28lnml62t467stf FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3495 (class 2606 OID 494684)
-- Name: fk_qvnfv0ksabxyd9gcsp8lfoyau; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY geodeticdatum
    ADD CONSTRAINT fk_qvnfv0ksabxyd9gcsp8lfoyau FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3545 (class 2606 OID 494689)
-- Name: fk_r8khohpekxqthqt0wfv1psb3h; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY primemeridian
    ADD CONSTRAINT fk_r8khohpekxqthqt0wfv1psb3h FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3515 (class 2606 OID 494694)
-- Name: fk_rbe4tud8squdfgv0cmaw79mqk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationparametergroupitem
    ADD CONSTRAINT fk_rbe4tud8squdfgv0cmaw79mqk FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3582 (class 2606 OID 494699)
-- Name: fk_rcfejk1nquuca10n53wpidx2d; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_proposalmanagementinformation
    ADD CONSTRAINT fk_rcfejk1nquuca10n53wpidx2d FOREIGN KEY (sponsor_uuid) REFERENCES re_submittingorganization(uuid);


--
-- TOC entry 3531 (class 2606 OID 494704)
-- Name: fk_rdr3tlrxpnk16vw9l3veyiw6d; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY organizationrelatedrole
    ADD CONSTRAINT fk_rdr3tlrxpnk16vw9l3veyiw6d FOREIGN KEY (organization_uuid) REFERENCES organization(uuid);


--
-- TOC entry 3599 (class 2606 OID 494709)
-- Name: fk_rl3yiuls3vjyp5lf0pln58njq; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_register_submitters
    ADD CONSTRAINT fk_rl3yiuls3vjyp5lf0pln58njq FOREIGN KEY (submitterid) REFERENCES re_submittingorganization(uuid);


--
-- TOC entry 3496 (class 2606 OID 494714)
-- Name: fk_rn12pmeqw08gjfg3lhtgbt2xc; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY geodeticdatum_aud
    ADD CONSTRAINT fk_rn12pmeqw08gjfg3lhtgbt2xc FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3507 (class 2606 OID 494719)
-- Name: fk_rnitb0ep4qbxrjki4t3lnej1y; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationmethoditem
    ADD CONSTRAINT fk_rnitb0ep4qbxrjki4t3lnej1y FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3637 (class 2606 OID 494724)
-- Name: fk_rplpwi4ecd5p09f9jv4dcqa53; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY singleoperationitem_generalparametervalue
    ADD CONSTRAINT fk_rplpwi4ecd5p09f9jv4dcqa53 FOREIGN KEY (parametervalue_uuid) REFERENCES generalparametervalue(uuid);


--
-- TOC entry 3463 (class 2606 OID 494729)
-- Name: fk_rueg1vx4ubc4e47ogg5jkp0hw; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY engineeringdatum_aud
    ADD CONSTRAINT fk_rueg1vx4ubc4e47ogg5jkp0hw FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3579 (class 2606 OID 494734)
-- Name: fk_ruwoh94j9fobe1m1mn15lbk59; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_itemclass_aud
    ADD CONSTRAINT fk_ruwoh94j9fobe1m1mn15lbk59 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3574 (class 2606 OID 494739)
-- Name: fk_s16y9amubytqmb6fuhlilju56; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_clarificationinformation
    ADD CONSTRAINT fk_s16y9amubytqmb6fuhlilju56 FOREIGN KEY (uuid) REFERENCES re_proposalmanagementinformation(uuid);


--
-- TOC entry 3607 (class 2606 OID 494744)
-- Name: fk_s5ikuot891kvxvqmryn2408cw; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_registeritem_re_alternativeexpression
    ADD CONSTRAINT fk_s5ikuot891kvxvqmryn2408cw FOREIGN KEY (alternativeexpressions_uuid) REFERENCES re_alternativeexpression(uuid);


--
-- TOC entry 3661 (class 2606 OID 494749)
-- Name: fk_sb6cm02a016jd2vbjqkfii7g6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY unitofmeasure
    ADD CONSTRAINT fk_sb6cm02a016jd2vbjqkfii7g6 FOREIGN KEY (specificationlineage_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3626 (class 2606 OID 494754)
-- Name: fk_sbvk2omkjkpq7boju4mgpqwgn; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY role_aud
    ADD CONSTRAINT fk_sbvk2omkjkpq7boju4mgpqwgn FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3577 (class 2606 OID 494759)
-- Name: fk_sby8sanat5ocj2274ng6eow7j; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_itemclass_alternativenames
    ADD CONSTRAINT fk_sby8sanat5ocj2274ng6eow7j FOREIGN KEY (re_itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3662 (class 2606 OID 494764)
-- Name: fk_seuo2tlbbfjeow8lf68hq8v0e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY unitofmeasure
    ADD CONSTRAINT fk_seuo2tlbbfjeow8lf68hq8v0e FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3524 (class 2606 OID 494769)
-- Name: fk_sfjg7kam2y1o87ul7ndw65lc3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationparametervalue_parametervalue
    ADD CONSTRAINT fk_sfjg7kam2y1o87ul7ndw65lc3 FOREIGN KEY (uom_uuid) REFERENCES unitofmeasure(uuid);


--
-- TOC entry 3536 (class 2606 OID 494774)
-- Name: fk_sg8qggnxrvevkdcaqk0kg64yh; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY passthroughoperationitem
    ADD CONSTRAINT fk_sg8qggnxrvevkdcaqk0kg64yh FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3562 (class 2606 OID 494779)
-- Name: fk_sh5s3l5l2eq85v3epn41t0dh7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY proposalgroup_aud
    ADD CONSTRAINT fk_sh5s3l5l2eq85v3epn41t0dh7 FOREIGN KEY (uuid, rev) REFERENCES proposal_aud(uuid, rev);


--
-- TOC entry 3532 (class 2606 OID 494784)
-- Name: fk_skpn04oj2dd5c1lsygn7ns798; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY organizationrelatedrole_aud
    ADD CONSTRAINT fk_skpn04oj2dd5c1lsygn7ns798 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3537 (class 2606 OID 494789)
-- Name: fk_sm85b7cicjiviydhn5n0eqjui; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY passthroughoperationitem
    ADD CONSTRAINT fk_sm85b7cicjiviydhn5n0eqjui FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3668 (class 2606 OID 494794)
-- Name: fk_smhomhcwnf0tvh0cjmp45nu7d; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY verticalcrs
    ADD CONSTRAINT fk_smhomhcwnf0tvh0cjmp45nu7d FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3378 (class 2606 OID 494799)
-- Name: fk_sr6m5xbl4vt74998s75opywnu; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_contact
    ADD CONSTRAINT fk_sr6m5xbl4vt74998s75opywnu FOREIGN KEY (address_uuid) REFERENCES ci_address(uuid);


--
-- TOC entry 3326 (class 2606 OID 494804)
-- Name: fk_sukm2sxe3hvvjr0viq5bwtofg; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_class_aud
    ADD CONSTRAINT fk_sukm2sxe3hvvjr0viq5bwtofg FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3635 (class 2606 OID 494809)
-- Name: fk_t0urqp12c9taj5608ikq9t5tr; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY singleoperationitem
    ADD CONSTRAINT fk_t0urqp12c9taj5608ikq9t5tr FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3389 (class 2606 OID 494814)
-- Name: fk_t2k93o8y0wy2yxipqjptkxin6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY clarification_aud
    ADD CONSTRAINT fk_t2k93o8y0wy2yxipqjptkxin6 FOREIGN KEY (uuid, rev) REFERENCES simpleproposal_aud(uuid, rev);


--
-- TOC entry 3521 (class 2606 OID 494819)
-- Name: fk_t2ukcvrnhwy41hi53htp7c57c; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY operationparameteritem
    ADD CONSTRAINT fk_t2ukcvrnhwy41hi53htp7c57c FOREIGN KEY (itemclass_uuid) REFERENCES re_itemclass(uuid);


--
-- TOC entry 3551 (class 2606 OID 494824)
-- Name: fk_t6iypaq96li47enlsti0j1pui; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY projectedcrs
    ADD CONSTRAINT fk_t6iypaq96li47enlsti0j1pui FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3365 (class 2606 OID 494829)
-- Name: fk_t72vew43pqle7ngf1vd09vf95; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ci_citation
    ADD CONSTRAINT fk_t72vew43pqle7ngf1vd09vf95 FOREIGN KEY (authority_uuid) REFERENCES ci_citation(uuid);


--
-- TOC entry 3420 (class 2606 OID 494834)
-- Name: fk_tagf6ly9cpavv7a3o7mkqt6dr; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY crs
    ADD CONSTRAINT fk_tagf6ly9cpavv7a3o7mkqt6dr FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3468 (class 2606 OID 494839)
-- Name: fk_tixiitaanrx3e36jyshvntg4u; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ex_extent_ex_verticalextent
    ADD CONSTRAINT fk_tixiitaanrx3e36jyshvntg4u FOREIGN KEY (ex_extent_uuid) REFERENCES ex_extent(uuid);


--
-- TOC entry 3611 (class 2606 OID 494844)
-- Name: fk_tixuojyptlijp2gt93e5o1tgb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY re_registermanager_aud
    ADD CONSTRAINT fk_tixuojyptlijp2gt93e5o1tgb FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3358 (class 2606 OID 494849)
-- Name: fk_tqp4ae731uqjdwyp8los6l3sg; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY cartesiancs
    ADD CONSTRAINT fk_tqp4ae731uqjdwyp8los6l3sg FOREIGN KEY (register_uuid) REFERENCES re_register(uuid);


--
-- TOC entry 3487 (class 2606 OID 494854)
-- Name: fk_trxe8axwov4i4wrfwr7llxnyr; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY geodeticcrs
    ADD CONSTRAINT fk_trxe8axwov4i4wrfwr7llxnyr FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3421 (class 2606 OID 494859)
-- Name: fk_vvkiu2hmtodpxor7mnv9p0w6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY crs
    ADD CONSTRAINT fk_vvkiu2hmtodpxor7mnv9p0w6 FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3411 (class 2606 OID 494864)
-- Name: fk_yfkrmbfgdr13vbc5uj9nfglh; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY conversionitem
    ADD CONSTRAINT fk_yfkrmbfgdr13vbc5uj9nfglh FOREIGN KEY (specificationsource_uuid) REFERENCES re_reference(uuid);


--
-- TOC entry 3794 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: -
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-02-10 14:52:06

--
-- PostgreSQL database dump complete
--

