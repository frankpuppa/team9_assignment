from rest_framework.test import APITestCase
from rest_framework import status
from android.models import Staff, Student, Class
from datetime import datetime, timedelta



class LoginTest(APITestCase):

    def setUp(self):
        now = datetime.now()
        later = now + timedelta(hours=9)
        staff1 = Staff(staffid=101, email= 'staff01@test.com', first_name='John', last_name='Doe',
                  password='password', hash= 'staff')
        staff2 = Staff(staffid=102, email= 'staff02@test.com', first_name='Dave', last_name='Doe',
                  password='password', hash= 'staff')
        student1 = Student(matric_number=201, email= 'student01@test.com', first_name='John', last_name='Coltrane',
                  password='password', hash= 'student')
        student2 = Student(matric_number=202, email= 'student02@test.com', first_name='Dave', last_name='Coltrane',
                  password='password', hash= 'student')

        cls = Class(room_id='qmbsmr', start_time= now, qrCode=120, end_time=later)

        cls.save()
        staff1.save()
        student1.save()
        staff2.save()
        student2.save()
        cls.class_register.add(student2)
        cls.save()

    def test_cant_login_if_not_registered(self):
        url = '/login/'
        data = {'email_address': 'doesntexist', 'password': 'wrong'}
        response = self.client.post(url, data)
        self.assertEqual(response.status_code, status.HTTP_406_NOT_ACCEPTABLE)

    def test_staff_can_login_if_registered(self):
        url = '/login/'
        data = {'email_address': 'staff01@test.com', 'password': 'password'}
        response = self.client.post(url, data)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response.data, {'staffid': 101, 'hash': 'staff'})

    def test_studet_can_login_if_registered(self):
        url = '/login/'
        data = {'email_address': 'student01@test.com', 'password': 'password'}
        response = self.client.post(url, data)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response.data, {'matric_number': 201, 'hash': 'student'})

    def test_list_all_students(self):
        url = '/students/'
        response = self.client.get(url)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(len(response.data), 2)

    def test_list_all_staff(self):
        url = '/staff/'
        response = self.client.get(url)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(len(response.data), 2)

    def test_sign_in_updates_database(self):
        url = '/student_signin/'
        data = {'student_id': '201', 'room_id': 'qmbsmr'}
        response = self.client.put(url, data)
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)