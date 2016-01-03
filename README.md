# UDP-CursorLand
This is an experimental project which uses the UDP protocol in IP Multicasting.
![Screenshot of the program](screenshot.png?raw=true "Program Screenshot")

#What does it do?
This program opens up a window. You can move your cursor around the window, and any other client connected to the same network as you are on can also see your cursor too, and vice versa. This program has no limitations to how many clients it can support, only the network itself. What's cool is that this entire program is serverless, and is a peer-to-peer program.

#How does it work?
The heart of the program is that it uses IP Multicasting. In IP Multicasting, there are groups that any client can join. If a packet is send inside the group, all of the other clients will recieve it as well. This program broadcasts the cursor position inside the group whenever the cursor is moved.

#Can anyone in the world see my cursor?
No, only the people connected to the network you're in can see it.
