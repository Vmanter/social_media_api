<#import "blocks/template.ftlh" as t>
<@t.template user>
<h1 style="text-align: center">Мои друзья</h1>
<hr>
<div class="card-columns">
    <#list friends as friend>
    <a href="/user/${friend.id}" class="post-link">
        <div class="card text-white bg-dark my-3 post">
            <div class="card-top">
                <form action="/user/${friend.id}/unfriend" method="post">
                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                    <button type="submit" class="btn btn-danger m-3">
                        Удалить из друзей и отписаться
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
    <#else>
        <h1 style="text-align: left">У вас пока нет друзей</h1>
    </#list>
</div>
<br>
<hr>
<#list askeds as asked>
    <div class="card-columns">
        <div class="card text-white bg-dark my-3 post">
            <div class="card-body">
                <p class="card-text">Пользователь ${asked.nickname} предложил Вам подружиться</p>
                <table style="width: 100%">
                    <tr>
                        <td style="text-align: left"><a href="/user/${asked.id}/follow">Принять предложение</a></td>
                        <td style="text-align: right"><a href="/user/${asked.id}/unfollow">Отказаться</a></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</#list>
</@t.template>