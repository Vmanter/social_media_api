<#import "blocks/template.ftlh" as t>
<@t.template user>
<h1 style="text-align: center">Лента публикаций</h1><hr>
<form method="get" action="/">
    <div class="form-row">
            <div class="form-group col-md-10">
            <label for="searchWord">Поиск по заголовкам</label>
            <input type="text" name="searchWord" <#if searchWord??>value="${searchWord}"</#if> class="form-control"
        id="searchWord">
    </div>
    <button style="width: 100%" type="submit" class="btn btn-dark">Поиск</button>
    </div>
</form>
<hr>
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

<div class="pagination justify-content-center">
    <ul class="pagination">
        <#if pageNumber?has_content && pageNumber gt 1>
        <li class="page-item"><a class="page-link" href="?<#if searchWord??>searchWord=${searchWord}&amp;</#if>pageNumber=${pageNumber-1}&amp;pageSize=${pageSize}">Предыдущая</a></li>
    </#if>
    <#list 1..totalPages as page>
    <li class="page-item<#if page == pageNumber> active</#if>"><a class="page-link" href="?<#if searchWord??>searchWord=${searchWord}&amp;</#if>pageNumber=${page}&amp;pageSize=${pageSize}">${page}</a></li>
</#list>
<#if pageNumber < totalPages>
<li class="page-item"><a class="page-link" href="?<#if searchWord??>searchWord=${searchWord}&amp;</#if>pageNumber=${pageNumber+1}&amp;pageSize=${pageSize}">Следующая</a></li>
</#if>
</ul>
</div>

</@t.template>