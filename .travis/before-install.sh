#!/bin/sh

if [ -n "${encrypted_464f3278645d_iv}" ]; then
  openssl aes-256-cbc -K $encrypted_464f3278645d_key -iv $encrypted_464f3278645d_iv -in deployment/signingkey.asc.enc -out deployment/signingkey.asc -d
  echo "allow-loopback-pinentry" > ~/.gnupg/gpg-agent.conf
  gpg2 --batch --keyring=$TRAVIS_BUILD_DIR/pubring.gpg --secret-keyring=$TRAVIS_BUILD_DIR/secring.gpg --no-default-keyring --import deployment/signingkey.asc
fi