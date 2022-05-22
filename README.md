# Parking_Application

Parking and finding a vacant spot nowadays could be time-consuming due to increasing

private vehicles. This problem affects car owners, but it also wastes private and public re-
sources like electricity and local authorities, as they have to deal with congestion. This study

proposes an intelligent parking and data management system that would reduce the congestion
problem and increase the efficiency and profitability of the parking lot. Instead of a traditional
parking lot that depends on guards, lacks security, collects no data, and takes a long time to find
a parking lot, our proposed system uses the Internet of Things to process data and depends on a

network of sensors, cameras, and image processing algorithms to automate the system. The sys-
tem also comes with an android application used for using the parking system. Users will use

the application to reserve parking, find available spots, and pay the parking fee. Parking man-
agers will have records of all transactions and vehicles that used the system and analyze the

data to improve the system. The system will be built with a Client-Server architecture that has
four main nodes. The server will consist of a Raspberry Pi connected to several IR sensors and
cameras. The client will be built as an Android application. The database will be hosted using

Amazon Relational Database Service, and a Microsoft Access application will be used to man-
age the system's data.

The prototype built in this project used IR sensors at the entrance to trigger the LCD to
show the parking lot status to guide the driver on where to park. The IR sensors at each parking
spot and the exit trigger the camera to take a picture of the vehicle's license plate and send it to
the system. Then, the system will apply the digital image processing technique to the image.
The algorithm comprises different stages such as grayscale conversion, threshold conversion,
image edge detection, etc. Afterward, the resulting data are compared against records in a
stored database. Users will use the application to reserve parking, find available spots, and pay
the parking fee.

----
To implement an intelligent parking management system, we came up with requirements

that our system must satisfy. Both the functional and non-functional requirements are stated be-
low:

• Functional Requirements
o Hardware:
§ Detection sensors for entrance, exit, and individual parking slots
§ Number board for each parking line identifying the number of available
parking spots in that line
§ Camera for every 2-3 parking spots to identify license plate
• Software (Application):
§ Check availability of parking spots and have a reservation system
§ System to deal with parking spots taken by someone else
§ Sufficient data management
§ Data analysis capabilities
§ Wireless communication between devices (hardware and software) and
the server
§ Cloud hosting service (AWS)
§ User friendly GUI (in Java)
§ Accurate calculations of the price based on parking rates
§ Controlled by different users (admin and customer)
Non-Functional Requirements

• Real-Time updates between the server, display boards, the application and the
sensors
• Wi-Fi availability in the parking lot (for the system and the customers)
• The backend (the database) should be available and accessible via any platform.
• Confidentiality on user’s data.
• Concurrent request handling by the server.

One of our major objectives for the software application is to ensure it has the capacity
to reserve parking, based on different locations, selected time by the user, and dependent on the
specific license plate number. The application also needed an effective database to help with
data analysis and be usable on mobile phones.
One of the goals for the hardware setup of this project is to ensure that the IR sensors

can accurately sense the objects in front of them and effectively communicate with the Rasp-
berry Pi and its other components, such as the LCD and the camera. Another is to have the

camera be optimal to take the most precise picture of the license plates so that the LPR algo-
rithm can recognize the plate characters. Finally, another objective is to have the LPR algo-
rithm include a preprocessing system that ensures that the license plate characters are detected

as accurately as possible, and that the database is receiving the correct information.


![image](https://user-images.githubusercontent.com/38332442/169718733-31a81c5d-2be0-441b-9a0e-62fc51a2838e.png)
![image](https://user-images.githubusercontent.com/38332442/169718752-8ea59f76-64f4-43b8-81ce-aa350de667dc.png)
![image](https://user-images.githubusercontent.com/38332442/169718764-74b64e2b-9b29-4864-90f9-54792a2e6f9d.png)
![image](https://user-images.githubusercontent.com/38332442/169718775-37653935-e88d-4b8a-97e1-e2ff26ff68a8.png)

P.S: The Raspberry Pi/Server logic can be seen in the sever.py file
