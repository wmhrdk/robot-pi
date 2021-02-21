#!/usr/bin/env python3

import RPi.GPIO as IO
from socket import *
import os
import time

IO.setmode(IO.BCM)

IO.setup(21, IO.IN, pull_up_down=IO.PUD_UP)
IO.setup(16, IO.OUT)

IO.output(16, IO.HIGH)

def Shutdown(channel):
    print("Shutting Down...")
    i = 0
    while (i<3):
        IO.output(16, IO.LOW)
        time.sleep(0.5)
        IO.output(16, IO.HIGH)
        time.sleep(0.5)
        i = i + 1
    time.sleep(3)
    os.system("sudo shutdown -h now")

IO.add_event_detect(21, IO.FALLING, callback=Shutdown, bouncetime=2000)

enableRight = 13
enableLeft = 12
motor_right_cw = 19
motor_right_ccw = 26
motor_left_cw = 6
motor_left_ccw = 5

IO.setup(enableRight, IO.OUT)
IO.setup(enableLeft, IO.OUT)
IO.setup(motor_right_cw, IO.OUT)
IO.setup(motor_right_ccw, IO.OUT)
IO.setup(motor_left_cw, IO.OUT)
IO.setup(motor_left_ccw, IO.OUT)

speedRight = IO.PWM(enableRight, 100)
speedLeft = IO.PWM(enableLeft, 100)

speedRight.start(0)
speedLeft.start(0)

_speed = 0

def moveForward(speed):
    speedRight.ChangeDutyCycle(speed)
    speedLeft.ChangeDutyCycle(speed)
    IO.output(motor_right_cw, IO.LOW)
    IO.output(motor_right_ccw, IO.HIGH)
    IO.output(motor_left_cw, IO.HIGH)
    IO.output(motor_left_ccw, IO.LOW)

def moveBackward(speed):
    speedRight.ChangeDutyCycle(speed)
    speedLeft.ChangeDutyCycle(speed)
    IO.output(motor_right_cw, IO.HIGH)
    IO.output(motor_right_ccw, IO.LOW)
    IO.output(motor_left_cw, IO.LOW)
    IO.output(motor_left_ccw, IO.HIGH)

def moveLeft(speed):
    speedRight.ChangeDutyCycle(speed)
    speedLeft.ChangeDutyCycle(speed)
    IO.output(motor_right_cw, IO.HIGH)
    IO.output(motor_right_ccw, IO.LOW)
    IO.output(motor_left_cw, IO.HIGH)
    IO.output(motor_left_ccw, IO.LOW)

def moveRight(speed):
    speedRight.ChangeDutyCycle(speed)
    speedLeft.ChangeDutyCycle(speed)
    IO.output(motor_right_cw, IO.LOW)
    IO.output(motor_right_ccw, IO.HIGH)
    IO.output(motor_left_cw, IO.LOW)
    IO.output(motor_left_ccw, IO.HIGH)

def idling(speed):
    speedRight.ChangeDutyCycle(speed)
    speedLeft.ChangeDutyCycle(speed)
    IO.output(motor_right_cw, IO.LOW)
    IO.output(motor_right_ccw, IO.LOW)
    IO.output(motor_left_cw, IO.LOW)
    IO.output(motor_left_ccw, IO.LOW)

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

ctrCmd = ["F","B", "R", "L", "I", "Q", "E", "Z", "C", "A"]

HOST = '192.168.4.1'
PORT = 2323

socket_server = socket(AF_INET, SOCK_STREAM)
socket_server.bind((HOST, PORT))
socket_server.listen()

try:
    while True:
        print("HOST : ",HOST)
        print(socket_server)
        print("--Waiting for connection...")
        client, addr = socket_server.accept()
        print("--Client connected...")

        try:
            while True:
                dist = distance()
                _speed = 100
                received_command = client.recv(1)
                received_command = received_command.decode('utf-8')
                received_command = received_command + ""
                print(received_command)

                if not received_command:
                    idling(90)
                    break
                if (dist > 25.0):
                    if (received_command == ctrCmd[0]):
                        print('Going Forward\n')
                        moveForward(_speed)
                elif (dist < 25.0):
                    moveForward(0)
                if (received_command == ctrCmd[1]):
                    print('Going Backward\n')
                    moveBackward(_speed)
                if (received_command == ctrCmd[2]):
                    print('Going Left\n')
                    moveLeft(_speed)
                if (received_command == ctrCmd[3]):
                    print('Going Right\n')
                    moveRight(_speed)
                if (received_command == ctrCmd[4]):
                    idling(_speed)
                    print('Idle\n')
                if (received_command == ctrCmd[5]):
                    moveLeft(_speed)
                    print('Moving 45 Left')
                    time.sleep(0.25)
                if (received_command == ctrCmd[6]):
                    moveRight(_speed)
                    print('Moving 45 Right')
                    time.sleep(0.25)
                if (received_command == ctrCmd[7]):
                    moveLeft(_speed)
                    print('Moving 135 Left')
                    time.sleep(0.75)
                if (received_command == ctrCmd[8]):
                    moveRight(_speed)
                    print('Moving 135 Right')
                    time.sleep(0.75)
                if (received_command == ctrCmd[9]):
                    if (dist > 25.0):
                        moveForward(_speed)
                        print('Auto: Forward')
                    elif (dist < 25.0):
                        print('Avoiding')
                        moveRight(_speed)
                        time.sleep(0.5)
                print('Distance = %.1f' %dist)
        except KeyboardInterrupt:
            socket_server.close()
            IO.cleanup()
        idling(90)

except KeyboardInterrupt:
    socket_server.close()
    IO.cleanup()
