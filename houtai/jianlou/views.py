# encoding: utf-8
from django.conf import settings
from django.core.paginator import InvalidPage, Paginator
from django.http import Http404

from haystack.forms import ModelSearchForm
from haystack.query import EmptySearchQuerySet, SearchQuerySet
from django.http import HttpResponse, JsonResponse
from .models import *
from django.contrib.auth.decorators import *
import hashlib
import datetime
import operator
from functools import reduce
import time
import requests
from haystack.views import SearchView


# 加盐加密
def salted_password(password):
    salt = "`1234567890~!@#$%^&*()-=[];',./ZXCVBNSADFYWQET"
    # 普通加密
    hash1 = md5hex(password)
    # 加盐加密
    hash2 = md5hex(hash1 + salt)
    return hash2


def md5hex(ascii_str):
    return hashlib.md5(ascii_str.encode('utf-8')).hexdigest()


# 注册
def validate_register(request):
    account = request.POST['account']
    password = request.POST['password']
    password = salted_password(password)
    # 用户名已存在就不允许注册 否则可以注册
    # 判断用户名
    try:
        u = User.objects.get(account=account)
    except Exception:
        u = None
    if u:
        return HttpResponse('failed')
    else:
        try:
            User.objects.create(account=account, password=password, username='捡喽用户'+account)
            return HttpResponse('success')
        except Exception:
            return HttpResponse('failed')


# 登录
def validate_login(request):
    account = request.POST['account']
    password = request.POST['password']

    try:
        u = User.objects.get(pk=account)
    except Exception:
        return HttpResponse('failed')

    if u.password == salted_password(password):
        cookie = md5hex(str(datetime.datetime.now()) + account + str(datetime.datetime.now()))
        if Cookie.objects.filter(account=account).update(cookie=cookie):
            pass
        else:
            Cookie.objects.create(account=account, cookie=cookie)
        res = {
            "cookie": cookie,
            "user_name": u.username,
        }
        return JsonResponse(res)
    else:
        return HttpResponse('failed')


def edit_name(request):
    try:
        account = Cookie.objects.get(cookie=request.POST['cookie']).account
        User.objects.filter(account=account).update(username=request.POST['user_name'])
        return HttpResponse('success')
    except Exception:
        return HttpResponse('failed')


def edit_head(request):
    try:
        account = Cookie.objects.get(cookie=request.POST['cookie']).account
        img_file = request.FILES.getlist('image')
        User.objects.filter(account=account).update(head=img_file)
        return HttpResponse('success')
    except Exception:
        return HttpResponse('failed')


# 修改密码
def change_pwd(request):
    try:
        account = Cookie.objects.get(cookie=request.POST['cookie']).account
        user = User.objects.get(account=account)
    except:
        return HttpResponse('failed')
    try:
        old_password = request.POST['old_password']
        if salted_password(old_password) == user.password:
            user.update(password=salted_password(request.POST['new_password']))
        else:
            return HttpResponse("failed")
        return HttpResponse("success")
    except:
        return HttpResponse("failed")


def publish(request):
    img = request.FILES.getlist('image')
    try:
        account = Cookie.objects.get(cookie=request.POST['cookie']).account
        user = User.objects.get(account=account)
        goods = Goods.objects.create(owner=user, hash=md5hex(str(user.account)+str(request.POST['content'])),
                                     content=request.POST['content'], money=request.POST['money'],
                                     origin_money=request.POST['origin_money'], send_money=request.POST['send_money'],
                                     classify=request.POST['classify'])
        img_file = request.FILES.getlist('image')
        for f in img_file:
            GoodsImage.objects.create(goods=goods, image=f)

        return HttpResponse('success')
    except Exception:
        return HttpResponse('failed')


def edit(request):
    try:
        account = Cookie.objects.get(cookie=request.POST['cookie']).account
        goods = Goods.objects.get(hash=request.POST['hash'])
    except goods.DoesNotExist:
        return HttpResponse('failed')

    if account == goods.owner.account:
        goods.update(content=request.POST['content'], money=request.POST['price'],
                     hash=md5hex(account + request.POST['content']))
    else:
        return HttpResponse('failed')


def get_goods(request):
    goods_list = Goods.objects.order_by('edit_date')[:10]
    ret = []
    for goods in goods_list:
        image_url = str(goods.goodsimage_set.all()[0].image)

        a = {
            "goodsID": goods.hash,
            "image": image_url,
            "content": goods.content,
            "money": goods.money,
            "user_name": goods.owner.username,
        }
        ret.append(a)
    return JsonResponse(ret, safe=False)


