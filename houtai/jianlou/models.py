import os
from django.db import models

# Create your models here.


def user_head_path(instance, filename):
    # 文件上传到MEDIA_ROOT/<account>/head/<filename>目录中
    return os.path.join(instance.account, 'head', filename)


def user_goods_path(instance, filename):
    # 文件上传到MEDIA_ROOT/<account>/<hash>/<filename>目录中
    return os.path.join(instance.goods.owner.account, instance.goods.hash, filename)


class User(models.Model):
    account = models.CharField(max_length=11, primary_key=True, db_index=True)
    password = models.CharField(max_length=32)
    username = models.CharField(max_length=50, blank=True)
    sex = models.CharField(max_length=2, default="未知", blank=True)
    birthday = models.DateField(default='1949-10-01', blank=True)
    location = models.CharField(max_length=50, null=True, blank=True)
    introduction = models.TextField(max_length=100, null=True, blank=True)
    school = models.CharField(max_length=10, null=True, blank=True)
    head = models.ImageField(default='', upload_to=user_head_path, blank=True, null=True)


class Goods(models.Model):
    owner = models.ForeignKey(User, on_delete=models.CASCADE)
    hash = models.CharField(max_length=32, primary_key=True)  # 设置为主键
    content = models.CharField(max_length=300, db_index=True)
    money = models.CharField(max_length=10)
    origin_money = models.CharField(max_length=10)
    send_money = models.CharField(max_length=10)
    classify = models.CharField(max_length=200)
    edit_date = models.DateTimeField(auto_now=True, db_index=True)  # 编辑日期


class GoodsImage(models.Model):
    goods = models.ForeignKey(Goods, on_delete=models.CASCADE)
    image = models.ImageField(default='', upload_to=user_goods_path, blank=True, null=True)


class Cookie(models.Model):
    cookie = models.CharField(max_length=32, primary_key=True, db_index=True)
    change_time = models.DateTimeField(auto_now=True)
    account = models.CharField(max_length=11)


class Conversation(models.Model):
    id = models.CharField(max_length=32, primary_key=True)
    sender = models.CharField(max_length=11)
    receiver = models.CharField(max_length=11)


class ConversationContent(models.Model):
    owner = models.ForeignKey(Conversation, on_delete=models.CASCADE)
    sender = models.CharField(max_length=11)
    receiver = models.CharField(max_length=11)
    message = models.CharField(max_length=200)
    send_time = models.CharField(max_length=17, db_index=True)





















