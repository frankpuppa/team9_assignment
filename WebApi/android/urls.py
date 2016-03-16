from django.conf.urls import url
from rest_framework.urlpatterns import format_suffix_patterns
from android import views


urlpatterns = [
    url(r'^students/$', views.StudentList.as_view()),
    url(r'^staff/$', views.StaffList.as_view()),
    url(r'^staffModuleList/(?P<pk>[0-9]+)$', views.staff_module_list),
    url(r'^module_enrollments/(?P<pk>[0-9]+)/$', views.module_enrollment_list),
    # url(r'^students/(?P<pk>[0-9]+)/$', views.StudentDetail.as_view()),
    # url(r'^staff/(?P<pk>[0-9]+)/$', views.StaffDetail.as_view()),
    url(r'^login/$', views.login),
    url(r'^students/(?P<pk>[0-9]+)/$', views.StudentDetail.as_view()),
    url(r'^staff/(?P<pk>[0-9]+)/$', views.StaffDetail.as_view()),
    url(r'^student_signin/$', views.sign_to_class),


]

urlpatterns = format_suffix_patterns(urlpatterns)