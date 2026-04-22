#!/usr/bin/env bash

BASEDIR=$(realpath $(dirname ${0})/../../)

if [ -f $BASEDIR/.env ]
then
  export $(cat $BASEDIR/.env | sed 's/#.*//g' | xargs)
fi

set -e

username=${1}
password=${2}

IDAM_API_URL=${IDAM_API_URL_BASE:-http://localhost:5000}
IDAM_URL=${IDAM_STUB_LOCALHOST:-$IDAM_API_URL}
CLIENT_ID=${CLIENT_ID:-adoption-web}
#CLIENT_ID=${CLIENT_ID:-xuiwebapp}
clientSecret=${OAUTH2_CLIENT_SECRET}
redirectUri=http://localhost:3000/receiver
#redirectUri=http://localhost:3000/oauth2/callback

curl --silent --location --show-error "${IDAM_URL}/o/token" \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode "client_id=$CLIENT_ID" \
--data-urlencode "client_secret=$clientSecret" \
--data-urlencode "redirect_uri=$redirectUri" \
--data-urlencode "username=$username" \
--data-urlencode "password=$password" \
--data-urlencode 'scope=openid profile roles' \
--data-urlencode 'grant_type=password' \
| jq -r .access_token
