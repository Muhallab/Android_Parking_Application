import RPi.GPIO as GPIO #import library to access GPIO pin
import time
from rpi_lcd import LCD
import math
from picamera import PiCamera
from time import sleep
from PIL import Image
import pytesseract
import cv2
import numpy as np
from pytesseract import Output
import os
import mysql.connector
import traceback
import imutils
from datetime import datetime

mydb = mysql.connector.connect(
  host="capstone.cayk5wnefmpg.us-east-1.rds.amazonaws.com",
  user="admin",
  password="nuttertools"
)

IR_PIN= 11 #define pin for ir sensor
IR_PIN2= 13
IR_PIN3 = 15
IR_PINENTER = 18
IR_PINEXIT = 16
slot1 = True
slot2 = True
slot3 = True
availableSlots = 0
lcd=LCD()

GPIO.setmode(GPIO.BOARD) #consider complete rpi board
GPIO.setup(IR_PIN,GPIO.IN) #set pin function as input
GPIO.setup(IR_PIN2,GPIO.IN) #set pin function as input
GPIO.setup(IR_PIN3,GPIO.IN) #set pin function as input
GPIO.setup(IR_PINENTER,GPIO.IN) #set pin function as input
GPIO.setup(IR_PINEXIT,GPIO.IN) #set pin function as input

def ifDetected2(words):
    mydb = mysql.connector.connect(
    host="capstone.cayk5wnefmpg.us-east-1.rds.amazonaws.com",
    user="admin",
    password="nuttertools")
    
    mycursor = mydb.cursor ()
    mycursor.execute("use capstone;")
    sql = "select LicensePlate from ReservedParking WHERE LicensePlate = '{}' AND ParkingSlot = 2;".format(words)
    mycursor.execute(sql,)
    rows = mycursor.fetchone()
    if rows != None:
        data=mycursor.fetchall()
        print(rows[0])
        lcd.text("PARKING AT SLOT 2:",1)
        lcd.text(words,2)
        time.sleep(0.25)
    else:
        print("This is a reservation only slot, Kindly leave this slot as it is not reserved for you")
        lcd.text("This spot is reserved.",1)
        lcd.text("Please leave; thank you!",2)
    mydb.close()

def ifDetected1(words):
    mydb = mysql.connector.connect(
    host="capstone.cayk5wnefmpg.us-east-1.rds.amazonaws.com",
    user="admin",
    password="nuttertools")
    
    mycursor = mydb.cursor ()
    mycursor.execute("use capstone;")
    sql = "select LicensePlate from ReservedParking WHERE LicensePlate = '{}' AND ParkingSlot = 1;".format(words)
    mycursor.execute(sql,)
    rows = mycursor.fetchone()
    if rows != None:
        data=mycursor.fetchall()
        print(rows[0])
        lcd.text("THANK YOU FOR",1)
        lcd.text("PARKING WITH US!",2)
        time.sleep(0.25)
    else:
        print("This is a reservation only slot, Kindly leave this slot as it is not reserved for you")
        lcd.text("KINDLY LEAVE,THE",1)
        lcd.text("SPOT IS RESERVED",2)
        time.sleep(1)
    mydb.close()
    
