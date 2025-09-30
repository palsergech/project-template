#!/usr/bin/env sh

set -eu

if [ $# -gt 0 ]; then
	exec "$@"
else
	exec /service/bin/backend
fi
