import json
from django.utils import timezone
from django.db.models.query import Q
from android.serializers import *
from rest_framework import generics
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
import json


# Returns a list of modules that a specific staff memember teaches/coordinates
@api_view(['GET'])
def staff_module_list(request, pk):
    try:
        modules = Module.objects.filter(coordinators=pk)
    except Module.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = StaffModuleListSerializer(modules, many=True)
        return Response(serializer.data)


@api_view(['GET'])
def module_attendance_by_semester(request, pk):
    try:
        all_classes = Class.objects.filter(module_id=pk).order_by('class_type')
    except Module.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = SemesterAttendanceSerializer(all_classes, many=True)
        return Response(serializer.data)



@api_view(['GET'])
def student_attendance_to_module(request, pk, sid):
    data = []
    student = Student.objects.get(matric_number=sid)
    module = Module.objects.get(moduleid=pk)
    classes = Class.objects.filter(module=module).all()
    students = module.students_enrolled.all()

    if student not in students:
        return Response(status=status.HTTP_404_NOT_FOUND)

    for cls in classes:
        val = {}
        val['week'] = cls.week
        val['class_type'] = cls.class_type
        val['weekday'] = cls.start_time.weekday()
        val['date'] = str(cls.start_time.strftime("%d-%m-%Y"))
        val['start_time'] = str(cls.start_time.strftime("%H:%M"))
        if student in cls.class_register.all():
            val['attended'] = 'yes'
        else:
            val['attended'] = 'no'
        data.append(val)

    json_obj = json.dumps(data)
    return Response(json.loads(json_obj))


@api_view(['GET'])
def module_attendance(request, pk):
    data = []
    try:
        students = Module.objects.get(moduleid=pk).students_enrolled
        classes = Class.objects.filter(module=pk)
        number_of_classes = classes.count()
    except Module.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    for student in students.all():
        stdnt = {}
        count = 0
        for cls in classes.iterator():
            if student in cls.class_register.all():
                count += 1
        if count != 0:
            percentage = int(100 * count / number_of_classes)
        else:
            percentage = 0
        stdnt['percentage'] = repr(percentage) + '%'
        stdnt['first_name'] = student.first_name
        stdnt['last_name'] = student.last_name
        stdnt['matric_number'] = student.matric_number
        data.append(stdnt)
    json_obj = json.dumps(data)
    return Response(json.loads(json_obj))

# Returns a list of student attendance for a specific module for a specific week
@api_view(['POST'])
def module_attendance_by_week(request):

    if request.method == 'POST':
        module_id = request.data['module_id']
        week = request.data['week']
    try:
        class_id = Class.objects.filter(module_id=module_id)
        attendance = class_id.filter(week=week)

    except Module.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'POST':
        serializer = ModuleAttendanceSerializer(attendance, many=True)
        return Response(serializer.data)


# Returns the list of all classes that are linked to a specific module
@api_view(['GET'])
def module_classes(request, pk):
    try:
        classes = Class.objects.filter(module=pk)
    except Class.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = ClassSerializer(classes, many=True)
        return Response(serializer.data)


# Returns a list of students that are enrolled to a given module.
@api_view(['GET'])
def module_enrollment_list(request, pk):
    try:
        students = Module.objects.get(moduleid=pk).students_enrolled
    except Module.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = StudentSerializer(students, many=True)
        return Response(serializer.data)


# Returns a list of students that have attended to a given class.
@api_view(['GET'])
def class_register(request, pk):
    try:
        students = Class.objects.get(id=pk).class_register
    except Module.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = StudentSerializer(students, many=True)
        return Response(serializer.data)


# Returns a list of all students.
class StudentList(generics.ListCreateAPIView):
    queryset = Student.objects.all()
    serializer_class = StudentSerializer

# Allows deletion, update, retrieval of a Student instance in the db.
class StudentDetail(generics.RetrieveUpdateDestroyAPIView):
    queryset = Student.objects.all()
    serializer_class = StudentSerializer


# Returns a list of all Staff members
class StaffList(generics.ListCreateAPIView):
    queryset = Staff.objects.all()
    serializer_class = StaffSerializer


# Allows deletion, update, retrieval of a Staff instance in the db.
class StaffDetail(generics.RetrieveUpdateDestroyAPIView):
    queryset = Staff.objects.all()
    serializer_class = StaffSerializer


# Returns a list of all modules
class ModuleList(generics.ListCreateAPIView):
    queryset = Module.objects.all()
    serializer_class = ModuleSerializer


