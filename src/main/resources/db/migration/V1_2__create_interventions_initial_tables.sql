CREATE TYPE intervention_type AS ENUM ('SI','ACP','CRS');
CREATE TYPE setting_type AS ENUM ('COMMUNITY','CUSTODY');
CREATE TYPE rosh_levels AS ENUM ('LOW','MEDIUM','HIGH','VERY_HIGH');
CREATE TYPE complexities AS ENUM ('LOW','MEDIUM','HIGH');
CREATE TABLE auth_user (
	id text NOT NULL,
	auth_source text NOT NULL,
	user_name text NOT NULL DEFAULT 'unknown'::text,
	deleted bool NULL,
	CONSTRAINT auth_user_pkey PRIMARY KEY (id)
);
create table nps_region (
              id varchar(1) not null,
              name text,
              primary key (id)
);
create table pcc_region (
              id text not null,
              name text,
              nps_region varchar(1) not null,
              primary key (id),
              constraint fk__prr__nps_region foreign key (nps_region) references nps_region
);
CREATE TABLE contract_type (
	id uuid NOT NULL,
	"name" text NOT NULL,
	code varchar(10) NOT NULL,
	CONSTRAINT contract_type_pkey PRIMARY KEY (id)
);
CREATE TABLE service_category (
	id uuid NOT NULL,
	created timestamp with time zone NULL,
	"name" text NOT NULL,
	CONSTRAINT service_category_pkey PRIMARY KEY (id)
);
create table intervention_catalogue (
    id uuid not null,
    name text not null,
    int_type intervention_type not null,
    short_decription text not null,
    long_decription text not null,
    topic text not null,
    session_detail text not null,
    commencement_date date not null,
    termination_date date not null,
    created timestamp with time zone not null,
    created_by text not null,
    last_modified timestamp with time zone,
    last_modified_by text,
    primary key (id),
    constraint fk__ic__auth_user_id foreign key (created_by) references auth_user,
    constraint fk__ic__auth_user_id_mod foreign key (last_modified_by) references auth_user
);
create table delivery_method (
    id uuid not null,
    intervention_id uuid not null,
    delivery_method_description text,
    primary key (id),
    constraint fk__dm__intervention_id foreign key (intervention_id) references intervention_catalogue
);
create table delivery_method_setting (
    id uuid not null,
    delivery_method_id uuid not null,
    setting setting_type not null,
    primary key (id),
    constraint fk__dms__delivery_method_id foreign key (delivery_method_id) references delivery_method
);
create table criminogenic_need_ref (
    id uuid not null,
    name text not null,
    primary key (id)
);
create table criminogenic_need (
    id uuid not null,
    need_id uuid not null,
    intervention_id uuid not null,
    primary key (id),
    constraint fk__cn__criminogenic_need_ref foreign key (need_id) references criminogenic_need_ref,
    constraint fk__cn__intervention_id foreign key (intervention_id) references intervention_catalogue
);
create table special_educational_need (
    id uuid not null,
    intervention_id uuid not null,
    literacy_level_guide text,
    learning_disability_catered_for boolean not null default false,
    equivalent_non_ldc_programme_guide text,
    primary key (id),
    constraint fk__sen__intervention_id foreign key (intervention_id) references intervention_catalogue
);
create table possible_outcome (
    id uuid not null,
    intervention_id uuid,
    outcome text not null,
    primary key (id),
    constraint fk__po__intervention_id foreign key (intervention_id) references intervention_catalogue
);
create table personal_eligibility (
    id uuid not null,
    intervention_id uuid,
    min_age integer default 18,
    max_age integer default 120,
    males boolean not null,
    females boolean not null,
    primary key (id),
    constraint fk__pe__intervention_id foreign key (intervention_id) references intervention_catalogue
);
create unique index unq__fk__pe__intervention_id
          on personal_eligibility (intervention_id);
