from django.db import models


class Staff(models.Model):
    staffid = models.IntegerField(primary_key=True)
    email = models.CharField(max_length=60)
    first_name = models.CharField(max_length=30)
    last_name = models.CharField(max_length=30)
    password = models.CharField(max_length=30)
    hash = models.CharField(max_length=128, null=True)

    def create_hash(self):
        return "staff"

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
        return "student"

    def save(self, *args, **kwargs):
        # check if the row with this hash already exists.
        self.hash = self.create_hash()
        super(Student, self).save(*args, **kwargs)


class Module(models.Model):
    moduleid = models.IntegerField(primary_key=True)
    module_code = models.CharField(max_length=20)
    module_title = models.CharField(max_length=50)
    coordinators = models.ManyToManyField(Staff, related_name='modules')
    students_enrolled = models.ManyToManyField(Student)

    def __unicode__(self):
        return self.moduleid


class Class(models.Model):
    qrCode = models.IntegerField()
    week = models.IntegerField(null=True)
    class_type = models.CharField(max_length=256)
    start_time = models.DateTimeField()
    room_id = models.CharField(max_length=10)
    end_time = models.DateTimeField()
    building = models.CharField(max_length=20)
    module = models.ForeignKey(Module, related_name='classes')
    class_register = models.ManyToManyField(Student)
