#!/bin/sh

# $1 := SONAR CLOUD PROJECT KEY
echo "==> Get quality gate for project $1"

quality_gate_result=$(curl -s "https://sonarcloud.io/api/qualitygates/project_status?projectKey=$1" | python -mjson.tool)

echo "==> Result: $quality_gate_result"

if echo "$quality_gate_result" | grep -q ERROR; then
    echo "==> Found ERROR! Quality gate failure"
    exit 1
else
    echo "==> Quality gate passed"
    exit 0
fi