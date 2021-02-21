import RPi.GPIO as IO
import time

IO.setmode(IO.BCM)

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
    IO.output(motor_right_cw, IO.HIGH)
    IO.output(motor_right_ccw, IO.LOW)
    IO.output(motor_left_cw, IO.LOW)
    IO.output(motor_left_ccw, IO.HIGH)

def moveBackward(speed):
    speedRight.ChangeDutyCycle(speed)
    speedLeft.ChangeDutyCycle(speed)
    IO.output(motor_right_cw, IO.LOW)
    IO.output(motor_right_ccw, IO.HIGH)
    IO.output(motor_left_cw, IO.HIGH)
    IO.output(motor_left_ccw, IO.LOW)

def moveLeft(speed):
    speedRight.ChangeDutyCycle(speed)
    speedLeft.ChangeDutyCycle(speed)
    IO.output(motor_right_cw, IO.HIGH)
    IO.output(motor_right_ccw, IO.LOW)
    IO.output(motor_left_cw, IO.HIGH)
    IO.output(motor_left_ccw, IO.LOW)

def moveLeft(speed):
    speedRight.ChangeDutyCycle(speed)
    speedLeft.ChangeDutyCycle(speed)
    IO.output(motor_right_cw, IO.LOW)
    IO.output(motor_right_ccw, IO.HIGH)
    IO.output(motor_left_cw, IO.LOW)
    IO.output(motor_left_ccw, IO.HIGH)

try:
    while True:
        _speed = 90
        moveForward(_speed)

except KeyboardInterrupt:
    IO.cleanup()

