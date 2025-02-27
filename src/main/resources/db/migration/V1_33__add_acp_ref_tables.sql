CREATE TABLE course
(
    course_id                      uuid                     NOT NULL,
    name                           text                     NOT NULL,
    description                    text,
    alternate_name                 text,
    identifier                     text    DEFAULT ''::text NOT NULL,
    withdrawn                      boolean DEFAULT false    NOT NULL,
    audience                       text,
    audience_colour                text,
    list_display_name              text,
    version                        bigint  DEFAULT 0        NOT NULL,
    display_on_programme_directory boolean DEFAULT false    NOT NULL,
    intensity                      text,
    CONSTRAINT course_identifier_unique UNIQUE (identifier),
    CONSTRAINT course_pkey PRIMARY KEY (course_id)
);

COMMENT ON TABLE course IS 'Contains the ACP equivalent data of our intervention catalogue';
COMMENT ON COLUMN course.course_id IS 'Unique identifier for the intervention';
COMMENT ON COLUMN course.name IS 'The name of the intervention';
COMMENT ON COLUMN course.description IS 'The description of the intervention';
COMMENT ON COLUMN course.alternate_name IS 'The alternative name of the intervention';
COMMENT ON COLUMN course.identifier IS 'The programme code of the intervention';
COMMENT ON COLUMN course.withdrawn IS 'Has this intervention been withdrawn';
COMMENT ON COLUMN course.audience IS 'Which offences is this intervention aimed at';
COMMENT ON COLUMN course.audience_colour IS 'The colour of the offences this intervention is aimed at';
COMMENT ON COLUMN course.list_display_name IS 'The display name for the list';
COMMENT ON COLUMN course.version IS 'The version of the intervention';
COMMENT ON COLUMN course.display_on_programme_directory IS 'Should this be displayed on the catalogue';
COMMENT ON COLUMN course.intensity IS 'The intensity of the intervention';

INSERT INTO metadata (table_name, column_name, sensitive, domain_data)
VALUES ('course', 'course_id', false, true),
       ('course', 'name', false, true),
       ('course', 'description', false, true),
       ('course', 'alternate_name', false, true),
       ('course', 'identifier', false, true),
       ('course', 'withdrawn', false, true),
       ('course', 'audience', false, true),
       ('course', 'audience_colour', false, true),
       ('course', 'list_display_name', false, true),
       ('course', 'version', false, true),
       ('course', 'display_on_programme_directory', false, true),
       ('course', 'intensity', false, true);

CREATE TABLE prerequisite
(
    course_id   uuid NOT NULL,
    name        text NOT NULL,
    description text NOT NULL,
    CONSTRAINT prerequisite_pkey PRIMARY KEY (course_id, name, description),
    CONSTRAINT prerequisite_course_id_fkey FOREIGN KEY (course_id) REFERENCES course (course_id)
);

COMMENT ON TABLE prerequisite IS 'Contains the criteria required to be eligible for an intervention';
COMMENT ON COLUMN prerequisite.course_id IS 'The intervention that this prerequisite references';
COMMENT ON COLUMN prerequisite.name IS 'Name of the criteria';
COMMENT ON COLUMN prerequisite.description IS 'Description of the criteria required for this intervention';

INSERT INTO metadata (table_name, column_name, sensitive, domain_data)
VALUES ('prerequisite', 'course_id', false, true),
       ('prerequisite', 'name', false, true),
       ('prerequisite', 'description', false, true);

CREATE TABLE organisation
(
    organisation_id uuid NOT NULL,
    code            text NOT NULL,
    name            text NOT NULL,
    gender          text,
    CONSTRAINT organisation_code UNIQUE (code),
    CONSTRAINT organisation_pkey PRIMARY KEY (organisation_id)
);

