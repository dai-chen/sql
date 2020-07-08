#!/bin/bash

DIR=$(dirname "$0")

if hash python3.7 2> /dev/null; then
    PYTHON=python3.7
elif hash python3 2> /dev/null; then
    # fallback to python3 in case there is no python3.7 alias; should be 3.7
    PYTHON=python3
else
    echo 'python3.7 required'
    exit 1
fi

$PYTHON -m venv $DIR/.venv
if [ ! -f $DIR/.venv/bin/pip ]; then
    wget https://bootstrap.pypa.io/get-pip.py
    $DIR/.venv/bin/python get-pip.py
    rm -f get-pip.py
fi

$DIR/.venv/bin/pip install -U pip setuptools wheel
$DIR/.venv/bin/pip install --upgrade pip

# Install TDVT from subdirectory on GitHub repository by pip directly (without need to git clone first)
$DIR/.venv/bin/pip install -e 'git+https://github.com/tableau/connector-plugin-sdk.git#egg=version_subpkg&subdirectory=tdvt'

#cd $DIR/tdvt_workspace
$DIR/.venv/bin/python -m tdvt.tdvt list mydb
$DIR/.venv/bin/python -m tdvt.tdvt run mydb
