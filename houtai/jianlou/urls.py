from django.urls import path
from . import views
from django.views.static import serve
from django.conf import settings
from django.conf.urls import url


app_name = 'shop'

urlpatterns = [
    path('user/register/', views.validate_register, name='register'),
    path('user/login/', views.validate_login, name='login'),
    path('user/editname/', views.edit_name, name='edit_name'),
    path('publish/', views.publish, name='publish'),
    path('index/', views.get_goods, name='get_goods'),
    path('mygoods/', views.my_goods, name='my_goods'),
    path('goods/detail/', views.goods_detail, name='goods_detail'),
    path('chats/friends/', views.get_conversation_list, name='get_conversation_list'),
    path('chats/', views.get_message, name='get_message'),
    path('chats/push/', views.chats, name='chats'),
    path('test/', views.test, name='test'),
    path('search/', views.MySearchView(), name='haystack_search'),
    path('test/', views.test, name='test'),
    url(r'image/(?P<path>.*)', serve, {'document_root': settings.MEDIA_ROOT}),
]
