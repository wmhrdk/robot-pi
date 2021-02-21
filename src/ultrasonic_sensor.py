#!/usr/bin/env python3

import RPi.GPIO as IO
import time

IO.setmode(IO.BCM)

IO_TRIGGER = 17
IO_ECHO = 27

IO.setup(IO_TRIGGER, IO.OUT)
IO.setup(IO_ECHO, IO.IN)

def distance():
    IO.output(IO_TRIGGER, True)
    time.sleep(0.00001)
    IO.output(IO_TRIGGER, False)

    start_time = time.time()
    stop_time = time.time()

    while IO.input(IO_ECHO) == 0:
        start_time = time.time()
        if (start_time - stop_time) > 0.02 :
            distance = 100
            break

    while IO.input(IO_ECHO) == 1:
        stop_time = time.time()
        if (stop_time - start_time) > 0.02 :
            distance = 100
            break

    time_elapsed = stop_time - start_time
    distance = (time_elapsed * 34300) / 2

    return distance

try:
    while True:
        dist = distance()
        print("Measured Distance = %.1f cm" %dist)
        time.sleep(0.01)
except KeyboardInterrupt:
    print("Measurement Stopped")
    IO.cleanup()

