#!/bin/bash

# get env file name from args
if [[ -z "$1" ]]; then
    echo "Usage: $0 <env-file>"
    exit 1
fi

source $1

python3 generate_users.py \
        --overwrite \
        --count $USERS_COUNT \
        --realm $KEYCLOAK_REALM \
        --url $KEYCLOAK_URL \
        --admin $KEYCLOAK_ADMIN_USER \
        --password $KEYCLOAK_ADMIN_PASS \
        --out $OUTPUT_FILE