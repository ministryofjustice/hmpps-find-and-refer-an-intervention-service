alter table intervention_catalogue rename column short_decription to short_description;
alter table intervention_catalogue rename column long_decription to long_description;

-- removing constraints as data not mandatory for all intervention types
alter table intervention_catalogue alter column long_description drop not null;
alter table intervention_catalogue alter column topic drop not null;
alter table intervention_catalogue alter column session_detail drop not null;
alter table intervention_catalogue alter column commencement_date drop not null;
alter table intervention_catalogue alter column termination_date drop not null;

-- adding column in this table until final decision is made on where this data is displayed
alter table intervention_catalogue add column reasons_for_referral text null;

alter table delivery_location rename column pdu_estabishments to pdu_establishments;

alter table eligibile_offence rename to eligible_offence;
alter table eligibile_ethnicity rename to eligible_ethnicity;
