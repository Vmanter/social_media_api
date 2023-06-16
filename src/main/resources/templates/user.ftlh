<#import "blocks/template.ftlh" as t>
<@t.template userByPrincipal>
<h1 style="text-align: center">Информация о пользователе</h1>
<hr>
<div style="text-align: center">
    <img src="/static/images/avatar.png"
         style="border:2px solid black; border-radius: 20px" height="120">
    <br>
    <br>
    <h2>${user.name}</h2>
    <br>
</div>
<br>
<#if is_friends>
<a href="/user/${user.id}/unfollow">Удалить из друзей и отписка</a>
<#elseif is_follower>
<a href="/user/${user.id}/unfollow">Отписаться</a>
<#else>
<a href="/user/${user.id}/follow">Подружиться и подписаться на автора</a>
</#if>
<hr>
<a href="/user/${user.id}/sendmessage">Отправить сообщение</a>
<hr>
<h1 style="text-align: center">Публикации пользователя ${user.name}</h1>
<br>
<div class="card-columns">
    <#list posts as post>
    <a href="/post/${post.id}" class="post-link">
        <div class="card text-white bg-dark my-3 post">

            <img src="/images/${post.previewImageId}"
                 class="card-img-top">
            <div class="card-body">
                <h5 class="card-title">${post.title}</h5>
                <p class="card-text">${post.text}</p>
            </div>
            <div class="card-footer">
                ${post.dateOfCreated}
            </div>
        </div>
    </a>
    <#else>
    <h1 style="text-align: center">Не найдено</h1>
</#list>
</div>
</@t.template>