def goods_detail(request):
    try:
        goods = Goods.objects.get(hash=request.POST['goodsID'])
    except:
        return HttpResponse('failed')
    images_url = []
    for image_url in goods.goodsimage_set.all():
        image = {"image": str(image_url.image)}
        images_url.append(image)
    ret = {
        "username": goods.owner.account,
        "user_name": goods.owner.username,
        "money": goods.money,
        "content": goods.content,
        "origin_money": goods.origin_money,
        "send_money": goods.send_money,
        "images": images_url,
        "time": goods.edit_date.strftime("%Y-%m-%d %H:%M:%S")
    }
    return JsonResponse(ret, safe=False)


def chats(request):
    try:
        sender = Cookie.objects.get(cookie=request.POST['cookie']).account
    except:
        return HttpResponse('failed')
    receiver = request.POST['username']
    id = md5hex(str(int(sender) ^ int(receiver)))

    try:
        conversation = Conversation.objects.get(id=id)
    except:
        conversation = Conversation.objects.create(id=id, sender=sender, receiver=receiver)
    try:
        ConversationContent.objects.create(owner=conversation, sender=sender, receiver=receiver,
                                           message=request.POST['message'], send_time=str(time.time()).replace('.', ''))
        return HttpResponse('success')
    except:
        return HttpResponse('failed')


def get_message(request):
    try:
        sender = Cookie.objects.get(cookie=request.POST['cookie']).account
    except:
        return HttpResponse('failed')

    receiver = request.POST['username']
    if int(sender) ^ int(receiver):
        id = md5hex(str(int(sender) ^ int(receiver)))
    else:
        id = md5hex(sender)

    try:
        Conversation.objects.get(id=id)
    except:
        return HttpResponse('')

    try:
        if request.POST['time']:
            messages = list(
                ConversationContent.objects.filter(owner_id=id).filter(
                    send_time__gt=request.POST['time']).values(
                    "sender", "message", "send_time"))
            # for message in messages:
                # message['send_time'] = message['send_time'].strftime("%Y-%m-%d %H:%M:%S")
        else:
            messages = list(ConversationContent.objects.filter(owner_id=id).values("sender", "message", "send_time"))
            # for message in messages:
                # message['send_time'] = message['send_time'].strftime("%Y-%m-%d %H:%M:%S")
        return JsonResponse(messages, safe=False)
    except:
        return HttpResponse('failed')


def get_conversation_list(request):
    try:
        user = Cookie.objects.get(cookie=request.POST['cookie']).account
    except:
        return HttpResponse('failed')
    conversation_send = Conversation.objects.filter(sender=user)
    conversation_receive = Conversation.objects.filter(receiver=user)

    ret = []
    for conversation in conversation_send:
        conversationcontent = conversation.conversationcontent_set.order_by('send_time')[0]
        a = {
            "username": conversation.receiver,
            "user_name": User.objects.get(pk=conversation.receiver).username,
            "message": conversationcontent.message,
            "datetime": conversationcontent.send_time,
        }
        ret.append(a)

    for conversation in conversation_receive:
        conversationcontent = conversation.conversationcontent_set.order_by('send_time')[0]
        a = {
            "username": conversation.sender,
            "user_name": User.objects.get(pk=conversation.sender).username,
            "message": conversationcontent.message,
            "datetime": conversationcontent.send_time,
        }
        ret.append(a)
    run_function = lambda x, y: x if y in x else x + [y]
    ret = reduce(run_function, [[], ] + ret)
    ret.sort(key=operator.itemgetter('datetime'))
    return JsonResponse(ret, safe=False)


def my_goods(request):
    try:
        user_account = Cookie.objects.get(cookie=request.POST['cookie']).account
        goods_list = User.objects.get(account=user_account).goods_set.order_by('edit_date')
    except:
        return HttpResponse('failed')

    ret = []
    for goods in goods_list:
        image_url = str(goods.goodsimage_set.all()[0].image)

        a = {
            "goodsID": goods.hash,
            "image": image_url,
            "content": goods.content,
            "money": goods.money,
        }
        ret.append(a)
    return JsonResponse(ret, safe=False)


