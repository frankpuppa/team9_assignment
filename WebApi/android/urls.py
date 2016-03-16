from django.conf.urls import url
from rest_framework.urlpatterns import format_suffix_patterns
from android import views

urlpatterns = [
    url(r'^students/sign/$', views.ClassSign.as_view()),
    url(r'^staffModuleList/(?P<pk>[0-9]+)$', views.staff_module_list),
    url(r'^moduleAttendanceList/$', views.module_attendance_by_week),
    url(r'^moduleAttendance/(?P<pk>[0-9]+)/$', views.module_attendance),
    url(r'^studentAttendance/(?P<pk>[0-9]+)/(?P<sid>[0-9]+)/$', views.student_attendance_to_module),
    url(r'^module_enrollments/(?P<pk>[0-9]+)/$', views.module_enrollment_list),
    url(r'^modules/$', views.ModuleList.as_view()),
    url(r'^modules/(?P<pk>[0-9]+)/$', views.ModuleDetail.as_view()),
    url(r'^classes/$', views.ClassList.as_view()),
    url(r'^classes/(?P<pk>[0-9]+)/$', views.ClassRegister.as_view()),
    url(r'^class_register/(?P<pk>[0-9]+)/$', views.class_register),

    url(r'^login/$', views.login),
    url(r'^students/$', views.StudentList.as_view()),
    url(r'^staff/$', views.StaffList.as_view()),
    url(r'^students/(?P<pk>[0-9]+)/$', views.StudentDetail.as_view()),
    url(r'^staff/(?P<pk>[0-9]+)/$', views.StaffDetail.as_view()),
    url(r'^module_classes/(?P<pk>[0-9]+)/$', views.module_classes),
]

urlpatterns = format_suffix_patterns(urlpatterns)
