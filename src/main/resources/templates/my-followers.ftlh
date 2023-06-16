<#import "blocks/template.ftlh" as t>
<@t.template user>
<h1 style="text-align: center">Мои подписчики</h1>
<hr>
<div class="card-columns">
    <#list friends as friend>
    <a href="/user/${friend.id}" class="post-link">
        <div class="card text-white bg-dark my-3 post">
            <img src="/static/images/avatar.png"
                 class="card-img-top">
            <div class="card-body">
                <h5 class="card-title">${friend.name}</h5>
                <p class="card-text">&nbsp;</p>
            </div>
        </div>
    </a>
    <#else>
    <h1 style="text-align: left">У вас пока нет подписчиков</h1>
</#list>
</div>
</@t.template>