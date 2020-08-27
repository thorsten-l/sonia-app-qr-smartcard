#!/bin/bash

echo "wf1 - 141.41.12.123"
scp target/qr-smartcard-app.jar pi@141.41.12.123:
ssh pi@141.41.12.123 'sudo shutdown -r now'

echo "wf2 - 141.41.12.175"
scp target/qr-smartcard-app.jar pi@141.41.12.175:
ssh pi@141.41.12.175 'sudo shutdown -r now'

echo "wob1 - 141.41.108.186"
scp target/qr-smartcard-app.jar pi@141.41.108.186:
ssh pi@141.41.108.186 'sudo shutdown -r now'

echo "sud1 - 141.41.181.155"
scp target/qr-smartcard-app.jar pi@141.41.181.155:
ssh pi@141.41.181.155 'sudo shutdown -r now'

echo "sz1 - 141.41.143.62 "
scp target/qr-smartcard-app.jar pi@141.41.143.62:
ssh pi@141.41.143.62 'sudo shutdown -r now'