create table ethnicity_ref (
    id uuid not null,
    broad_group text not null,
    name text not null,
    primary key (id)
);
create table eligibile_ethnicity (
    id uuid not null,
    ethnicity_id uuid,
    personal_eligibility_id uuid,
    primary key (id),
    constraint fk__ee__personal_eligibility_id foreign key (personal_eligibility_id) references personal_eligibility,
    constraint fk__ee__ethnicity_id foreign key (ethnicity_id) references ethnicity_ref
);
create table offence_type_ref (
    id uuid not null,
    name text not null,
    primary key (id)
);
create table eligibile_offence (
    id uuid not null,
    offence_type_id uuid,
    intervention_id uuid,
    victim_type text,
    motivation text,
    primary key (id),
    constraint fk__eo__offence_type_id foreign key (offence_type_id) references offence_type_ref,
    constraint fk__eo__intervention_id foreign key (intervention_id) references intervention_catalogue
);
create table enabling_intervention (
    id uuid not null,
    intervention_id uuid,
    enabling_intervention_detail text,
    primary key (id),
    constraint fk__ei__intervention_id foreign key (intervention_id) references intervention_catalogue
);
create table delivery_location (
    id uuid not null,
    intervention_id uuid,
    provider_name text not null,
    contact text not null,
    pdu_estabishments text not null, -- need R&M PDU
    primary key (id),
    constraint fk__dl__intervention_id foreign key (intervention_id) references intervention_catalogue
);
create table risk_consideration (
    id uuid not null,
    intervention_id uuid not null,
    cn_score_guide text,
    extremism_risk_guide text,
    sara_partner_score_guide text,
    sara_other_score_guide text,
    osp_score_guide text,
    osp_dc_icc_combination_guide text,
    ogrs_score_guide text,
    ovp_guide text,
    ogp_guide text,
    pna_guide text,
    rosh_level rosh_levels,
    rsr_guide text,
    primary key (id),
    constraint fk__rc__intervention_id foreign key (intervention_id) references intervention_catalogue
);
create unique index unq__fk__rc__intervention_id
          on risk_consideration (intervention_id);
create table exclusion (
    id uuid not null,
    intervention_id uuid not null,
    min_remaining_sentence_duration_guide text,
    remaining_license_community_order_guide text,
    alcohol_drug_problem_guide text,
    mental_health_problem_guide text,
    other_preferred_method_guide text,
    same_type_rule_guide text,
    schedule_frequency_guide text,
    primary key (id),
    constraint fk__ex__intervention_id foreign key (intervention_id) references intervention_catalogue
);
create unique index unq__fk__ex__intervention_id
          on exclusion (intervention_id);
