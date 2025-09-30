CREATE OR REPLACE FUNCTION truncate_tables_and_sequences() RETURNS INT AS $$
DECLARE
tableStatements CURSOR FOR
SELECT tablename FROM pg_tables
WHERE schemaname = 'public';
sequenceStatements CURSOR FOR
SELECT sequencename FROM pg_sequences
WHERE schemaname = 'public';
BEGIN
FOR stmt IN tableStatements
        LOOP
            IF (stmt.tablename = 'flyway_schema_history') THEN
                CONTINUE;
            END IF;

            EXECUTE 'TRUNCATE TABLE ' || quote_ident(stmt.tablename) || ' CASCADE;';

        END LOOP;

FOR stmt IN sequenceStatements
        LOOP
            EXECUTE 'ALTER SEQUENCE ' || quote_ident(stmt.sequencename) || ' RESTART WITH 1';
END LOOP;
RETURN 1;
END;
$$ LANGUAGE plpgsql;
