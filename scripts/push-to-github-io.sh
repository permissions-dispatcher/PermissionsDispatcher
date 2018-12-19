#!/bin/bash

declare repoName="permissions-dispatcher.github.io"
declare repoUrl="git@github.com:permissions-dispatcher/${repoName}.git"
declare distDir="_book"
declare branch="master"
declare commitMessage="Update content from TravisCI."

commit_and_push_changes() {
  git config --global user.email "$GH_USER_EMAIL" \
    && git config --global user.name "$GH_USER_NAME" \
    && git add -A \
    && git commit --message "$2" \
    && git push --quiet --force "${repoUrl}" "$1"
}

# if [ "$TRAVIS_BRANCH" != "master" ] || [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
#   echo "Skip updating gitbook: branch is not master or the build was triggered by pull request."
#   exit 0;
# fi

echo "Decrypt deploy key"
openssl aes-256-cbc -K $encrypted_786b61619ad8_key -iv $encrypted_786b61619ad8_iv -in .travis/deploy_key.enc -out deploy_key -d
chmod 600 deploy_key
eval `ssh-agent -s`
ssh-add deploy_key

echo "Clone repo"
rm -rf ${repoName}
git clone ${repoUrl} ${repoName}

echo "Install Node and dependencies"
. $HOME/.nvm/nvm.sh
nvm install stable
nvm use stable
npm install -g gitbook-cli

echo "Build HTML"
gitbook build

echo "Copy files"
cp -Rf "${distDir}/doc"* "${distDir}/gitbook"* "${distDir}/index.html" "${distDir}/search_index.json" "${repoName}"

echo "Commit and push"
cd "${repoName}"
commit_and_push_changes "${branch}" "${commitMessage}"