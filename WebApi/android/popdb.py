from android.models import *
import csv
from datetime import datetime, timedelta
from random import shuffle

now = datetime.now()
later = now + timedelta(hours=1)
tomorrow = now + timedelta(days=1)

# CREATING STAFF AND STUDENTS
number = 1
password = 'password'

with open('uk-500.csv', 'r') as f:
    reader = csv.reader(f)
    next(reader, None)
    for row in reader:
        first_name = row[0]
        last_name = row[1]
        if number > 10:
            email = row[0].lower() + row[1].lower() + repr(number) + '@student.com'
            matric_number = number
            student = Student(matric_number=matric_number, first_name=first_name, last_name=last_name,
                          email=email, password=password)
            student.save()
        else:
            email = row[0].lower() + row[1].lower() + repr(number) + '@staff.com'
            staff_id = number
            staff = Staff(staffid=staff_id, first_name=first_name, last_name=last_name,
                          email=email, password=password)
            staff.save()
        number += 1


#CREATE A BUNCH OF MODULES
module1 = Module(moduleid=1000, module_code='AC0021', module_title='Programming With Java')
module2 = Module(moduleid=1001, module_code='AC0031', module_title='Programming With Python')
module3 = Module(moduleid=1002, module_code='AC0033', module_title='Programming With Haskell')
module4 = Module(moduleid=1003, module_code='AC0034', module_title='Programming With Perl')
module5 = Module(moduleid=1004, module_code='AC0035', module_title='Agile Programming')
module6 = Module(moduleid=1005, module_code='AC0036', module_title='Games Programming')
module7 = Module(moduleid=1006, module_code='AC0037', module_title='Algorithms and Data Structures')
module8 = Module(moduleid=1007, module_code='AC0038', module_title='Topics in Pure Mathemathics')
module9 = Module(moduleid=1008, module_code='AC0039', module_title='Human Computer Interaction')
module10 = Module(moduleid=1009, module_code='AC0040', module_title='Operating Systems')

module1.save()
module2.save()
module3.save()
module4.save()
module5.save()
module6.save()
module7.save()
module8.save()
module9.save()
module10.save()


# Add a few module coordinators
staff = Staff.objects.get(staffid=1)
module1.coordinators.add(staff)
staff = Staff.objects.get(staffid=2)
module2.coordinators.add(staff)
staff = Staff.objects.get(staffid=3)
module3.coordinators.add(staff)
staff = Staff.objects.get(staffid=4)
module4.coordinators.add(staff)
staff = Staff.objects.get(staffid=5)
module5.coordinators.add(staff)
staff = Staff.objects.get(staffid=6)
module6.coordinators.add(staff)
staff = Staff.objects.get(staffid=7)
module7.coordinators.add(staff)
staff = Staff.objects.get(staffid=8)
module8.coordinators.add(staff)
staff = Staff.objects.get(staffid=9)
module9.coordinators.add(staff)
staff = Staff.objects.get(staffid=10)
module10.coordinators.add(staff)

students = Student.objects.all()

## ADD A BUNCH OF STUDENTS ENROLLED TO THE MODULE
for i in range(len(students)):
    if i in range(50):
        module1.students_enrolled.add(students[i])
    if i in range(50,100):
        module2.students_enrolled.add(students[i])
    if i in range(100,150):
        module3.students_enrolled.add(students[i])
    if i in range(150,200):
        module4.students_enrolled.add(students[i])
    if i in range(200,250):
        module5.students_enrolled.add(students[i])
    if i in range(250,300):
        module6.students_enrolled.add(students[i])
    if i in range(300,350):
        module7.students_enrolled.add(students[i])
    if i in range(350, 400):
        module8.students_enrolled.add(students[i])
    if i in range(400,450):
        module9.students_enrolled.add(students[i])
    else:
        module10.students_enrolled.add(students[i])

## COMMIT THE CHANGES
module1.save()
module2.save()
module3.save()
module4.save()
module5.save()
module6.save()
module7.save()
module8.save()
module9.save()
module10.save()

cls1 = Class(room_id='qmbsmr', start_time=now, qrCode=0, end_time=later, module=module1,
            week=1, class_type='Seminar')
cls1.save()
cls2 = Class(room_id='qmbsmr', start_time=now + timedelta(days=1), qrCode=0, end_time=later + timedelta(days=1), module=module1,
            week=1, class_type='Lab')
cls2.save()
cls3 = Class(room_id='qmbsmr', start_time=now - timedelta(days=1), qrCode=0, end_time=later - timedelta(days=1), module=module1,
            week=1, class_type='Seminar')
cls3.save()
cls4 = Class(room_id='qmbsmr', start_time=now - timedelta(days=2), qrCode=0, end_time=later - timedelta(days=2), module=module1,
            week=1, class_type='Lecture')
cls4.save()
cls5 = Class(room_id='qmbsmr', start_time=now - timedelta(days=3), qrCode=0, end_time=later - timedelta(days=3), module=module1,
            week=1, class_type='Seminar')
cls5.save()

stds = module1.students_enrolled.all()
for i in range(len(stds) - 7):
    cls1.class_register.add(stds[i])

for i in range(len(stds) - 20):
    cls2.class_register.add(stds[i])

for i in range(len(stds) - 30):
    cls3.class_register.add(stds[i])

for i in range(len(stds) - 2):
    cls4.class_register.add(stds[i])

for i in range(len(stds) - 25):
    cls5.class_register.add(stds[i])

cls1.save()
cls2.save()
cls3.save()
cls4.save()
cls5.save()