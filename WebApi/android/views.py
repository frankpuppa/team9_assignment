from android.models import *
from android.serializers import *
from rest_framework import generics
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
import datetime



@api_view(['GET', 'PUT', 'DELETE'])
def staff_module_list(request, pk):

    try:
        modules = Module.objects.filter(coordinators=pk)
    except Module.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = StaffModuleListSerializer(modules, many=True)
        return Response(serializer.data)


@api_view(['GET'])
def module_enrollment_list(request, pk):

    try:
        students = Module.objects.get(moduleid=pk).students_enrolled
    except Module.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = StudentSerializer(students, many=True)
        return Response(serializer.data)


class StudentList(generics.ListCreateAPIView):
    queryset = Student.objects.all()
    serializer_class = StudentSerializer


class StudentDetail(generics.RetrieveUpdateDestroyAPIView):
    queryset = Student.objects.all()
    serializer_class = StudentSerializer


class StaffList(generics.ListCreateAPIView):
    queryset = Staff.objects.all()
    serializer_class = StaffSerializer


class StaffDetail(generics.RetrieveUpdateDestroyAPIView):
    queryset = Staff.objects.all()
    serializer_class = StaffSerializer


@api_view(['POST'])
def login(request):
    if request.method == 'POST':
        email = request.data['email_address']
        password = request.data['password']
        try:
            staff = Staff.objects.get(email=email)
            if staff.password == password:
                serializer = StaffLoginSerializer(staff)
                return Response(serializer.data)
            return Response(status=status.HTTP_406_NOT_ACCEPTABLE)
        except(KeyError, Staff.DoesNotExist):
            try:
                student = Student.objects.get(email=email)
                if student.password == password:
                    serializer = StudentLoginSerializer(student)
                    return Response(serializer.data)
                return Response(status=status.HTTP_406_NOT_ACCEPTABLE)
            except(KeyError, Student.DoesNotExist):
                return Response(status=status.HTTP_406_NOT_ACCEPTABLE)
    return Response(status=status.HTTP_405_METHOD_NOT_ALLOWED)


@api_view(['PUT'])
def sign_to_class(request):
    if request.method == 'PUT':
        now = datetime.datetime.now().hour
        student_id = request.data['student_id']
        room_id = request.data['room_id']

        if Class.objects.filter(start_time__hour=now, room_id=room_id).exists():
            cls = Class.objects.filter(start_time__hour=now).filter(room_id=room_id)[0]
            try:
                student = Student.objects.get(matric_number=student_id)
                cls.class_register.add(student)
                cls.save()
            except(KeyError, Student.DoesNotExist):
                return Response(status=status.HTTP_204_NO_CONTENT)
            return Response(status=status.HTTP_201_CREATED)
    return Response(status=status.HTTP_406_NOT_ACCEPTABLE)