# Allows deletion, update, retrieval of a module instance in the db.
class ModuleDetail(generics.RetrieveUpdateDestroyAPIView):
    queryset = Module.objects.all()
    serializer_class = ModuleSerializer


# Returns a list of all classes
class ClassList(generics.ListCreateAPIView):
    queryset = Class.objects.all()
    serializer_class = ClassSerializer


# REFACTORED (ORIGINAL CODE COMMENTED OUT AT THE BOTTOM OF THIS FILE)
# Allows deletion, update, retrieval of a Class instance in the db including its class register.
class ClassRegister(generics.RetrieveUpdateDestroyAPIView):
    queryset = Class.objects.all()
    serializer_class = RegisterSerializer


class ClassSign(generics.UpdateAPIView):
    serializer_class = ClassSerializer

    def put(self, request, format=None):
        roomid = request.data["room_id"]
        studentid = request.data["student_id"]
        
        try:
            student = Student.objects.get(matric_number=studentid)
            
            # Select all classes which start within +/- 60 minutes in the specified room
            now = timezone.now()
            startTime = now + timezone.timedelta(minutes=-60)
            endTime = now + timezone.timedelta(minutes=60)
            classes = Class.objects.filter(Q(start_time__range=(startTime, endTime)) & Q(room_id=roomid))
            
            result = 'success'
            responseStatus = status.HTTP_200_OK
            
            signedIn = False
            action = 0
            
            # Find the classes which are within the 45 minutes sign in window that the student is enrolled in and sign the student into them
            for thisClass in classes:
                minutesToStart = (thisClass.start_time - now).total_seconds() / 60
                if minutesToStart <= 15 and minutesToStart >= -30:
                    action = 1
                    if Module.objects.filter(
                                    Q(students_enrolled__exact=studentid) & Q(moduleid=thisClass.module_id)).count() != 0:
                        if thisClass.class_register.filter(matric_number=studentid).count() != 0:
                            action = 2
                        else:
                            thisClass.class_register.add(student)
                            signedIn = True

            if signedIn == True:
                result = 'Student id ' + str(
                    studentid) + ' has been signed into all classes with an open register in room id ' + str(roomid)
            else:
                if action == 0:
                    result = 'There are no classes with an open register taking place in room id ' + str(
                        roomid) + ' at this time.'
                    responseStatus = status.HTTP_404_NOT_FOUND
                elif action == 1:
                    result = 'There is a class taking place in ' + str(roomid) + ' but student id ' + str(
                        studentid) + ' is not enrolled in this class.'
                    responseStatus = status.HTTP_404_NOT_FOUND
                elif action == 2:
                    result = 'Student id ' + str(
                        studentid) + ' is already signed into all classes currently available in room id ' + str(roomid)
                    responseStatus = status.HTTP_409_CONFLICT

            content = {
                'result': result
            }

            return Response(content, status=responseStatus)
        except(KeyError, Student.DoesNotExist):
            return Response("The specified student does not exist.", status.HTTP_404_NOT_FOUND)
        except(ValueError):
            return Response("Invalid input.", status.HTTP_400_BAD_REQUEST)


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


# class ClassRegister(generics.ListCreateAPIView):
#     serializer_class = StudentSerializer
#     def get(self, request, format=None):
#         searchid = request.data["class_id"]
#
#         classToCheck = Class.objects.filter(classid = searchid)[:1].get()
#         signedIn = classToCheck.class_register.all()
#         studentsOnModule = Module.objects.filter(moduleid = classToCheck.module_id)[:1].get().students_enrolled.all()
#
#         registerList = list(chain(signedIn, studentsOnModule))
#         registerList = sorted(registerList, key = lambda instance: instance.last_name)
#         seen = []
#         resultList = []
#
#         for student in registerList:
#             found = False
#             for seenStudent in seen:
#                 if student.matric_number == seenStudent:
#                     found = True
#                     break
#
#             if found == False:
#                 seen.append(student.matric_number)
#                 hasSigned = False
#                 for signedStudent in signedIn:
#                     if signedStudent.matric_number == student.matric_number:
#                         hasSigned = True
#
#                 student.has_signed = hasSigned;
#                 resultList.append(student)
#
#
#         serializer = ClassRegisterSerializer(resultList, many=True)
#         return Response(serializer.data)
#
#         responseStatus = status.HTTP_200_OK
#         return Response(content, status = responseStatus)