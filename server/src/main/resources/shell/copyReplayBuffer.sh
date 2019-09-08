#!/bin/bash
## fswatch -o -d -i "replay_*\\.*" /Users/bmuthu/Documents/OBS-DONT-DELETE/instant-replay | xargs -n1 /Users/bmuthu/Documents/OBS-DONT-DELETE/copyFile.sh

# Main location
HOME_LOC="/Users/bmuthu/Documents/OBS-DONT-DELETE"

STORE_DIR=${HOME_LOC}/instant-replay
LASR_REPLAY_DIR=${HOME_LOC}/last-replay
BACKUP_DIR=${HOME_LOC}/backup

## making a copy in to backup
cp ${STORE_DIR}/replay*.* ${BACKUP_DIR}/replay_`date +%s`.mkv

## moving the file to last replay
mv ${STORE_DIR}/replay*.* ${LASR_REPLAY_DIR}/LastestReplay.mkv

exit;