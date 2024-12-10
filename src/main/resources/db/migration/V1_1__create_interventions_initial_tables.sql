CREATE TYPE approval_state AS ENUM ('APPROVED','ACCREDITED','UNSUCESSFUL','NOT_APPROVED');
CREATE TYPE intervention_type AS ENUM ('SI','ACP','CRS');
CREATE TYPE situation_type AS ENUM ('COMMUNITY','REMAND','PRE_RELEASE','CUSTODY','PSS');
CREATE TYPE gender AS ENUM ('M','F','T');
create table intervention (
    id uuid not null,
    name text not null,
    int_type intervention_type not null,
    short_decription text not null,
    long_decription text not null,
    topic text not null,
    state approval_state not null,
    literacy_level text,
    learning_disability_catered_for boolean not null,
    equivalent_non_ldc_programme text,
    commencement_date date not null,
    termination_date date not null,
    primary key (id)
);
create table delivery_method (
    id uuid not null,
    intervention_id uuid not null,
    delivery_method_description text,
    primary key (id),
    constraint fk__dm__intervention_id foreign key (intervention_id) references intervention
);
create table delivery_method_situation (
    id uuid not null,
    delivery_method_id uuid not null,
    situation situation_type not null,
    primary key (id),
    constraint fk__dms__delivery_method_id foreign key (delivery_method_id) references delivery_method
);
create table session (
    id uuid not null,
    delivery_method_id uuid not null,
    session_detail text not null,
    primary key (id),
    constraint fk__session__delivery_method_id foreign key (delivery_method_id) references delivery_method
);
create table criminogenic_need_ref (
    id uuid not null,
    name text not null,
    primary key (id)
);
create table criminogenic_need (
    id uuid not null,
    need_ref uuid not null,
    intervention_id uuid not null,
    primary key (id),
    constraint fk__cn__criminogenic_need_ref foreign key (need_ref) references criminogenic_need_ref,
    constraint fk__cn__intervention_id foreign key (intervention_id) references intervention
);
insert into criminogenic_need_ref (id, name) values
('ba962a0a-d760-48a4-bdcb-d74c445edd31','Accommodation'),
('f52f2bef-7806-447d-a0dd-869e463a0423','Employment and Education'),
('8d586911-dd57-4de3-b43e-8bfe77a06215','Finance and Debt'),
('1cd19745-152d-4fe4-a4d2-6fcd8537f52c','Drug Use'),
('025215d3-bf02-433f-b8bc-90ba2e391e3a','Alcohol Use'),
('edd87755-2b0e-4f07-838e-4f9828e570d3','Health and Wellbeing'),
('e8442b7f-720a-43de-8273-7d1f83f96cd1','Personal Relationships and Community'),
('b1086ca9-b51d-440f-88a5-b597852df483','Thinking, Behaviours and Attitudes'),
('6a967d65-4576-4293-979f-889b48ca2cba','Lifestyle and Associates'),
('71471a03-f821-4b5d-a055-e4c7c6cf70d6','Relatives and Family');
create table possible_outcome (
    id uuid not null,
    intervention_id uuid,
    outcome text not null,
    primary key (id),
    constraint fk__po__intervention_id foreign key (intervention_id) references intervention
);
create table personal_eligibility (
    id uuid not null,
    intervention_id uuid,
    min_age integer,
    max_age integer,
    primary key (id),
    constraint fk__po__intervention_id foreign key (intervention_id) references intervention
);
create table eligibile_gender (
    id uuid not null,
    gender gender,
    personal_eligibility_id uuid,
    primary key (id),
    constraint fk__eg__personal_eligibility_id foreign key (personal_eligibility_id) references personal_eligibility
);
create table ethnicity_ref (
    id uuid not null,
    broad_group text not null,
    name text not null,
    primary key (id)
);
insert into ethnicity_ref (id, broad_group, name) values
('52e445f5-76ef-40a3-a7c1-6e8767d1658a','Asian or Asian British','Indian'),
('2593c35b-2dd8-4aab-aa94-055c8a793ee7','Asian or Asian British','Pakistani'),
('97b3f4e4-dc43-474c-9801-65fb737ef656','Asian or Asian British','Bangladeshi'),
('9901c7a5-2b34-48cb-ae56-495bb0f1c4cc','Asian or Asian British','Chinese'),
('6f207517-cce7-4f45-94a2-2a03571fa575','Asian or Asian British','Any other Asian background'),
('b7a945c5-bd3e-4604-86e2-9acc0188acac','Black, Black British, Caribbean or African','African'),
('90a4f5cd-49bd-4097-8607-faa379003334','Black, Black British, Caribbean or African','Caribbean'),
('04a902c8-7c92-4c04-9c10-bbfaff998f0d','Black, Black British, Caribbean or African','Any other Black, Black British, or Caribbean background'),
('ef27cf80-9511-4960-9669-9670171d3350','Mixed or multiple ethnic groups','White and Black Caribbean'),
('332ee85a-7982-4f16-a08e-97f0703c6eb1','Mixed or multiple ethnic groups','White and Black African'),
('56890bc7-0423-42e9-b71e-13fd03dc7e7b','Mixed or multiple ethnic groups','White and Asian'),
('9470ac74-fc29-412c-8085-a4527810c328','Mixed or multiple ethnic groups','Any other Mixed or multiple ethnic background'),
('f5feeeb6-7485-4823-ac68-96b3db2a245b','White','English, Welsh, Scottish, Northern Irish or British'),
('c293013a-9547-43f1-93f1-e563fe2eae2b','White','Irish'),
('6a0eb91f-ae2b-4d26-8295-e4bcc5333fb5','White','Gypsy or Irish Traveller'),
('b3d5f511-08a7-4b31-989b-3ec8aaf1c918','White','Roma'),
('31e0f2c2-a5fa-4f34-9a16-61b619027519','White','Any other White background'),
('8f298dc9-c9d3-4777-b606-c6af4b0a38c3','Other ethnic group','Arab'),
('714d36d3-e76e-4a8e-b510-fe9bc5d28e1a','Other ethnic group','Any other ethnic group');
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
insert into offence_type_ref (id, name) values
('1f8a7fde-b81b-490a-a9bb-fe056d5b9294','Acquisitive offence'),
('ec6720a8-fa08-488b-82be-2496581a8491','All offences'),
('b0517921-0d72-4f10-bea3-be10d6036e4f','Driving offence'),
('c19708f5-85ac-43f6-bd46-fbac6f9dae2b','Extremism offence'),
('d5cba2c9-cfa7-4d4f-be4b-896f9063cf50','Gang or Organised Crime-Related offence'),
('18630563-e5d3-4917-a189-3accfd7e3511','General Violence (GV)'),
('1585e943-d552-4e2c-bd5d-08229646f99c','Intimate Partner Violence (IPV)'),
('cdca173d-8189-4c3d-9618-4b40bda56274','Sexual offence'),
('e62edfb1-e533-480b-84c1-587711f0c259','Substance-Related offence');
create table eligibile_offence (
    id uuid not null,
    offence_type_id uuid,
    personal_eligibility_id uuid,
    victim_type text,
    motivation text,
    primary key (id),
    constraint fk__eo__offence_type_id foreign key (offence_type_id) references offence_type_ref,
    constraint fk__eo__personal_eligibility_id foreign key (personal_eligibility_id) references personal_eligibility
);
create table enabling_intervention (
    id uuid not null,
    intervention_id uuid,
    enabling_intervention_detail text,
    primary key (id),
    constraint fk__ei__intervention_id foreign key (intervention_id) references intervention
);
create table delivery_location (
    id uuid not null,
    intervention_id uuid,
    provider_name text not null,
    contact text not null,
    pdu_estabishments text not null,
    primary key (id),
    constraint fk__dl__intervention_id foreign key (intervention_id) references intervention
);