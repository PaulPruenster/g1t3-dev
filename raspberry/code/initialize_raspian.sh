#!/bin/bash

echo "Setting up the Raspberry Pi!"

# Check if Python 3 is installed
if ! command -v python3 &> /dev/null; then
    echo "Python 3 is not installed. Installing..."
    sudo apt-get update
    sudo apt-get upgrade -y
    sudo apt-get install -y python3
else
    echo "Python 3 is already installed."
fi

# Check if pip3 is installed
if ! command -v pip3 &> /dev/null; then
    echo "pip3 is not installed. Installing..."
    sudo apt-get install -y python3-pip
else
    echo "pip3 is already installed."
fi

# Install required Python packages
echo "Installing Python packages..."
pip3 install pyyaml
pip3 install bleak
pip3 install requests

echo "Setup finished!"