COMMENT ON TABLE organisation IS 'Contains Details about organisations which deliver interventions';
COMMENT ON COLUMN organisation.organisation_id IS 'The unique identifier for an organisation';
COMMENT ON COLUMN organisation.code IS 'The code of the organisation';
COMMENT ON COLUMN organisation.name IS 'The name of the organisation';
COMMENT ON COLUMN organisation.gender IS 'The gender(s) that the organisation caters for';

INSERT INTO metadata (table_name, column_name, sensitive, domain_data)
VALUES ('organisation', 'organisation_id', false, true),
       ('organisation', 'code', false, true),
       ('organisation', 'name', false, true),
       ('organisation', 'gender', false, true);

CREATE INDEX idx_organisation_code ON organisation USING btree (code);

CREATE TABLE offering
(
    offering_id             uuid                  NOT NULL,
    course_id               uuid                  NOT NULL,
    organisation_id         text                  NOT NULL,
    contact_email           text                  NOT NULL,
    secondary_contact_email text,
    withdrawn               boolean DEFAULT false NOT NULL,
    referable               boolean DEFAULT true  NOT NULL,
    version                 bigint  DEFAULT 0     NOT NULL,
    CONSTRAINT offering_business_key_unique UNIQUE (course_id, organisation_id),
    CONSTRAINT offering_pk PRIMARY KEY (offering_id),
    CONSTRAINT offering_course_fk FOREIGN KEY (course_id) REFERENCES course (course_id)
);

COMMENT ON TABLE offering IS 'Contains data on the various courses an organisation offers and contact details';
COMMENT ON COLUMN offering.offering_id IS 'The unique identifier for an offering';
COMMENT ON COLUMN offering.course_id IS 'The referenced course_id of the course that is being offered';
COMMENT ON COLUMN offering.organisation_id IS 'The referenced organisation_id of where the offering can be delivered';
COMMENT ON COLUMN offering.contact_email IS 'Primary contact email';
COMMENT ON COLUMN offering.secondary_contact_email IS 'Secondary contact email';
COMMENT ON COLUMN offering.withdrawn IS 'Is this offering available to be enrolled in';
COMMENT ON COLUMN offering.referable IS 'Is this offering open for referrals';
COMMENT ON COLUMN offering.version IS 'Version of the offering';

INSERT INTO metadata (table_name, column_name, sensitive, domain_data)
VALUES ('offering', 'offering_id', false, true),
       ('offering', 'course_id', false, true),
       ('offering', 'organisation_id', false, true),
       ('offering', 'contact_email', true, true),
       ('offering', 'secondary_contact_email', true, true),
       ('offering', 'withdrawn', false, true),
       ('offering', 'referable', false, true),
       ('offering', 'version', false, true);

CREATE TABLE intervention_catalogue_to_course_map
(
    intervention_catalogue_id UUID NOT NULL,
    course_id                 UUID NOT NULL,
    CONSTRAINT intervention_catalogue_to_course_map_pkey PRIMARY KEY (intervention_catalogue_id, course_id),
    CONSTRAINT fk__icc__intervention_catalogue_id FOREIGN KEY (intervention_catalogue_id) REFERENCES intervention_catalogue (id),
    CONSTRAINT fk__icm__course_id FOREIGN KEY (course_id) REFERENCES course (course_id)
);

COMMENT ON TABLE intervention_catalogue_to_course_map IS 'Contains the mapping between the Intervention Catalogue (Find & Refer) and Course (ACP) tables';
COMMENT ON COLUMN intervention_catalogue_to_course_map.intervention_catalogue_id IS 'The unique reference to an intervention in the catalogue table';
COMMENT ON COLUMN intervention_catalogue_to_course_map.course_id IS 'The unique reference to the intervention in the course table';

INSERT INTO metadata (table_name, column_name, sensitive, domain_data)
VALUES ('intervention_catalogue_to_course_map', 'intervention_catalogue_id', false, true),
       ('intervention_catalogue_to_course_map', 'course_id', false, true);