def onLeave(words):
    mydb = mysql.connector.connect(
    host="capstone.cayk5wnefmpg.us-east-1.rds.amazonaws.com",
    user="admin",
    password="nuttertools")
    
    mycursor = mydb.cursor ()
    mycursor.execute("use capstone;")
    sql = "select * from ReservedParking WHERE licensePlate = '{}';".format(words)
    mycursor.execute(sql,)
    rows = mycursor.fetchone()
    if rows != None:
        ID = rows[0]
        ParkingSlot = rows[1]
        LicensePlate = rows[2]
        customerID = rows[3]
        EndDateTime = rows[4].strftime('%Y-%m-%d %H:%M:%S')
        StartDateTime = rows[5].strftime('%Y-%m-%d %H:%M:%S')
        now = datetime.now()
        currentTime = now.strftime('%Y-%m-%d %H:%M:%S')
        print("current time is",currentTime,"\nEnd time is ",EndDateTime)
        if(currentTime>EndDateTime):
            EndDateTime = currentTime
        sql = "DELETE FROM ReservedParking WHERE licensePlate = '{}';".format(words)
        mycursor.execute(sql,)
        sql = "INSERT INTO AvailableParking(ParkingSlot) VALUES({});".format(ParkingSlot)
        mycursor.execute(sql,)
        sql = "INSERT INTO ParkingHistory(LicensePlate,CustomerID,ParkingSlot,EntranceDate,ExitDate) VALUES('{}',{},{},'{}','{}');".format(LicensePlate,customerID,ParkingSlot,StartDateTime,EndDateTime)
        mycursor.execute(sql,)
        mydb.commit()
    else :
        print("Please Pay at the app")
        lcd.text("THANK YOU, PLEASE",1)
        lcd.text("PAY WITH THE APP",2)
        time.sleep(0.25)
        
