insert into service_provider(id, name)
values ('HARMONY_LIVING', 'Harmony Living'),
       ('HOME_TRUST', 'Home Trust'),
       ('HELPING_HANDS', 'Helping Hands');
INSERT INTO contract_type (id, name, code)
VALUES ('72e60faf-b8e5-4699-9d7c-aef631cca71b', 'Accommodation', 'ACC'),
       ('b402486d-991e-4977-9291-073a3526d60f', 'Education, Training and Employment (ETE)', 'ETE'),
       ('f9b59d2c-c60b-4eb0-8469-04c975d2e2ee', 'Personal Wellbeing', 'PWB'),
       ('b74c3f1d-2c53-45c0-bd23-d61befaf00af', 'Women''s Services', 'WOS'),
       ('bc65434d-089b-4c37-80c2-50efebb76933', 'Mentoring', 'MTR'),
       ('5a7d2856-2126-478d-ab12-afcc07deb977', 'Dependency and Recovery', 'DNR'),
       ('a93c152c-ed56-48f9-92e8-401ff7aa2fa8', 'Women''s Support Services (GM)', 'WSM'),
       ('5aa47f40-5f98-4300-a274-f8272c2f127d', 'Finance, Benefit and Debt (East Midlands)', 'FBD-EM'),
       ('4c29a22e-39a5-41ef-a8e4-69c8507ae058', 'Finance, Benefit and Debt (West Midlands)', 'FBD-WM'),
       ('c15ad3f3-f24b-4cd6-8ff6-03df4927fbe9', 'Finance, Benefit and Debt (London)', 'FBD-L');
INSERT INTO contract_type (id, name, code)
VALUES ('f7569c39-5c3a-457e-9a29-435858af55e0', 'Finance, Benefit and Debt (South Central)', 'FBD-SC'),
       ('c1053875-9492-48b3-94bc-ecde8c249b26', 'Dependency and Recovery (Yorkshire and the Humber)', 'DNR-Y'),
       ('c8a17e8a-1804-4c68-8109-f58447f5f06d', 'Dependency and Recovery (London)', 'DNR-L'),
       ('8028db0e-ee90-4982-b894-78f940ebd8c8', 'Dependency and Recovery (North East)', 'DNR-NE'),
       ('7af3a832-2b46-43b2-9db1-6bc43181a233', 'Dependency and Recovery (Kent, Surrey and Sussex)', 'DNR-K'),
       ('588fea26-dae1-412e-add6-73e4e6d84a3c', 'Dependency and Recovery (North East: Northumbria)', 'DNR-NEN'),
       ('5379196d-559a-4709-bf37-fe6a6d65d9b5', 'Finance, Benefit and Debt (Wales)', 'FBD-W'),
       ('d10c792d-12bd-4ac4-a999-04dbdbf59940', 'Dependency and Recovery (South Central)', 'DNR-SC'),
       ('bcb3eb1d-b95d-4679-bb85-b91aa3240ef5', 'Dependency and Recovery (South Yorkshire)', 'DNR-SY'),
       ('a19f3da7-c5eb-4936-9f76-f877ddf5c656', 'Dependency and Recovery (West Midlands)', 'DNR-WM');
INSERT INTO contract_type (id, name, code)
VALUES ('ee20282e-aea4-429e-8b3f-0d868c46e8bc', 'Finance, Benefit and Debt (Yorkshire and the Humber)', 'FBD-Y'),
       ('bb6b0997-a9bd-4d90-b908-15606f61d230', 'Finance, Benefit and Debt (North West)', 'FBD-NW'),
       ('69b593ec-705c-4095-afcb-652a1eddb071', 'Finance, Benefit and Debt (South West)', 'FBD-SW'),
       ('0b83447e-5a15-4bfe-af8e-0650cbe74a49', 'Dependency and Recovery (South West)', 'DNR-SW'),
       ('8ecbf69b-9b54-49d8-a2bc-54c7b7cc4731', 'Finance, Benefit and Debt in the North East', 'FBD-NE'),
       ('8cd135aa-c7d9-4f60-be25-81f0fe80e736', 'Finance, Benefit and Debt (Stockport)', 'FBD-SP'),
       ('d5805712-42a4-44b6-8368-ba701defa9fa', 'Finance, Benefit and Debt (Bolton)', 'FBD-B'),
       ('17a6abbd-ca97-4aaa-824f-6bae05523d81', 'Finance, Benefit and Debt (Trafford)', 'FBD-TR'),
       ('42dff63e-1b46-4296-bc56-4b213da84336', 'Finance, Benefit and Debt (Rochdale)', 'FBD-R'),
       ('68240d7b-58aa-4156-8792-900b212751e2', 'Finance, Benefit and Debt (Salford)', 'FBD-SA');
INSERT INTO contract_type (id, name, code)
VALUES ('26239d61-b742-423a-842c-fab1950e17a2', 'Finance, Benefit and Debt (Bury)', 'FBD-BU'),
       ('50b1a1cc-d326-4e93-903c-633c9f273ee6', 'Finance, Benefit and Debt (Manchester)', 'FBD-M'),
       ('15fe353b-8bc1-4e72-940a-44ebad8e3f41', 'Finance, Benefit and Debt (Tameside)', 'FBD-TA'),
       ('8985ba5b-104b-4329-aa49-df3aa9a7beef', 'Finance, Benefit and Debt (Wigan)', 'FBD-WI'),
       ('95fbfee1-04a4-4e61-9b3e-c6dffef4fcee', 'Finance, Benefit and Debt (Oldham)', 'FBD-O');

