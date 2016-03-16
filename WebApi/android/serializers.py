from rest_framework import serializers
from android.models import *


# List all students
class StudentSerializer(serializers.ModelSerializer):
    class Meta:
        model = Student
        fields = ('matric_number', 'email', 'first_name', 'last_name')


# List all staff
class StaffSerializer(serializers.ModelSerializer):
    modules = serializers.PrimaryKeyRelatedField(many=True, read_only=True)
    class Meta:
        model = Staff
        fields = ('staffid', 'email', 'first_name', 'last_name', 'modules')


# List all modules, including an array of their staff coordinators
class ModuleSerializer(serializers.ModelSerializer):
    classes = serializers.PrimaryKeyRelatedField(many=True, read_only=True)
    class Meta:
        model = Module
        fields = ('moduleid', 'module_code', 'module_title', 'coordinators', 'students_enrolled', 'classes')



# List all classes including the class register (attending students as an array of their ids)
class ClassSerializer(serializers.ModelSerializer):
    class Meta:
        model = Class
        fields = ('id', 'qrCode', 'start_time', 'end_time' , 'room_id', 'building', 'module', 'class_register')


class ClassRegisterSerializer(serializers.ModelSerializer):
    class Meta:
        model = Student
        fields = ('matric_number', 'email', 'first_name', 'last_name', 'has_signed')


## Jamie's class
>>>>>>> upstream/master
class StaffModuleListSerializer(serializers.ModelSerializer):
    class Meta:
        model = Module
        fields = ('moduleid', 'module_code', 'module_title', 'coordinators')


## Jamie's class
class ModuleAttendanceSerializer(serializers.ModelSerializer):

    class Meta:
        model = Class
        fields = ('week', 'class_type', 'class_register')


# lists the role and id of the staff trying to login
class StaffLoginSerializer(serializers.ModelSerializer):
    class Meta:
        model = Staff
        fields = ('hash', 'staffid')


# lists the role and id of the student trying to login
class StudentLoginSerializer(serializers.ModelSerializer):
    class Meta:
        model = Student
        fields = ('hash', 'matric_number')


class RegisterSerializer(serializers.ModelSerializer):
    class Meta:
        model = Class
        fields = ('class_register', )
