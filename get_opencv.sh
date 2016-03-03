#!/bin/sh
set -e
# check to see if protobuf folder is empty
if [ ! -d "$HOME/opencv" ]; then
  git clone https://github.com/Itseez/opencv.git --branch 3.1.0 libopencv
  mkdir libopencv/build
  cd libopencv/build
  cmake -D CMAKE_BUILD_TYPE=RELEASE -D CMAKE_INSTALL_PREFIX=$HOME/opencv .. && make && make install
else
  echo "Using cached directory."
fi
