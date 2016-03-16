from django.db import models
import os, binascii


# Create your models here.


class Staff(models.Model):
    staff_id = models.IntegerField(primary_key=True)
    email = models.CharField(max_length=60)
    first_name = models.CharField(max_length=30)
    last_name = models.CharField(max_length=30)
    password = models.CharField(max_length=30)
    hash = models.CharField(max_length=128, null=True)

    def create_hash(self):
        return "staff" #+ binascii.b2a_hex(os.urandom(15)).decode()

    def save(self, *args, **kwargs):
        self.hash = self.create_hash()
        super(Staff, self).save(*args, **kwargs)


class Student(models.Model):
    email = models.CharField(max_length=60)
    matric_number = models.IntegerField(primary_key=True)
    first_name = models.CharField(max_length=30)
    last_name = models.CharField(max_length=30)
    password = models.CharField(max_length=30)
    hash = models.CharField(max_length=128,  null=True)

    def create_hash(self):
        return "student" #+ binascii.b2a_hex(os.urandom(15)).decode()

    def save(self, *args, **kwargs):
        # check if the row with this hash already exists.
        self.hash = self.create_hash()
        super(Student, self).save(*args, **kwargs)


class Module(models.Model):
    module_id = models.IntegerField(primary_key=True)
    module_code = models.CharField(max_length=20)
    module_title = models.CharField(max_length=50)
    coordinators = models.ManyToManyField(Staff)
    students_enrolled = models.ManyToManyField(Student)


class Class(models.Model):
    qrCode = models.IntegerField()
    start_time = models.DateTimeField()
    room_id = models.CharField(max_length=10)
    end_time = models.DateTimeField()
    building = models.CharField(max_length=20)
    class_register = models.ManyToManyField(Student)


