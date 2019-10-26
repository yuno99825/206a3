#!/bin/bash

LENGTH=$(soxi -D .temp/creation_audio.wav)
NUM_IMAGES=$(ls -l .temp/images/selected | wc -w)
FRAMERATE=$((NUM_IMAGES/LENGTH))

cat ./.temp/images/selected/*.jpg | ffmpeg -f image2pipe -framerate $FRAMERATE -i - -c:v libx264 -pix_fmt yuv420p -vf "scale=w=1080:h=720:force_original_aspect_ratio=1,pad=1080:720:(ow-iw)/2:(oh-ih)/2" -r 25 -y ./.temp/video.mp4