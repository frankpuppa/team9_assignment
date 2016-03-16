# team9_assignment

## Notes for Team

Simple rules to stick to really.
- Fork this repository to your github account.
- Follow the Fork-Branch workflow as explained here http://blog.scottlowe.org/2015/01/27/using-fork-branch-git-workflow/
- Once you're done with your work, make a pull request to me.
- Master branch should be ready for deployement at any given time. Therfore, make sure your code is tested before submitting a pull request.


## Notes for Mark:
### Android Studio notes:

We decided not to use Eclipse or Netbeans and opted for Android Studio. We recommend opening the project in Android Studio.
When doing so, please make sure you don't open the folder containing the project, instead simply open the **build.gradle** file. This should allow the IDE to install all dependencies automatically.

### Testing the API
Please note that these are only guidelines and that there are different ways of accomplishing the same results.
Also these are steps to follow if you're using a Debian based distribution.

Make sure these are installed in your general environment first:
#### Python3 (any version) 
`sudo apt-get install python3`

#### VirtualEnv 
  `sudo apt-get install virtualenv`
#### A few mysql needed packages sudo 
  `sudo apt-get install python-dev python3-dev`
  And then :
     `sudo apt-get install libmysqlclient-dev`

#### Setup a virtual environment:
  `virtualenv -p /usr/bin/python3 venv`
  
#### Run your virtual environment:
  `source venv/bin/activate`

#### Finally:
- cd onto the folder where the file **requirements.txt** resides `cd team9_assignment/WebApi/`
- install the dependencies `pip install -r requirements.txt`
- run the tests `python manage.py test`

Tests can be found at `team9_assignment/WebApi/android/tests.py`

