# About:
   basicaly it is an api for hack tinder system
   with this you can get sure match and see who
   likes you for free, enjoy!

# Setup:

1) get your auth token for tinder from console
   do follow command: "localStorage.getItem('TinderWeb/APIToken')"

![alt text](https://i.imgur.com/D6Kngbf.gif)

2) go in class "Storage" on package "it.adrian.code.storage"
   and put your token here:
   
![alt text](https://i.imgur.com/4HttIcu.png)
   
3) now you can start server (this running on port 8000)
   if you want you can change it here:
   
![alt text](https://i.imgur.com/TLcjD62.png)

# Paths:

1) (your_host)/api/decision (this is for sending like or dislike)
2) (your_host)/api/search (show single girl or a girls [you can specify range])
3) (your_host)/api/admirers (show who likes you with user_id in jsonarray)
4) (your_host)/api/sendMessage (this is for sending message to your match)
5) (your_host)/api/getMatchs (show list of your matchs id)

# Note:
   Tinder have limits for the mans for example:
    - 100 like in 12h
