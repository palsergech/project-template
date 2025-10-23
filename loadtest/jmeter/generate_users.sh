#!/bin/bash

# parse env variables
source .env.local

python3 generate_users.py \
        --count $USERS_COUNT \
        --realm $KEYCLOAK_REALM \
        --url $KEYCLOAK_URL \
        --admin $KEYCLOAK_ADMIN_USER \
        --password $KEYCLOAK_ADMIN_PASS \
        --out $OUTPUT_FILE