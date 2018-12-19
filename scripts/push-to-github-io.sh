#!/bin/bash
set -eu

declare repoName="permissions-dispatcher.github.io"
declare repoUrl="git@github.com:permissions-dispatcher/${repoName}.git"
declare commitMessage="Update content from TravisCI."
declare distDir="_book"

if [ "$TRAVIS_BRANCH" != "master" ]; then
  echo "Skip the job as current branch is not master."
  exit 0;
fi

commit_and_push_changes() {
    git config --global user.email "$GH_USER_EMAIL" \
        && git config --global user.name "$GH_USER_NAME" \
        && git init \
        && git add -A \
        && git commit --message "$2" \
        && git push --quiet --force "${repoUrl}" "$1"
}

echo "Clone repo"
rm -rf "${repoName}"
mkdir "${repoName}"
git clone ${repoUrl} ${repoName}
echo "Remove files"
find "${repoName}" -type f -delete 
echo "Install dependencies"
yarn global add gitbook-cli
echo "Update content"
gitbook build
echo "Copy files"
cp -Rf "${distDir}/doc"* "${distDir}/gitbook"* "${distDir}/index.html" "${distDir}/search_index.json" "${repoName}"
echo "Commit and push"
cd "${repoName}"
commit_and_push_changes  "master" "${commitMessage}"
