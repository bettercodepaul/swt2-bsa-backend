#!/bin/sh

# $1 := SONAR CLOUD PROJECT KEY
# $2 := GIT BRANCH

echo "==> Get quality gate for project $1 and branch $2"

url=$(echo "https://sonarcloud.io/api/project_branches/list?project=$1")
branch=$2

command=$(python sonar_branch_result_parser.py $url $branch)

if echo "$command" | grep -q ERROR; then
    echo "==> Found ERROR! Quality gate failure"
    exit 1
else
    echo "==> Quality gate passed"
    exit 0
fi