def staticRecognition(path):
    img = cv2.imread(path,cv2.IMREAD_COLOR)
    img = cv2.resize(img, (600,400) )

    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY) 
    gray = cv2.bilateralFilter(gray, 13, 15, 15) 

    edged = cv2.Canny(gray, 30, 200) 
    contours = cv2.findContours(edged.copy(), cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    contours = imutils.grab_contours(contours)
    contours = sorted(contours, key = cv2.contourArea, reverse = True)[:10]
    screenCnt = None
    for c in contours:
        peri = cv2.arcLength(c, True)
        approx = cv2.approxPolyDP(c, 0.018 * peri, True)
        if len(approx) == 4:
            screenCnt = approx
            break
    if screenCnt is None:
        detected = 0
        print ("No contour detected")
    else:
         detected = 1

    if detected == 1:
        cv2.drawContours(img, [screenCnt], -1, (0, 0, 255), 3)

    mask = np.zeros(gray.shape,np.uint8)
    new_image = cv2.drawContours(mask,[screenCnt],0,255,-1,)
    new_image = cv2.bitwise_and(img,img,mask=mask)
    (x, y) = np.where(mask == 255)
    (topx, topy) = (np.min(x), np.min(y))
    (bottomx, bottomy) = (np.max(x), np.max(y))
    Cropped = gray[topx:bottomx+1, topy:bottomy+1]

    text = pytesseract.image_to_string(Cropped, config='--psm 11')
    print("Detected license plate Number is:",text.strip())
    img = cv2.resize(img,(500,300))
    Cropped = cv2.resize(Cropped,(400,200))
    cv2.imshow('car',img)
    cv2.imshow('Cropped',Cropped)
    cv2.waitKey(0)
    cv2.destroyAllWindows()
    return text.strip()
    
def processImage(path):
                      
    img=cv2.imread(path,cv2.IMREAD_COLOR)
    img=cv2.resize(img,(720,360))
    crop=img[0:300,0:600]
    gry=cv2.cvtColor(crop,cv2.COLOR_BGR2GRAY)
    gry=cv2.bilateralFilter(gry,13,15,15)
    thr=cv2.threshold(gry,0,255,cv2.THRESH_BINARY_INV+cv2.THRESH_OTSU)[1]
    kernel = np.ones((5, 5), np.uint8)
    opening=cv2.morphologyEx(thr, cv2.MORPH_OPEN, kernel)
    words=pytesseract.image_to_string(opening)
    platenum=words.strip()
    print(platenum)

def slot2():
    camera = PiCamera() #camera is turned on
    camera.start_preview()
    time.sleep(3)
    camera.stop_preview()
    camera.resolution = (600, 360)
    camera.exposure_mode = 'off'
    path = "/home/pi/Desktop/autopark/pictures/"
             # Camera warm-up time
    sleep(0.1)
    camera.capture(path+'slot2.jpg')
    
    processImage("/home/pi/Desktop/autopark/pictures/slot2.jpg")
    camera.close()
    
def slot1():
    path = "/home/pi/Desktop/autopark/pictures/"
    img=cv2.imread(path+'jeep.jpg')
    img=cv2.resize(img,(720,360))
    crop=img[0:300,0:600]
    gry=cv2.cvtColor(crop,cv2.COLOR_BGR2GRAY)
    thr=cv2.threshold(gry,0,255,cv2.THRESH_BINARY_INV+cv2.THRESH_OTSU)[1]
    kernel = np.ones((5, 5), np.uint8)
    opening=cv2.morphologyEx(thr, cv2.MORPH_OPEN, kernel)
    cannyy=cv2.Canny(opening, 100, 200)
    words=pytesseract.image_to_string(cannyy)
    platenum=words[0:8]
    print(platenum)

    cv2.imshow('img', cannyy)
    #cv2.destroyAllWindows()
    #exec(open("ir_trigger_cam.py").read())
    cv2.waitKey(0)

def updateAvaliableParking():
    availableSlots=0
    if(slot1):
        availableSlots += 1
    if(slot2):
        availableSlots += 1
    if(slot3):
        availableSlots += 1
        
while True: #check if something is there
    if(GPIO.input(IR_PINENTER) == 0):  #IR SENSOR AT ENTRANCE NEAR LCD DISPLAY
        print("SLOT 1&2 RESERVED, SLOT3 AVAILABLE, IF RESERVED, PARK AT SLOT1&2 ELSE AT SLOT 3") #if car detected, 
        lcd.text("SLOT 1&2: FOR",1) #show spaces available
        lcd.text("RESERVATION ONLY",2)
        time.sleep(2)
        lcd.text("SLOT 3: FOR ANY",1)
        lcd.text("CUSTOMER",2)
        time.sleep(1)
    else:
        print("AUTOPARK") #if not show name
        lcd.text("WELCOME TO",1)
        lcd.text("       AUTOPARK",2)
        time.sleep(2)
        
    if(GPIO.input(IR_PIN) == 0): #if slot 1 is taken 
        slot1=False
        lcd.text("CHECKING VEHICLE",1)
        lcd.text("STATUS...",2)
        time.sleep(0.25)
        try:
            platenum = staticRecognition("/home/pi/Desktop/autopark/pictures/cartest.jpg")
            ifDetected1(platenum)
            #slot1()
        except Exception:
            traceback.print_exc()
            print("error calling slot1()")
        time.sleep(1)
    else:
        print("PARKING SLOT 1 IS EMPTY")
        time.sleep(0.5)
    
    if(GPIO.input(IR_PIN2) == 0):
        print("PARKING SLOT 2 IS OCCUPIED")
        lcd.text("CHECKING VEHICLE",1)
        lcd.text("STATUS...",2)
        time.sleep(0.25)
        try:
            time.sleep(5)
            slot2()
        except Exception:
            traceback.print_exc()
        time.sleep(1)
        
    else:
        print("PARKING SLOT 2 IS EMPTY")
        time.sleep(0.5)
        
    if(GPIO.input(IR_PIN3) == 0):
        print("PARKING SLOT 3 IS OCCUPIED")
        lcd.text("CHECKING VEHICLE",1)
        lcd.text("STATUS...",2)
        time.sleep(0.25)
        try:
            time.sleep(5)
            slot2()
            #exec(open("slot2.py").read())
        except Exception:
            traceback.print_exc()
        time.sleep(1)
                
    else:
        print("PARKING SLOT 3 IS EMPTY")
        time.sleep(0.5)
    
    if(GPIO.input(IR_PINEXIT) == 0): 
        print("THANK YOU FOR PARKING")
        lcd.text("PLEASE WAIT WHILE",1)
        lcd.text("WE PROCESS YOUR BILL",2)
        time.sleep(1)
        platenum = staticRecognition("/home/pi/Desktop/autopark/pictures/cartest.jpg")
        onLeave(platenum)
    else:
        #print("AUTOPARK")
        #lcd.text("AUTOPARK",1)
        time.sleep(0.5)



    