create table excluded_offence (
              id uuid not null,
              offence_type_id uuid,
              intervention_id uuid,
              victim_type text,
              motivation text,
              primary key (id),
              constraint fk__exo__offence_type_id foreign key (offence_type_id) references offence_type_ref,
              constraint fk__exo__intervention_id foreign key (intervention_id) references intervention_catalogue
);
CREATE TABLE complexity_level (
	id uuid NOT NULL,
	title text NOT NULL,
	description text NOT NULL,
	service_category_id uuid NOT NULL,
	complexity complexities NULL,
	CONSTRAINT complexity_level_pkey PRIMARY KEY (id),
	CONSTRAINT fk__cl__service_category_id FOREIGN KEY (service_category_id) REFERENCES service_category(id)
);
CREATE TABLE desired_outcome (
	id uuid NOT NULL,
	description text NOT NULL,
	service_category_id uuid NOT NULL,
	CONSTRAINT desired_outcome_pkey PRIMARY KEY (id),
	CONSTRAINT fk__desired_outcome__service_category FOREIGN KEY (service_category_id) REFERENCES service_category(id)
);
CREATE TABLE service_provider (
	id varchar(30) NOT NULL,
	name text NOT NULL,
	CONSTRAINT service_provider_pkey PRIMARY KEY (id)
);
CREATE TABLE contract_type_service_category (
	contract_type_id uuid NOT NULL,
	service_category_id uuid NOT NULL,
	CONSTRAINT pk_contract_type_service_category PRIMARY KEY (contract_type_id, service_category_id),
	CONSTRAINT fk_contract_type FOREIGN KEY (contract_type_id) REFERENCES contract_type(id),
	CONSTRAINT fk__ctsc__service_category_id FOREIGN KEY (service_category_id) REFERENCES service_category(id)
);
CREATE TABLE dynamic_framework_contract (
	id uuid NOT NULL,
	prime_provider_id varchar(30) NOT NULL,
	start_date date NOT NULL,
	end_date date NOT NULL,
	nps_region_id bpchar(1) NULL,
	pcc_region_id text NULL,
	allows_female bool NOT NULL DEFAULT true,
	allows_male bool NOT NULL DEFAULT true,
	minimum_age int4 NOT NULL DEFAULT 18,
	maximum_age int4 NULL,
	contract_reference varchar(30) NOT NULL,
	contract_type_id uuid NOT NULL,
	referral_start_date date NOT NULL DEFAULT '2021-01-01'::date,
	referral_end_at timestamp with time zone NULL,
	CONSTRAINT dynamic_framework_contract_contract_reference_key UNIQUE (contract_reference),
	CONSTRAINT dynamic_framework_contract_pkey PRIMARY KEY (id),
	CONSTRAINT fk__contract_type__contract_type_id FOREIGN KEY (contract_type_id) REFERENCES contract_type(id),
	CONSTRAINT fk__dynamic_framework_contract__nps_region_id FOREIGN KEY (nps_region_id) REFERENCES nps_region(id),
	CONSTRAINT fk__dynamic_framework_contract__pcc_region_id FOREIGN KEY (pcc_region_id) REFERENCES pcc_region(id),
	CONSTRAINT fk__dynamic_framework_contract__service_provider_id FOREIGN KEY (prime_provider_id) REFERENCES service_provider(id)
);
CREATE INDEX idx_prime_provider_id ON dynamic_framework_contract USING btree (prime_provider_id);
CREATE TABLE dynamic_framework_contract_sub_contractor (
	dynamic_framework_contract_id uuid NOT NULL,
	subcontractor_provider_id varchar(30) NOT NULL,
	CONSTRAINT pk_subcontractor PRIMARY KEY (subcontractor_provider_id, dynamic_framework_contract_id),
	CONSTRAINT fk_contract_id FOREIGN KEY (dynamic_framework_contract_id) REFERENCES dynamic_framework_contract(id),
	CONSTRAINT fk_subcontractor_id FOREIGN KEY (subcontractor_provider_id) REFERENCES service_provider(id)
);
CREATE INDEX idx_sub_contractor_prime_provider_id ON dynamic_framework_contract_sub_contractor USING btree (subcontractor_provider_id);
CREATE TABLE intervention (
	id uuid NOT NULL,
	dynamic_framework_contract_id uuid NOT NULL,
	created_at timestamp with time zone NOT NULL,
	title text NOT NULL,
	description text NOT NULL,
	incoming_referral_distribution_email text NOT NULL DEFAULT '__no_data__'::text,
	CONSTRAINT intervention_pkey PRIMARY KEY (id),
	CONSTRAINT fk__intervention__dynamic_framework_contract_id FOREIGN KEY (dynamic_framework_contract_id) REFERENCES public.dynamic_framework_contract(id)
);
CREATE UNIQUE INDEX idx_intervention_dynamic_framework_contract_id ON intervention USING btree (dynamic_framework_contract_id);
CREATE TABLE intervention_catalogue_map (
	intervention_catalogue_id uuid NOT NULL,
	intervention_id uuid NOT NULL,
	CONSTRAINT intervention_catalogue_map_pkey PRIMARY KEY (intervention_catalogue_id, intervention_id),
	CONSTRAINT fk__icm__intervention_catalogue_id FOREIGN KEY (intervention_catalogue_id) REFERENCES intervention_catalogue(id),
	CONSTRAINT fk__icm__intervention_id FOREIGN KEY (intervention_id) REFERENCES intervention(id)
);