insert into dynamic_framework_contract (id, contract_type_id, prime_provider_id, start_date, end_date, nps_region_id,
                                        pcc_region_id, allows_female, allows_male, minimum_age, maximum_age,
                                        contract_reference)
values ('1d7f8fcc-aa12-4705-a6a5-0d40467e03e9', '72e60faf-b8e5-4699-9d7c-aef631cca71b', 'HARMONY_LIVING',
        TO_DATE('2020-12-15', 'YYYY-MM-DD'), TO_DATE('2023-12-15', 'YYYY-MM-DD'), 'G', null, true, true, 18, 25,
        '0001'),
       ('f9d24b4a-390d-4cc1-a7ee-3e6f022e1599', '72e60faf-b8e5-4699-9d7c-aef631cca71b', 'HARMONY_LIVING',
        TO_DATE('2020-01-01', 'YYYY-MM-DD'), TO_DATE('2022-12-31', 'YYYY-MM-DD'), 'G', null, true, false, 18, 25,
        '0002'),
       ('24f7a423-15a6-438d-9d28-063e92b25a9b', '72e60faf-b8e5-4699-9d7c-aef631cca71b', 'HARMONY_LIVING',
        TO_DATE('2021-12-11', 'YYYY-MM-DD'), TO_DATE('2025-12-11', 'YYYY-MM-DD'), null, 'avon-and-somerset', true, true,
        25, null, '0003'),
       ('c7d39f92-6f43-49a4-bb62-e0f42c864765', 'f9b59d2c-c60b-4eb0-8469-04c975d2e2ee', 'HARMONY_LIVING',
        TO_DATE('2021-01-01', 'YYYY-MM-DD'), TO_DATE('2035-05-01', 'YYYY-MM-DD'), 'G', null, false, true, 18, null,
        '0004'),
       ('0b60d842-9c08-408e-8c8d-f6dbf8e5c3f4', 'f9b59d2c-c60b-4eb0-8469-04c975d2e2ee', 'HARMONY_LIVING',
        TO_DATE('2021-01-01', 'YYYY-MM-DD'), TO_DATE('2035-05-01', 'YYYY-MM-DD'), 'J', null, true, true, 18, null,
        '0005'),
       ('56ad7d77-94c7-4fbf-a704-29e0f6ad078f', 'f9b59d2c-c60b-4eb0-8469-04c975d2e2ee', 'HARMONY_LIVING',
        TO_DATE('2021-01-01', 'YYYY-MM-DD'), TO_DATE('2035-05-01', 'YYYY-MM-DD'), 'A', null, true, true, 18, null,
        '0006'),
       ('1435d1c5-0c22-459a-bd1a-ce593fba6c05', 'f9b59d2c-c60b-4eb0-8469-04c975d2e2ee', 'HOME_TRUST',
        TO_DATE('2021-01-01', 'YYYY-MM-DD'), TO_DATE('2035-05-01', 'YYYY-MM-DD'), 'A', null, true, true, 18, null,
        '0007');

insert into intervention (id, dynamic_framework_contract_id, created_at, title, description,
                          incoming_referral_distribution_email)
values ('98a42c61-c30f-4beb-8062-04033c376e2d', '1d7f8fcc-aa12-4705-a6a5-0d40467e03e9',
        TO_DATE('2020-10-15', 'YYYY-MM-DD'), 'Accommodation Service',
        'The service aims are to support in securing settled accommodation.', 'contact@harmonyliving.com'),
       ('90e35306-5d65-4901-bb36-c4c44c7d19f9', 'f9d24b4a-390d-4cc1-a7ee-3e6f022e1599',
        TO_DATE('2021-01-15', 'YYYY-MM-DD'), 'Sheltered Accommodation',
        'Providing help to move female service users into safe accommodation.', 'contact@harmonyliving.com'),
       ('08524319-7d5b-4b56-862a-bfe2c9a545f5', '24f7a423-15a6-438d-9d28-063e92b25a9b',
        TO_DATE('2021-02-01', 'YYYY-MM-DD'), 'Accommodation/Good Tenant',
        'This course offers practical information and advice helping participants understand what it means to be a ''good'' tenant.',
        'contact@harmonyliving.com'),
       ('f803445f-326c-4ef8-aee2-f7716d417832', 'c7d39f92-6f43-49a4-bb62-e0f42c864765',
        TO_DATE('2020-11-11', 'YYYY-MM-DD'), 'Social Inclusion', 'This programme is aimed at males who have offended.

• This programme is aimed at males who have offended.
• This is some more text about the intervention', 'contact@hometrust.com'),
       ('15237ae5-a017-4de6-a033-abf350f14d99', '0b60d842-9c08-408e-8c8d-f6dbf8e5c3f4',
        TO_DATE('2020-11-11', 'YYYY-MM-DD'), 'Begins at Home',
        'This service is dedicated for handling challenging situations at home.', 'contact@hometrust.com'),
       ('3ccb511b-89b2-42f7-803b-304f54d85a24', '1435d1c5-0c22-459a-bd1a-ce593fba6c05',
        TO_DATE('2020-11-11', 'YYYY-MM-DD'), 'Kick your Habit', 'Drug and alcohol rehab.', 'contact@hometrust.com');

insert into dynamic_framework_contract_sub_contractor(dynamic_framework_contract_id, subcontractor_provider_id)
values ('1d7f8fcc-aa12-4705-a6a5-0d40467e03e9', 'HOME_TRUST'),
       ('1d7f8fcc-aa12-4705-a6a5-0d40467e03e9', 'HELPING_HANDS'),
       ('1435d1c5-0c22-459a-bd1a-ce593fba6c05', 'HARMONY_LIVING');
