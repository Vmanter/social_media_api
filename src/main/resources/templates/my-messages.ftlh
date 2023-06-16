<#import "blocks/template.ftlh" as t>
<@t.template user>
<h1 style="text-align: center">Мои сообщения</h1>
<hr>
<#list messages as message>
<div class="card-columns">
    <div class="card text-white bg-dark my-3 post">
        <div class="card-body">
            <p class="card-text">Пользователь ${message.sender.nickname} прислал Вам сообщение</p>
            <p class="card-text">${message.message}</p>
            <table style="width: 100%">
                <tr>
                    <td style="text-align: left"><a href="/message/${message.id}/read">Прочитано</a></td>
                    <td style="text-align: right"><a href="/message/${message.id}/delete">Удалить</a></td>
                </tr>
            </table>
        </div>
    </div>
</div>
<#else>
<h1 style="text-align: left">У вас пока нет сообщений</h1>
</#list>
<br>
<hr>
<div class="card-columns">
    <#list friends as friend>
    <a href="/user/${friend.id}" class="post-link">
        <div class="card text-white bg-dark my-3 post">
            <div class="card-top">
                <form action="/user/${friend.id}/send" method="post">
                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                    <button type="submit" class="btn btn-light my-3">
                        Отправить сообщение
                    </button>
                </form>
            </div>
            <img src="/static/images/avatar.png"
                 class="card-img-top">
            <div class="card-body">
                <h5 class="card-title">${friend.name}</h5>
                <p class="card-text">&nbsp;</p>
            </div>
        </div>
    </a>
</#list>
</div>
</@t.template>