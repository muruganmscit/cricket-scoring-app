#!/bin/bash
fswatch -o -d -i "replay_*\\.*" /Users/bmuthu/Documents/OBS-DONT-DELETE/instant-replay | xargs -n1 /Users/bmuthu/Documents/OBS-DONT-DELETE/copyFile.sh