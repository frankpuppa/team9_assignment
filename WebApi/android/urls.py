from django.conf.urls import url
from rest_framework.urlpatterns import format_suffix_patterns
from android import views


urlpatterns = [
    url(r'^students/$', views.StudentList.as_view()),
    url(r'^staff/$', views.StaffList.as_view()),
    # url(r'^students/(?P<pk>[0-9]+)/$', views.StudentDetail.as_view()),
    # url(r'^staff/(?P<pk>[0-9]+)/$', views.StaffDetail.as_view()),
]

urlpatterns = format_suffix_patterns(urlpatterns)