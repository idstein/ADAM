#!/bin/sh
#set -e
wget https://github.com/github/git-lfs/releases/download/v1.1.2/git-lfs-linux-amd64-1.1.2.tar.gz
mkdir git-lfs && tar vxzf git-lfs-linux-amd64-1.1.2.tar.gz -C git-lfs --strip=1
git-lfs/git-lfs init
git-lfs/git-lfs fetch