def test(request):
    data = {
        'q': '图书',
        'method': '2',
        'page': 3,
    }

    url = 'http://10.131.32.240:8000/search/'

    z = requests.post(url=url, data=data)
    a = SearchQuerySet()
    return HttpResponse('success')






RESULTS_PER_PAGE = getattr(settings, 'HAYSTACK_SEARCH_RESULTS_PER_PAGE', 20)


class MySearchView(SearchView):
    template = 'search/search.html'
    extra_context = {}
    query = ''
    results = EmptySearchQuerySet()
    request = None
    form = None
    results_per_page = RESULTS_PER_PAGE

    def __init__(self, template=None, load_all=True, form_class=None, searchqueryset=None, results_per_page=None):
        self.load_all = load_all
        self.form_class = form_class
        self.searchqueryset = searchqueryset

        if form_class is None:
            self.form_class = ModelSearchForm

        if not results_per_page is None:
            self.results_per_page = results_per_page

        if template:
            self.template = template

    def __call__(self, request):
        """
        Generates the actual response to the search.

        Relies on internal, overridable methods to construct the response.
        """
        self.request = request

        self.form = self.build_form()
        self.query = self.get_query()
        self.results = self.get_results()

        return self.create_response()

    def build_form(self, form_kwargs=None):
        """
        Instantiates the form the class should use to process the search query.
        """
        data = None
        kwargs = {
            'load_all': self.load_all,
        }
        if form_kwargs:
            kwargs.update(form_kwargs)

        if len(self.request.POST):
            data = self.request.POST

        if self.searchqueryset is not None:
            kwargs['searchqueryset'] = self.searchqueryset

        return self.form_class(data, **kwargs)

    def get_query(self):
        """
        Returns the query provided by the user.

        Returns an empty string if the query is invalid.
        """
        if self.form.is_valid():
            return self.form.cleaned_data['q']

        return ''

    def get_results(self):
        """
        Fetches the results via the form.

        Returns an empty list if there's no query to search with.
        """
        return self.form.search()

    def build_page(self):
        """
        Paginates the results appropriately.

        In case someone does not want to use Django's built-in pagination, it
        should be a simple matter to override this method to do what they would
        like.
        """
        try:
            page_no = int(self.request.POST.get('page', 1))
        except (TypeError, ValueError):
            raise Http404("Not a valid number for page.")

        if page_no < 1:
            raise Http404("Pages should be 1 or greater.")

        start_offset = (page_no - 1) * self.results_per_page
        self.results[start_offset:start_offset + self.results_per_page]

        paginator = Paginator(self.results, self.results_per_page)

        try:
            page = paginator.page(page_no)
        except InvalidPage:
            raise Http404("No such page!")

        return (paginator, page)

    def extra_context(self):
        """
        Allows the addition of more context variables as needed.

        Must return a dictionary.
        """
        return {}

    def get_context(self):
        (paginator, page) = self.build_page()

        context = {
            'query': self.query,
            'form': self.form,
            'page': page,
            'paginator': paginator,
            'suggestion': None,
        }

        if hasattr(self.results, 'query') and self.results.query.backend.include_spelling:
            context['suggestion'] = self.form.get_suggestion()

        context.update(self.extra_context())

        return context

    def create_response(self):
        """
        Generates the actual HttpResponse to send back to the user.
        """

        context = self.get_context()

        ret = []
        for result in context['paginator'].object_list:
            goods = result._object
            image_url = str(goods.goodsimage_set.all()[0].image)

            a = {
                "goodsID": goods.hash,
                "image": image_url,
                "content": goods.content,
                "money": goods.money,
                "user_name": goods.owner.username,
                "time": goods.edit_date,
            }
            ret.append(a)

        if self.request.POST['method'] == '1':
            run_function = lambda x, y: x if y in x else x + [y]
            ret = reduce(run_function, [[], ] + ret)
            ret.sort(key=operator.itemgetter('time'))
        elif self.request.POST['method'] == '2':
            run_function = lambda x, y: x if y in x else x + [y]
            ret = reduce(run_function, [[], ] + ret)
            ret.sort(key=operator.itemgetter('time'), reverse=True)
        ret = ret[(int(self.request.POST['page']) - 1) * RESULTS_PER_PAGE:
                  int(self.request.POST['page']) * RESULTS_PER_PAGE]
        ret.append({"max_page": context['paginator'].num_pages, })
        return JsonResponse(ret, safe=False)






